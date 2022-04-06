package com.smitimaheshwari.impl;

import com.smitimaheshwari.entities.Bank;
import com.smitimaheshwari.entities.Customer;
import com.smitimaheshwari.entities.Ledger;
import com.smitimaheshwari.entities.Loan;
import com.smitimaheshwari.entities.LumpSum;
import com.smitimaheshwari.exceptions.BalanceCalculationException;
import com.smitimaheshwari.exceptions.LoanAdditionException;
import com.smitimaheshwari.exceptions.PaymentRecordException;
import com.smitimaheshwari.interfaces.LedgerService;
import com.smitimaheshwari.utils.OutputPrinter;
import lombok.Builder;

import java.util.ArrayList;

@Builder
public class LedgerServiceImpl implements LedgerService {

    private Ledger ledger;
    private OutputPrinter outputPrinter;

    private static final int MONTHS_COUNT = 12;
    private static final String LOAN_RECORD_ALREADY_EXISTS = "Customer %s has already taken loan from %s bank. Hence, can't be added again!";
    private static final String CUSTOMER_RECORD_NOT_FOUND = "Customer %s does not exist in %s bank. Hence, cannot add payment in this record!";
    private static final String LUMP_SUM_PAYMENT_LESS_THAN_EMI = "Lump sum amount %d is lesser than EMI (%d) in %s bank for %s. Payment not recorded!";
    private static final String LUMP_SUM_PAYMENT_GREATER_THAN_LOAN_AMOUNT = "Lump sum amount %d with already paid installments is adding up to be more than total loan amount(%d) in %s bank for %s. Payment not recorded!";

    /**
     * This would add the loan for the given customer using the details provided.
     *
     * @param bankName
     * @param customerName
     * @param principal
     * @param years
     * @param interestRate
     * @throws LoanAdditionException
     */
    @Override
    public void addLoan(final String bankName, final String customerName, final long principal,
                        final int years, final int interestRate) throws LoanAdditionException {
        final Bank bank = ledger.getOrAddBank(bankName);
        if(bank.getCustomers().containsKey(customerName)) {
            throw new LoanAdditionException(String.format(LOAN_RECORD_ALREADY_EXISTS, customerName, bankName));
        }
        final long totalAmount = getTotalAmount(principal, years, interestRate);
        final Loan loan = Loan.builder().lumpSums(new ArrayList<>()).years(years).interestRate(interestRate)
                .principalAmount(principal).totalAmount(totalAmount).emiAmount(getEmiAmount(totalAmount, years)).build();
        final Customer customer = Customer.builder().loan(loan).name(customerName).build();
        bank.getCustomers().put(customerName, customer);
    }

    /**
     * This records the lump sum payment made by the customer.
     *
     * @param bankName
     * @param customerName
     * @param lumpSumAmount
     * @param emiNumber
     * @throws PaymentRecordException
     */
    @Override
    public void recordPayment(final String bankName, final String customerName, final long lumpSumAmount, final int emiNumber) throws PaymentRecordException {
        if(!customerRecordExists(bankName, customerName)) {
            throw new PaymentRecordException(String.format(CUSTOMER_RECORD_NOT_FOUND, customerName, bankName));
        }
        final Customer customer = ledger.getBanks().get(bankName).getCustomers().get(customerName);
        final Loan loan = customer.getLoan();
        if(lumpSumAmountLessThanEMI(loan, lumpSumAmount)) {
            throw new PaymentRecordException(String.format(LUMP_SUM_PAYMENT_LESS_THAN_EMI, lumpSumAmount, loan.getEmiAmount(), customerName, bankName));
        }
        if(lumpSumAmountingGreaterThanTotalAmount(loan, lumpSumAmount, emiNumber)) {
            throw new PaymentRecordException(String.format(LUMP_SUM_PAYMENT_GREATER_THAN_LOAN_AMOUNT, lumpSumAmount, loan.getTotalAmount(), customerName, bankName));
        }
        loan.addLumpSumRecord(LumpSum.builder().emiNumber(emiNumber).lumpSumAmount(lumpSumAmount).build());
    }

