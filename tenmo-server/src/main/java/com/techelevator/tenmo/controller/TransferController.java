package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private final UserDao userDao;

    @Autowired
    private final AccountDao accountDao;

    @Autowired
    private final TransferService transferService;

    public TransferController(UserDao userDao, AccountDao accountDao, TransferService transferService) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferService = transferService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void userToUserTransfer(@RequestBody TransferRequest transferRequest) {
        try {
            Transfer transfer = mapTransferRequestToTransfer(transferRequest);
            User recipient = userDao.getUserById((transferRequest.getReceiverId()-1000));
            User user = userDao.getUserById(transferRequest.getSenderId());

            if (recipient != null && user != null && !recipient.equals(user)) {
                Account senderAccount = accountDao.getAccountUserId(user.getId());
                Account recipientAcct = accountDao.getAccountUserId(recipient.getId());
                BigDecimal transferAmt = transferRequest.getAmount();

                //fixed statement below
                if (transferAmt.doubleValue() > 0 && senderAccount.getBalance() > transferAmt.doubleValue()) {
                    recipientAcct.setBalance(recipientAcct.getBalance() + transferAmt.doubleValue());
                    senderAccount.setBalance(senderAccount.getBalance() - transferAmt.doubleValue());
                    transferService.createTransfer(transfer);
                    // call account dao twice to update the accounts
                    accountDao.updateAccount(senderAccount);
                    accountDao.updateAccount(recipientAcct);
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path="/{userId}/transferHistory", method = RequestMethod.GET)
    public List<TransferResponse> getUserTransfersById(@PathVariable int userId){
        List<TransferResponse> transferResponses = new ArrayList<>();
        try{
            List<Transfer> transfers = transferService.getTransfersByUserId(userId);
            if(transfers != null) {
                for (Transfer transfer : transfers) {
                    transferResponses.add(mapTransferToTransferResponse(transfer));
                }
            }
        } catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers found");
        }
        return transferResponses;
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/{userid}/transferHistory/{transferId}", method = RequestMethod.GET)
    public TransferResponse getTransferById(@PathVariable int userid, @PathVariable int transferId){
        TransferResponse transferResponse = null;
        try{
            Transfer transfer = transferService.getTransferById(transferId);
            if(transfer != null) {
                transferResponse = mapTransferToTransferResponse(transfer);
            }
        } catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer with id " + transferId + " NOT FOUND");
        }
        return transferResponse;
    }
    private Transfer mapTransferRequestToTransfer(TransferRequest transferRequest) {
        Transfer transfer = new Transfer();
        transfer.setAmount(transferRequest.getAmount());
        transfer.setAccountTo(transferRequest.getReceiverId());
        transfer.setAccountFrom(transferRequest.getSenderId());
        return transfer;
    }

    private TransferResponse mapTransferToTransferResponse(Transfer transfer) {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setTransferId(transfer.getId());
        transferResponse.setTransferAmount(transfer.getAmount());
        transferResponse.setTransferStatus(transferService.getTransferStatusDescription(transfer.getId()));
        transferResponse.setTransferType(transferService.getTransferTypeDescription(transfer.getId()));

        //Get user via account id
        transferResponse.setReceiverId(userDao.getUserById(transfer.getAccountTo() - 1000).getId());
        transferResponse.setSenderId(userDao.getUserById(transfer.getAccountFrom() - 1000).getId());

        return transferResponse;

    }
}