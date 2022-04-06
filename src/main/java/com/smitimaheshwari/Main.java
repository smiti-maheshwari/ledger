package com.smitimaheshwari;


import com.smitimaheshwari.commands.CommandExecutorFactory;
import com.smitimaheshwari.entities.Bank;
import com.smitimaheshwari.entities.Ledger;
import com.smitimaheshwari.inputmode.FileMode;
import com.smitimaheshwari.impl.LedgerServiceImpl;
import com.smitimaheshwari.utils.OutputPrinter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(final String[] args) throws IOException {
        final OutputPrinter outputPrinter = new OutputPrinter();
        final Map<String, Bank> banks = new HashMap<>();
        final Ledger ledger = Ledger.builder().banks(banks).build();
        final LedgerServiceImpl ledgerServiceImpl = LedgerServiceImpl.builder().ledger(ledger).outputPrinter(outputPrinter).build();
        final CommandExecutorFactory commandExecutorFactory = new CommandExecutorFactory(outputPrinter, ledgerServiceImpl);
        final FileMode fileMode = FileMode.builder().commandExecutorFactory(commandExecutorFactory)
                .outputPrinter(outputPrinter).fileName(args[0]).build();
        fileMode.process();
    }
}
