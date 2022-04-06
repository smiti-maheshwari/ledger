package com.smitimaheshwari.interfaces;

import com.smitimaheshwari.exceptions.BalanceCalculationException;
import com.smitimaheshwari.exceptions.LoanAdditionException;
import com.smitimaheshwari.exceptions.PaymentRecordException;

public interface LedgerService {

    void addLoan(final String bankName, final String customerName, final long principal, final int years, final int interestRate) throws LoanAdditionException;
    void recordPayment(final String bankName, final String customerName, final long lumpSumAmount, final int emiNumber) throws PaymentRecordException;
    void calculateBalance(final String bankName, final String customerName, final int emiNumber) throws BalanceCalculationException;
}
