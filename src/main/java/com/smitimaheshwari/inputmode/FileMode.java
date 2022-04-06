package com.smitimaheshwari.inputmode;

import com.smitimaheshwari.utils.OutputPrinter;
import com.smitimaheshwari.commands.CommandExecutor;
import com.smitimaheshwari.commands.CommandExecutorFactory;
import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.InvalidCommandException;
import lombok.Builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Builder
public class FileMode {

    private CommandExecutorFactory commandExecutorFactory;
    private String fileName;
    protected OutputPrinter outputPrinter;

    /**
     * Helper method to process a command. It basically uses {@link CommandExecutor} to run the given
     * command.
     * @param command Command to be processed.
   */
    private void processCommand(final Command command) {
        final CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(command);
        if (commandExecutor.validate(command)) {
            commandExecutor.execute(command);
        } else {
            throw new InvalidCommandException("Command Invalid!");
        }
    }

    public void process() throws IOException {
        final File file = new File(fileName);
        final BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (final FileNotFoundException e) {
            outputPrinter.invalidFile(e.getMessage());
            return;
        }

        String input = reader.readLine();
        while (input != null) {
            final Command command = new Command(input);
            try {
                processCommand(command);
            } catch (InvalidCommandException e) {
                outputPrinter.printError(e.getMessage());
            }
            input = reader.readLine();
        }
    }
}
