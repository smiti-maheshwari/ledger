package com.smitimaheshwari.commands;
import com.smitimaheshwari.utils.OutputPrinter;
import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.interfaces.LedgerService;

public abstract class CommandExecutor {

    protected OutputPrinter outputPrinter;
    protected LedgerService ledgerService;

    protected CommandExecutor(final OutputPrinter outputPrinter, final LedgerService ledgerService) {
        this.outputPrinter = outputPrinter;
        this.ledgerService = ledgerService;
    }

    /**
     * Validates that whether a command is valid to be executed or not.
     *
     * @param command Command to be validated.
     * @return Boolean indicating whether command is valid or not.
     */
    public abstract boolean validate(Command command);

    /**
     * Executes the command.
     *
     * @param command Command to be executed.
     */
    public abstract void execute(Command command);
}