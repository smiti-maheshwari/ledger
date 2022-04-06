package com.smitimaheshwari.commands;

import com.smitimaheshwari.utils.OutputPrinter;
import com.smitimaheshwari.entities.Command;
import com.smitimaheshwari.exceptions.PaymentRecordException;
import com.smitimaheshwari.interfaces.LedgerService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class PaymentCommandExecutor extends CommandExecutor {

    public static final String COMMAND_NAME = "PAYMENT";

    public PaymentCommandExecutor(final OutputPrinter outputPrinter, final LedgerService ledgerService) {
        super(outputPrinter, ledgerService);
    }

    /**
     Valid PAYMENT command looks like - Format - PAYMENT BANK_NAME BORROWER_NAME LUMP_SUM_AMOUNT EMI_NO
     */
    @Override
    public boolean validate(final Command command) {
        final List<String> params = command.getParams();
        return params.size() == 4 && StringUtils.isNotBlank(params.get(0))
                && StringUtils.isNotBlank(params.get(1)) && params.get(2).matches("\\d+") && params.get(3).matches("\\d+");
    }

    @Override
    public void execute(final Command command) {
        final List<String> params = command.getParams();
        final String bankName = params.get(0);
        final String customerName = params.get(1);
        final long lumpSumAmount = Integer.parseInt(params.get(2));
        final int emiNumber = Integer.parseInt(params.get(3));
        try {
            ledgerService.recordPayment(bankName, customerName, lumpSumAmount, emiNumber);
        } catch(final PaymentRecordException e) {
            outputPrinter.printError(e.getMessage());
        }
    }
}
