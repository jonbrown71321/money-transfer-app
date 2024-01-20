package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account getAccountUserId(int id);
    Account createAccount(int userId, double balance);
    Account getAccountByAccountId(int id);

    List<Account> getAccounts();

    Account updateAccount(Account account);



}
