package com.example.fetchrewardscoding.Entity;

import java.time.LocalDateTime;

public class Transactions {

    //Transaction Id
    private int transactionId;

    //Payer Name for the transaction
    private String payerName;

    //Points for the transaction
    private long points;

    //TimeStamp of the transaction
    private LocalDateTime timestamp;

    public Transactions(int transactionId, String payerName, long points, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.payerName = payerName;
        this.points = points;
        this.timestamp = timestamp;
    }


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
