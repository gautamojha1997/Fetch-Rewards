package com.example.fetchrewardscoding.Entity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Users {

    //User Id for each users
    private int userId;

    //Total Reward Points of the user
    private long totalRewardPoints;

    //Map<Payer Name, Payer Points> for the user
    private Map<String, Long> pointsPerPayer = new HashMap<>();

    //Priority Queue of transaction for each user based on timestamp
    private PriorityQueue<Transactions> transactionsQueue;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTotalrewardPoints() {
        return totalRewardPoints;
    }

    public void setTotalrewardPoints(long totalrewardPoints) {
        this.totalRewardPoints = totalrewardPoints;
    }

    public Map<String, Long> getPointsPerPayer() {
        return pointsPerPayer;
    }

    public void setPointsPerPayer(Map<String, Long> pointsPerPayer) {
        this.pointsPerPayer = pointsPerPayer;
    }

    public PriorityQueue<Transactions> getTransactionsQueue() {
        return transactionsQueue;
    }

    public void setTransactionsQueue(PriorityQueue<Transactions> transactionsQueue) {
        this.transactionsQueue = transactionsQueue;
    }

    public Users(int userId) {
        this.userId = userId;
        this.totalRewardPoints = 0;
        this.pointsPerPayer = pointsPerPayer;
        this.transactionsQueue = new PriorityQueue<>(new Comparator<Transactions>() {
            @Override
            public int compare(Transactions o1, Transactions o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });
    }
}
