package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferRequest {
    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    private int senderId;
    private int recipientId;
    private BigDecimal transferAmount;

    public TransferRequest(int senderId, int recipientId, BigDecimal transferAmount){
        this.senderId = senderId;
        this.transferAmount = transferAmount;
        this.recipientId = recipientId;
    }

    public TransferRequest(){}
}
