package com.smitimaheshwari.commands;

import com.smitimaheshwari.exceptions.BalanceCalculationException;
import com.smitimaheshwari.utils.OutputPrinter;
import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.interfaces.LedgerService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Executor to handle BALANCE command.
 */
public class BalanceCommandExecutor extends CommandExecutor {

    public static final String COMMAND_NAME = "BALANCE";

    public BalanceCommandExecutor(final OutputPrinter outputPrinter, final LedgerService ledgerService) {
        super(outputPrinter, ledgerService);
    }

    /**
     Valid BALANCE command looks like - BALANCE BANK_NAME BORROWER_NAME EMI_NO
    */
    @Override
    public boolean validate(final Command command) {
        final List<String> params = command.getParams();
        return params.size() == 3 && StringUtils.isNotBlank(params.get(0))
                && StringUtils.isNotBlank(params.get(1)) && params.get(2).matches("\\d+");
    }

    @Override
    public void execute(final Command command) {
        final List<String> params = command.getParams();
        final String bankName = params.get(0);
        final String customerName = params.get(1);
        final int emiNumber = Integer.parseInt(params.get(2));
        try {
            ledgerService.calculateBalance(bankName, customerName, emiNumber);
        } catch(final BalanceCalculationException e) {
            outputPrinter.printError(e.getMessage());
        }
    }
}
