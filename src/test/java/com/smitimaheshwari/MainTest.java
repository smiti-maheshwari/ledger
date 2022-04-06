package com.smitimaheshwari;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * This is not a unit test as it's running the system completely. More like an Integration Test.
 */
public class MainTest {

    private InputStream sysInBackup;
    private PrintStream sysOutBackup;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        sysInBackup = System.in; // backup System.in to restore it later
        sysOutBackup = System.out; // backup System.out to restore it later
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() throws Exception {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }

    @Test
    public void testMain() throws IOException {
        final String expectedOutput =
                "IDIDI Dale 1000 55\n" +
                        "IDIDI Dale 8000 20\n" +
                        "MBI Harry 1044 12\n" +
                        "MBI Harry 0 24\n";
        Main.main(new String[] {"test_input.txt"});
        assertEquals(expectedOutput, outContent.toString());
    }
}