    /**
     * This calculates the amount that is paid till the EMI provided including the given emi and lump sum payments made till then.
     * It also prints the EMI count remaining.
     *
     * @param bankName
     * @param customerName
     * @param emiNumber
     * @throws BalanceCalculationException
     */
    @Override
    public void calculateBalance(final String bankName, final String customerName, final int emiNumber) throws BalanceCalculationException {
        if(!customerRecordExists(bankName, customerName)) {
            throw new BalanceCalculationException(String.format(CUSTOMER_RECORD_NOT_FOUND, customerName, bankName));
        }
        final Customer customer = ledger.getBanks().get(bankName).getCustomers().get(customerName);
        final Loan loan = customer.getLoan();
        final long alreadyPaidLumpSums = getLumpSumTotalTillEmiNumber(loan, emiNumber);
        final long alreadyPaidEMIs = emiNumber * loan.getEmiAmount();
        final long amountPaid = alreadyPaidLumpSums + alreadyPaidEMIs;
        final long remainingEMIs = getRemainingEMICount(loan, amountPaid);
        outputPrinter.printNewLine(String.format("%s %s %d %d", bankName, customer.getName(), amountPaid, remainingEMIs));
    }

    /**
     * This calculates the number of EMIs remaining.
     * @param loan
     * @param amountPaid
     * @return number of EMIs remaining.
     */
    private long getRemainingEMICount(final Loan loan, final long amountPaid) {
        final long totalAmount = loan.getTotalAmount();
        final long amountRemaining = totalAmount - amountPaid;
        return (int) Math.ceil((double) amountRemaining / loan.getEmiAmount());
    }

    /**
     * This calculates the total of lump sum payments till a particular EMI number.
     * @param loan
     * @param emiNumber
     * @return total lump sum payments till the EMI number.
     */
    private long getLumpSumTotalTillEmiNumber(final Loan loan, final int emiNumber) {
        return loan.getLumpSums().stream().filter(ls -> ls.getEmiNumber() <= emiNumber)
                .map(LumpSum::getLumpSumAmount).reduce(0L, Long::sum);
    }

    /**
     * This checks whether the lump sum amount to be added is less than the EMI amount.
     *
     * @param loan
     * @param lumpSumAmount
     * @return true if the lump sum amount is less than EMI, false otherwise.
     */
    private boolean lumpSumAmountLessThanEMI(final Loan loan, final long lumpSumAmount) {
        return loan.getEmiAmount() > lumpSumAmount;
    }

    /**
     * This checks whether the lump sum amount to be added is greater than the amount remaining to be paid for the loan to be completed.
     *
     * @param loan
     * @param lumpSumAmount
     * @param emiNumber
     * @return true if the lump sum is greater, false otherwise.
     */
    private boolean lumpSumAmountingGreaterThanTotalAmount(final Loan loan, final long lumpSumAmount, final int emiNumber) {
        final long totalLoanAmount = loan.getTotalAmount();
        final long alreadyAddedLumpSums = getLumpSumTotalTillEmiNumber(loan, emiNumber);
        return totalLoanAmount < (emiNumber * loan.getEmiAmount() + lumpSumAmount + alreadyAddedLumpSums);
    }

    /**
     * This checks if the customer record exists in the bank.
     *
     * @param bankName
     * @param customerName
     * @return true if the account exists, false otherwise.
     */
    private boolean customerRecordExists(final String bankName, final String customerName) {
        return ledger.containsBank(bankName)
                && ledger.getBanks().get(bankName).containsCustomer(customerName);
    }

    /**
     * This gets the total amount of the loan ( A = P + I where I = (P * R * N)/100 ) to be paid.
     * @param principal
     * @param years
     * @param interestRate
     * @return the total amount of the loan.
     */
    private long getTotalAmount(final long principal, final int years, final int interestRate) {
        final long amount = principal * years * interestRate;
        return principal + (long) Math.ceil((double) amount / 100);
    }

    /**
     * This gets the EMI amount to be paid per month.
     * @param totalAmount
     * @param years
     * @return EMI amount.
     */
    private long getEmiAmount(final long totalAmount, final int years) {
        final int months = MONTHS_COUNT * years;
        return (long) Math.ceil((double) totalAmount / months);
    }
}
