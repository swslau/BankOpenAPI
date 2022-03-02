package com.open.bank.api.account.entity;

import java.beans.JavaBean;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JavaBean
@JsonPropertyOrder({ "currentBalance", "currency" })
public class CurrencyBalance {
	private BigDecimal currentBalance;
	
	private String currency;
	
	public CurrencyBalance(BigDecimal currentBalance, String currency) {
		this.currentBalance = currentBalance;
		this.currency = currency;
	}
	
	public BigDecimal getCurrentBalance() {
		return this.currentBalance;
	}
	
	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
