package com.smitimaheshwari.utils;

public class OutputPrinter {

    public void invalidFile(final String errorMessage) {
        printError(String.format("Invalid file given: %s", errorMessage));
    }

    public void printNewLine(final String msg) {
        System.out.println(msg);
    }

    public void printError(final String msg) {
        System.err.println(msg);
    }
}
