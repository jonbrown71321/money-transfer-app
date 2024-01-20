package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferResponse {
    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public TransferResponse(int transferId, int senderId, int receiverId, BigDecimal transferAmount) {
        this.transferId = transferId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transferAmount = transferAmount;
    }
    public TransferResponse(int transferId, int senderId, int receiverId, BigDecimal transferAmount, String transferType, String transferStatus){
        this(transferId,senderId, receiverId, transferAmount);
        this.transferStatus = transferStatus;
        this.transferType = transferType;
    }
    public TransferResponse() {
    }

    private int transferId;
    private int senderId;
    private int receiverId;

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    private String transferType;
    private String transferStatus;
    private BigDecimal transferAmount;
}