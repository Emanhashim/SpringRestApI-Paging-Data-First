package com.bazra.usermanagement.response;

import java.math.BigDecimal;
import java.util.Date;

public class WithdrawResponse {
    
    private BigDecimal amount;
    private Date time;
    private String message;
    
    
    public WithdrawResponse( BigDecimal amount, Date timestamp, String message) {
        
    
        this.amount = amount;
        this.time = timestamp;
        this.message= message;
       }


    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Date getTime() {
        return time;
    }


    public void setTime(Date time) {
        this.time = time;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
