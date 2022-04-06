package com.smitimaheshwari.entities;

import com.smitimaheshwari.exceptions.InvalidCommandException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CommandTest {

    @Test
    public void testCommandParsingFromInput() {
        validateCommandParsing("my_command 1 2 3", "my_command", Arrays.asList("1", "2", "3"));
        validateCommandParsing("my_command   1  2 ", "my_command", Arrays.asList("1", "2"));
        validateCommandParsing("my_command", "my_command", Collections.emptyList());
        validateCommandParsing("  my_command     ", "my_command", Collections.emptyList());
    }

    @Test(expected = InvalidCommandException.class)
    public void testCommandParsingFromInputHavingOnlySpaces() {
        Command command = new Command("   ");
    }

    @Test(expected = InvalidCommandException.class)
    public void testCommandParsingFromEmptyInput() {
        Command command = new Command("");
    }

    private void validateCommandParsing(
            final String input, final String expectedCommandName, final List expectedParams) {
        Command command = new Command(input);
        assertNotNull(command);
        assertEquals(expectedCommandName, command.getCommandName());
        assertEquals(expectedParams, command.getParams());
    }
}
