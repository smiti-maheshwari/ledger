package com.smitimaheshwari.entities;

import com.smitimaheshwari.exceptions.InvalidCommandException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity to represent an input command.
 */
@Getter
public class Command {

    private static final String SPACE = " ";
    private String commandName;
    private List<String> params;

    /**
     * Constructor. It takes the input line and parses the command name and param out of it. If the
     * command or its given params are not valid, then {@link InvalidCommandException} is thrown.
     *
     * @param inputLine Given input command line.
     */
    public Command(final String inputLine) {
        final List<String> tokensList = Arrays.stream(inputLine.trim().split(SPACE))
                .map(String::trim)
                .filter(token -> (token.length() > 0)).collect(Collectors.toList());

        if (tokensList.isEmpty()) {
            throw new InvalidCommandException("No Params Found");
        }
        commandName = tokensList.get(0);
        tokensList.remove(0);
        params = tokensList;
    }
}
