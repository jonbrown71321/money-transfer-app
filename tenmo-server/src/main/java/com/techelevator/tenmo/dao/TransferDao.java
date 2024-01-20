package com.techelevator.tenmo.dao;

import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    Transfer getTransferById(int id);
    List<Transfer> getAllTransfers();

    Transfer saveTransfer(Transfer userTransfer);

    Transfer updateTransfer(Transfer updatedTransfer);

    void deleteTransfer(int id);

    List<Transfer> getTransferByAccountId(int id);
    List<Transfer> getTransferByUserId(int id);

    String getTransferTypeDesc(int transferId);
    String getTransferStatusDesc(int transferId);

}
