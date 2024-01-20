package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }


    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Account getAccount(Principal principal){
        try{
            User user = userDao.getUserByUsername(principal.getName());
            return accountDao.getAccountUserId(user.getId());
        }
        catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }

    @RequestMapping(path = "/accounts", method = RequestMethod.GET)
    public List<Account> getAccounts(){
        List<Account> accounts = null;
        try {
           accounts = accountDao.getAccounts();
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users available");
        }
        return accounts;
    }

}
