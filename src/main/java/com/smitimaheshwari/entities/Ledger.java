package com.smitimaheshwari.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class Ledger {

    /**
     * Stores the bank information (bankName -> bankObject).
     */
    @NonNull Map<String, Bank> banks;

    /**
     * This gets the bank in the ledger if the bank exists already, otherwise it first adds the bank in the ledger and then returns it.
     *
     * @param bankName
     * @return the bank record in the ledger.
     */
    public Bank getOrAddBank(final String bankName) {
        if(!banks.containsKey(bankName)) {
            final Map<String, Customer> customers = new HashMap<>();
            final Bank bank = Bank.builder().name(bankName).customers(customers).build();
            banks.put(bankName, bank);
        }
        return banks.get(bankName);
    }

    /**
     * Checks if ledger contains the bank.
     * @param bankName
     * @return true if the bank record exists, false otherwise.
     */
    public boolean containsBank(final String bankName) {
        return banks.containsKey(bankName);
    }
}
