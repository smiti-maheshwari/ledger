package com.smitimaheshwari.commands;

import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.PaymentRecordException;
import com.smitimaheshwari.interfaces.LedgerService;
import com.smitimaheshwari.utils.OutputPrinter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PaymentCommandExecutorTest {

    private LedgerService ledgerService;
    private OutputPrinter outputPrinter;
    private PaymentCommandExecutor paymentCommandExecutor;

    @Before
    public void setUp() throws Exception {
        ledgerService = mock(LedgerService.class);
        outputPrinter = mock(OutputPrinter.class);
        paymentCommandExecutor = new PaymentCommandExecutor(outputPrinter, ledgerService);
    }

    @Test
    public void testValidCommand() {
        assertTrue(paymentCommandExecutor.validate(new Command("PAYMENT MBI Dale 1000 5")));
    }

    @Test
    public void testInvalidCommands() {
        assertFalse(paymentCommandExecutor.validate(new Command("PAYMENT MBI Dale 1000")));
        assertFalse(paymentCommandExecutor.validate(new Command("PAYMENT MBI Dale")));
        assertFalse(paymentCommandExecutor.validate(new Command("PAYMENT MBI")));
        assertFalse(paymentCommandExecutor.validate(new Command("PAYMENT BANK_NAME BORROWER_NAME LUMP_SUM_AMOUNT EMI_NO")));
        assertFalse(paymentCommandExecutor.validate(new Command("PAYMENT BANK_NAME BORROWER_NAME LUMP_SUM_AMOUNT 5")));
        assertFalse(paymentCommandExecutor.validate(new Command("PAYMENT BANK_NAME BORROWER_NAME 1000 EMI_NO")));
    }

    @Test
    public void testRecordPayment() {
        paymentCommandExecutor.execute(new Command("PAYMENT MBI Dale 1000 5"));
        verify(ledgerService).recordPayment("MBI", "Dale", 1000, 5);
    }

    @Test
    public void testRecordPaymentHandleException() {
        doThrow(new PaymentRecordException("Customer Dale does not exist in MBI bank. Hence, cannot add payment in this record!")).when(ledgerService)
                .recordPayment("MBI", "Dale", 1000, 5);
        paymentCommandExecutor.execute(new Command("PAYMENT MBI Dale 1000 5"));
        verify(outputPrinter).printError("Customer Dale does not exist in MBI bank. Hence, cannot add payment in this record!");
    }
}
