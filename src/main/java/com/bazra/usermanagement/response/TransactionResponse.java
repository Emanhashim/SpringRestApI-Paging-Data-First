package com.bazra.usermanagement.response;

import java.util.List;

import com.bazra.usermanagement.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
public class TransactionResponse {
	@JsonProperty(value = "summary", required = true)
    private List<Transaction> transaction;
	
	public List<Transaction> getTransaction() {
        return transaction;
    }
    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }
   
    public TransactionResponse(List<Transaction> transaction) {
        this.transaction= transaction;
        
        
    }

}
