package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(int id) {
        String sql = "SELECT * FROM transfer WHERE transfer_id=?";
        Transfer transfer = null;
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                transfer = mapToTransfer(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        }
        return transfer;
    }

    private Transfer mapToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setId(rowSet.getInt("transfer_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * from transfer";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                transfers.add(mapToTransfer(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        }

        return transfers;
    }

    @Override
    public Transfer saveTransfer(Transfer userTransfer) {
        Transfer transfer = null;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(?, ?, ?, ?, ?) RETURNING transfer_id";
        try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class,
                    userTransfer.getTransferTypeId(),
                    userTransfer.getTransferStatusId(),
                    userTransfer.getAccountFrom() + 1000,
                    userTransfer.getAccountTo(),
                    userTransfer.getAmount());
            transfer = getTransferById(transferId);
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation ", e);
        }
        return transfer;
    }

    @Override
    public Transfer updateTransfer(Transfer updatedTransfer) {
        Transfer transfer = null;
        String sql = "UPDATE transfer " +
                "SET transfer_type_id=?," +
                "transfer_status_id=?, " +
                "account_from=?, " +
                "account_to=?, " +
                "amount=?, " +
                "WHERE transfer_id=?";
        try {
            jdbcTemplate.update(sql, updatedTransfer.getTransferTypeId(),
                    updatedTransfer.getTransferStatusId(),
                    updatedTransfer.getAccountFrom(),
                    updatedTransfer.getAccountTo(),
                    updatedTransfer.getAmount(),
                    updatedTransfer.getId());
            transfer = getTransferById(updatedTransfer.getId());
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation: ", e);
        }
        return transfer;
    }

    @Override
    public void deleteTransfer(int id) {
        String sql = "DELETE FROM transfer WHERE transfer_id=?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if(rowsAffected == 0){
            throw new DaoException("Transfer with id " + id + " does not exist");
        }
    }

    @Override
    public List<Transfer> getTransferByAccountId(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE aocount_from=? OR account_to=?";
        try {
           SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id, id);
           while(rowSet.next()){
               transfers.add(mapToTransfer(rowSet));
           }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        } catch ( DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation ", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransferByUserId(int id) {
        id = id + 1000; // temp fix
        List<Transfer> transfers = new ArrayList<>();
 //       String sql = "SELECT t.* FROM transfer t JOIN accounts a_sender ON t.account_from = a_sender.id " +
 //               "JOIN accounts a_receiver ON t.account_to = a_receiver.id " +
 //               "WHERE a_sender.user_id=? OR a_receiver.user_id=?";

        String sql = "SELECT t.* FROM transfer t JOIN account a_sender ON t.account_from = a_sender.account_id " +
                "JOIN account a_receiver ON t.account_to = a_receiver.account_id  " +
                "WHERE a_sender.account_id=? OR a_receiver.account_id=?";
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id, id);
            while(rowSet.next()){
                transfers.add(mapToTransfer(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database ", e);
        } catch ( DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation ", e);
        }
        return transfers;
    }

    @Override
    public String getTransferTypeDesc(int transferId) {
        String transferTypeDesc = "";
        String sql = "SELECT tt.transfer_type_desc FROM transfer_type tt JOIN transfer t USING(transfer_type_id)" +
                " WHERE t.transfer_id=?";
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
            if(rowSet.next()){
                transferTypeDesc = rowSet.getString("transfer_type_desc");
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database: ", e);
        } catch(DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation: ", e);
        }
        return transferTypeDesc;
    }

    @Override
    public String getTransferStatusDesc(int transferId) {
        String transferStatusDesc = "";
        String sql = "SELECT ts.transfer_status_desc FROM transfer_status ts JOIN transfer t USING(transfer_status_id)" +
                " WHERE t.transfer_id=?";
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
            if(rowSet.next()){
                transferStatusDesc = rowSet.getString("transfer_status_desc");
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to server or database: ", e);
        } catch(DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation: ", e);
        }
        return transferStatusDesc;
    }


}
