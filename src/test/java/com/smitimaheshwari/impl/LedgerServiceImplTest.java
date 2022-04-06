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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class LedgerServiceImplTest {

    private LedgerService ledgerService;
    private OutputPrinter outputPrinter;
    private Ledger ledger;

    @Before
    public void setUp() throws Exception {
        ledger = mock(Ledger.class);
        outputPrinter = mock(OutputPrinter.class);
        ledgerService = new LedgerServiceImpl(ledger, outputPrinter);
    }

    @Test
    public void addLoanSuccessTest() {
        final Bank bank = getBankObject("IDIDI", "Dale");
        when(ledger.getOrAddBank("IDIDI")).thenReturn(bank);
        ledgerService.addLoan("IDIDI", "Harry", 10000, 4, 5);
        // TODO: We could have used a return value to check if the loan is added successfully or not.
    }

    @Test(expected = LoanAdditionException.class)
    public void addLoanRecordAlreadyExistsTest() {
        final Bank bank = getBankObject("IDIDI", "Dale");
        when(ledger.getOrAddBank("IDIDI")).thenReturn(bank);
        ledgerService.addLoan("IDIDI", "Dale", 10000, 4, 5);
    }

    @Test
    public void recordPaymentSuccessTest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(true);
        ledgerService.recordPayment("IDIDI", "Dale", 1000, 1);
    }

    @Test(expected = PaymentRecordException.class)
    public void recordPaymentBankDoesNotExistTest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(false);
        ledgerService.recordPayment("IDIDI", "Dale", 1000, 1);
    }

    @Test(expected = PaymentRecordException.class)
    public void recordPaymentCustomerDoesNotExistTest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(true);
        ledgerService.recordPayment("IDIDI", "Harry", 2000, 2);
    }

    @Test(expected = PaymentRecordException.class)
    public void recordPaymentLumpSumLessThanEMITest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(true);
        ledgerService.recordPayment("IDIDI", "Dale", 20, 1);
    }

    @Test(expected = PaymentRecordException.class)
    public void recordPaymentLumpSumAdditionGreaterThanTotalAmountTest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(true);
        ledgerService.recordPayment("IDIDI", "Dale", 100000, 1);
    }

    @Test
    public void calculateBalanceSuccessTest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(true);
        ledgerService.calculateBalance("IDIDI", "Dale", 5);
        verify(outputPrinter).printNewLine("IDIDI Dale 1000 55");
    }

    @Test
    public void calculateBalanceHavingLumpSumTest() {
        final Map<String, Bank> banks = getBanksWithLumpSum("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(true);
        ledgerService.calculateBalance("IDIDI", "Dale", 5);
        verify(outputPrinter).printNewLine("IDIDI Dale 2000 50");
    }

    @Test(expected = BalanceCalculationException.class)
    public void calculateBalanceBankDoesNotExistTest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("MBI")).thenReturn(false);
        ledgerService.calculateBalance("MBI", "Harry", 1);
    }

    @Test(expected = BalanceCalculationException.class)
    public void calculateBalanceCustomerDoesNotExistTest() {
        final Map<String, Bank> banks = getBanks("IDIDI", "Dale");
        when(ledger.getBanks()).thenReturn(banks);
        when(ledger.containsBank("IDIDI")).thenReturn(false);
        ledgerService.calculateBalance("IDIDI", "Harry", 1);
    }

    private Map<String, Bank> getBanks(final String bankName, final String customerName) {
        final Map<String, Bank> banks = new HashMap<>();
        banks.put(bankName, getBankObject(bankName, customerName));
        return banks;
    }

    private Map<String, Bank> getBanksWithLumpSum(final String bankName, final String customerName) {
        final Map<String, Bank> banks = new HashMap<>();
        banks.put(bankName, getBankObjectWithLumpSum(bankName, customerName));
        return banks;
    }

    private Bank getBankObject(final String bankName, final String customerName) {
        final Map<String, Customer> customers = new HashMap<>();
        final List<LumpSum> lumpSums = new ArrayList<>();
        final Loan loan = Loan.builder().emiAmount(200).interestRate(5).principalAmount(10000).years(4).totalAmount(12000).lumpSums(lumpSums).build();
        customers.put(customerName, Customer.builder().name(customerName).loan(loan).build());
        return Bank.builder()
                .name(bankName).customers(customers).build();
    }

    private Bank getBankObjectWithLumpSum(final String bankName, final String customerName) {
        final Map<String, Customer> customers = new HashMap<>();
        final List<LumpSum> lumpSums = new ArrayList<>();
        lumpSums.add(LumpSum.builder().emiNumber(5).lumpSumAmount(1000).build());
        final Loan loan = Loan.builder().emiAmount(200).interestRate(5).principalAmount(10000).years(4).totalAmount(12000).lumpSums(lumpSums).build();
        customers.put(customerName, Customer.builder().name(customerName).loan(loan).build());
        return Bank.builder()
                .name(bankName).customers(customers).build();
    }
}
