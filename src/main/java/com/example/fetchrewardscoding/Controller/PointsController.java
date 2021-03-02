package com.example.fetchrewardscoding.Controller;

import com.example.fetchrewardscoding.Entity.Transactions;
import com.example.fetchrewardscoding.Entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PointsController {

    //logger for logging
    Logger logger = LoggerFactory.getLogger(PointsController.class);

    private static int transactionCounter = 0;
    private static final Map<Integer, Users> users = new HashMap<>();

    //Retrieve all the transactions of all users in the system.
    @GetMapping("/transactions")
    public Map<Integer, PriorityQueue<Transactions>> getUsersTransactions() {
        Map<Integer, PriorityQueue<Transactions>> usersTransactions = new HashMap<>();

        users.entrySet().forEach((e)-> usersTransactions.put(e.getKey(),e.getValue().getTransactionsQueue()));
        return usersTransactions;
    }

    //Retrieve each user transaction by userId
    @GetMapping("/transactions/{id}")
    public ResponseEntity<PriorityQueue<Transactions>> getTransactionById(@Validated @PathVariable(name = "id") int id){
        if(users.containsKey(id)){
            PriorityQueue<Transactions> transactionEachUser = users.get(id).getTransactionsQueue();
            return ResponseEntity.ok().body(transactionEachUser);
        }
        else {
            throw new RuntimeException("Invalid User Id!!");
        }
    }

    //Post request to add points for each transactions for users with specified userId
    @PostMapping("/addPoints/{id}")
    public ResponseEntity<Transactions> addPoints(@Validated @PathVariable int id, @Validated @RequestBody Transactions transactions){

        Users user;

        //Incrementing the transaction counter to use it as id for all new transactions
        transactionCounter++;

        //Get all the transactions details
        //Set the transaction Id.
        transactions.setTransactionId(transactionCounter);

        //set the transaction timestamp to now time.
        transactions.setTimestamp(LocalDateTime.now());

        //set payer name for the transaction
        String payer = transactions.getPayerName();

        //current transaction point
        long point = transactions.getPoints();

        //Create new user if user is not created
        if(!users.containsKey(id)){
            user = new Users(id);
            users.put(user.getUserId(), user);
        }
        else {
            user = users.get(id);
        }

        //Get User Total Rewards
        long totalPoints = user.getTotalrewardPoints();

        //Get User rewards map for each payer
        Map<String, Long> pointsPerPayer = user.getPointsPerPayer();

        //Get User transactions
        PriorityQueue<Transactions> transactionsQ = user.getTransactionsQueue();

        //If Point to be added is Positive simply add it to the transactionQ
        if(point>0) transactionsQ.offer(transactions);

        /*
        If point to be added is negative then:
            1. If it is a new Payer then the transaction is invalid because the points for the first payer can't be negative.
            2. For existing Payer we have following conditions:
                i. if old transaction point + current point to be added for the payer is > 0 then set the current transaction point = old transaction point + current point to be added.
                ii. else if it's == 0 then remove the current transaction from the transactionsQ.
                iii. else throw error.
        */
        else if(point<0) {
            if (!pointsPerPayer.containsKey(payer))
                throw new RuntimeException("Invalid transaction record");
            else {
                //Filter the transaction from transactionQ where existing Payer Name == current Payer
                Transactions currentTransaction = transactionsQ.stream()
                        .filter(t -> t.getPayerName().equals(transactions.getPayerName())).findFirst().orElse(null);

                if ((pointsPerPayer.get(payer) + point) > 0)
                    currentTransaction.setPoints(pointsPerPayer.get(payer) + point);

                else if ((pointsPerPayer.get(payer) + point) == 0) transactionsQ.remove(currentTransaction);

                else throw new RuntimeException("Invalid transaction record");

            }
        }

        //Update the user total points
        user.setTotalrewardPoints(user.getTotalrewardPoints() + point);

        //Update Points Per Payer
        pointsPerPayer.put(transactions.getPayerName(),(pointsPerPayer.getOrDefault(transactions.getPayerName(), 0L).longValue() + point));


        return ResponseEntity.ok().body(transactions);
    }


    //Subtract spend the points for userId with respect to each payer point
    @GetMapping("/spend/{spend}/{id}")
    public ResponseEntity<List<StringBuilder>> spendPoints(@Validated @PathVariable int id, @Validated @PathVariable long spend){

        if(!users.containsKey(id))
            throw new RuntimeException("User not found or absent!");

        //List of spend points from each payer
        List<StringBuilder> spentPerPayer = new ArrayList<>();

        Users user = users.get(id);
        //Get User rewards map for each payer
        Map<String, Long> pointsPerPayer = user.getPointsPerPayer();

        //Get User transactions
        PriorityQueue<Transactions> transactionsQ = user.getTransactionsQueue();

        //Get User Total Rewards
        long totalPoints = user.getTotalrewardPoints();
        logger.info("Initial Total Points Before Spending for user " + id + " : " + totalPoints);

        logger.info("Actual Spend Points:" + spend);

        //Check whether the point spend is greater than totalPoints then throw Exception
        if(spend > user.getTotalrewardPoints()) throw new RuntimeException("Unsufficient Reward Balance!!!");

        while(spend > 0 && !transactionsQ.isEmpty()){
            long remaining;
            Transactions currTransaction  = transactionsQ.poll();

            if(currTransaction.getPoints()<=spend)
                remaining = currTransaction.getPoints();
            else remaining = spend;

            spentPerPayer.add(new StringBuilder(currTransaction.getPayerName()).append(" -"+(remaining)));



            //Update the user total points
            user.setTotalrewardPoints(user.getTotalrewardPoints() - remaining);

            //Update Points Per Payer
            pointsPerPayer.put(currTransaction.getPayerName(),(pointsPerPayer.getOrDefault(currTransaction.getPayerName(), 0L).longValue() - remaining));

            logger.info("Spent Per Payer List:" + spentPerPayer);
            spend -= remaining;
            logger.info("Spend Points after deducting from "+currTransaction.getPayerName() + " : " + spend);

            logger.info("Total Points for the user" +" "+id+ " : "+user.getTotalrewardPoints());
        }
        return ResponseEntity.ok().body(spentPerPayer);
    }

    //Show the balance points of the user for each payer.
    @GetMapping("/balance/{id}")
    public ResponseEntity<List<StringBuilder>> balanceOfUserForEachPayer(@Validated @PathVariable int id){
        //List of Balanced Points for with given userId
        List<StringBuilder> balancePoint = new ArrayList<>();

        if(!users.containsKey(id)) throw new RuntimeException("User not found or absent!");

        Users user = users.get(id);

        user.getPointsPerPayer().entrySet().forEach(e-> balancePoint.add(new StringBuilder(e.getKey()+" : "+e.getValue())));

        return ResponseEntity.ok().body(balancePoint);
    }
}
