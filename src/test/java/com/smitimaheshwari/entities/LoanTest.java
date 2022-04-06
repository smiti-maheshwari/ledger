package com.smitimaheshwari.entities;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoanTest {

    @Test
    public void getOrAddBankExistingAlreadyTest() {
        final Ledger ledger = getLedgerObject();
        assertEquals("IDIDI", ledger.getOrAddBank("IDIDI").getName());
    }

    @Test
    public void getOrAddBankNewAdditionTest() {
        final Ledger ledger = getLedgerObject();
        assertEquals("MBI", ledger.getOrAddBank("MBI").getName());
    }

    @Test
    public void containsBankTrueTest() {
        final Ledger ledger = getLedgerObject();
        assertTrue(ledger.containsBank("IDIDI"));
    }

    @Test
    public void containsBankFalseTest() {
        final Ledger ledger = getLedgerObject();
        assertFalse(ledger.containsBank("UON"));
    }

    /**
     * Passing null attribute would cause Null Pointer Exception
     */
    @Test(expected = NullPointerException.class)
    public void nonNullAttributesTest() {
        Ledger.builder().build();
    }

    private Ledger getLedgerObject() {
        final Map<String, Bank> banks = new HashMap<>();
        final Map<String, Customer> customers = new HashMap<>();
        final List<LumpSum> lumpSums = new ArrayList<>();
        final Loan loan = Loan.builder().emiAmount(200).interestRate(5).principalAmount(10000).years(4).totalAmount(12000).lumpSums(lumpSums).build();
        customers.put("Dale", Customer.builder().name("Dale").loan(loan).build());
        banks.put("IDIDI", Bank.builder().name("IDIDI").customers(customers).build());
        return Ledger.builder().banks(banks).build();
    }
}
