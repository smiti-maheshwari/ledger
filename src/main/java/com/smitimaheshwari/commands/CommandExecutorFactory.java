package com.smitimaheshwari.commands;

import com.smitimaheshwari.utils.OutputPrinter;
import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.InvalidCommandException;
import com.smitimaheshwari.interfaces.LedgerService;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutorFactory {

    private Map<String, CommandExecutor> commands = new HashMap<>();

    public CommandExecutorFactory(final OutputPrinter outputPrinter, final LedgerService ledgerService) {
        commands.put(
                BalanceCommandExecutor.COMMAND_NAME,
                new BalanceCommandExecutor(outputPrinter, ledgerService));
        commands.put(
                LoanCommandExecutor.COMMAND_NAME,
                new LoanCommandExecutor(outputPrinter, ledgerService));
        commands.put(
                PaymentCommandExecutor.COMMAND_NAME,
                new PaymentCommandExecutor(outputPrinter, ledgerService));
    }

    /**
     * Gets {@link CommandExecutor} for a particular command. It basically uses name of command to
     * fetch its corresponding executor.
     *
     * @param command Command for which executor has to be fetched.
     * @return Command executor.
     */
    public CommandExecutor getCommandExecutor(final Command command) throws InvalidCommandException {
        final CommandExecutor commandExecutor = commands.get(command.getCommandName());
        if (commandExecutor == null) {
            throw new InvalidCommandException("Command not found!");
        }
        return commandExecutor;
    }
}
