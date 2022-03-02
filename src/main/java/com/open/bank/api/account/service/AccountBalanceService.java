package com.open.bank.api.account.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.open.bank.api.account.entity.AccountBalance;
import com.open.bank.api.account.entity.Accounts;
import com.open.bank.api.account.entity.PostAccountTxnRequestBody;
import com.open.bank.api.account.exception.AccountNotFoundException;
import com.open.bank.api.account.exception.AccountOpNotSupportedException;
import com.open.bank.api.account.repository.AccountBalanceRepository;
import com.open.bank.api.account.repository.AccountsRepository;

@Service
public class AccountBalanceService {
	private static final Logger log = LoggerFactory.getLogger(AccountBalanceService.class);
	
	@Autowired
	private AccountsRepository accountsRepo;
	
	@Autowired
	private AccountBalanceRepository accountBalanceRepo;
	
	public List<Accounts> getAccountInfo(String username) {
		return accountsRepo.findByUsername(username)
				.orElseThrow(() -> new AccountNotFoundException("Cannot find any account with your token"));
	}
	
	public List<Accounts> getAccountInfoByCurrency(String username, String currency) {
		Optional<List<Accounts>> opAccountList = accountsRepo.findByUsernameAndCurrency(username, currency);
		if(opAccountList.isPresent()) {
			List<Accounts> accountList = opAccountList.get();
			for(Accounts acc : accountList) {
				List<AccountBalance> abList = acc.getAccountBalance();
				List<AccountBalance> filteredAbList = abList.stream().filter(e -> e.getCurrency().equals(currency)).collect(Collectors.toCollection(ArrayList::new));
				acc.setAccountBalance(filteredAbList);
			}
			return accountList;
		} else {
			throw new AccountNotFoundException(String.format("Cannot find any account with currency %s with your token", currency));
		}
//		return accountsRepo.findByUsernameAndCurrency(username, currency).orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find any account with currency %s with your token", currency)));
	}
	
	public List<AccountBalance> getOneAccountBalance(String accountNumber, String username) {
		return accountBalanceRepo.findByAccountNumberAndUsername(accountNumber, username)
				.orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find the balance for the account %s with your token", accountNumber)));
	}
	
	public AccountBalance getOneAccountBalanceByCurrency(String accountNumber, String currency, String username) {
		return accountBalanceRepo.findByAccountNumberAndCurrencyAndUsername(accountNumber, currency, username)
				.orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find the balance for the account %s, currency %s with your token", accountNumber, currency)));
	}
	
	public AccountBalance postAccountOp(String accountNumber, PostAccountTxnRequestBody requestBody, String username) {
		BigDecimal requestTxnAmount = requestBody.getTxnAmount();
		String requestCurrency = requestBody.getCurrency();
		String requestAccountOp = requestBody.getAccountOp();
		
		log.info(String.format("%s, %s, %s, %s", accountNumber, requestTxnAmount, requestCurrency, requestAccountOp));
		
		if(!requestAccountOp.equals("DR") && !requestAccountOp.equals("CR")) {
			throw new AccountOpNotSupportedException("Account operation not supported, should be either 'DR' or 'CR'");
		}
		
		try {
			AccountBalance ab = getOneAccountBalanceByCurrency(accountNumber, requestCurrency, username);
			if(requestAccountOp.equals("DR")) {
				ab.setCurrentBalance(ab.getCurrentBalance().subtract(requestTxnAmount));
				ab.setLastUpdateTime(ZonedDateTime.now());
			} else if(requestAccountOp.equals("CR")) {
				ab.setCurrentBalance(ab.getCurrentBalance().add(requestTxnAmount));
				ab.setLastUpdateTime(ZonedDateTime.now());
			}
			
			return accountBalanceRepo.saveAndFlush(ab);
		} catch(AccountNotFoundException ex) {
			throw(ex);
		}
	}

}
