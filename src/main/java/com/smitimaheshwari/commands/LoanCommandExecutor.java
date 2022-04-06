package com.smitimaheshwari.commands;

import com.smitimaheshwari.utils.OutputPrinter;
import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.LoanAdditionException;
import com.smitimaheshwari.interfaces.LedgerService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Executor to handle LOAN command.
 */
public class LoanCommandExecutor extends CommandExecutor {

    public static final String COMMAND_NAME = "LOAN";

    public LoanCommandExecutor(final OutputPrinter outputPrinter, final LedgerService ledgerService) {
        super(outputPrinter, ledgerService);
    }

    /**
     Valid LOAN command looks like - LOAN BANK_NAME BORROWER_NAME PRINCIPAL NO_OF_YEARS RATE_OF_INTEREST
     */
    @Override
    public boolean validate(final Command command) {
        final List<String> params = command.getParams();
        return params.size() == 5 && StringUtils.isNotBlank(params.get(0))
                && StringUtils.isNotBlank(params.get(1)) && params.get(2).matches("\\d+")
                && params.get(3).matches("\\d+") && params.get(4).matches("\\d+");
    }

    @Override
    public void execute(final Command command) {
        final List<String> params = command.getParams();
        final String bankName = params.get(0);
        final String customerName = params.get(1);
        final long principal = Integer.parseInt(params.get(2));
        final int years = Integer.parseInt(params.get(3));
        final int interestRate = Integer.parseInt(params.get(4));
        try {
            ledgerService.addLoan(bankName, customerName, principal, years, interestRate);
        } catch(final LoanAdditionException e) {
            outputPrinter.printError(e.getMessage());
        }
    }
}
