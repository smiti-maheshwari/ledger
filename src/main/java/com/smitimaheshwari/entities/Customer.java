package com.smitimaheshwari.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class Customer {
    @NonNull private String name;
    @NonNull private Loan loan;
}
