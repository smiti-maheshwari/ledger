package com.smitimaheshwari.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class LumpSum {
    @NonNull private int emiNumber;
    @NonNull private long lumpSumAmount;
}
