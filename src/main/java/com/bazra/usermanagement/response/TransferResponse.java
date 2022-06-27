package com.bazra.usermanagement.response;

import java.math.BigDecimal;
import java.util.Date;

public class TransferResponse {
    private String accountNumber;

    private BigDecimal transactionAmount;

    private Date transactionDateTime;
    
    private String message;
    
    
    public TransferResponse(  String toAccountNumber, BigDecimal amount, Date timestamp, String message) {
        
        this.accountNumber = toAccountNumber;
        this.transactionAmount = amount;
        this.transactionDateTime = timestamp;
        this.message= message;
       }


    public String getAccountNumber() {
        return accountNumber;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }


    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    public Date getTransactionDateTime() {
        return transactionDateTime;
    }


    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
