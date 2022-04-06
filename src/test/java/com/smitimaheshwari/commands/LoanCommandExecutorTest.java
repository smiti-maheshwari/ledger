package com.smitimaheshwari.commands;

import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.LoanAdditionException;
import com.smitimaheshwari.interfaces.LedgerService;
import com.smitimaheshwari.utils.OutputPrinter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoanCommandExecutorTest {

    private LedgerService ledgerService;
    private OutputPrinter outputPrinter;
    private LoanCommandExecutor loanCommandExecutor;

    @Before
    public void setUp() throws Exception {
        ledgerService = mock(LedgerService.class);
        outputPrinter = mock(OutputPrinter.class);
        loanCommandExecutor = new LoanCommandExecutor(outputPrinter, ledgerService);
    }

    @Test
    public void testValidCommand() {
        assertTrue(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale 10000 5 4")));
    }

    @Test
    public void testInvalidCommands() {
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale 10000 5")));
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale 10000")));
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale")));
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI")));
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale PRINCIPAL NO_OF_YEARS RATE_OF_INTEREST")));
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale PRINCIPAL NO_OF_YEARS 4")));
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale PRINCIPAL 5 4")));
        assertFalse(loanCommandExecutor.validate(new Command("LOAN IDIDI Dale 10000 NO_OF_YEARS 4")));
    }

    @Test
    public void testLoanAddition() {
        loanCommandExecutor.execute(new Command("LOAN IDIDI Dale 10000 5 4"));
        verify(ledgerService).addLoan("IDIDI", "Dale", 10000, 5, 4);
    }

    @Test
    public void testLoanAdditionHandleException() {
        doThrow(new LoanAdditionException("Customer Dale has already taken loan from IDIDI bank. Hence, can't be added again!")).when(ledgerService)
                .addLoan("IDIDI", "Dale", 10000, 5, 4);
        loanCommandExecutor.execute(new Command("LOAN IDIDI Dale 10000 5 4"));
        verify(outputPrinter).printError("Customer Dale has already taken loan from IDIDI bank. Hence, can't be added again!");
    }
}
