package com.bazra.usermanagement.model;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Transaction")
public class Transaction {
   

    @Id
    @GeneratedValue

//  private String title;
    private Long transactionId;
    private Long userID;
    private String accountNumber;
    private String fromAccountNumber;

    private BigDecimal transactionAmount;

    private Date transactionDateTime;
    
    private String transaction_type;
//    
//    private Transaction() {
//        
//    }


//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

    

//    public Transaction() {
//		super();
//		// TODO Auto-generated constructor stub
//	}

	public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public String getaccountNumber() {
		return accountNumber;
	}

	public void setaccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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

    public Transaction(  String accountNumber, BigDecimal amount, Date timestamp) {
    
     this.accountNumber = accountNumber;
     this.transactionAmount = amount;
     this.transactionDateTime = timestamp;
    }
    public Transaction(Long userID, String fromAccountNumber,  String accountNumber, BigDecimal amount, Date timestamp,String transaction_type) {
    	this.userID=userID;
    	this.fromAccountNumber=fromAccountNumber;
        this.accountNumber = accountNumber;
        this.transactionAmount = amount;
        this.transactionDateTime = timestamp;
        this.transaction_type=transaction_type;
       }
 
    public Transaction(BigDecimal amount, Date timestamp) {
        this.transactionAmount=amount;
        this.transactionDateTime=timestamp;
        
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", userID=" + userID +
                ", accountNumber='" + accountNumber + '\'' +
                ", fromAccountNumber='" + fromAccountNumber + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", transactionDateTime=" + transactionDateTime +
                ", transaction_type='" + transaction_type + '\'' +
                '}';
    }
}
