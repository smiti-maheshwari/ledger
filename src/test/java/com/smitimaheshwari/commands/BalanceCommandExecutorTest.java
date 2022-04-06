package com.smitimaheshwari.commands;

import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.BalanceCalculationException;
import com.smitimaheshwari.interfaces.LedgerService;
import com.smitimaheshwari.utils.OutputPrinter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BalanceCommandExecutorTest {

    private LedgerService ledgerService;
    private OutputPrinter outputPrinter;
    private BalanceCommandExecutor balanceCommandExecutor;

    @Before
    public void setUp() throws Exception {
        ledgerService = mock(LedgerService.class);
        outputPrinter = mock(OutputPrinter.class);
        balanceCommandExecutor = new BalanceCommandExecutor(outputPrinter, ledgerService);
    }

    @Test
    public void testValidCommand() {
        assertTrue(balanceCommandExecutor.validate(new Command("BALANCE MBI Harry 12")));
    }

    @Test
    public void testInvalidCommands() {
        assertFalse(balanceCommandExecutor.validate(new Command("BALANCE MBI 12")));
        assertFalse(balanceCommandExecutor.validate(new Command("BALANCE MBI Harry")));
        assertFalse(balanceCommandExecutor.validate(new Command("BALANCE MBI")));
        assertFalse(balanceCommandExecutor.validate(new Command("BALANCE BANK_NAME BORROWER_NAME EMI_NO")));
    }

    @Test
    public void testBalanceCalculation() {
        balanceCommandExecutor.execute(new Command("BALANCE MBI Harry 12"));
        verify(ledgerService).calculateBalance("MBI", "Harry", 12);
    }

    @Test
    public void testBalanceCalculationHandleException() {
        doThrow(new BalanceCalculationException("Customer Harry does not exist in MBI bank. Hence, cannot add payment in this record!")).when(ledgerService)
                .calculateBalance("MBI", "Harry", 12);
        balanceCommandExecutor.execute(new Command("BALANCE MBI Harry 12"));
        verify(outputPrinter).printError("Customer Harry does not exist in MBI bank. Hence, cannot add payment in this record!");
    }
}
