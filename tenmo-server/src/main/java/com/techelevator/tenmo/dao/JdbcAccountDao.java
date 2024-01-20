package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements  AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountUserId(int userId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public Account createAccount(int userId, double balance) {
        Account account = null;
        String sql = "INSERT INTO account (user_id, balance) " +
                "VALUES(?, ?) RETURNING account_id";
        try {
           int accountId = jdbcTemplate.queryForObject(sql, int.class, userId, balance);
           account = getAccountByAccountId(accountId);
        }  catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation ", e);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(int id) {
        Account account = null;
        String sql = "SELECT * FROM account where account_id=?";
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if(rowSet.next()){
                account = mapRowToAccount(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        } catch ( DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation ", e);
        }

        return account;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Account account = mapRowToAccount(results);
                accounts.add(account);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }


    @Override
    public Account updateAccount(Account updatedAccount) {
        Account account = null;
        String sql = "UPDATE account " +
                "SET user_id=?, " +
                "balance=? " +
                "WHERE account_id=?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    updatedAccount.getUserId(),
                    updatedAccount.getBalance(),
                    updatedAccount.getAccountId());
            if (rowsAffected > 0) {
                account = getAccountByAccountId(updatedAccount.getAccountId());
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation: ", e);
        }
        return account;
    }




    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}
