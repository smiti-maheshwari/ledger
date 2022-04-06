package com.smitimaheshwari.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
@Builder
public class Loan {

    @NonNull private long principalAmount;
    @NonNull private int years;
    @NonNull private int interestRate;
    @NonNull private long totalAmount;
    @NonNull private long emiAmount;
    @NonNull private List<LumpSum> lumpSums;

    public void addLumpSumRecord(final LumpSum lumpSum) {
        lumpSums.add(lumpSum);
    }

}
