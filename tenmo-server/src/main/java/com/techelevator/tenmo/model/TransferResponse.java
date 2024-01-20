package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferResponse {
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

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public TransferResponse() {
    }

    public TransferResponse(int senderId, int receiverId, String transferType, String transferStatus, BigDecimal transferAmount, int transferId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.transferAmount = transferAmount;
        this.transferId = transferId;
    }

    private int senderId;
    private int receiverId;
    private String transferType;
    private String transferStatus;
    private BigDecimal transferAmount;
    private int transferId;
}
