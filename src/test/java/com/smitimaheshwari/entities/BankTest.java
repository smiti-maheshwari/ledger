package com.smitimaheshwari.entities;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BankTest {
    @Test
    public void testContainsCustomerTrue() {
        final Bank bank = getBankObject();
        assertTrue(bank.containsCustomer("Dale"));
    }

    @Test
    public void testContainsCustomerFalse() {
        final Bank bank = getBankObject();
        assertFalse(bank.containsCustomer("Harry"));
    }

    /**
     * Passing null attribute would cause Null Pointer Exception
     */
    @Test(expected = NullPointerException.class)
    public void testNonNullAttributes() {
        Bank.builder().build();
    }

    private Bank getBankObject() {
        final Map<String, Customer> customers = new HashMap<>();
        final List<LumpSum> lumpSums = new ArrayList<>();
        final Loan loan = Loan.builder().emiAmount(200).interestRate(5).principalAmount(10000).years(4).totalAmount(12000).lumpSums(lumpSums).build();
        customers.put("Dale", Customer.builder().name("Dale").loan(loan).build());
        return Bank.builder()
                .name("IDIDI").customers(customers).build();
    }

}
