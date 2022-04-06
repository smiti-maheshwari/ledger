package com.smitimaheshwari.commands;

import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.InvalidCommandException;
import com.smitimaheshwari.interfaces.LedgerService;
import com.smitimaheshwari.utils.OutputPrinter;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class CommandExecutorFactoryTest {

    private CommandExecutorFactory commandExecutorFactory;

    @Before
    public void setUp() throws Exception {
        final LedgerService ledgerService = mock(LedgerService.class);
        final OutputPrinter outputPrinter = mock(OutputPrinter.class);
        commandExecutorFactory = new CommandExecutorFactory(outputPrinter, ledgerService);
    }

    @Test
    public void testFetchingExecutorForBalanceCommand() {
        final CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(new Command("BALANCE BANK_NAME BORROWER_NAME EMI_NO"));
        assertNotNull(commandExecutor);
        assertTrue(commandExecutor instanceof BalanceCommandExecutor);
    }

    @Test
    public void testFetchingExecutorForPaymentCommand() {
        final CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(new Command("PAYMENT BANK_NAME BORROWER_NAME LUMP_SUM_AMOUNT EMI_NO"));
        assertNotNull(commandExecutor);
        assertTrue(commandExecutor instanceof PaymentCommandExecutor);
    }

    @Test
    public void testFetchingExecutorForLoanCommand() {
        final CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(new Command("LOAN BANK_NAME BORROWER_NAME PRINCIPAL NO_OF_YEARS RATE_OF_INTEREST"));
        assertNotNull(commandExecutor);
        assertTrue(commandExecutor instanceof LoanCommandExecutor);
    }

    @Test(expected = InvalidCommandException.class)
    public void testFetchingExecutorForInvalidCommand() {
        commandExecutorFactory.getCommandExecutor(new Command("some-random-command random-param1 random-param2"));
    }
}
