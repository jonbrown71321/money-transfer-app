package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

@Service
public class TransferService {
    private final TransferDao transferDao;
    private final UserDao userDao;

    @Autowired
    public TransferService(TransferDao transferDao, UserDao userDao){
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    public Transfer getTransferById(int id){
        return transferDao.getTransferById(id);
    }

    public List<Transfer> getTransfers(){
        return transferDao.getAllTransfers();
    }

    public void createTransfer(Transfer transfer){
        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(2);
        transferDao.saveTransfer(transfer);
    }

    public Transfer updateTransfer(Transfer transfer){
        return transferDao.updateTransfer(transfer);
    }

    public void deleteTransfer(int id){
        transferDao.deleteTransfer(id);
    }

    public List<Transfer> getTransfersByUserId(int id){
        return transferDao.getTransferByUserId(id);
    }
    public List<Transfer> getTransfersByUsername(String username){
        List<Transfer> transfers = null;
        User user = userDao.getUserByUsername(username);
        if(user != null){
            transfers = getTransfersByUserId(user.getId());
        }
        return transfers;
    }

    public List<Transfer> getTransfersByAccountId(int id){
        return transferDao.getTransferByAccountId(id);
    }
    public String getTransferTypeDescription(int transferId){
        return transferDao.getTransferTypeDesc(transferId);
    }

    public String getTransferStatusDescription(int transferId){
        return transferDao.getTransferStatusDesc(transferId);
    }
}
