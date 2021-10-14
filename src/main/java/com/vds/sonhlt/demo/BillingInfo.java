package com.vds.sonhlt.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String username;
    private String description;
    private String otp;

    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("money")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("transaction_id")
    public String getTransactionCode() {
        return transactionCode;
    }

    @JsonProperty("soft_otp")
    public String getOtp() {
        return otp;
    }
}
