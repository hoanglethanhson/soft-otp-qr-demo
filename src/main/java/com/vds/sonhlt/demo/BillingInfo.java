package com.vds.sonhlt.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingInfo {
    private String phoneNumber;
    private String name;
    private String amount;
    private String transactionCode;
}
