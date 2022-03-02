package com.open.bank.api.account.entity;

import java.math.BigDecimal;

public class PostAccountTxnRequestBody {
    private BigDecimal txnAmount;
    private String currency;
    private String accountOp;

    public BigDecimal getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(BigDecimal txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountOp() {
        return accountOp;
    }

    public void setAccountOp(String accountOp) {
        this.accountOp = accountOp;
    }
}
