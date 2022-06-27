package com.bazra.usermanagement.response;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceResponse {
    private BigDecimal amount;
    private String message;
    private String username;
    
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public BalanceResponse( BigDecimal amount, String message, String username) {
        
        this.username=username;
        this.amount = amount;
        this.message= message;
       }
    public BalanceResponse(String message) {
        this.message=message;
    }
}
