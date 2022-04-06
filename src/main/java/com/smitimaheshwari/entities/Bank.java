package com.smitimaheshwari.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

@Getter
@Builder
public class Bank {
    @NonNull private String name;
    @NonNull private Map<String, Customer> customers;

    public boolean containsCustomer(final String customerName) {
        return customers.containsKey(customerName);
    }
}
