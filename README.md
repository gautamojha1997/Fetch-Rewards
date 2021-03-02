# Fetch Rewards Coding Exercise - Backend Software Engineering

## The Project is built using Java Spring Boot Framework.

## Instructions to run the Project

### Prerequisites

- Install appropriate [Java JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
- Install [IntelliJ  Ultimate Edition](https://www.jetbrains.com/idea/download/#section=windows) or any appropriate Spring Boot IDE.
- To run using command line(windows) or terminal install [Maven](https://maven.apache.org/install.html) and add it to your environment variable.
- [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en) to test the API.


### Run the Project using IntelliJ

- Simply open the project in IntelliJ and run the FetchRewardsCodingApplication.java file.
- Test the api in Postman by sending request and checking the responses.

### Run the Project using cmd or terminal

- Navigate to the project folder.
- Run using command : ```mvn spring-boot:run```

### Assumption 

- For transaction timestamp whenever the transaction is added the local/current date and time are considered.

### REST Web Service Routes

1. Add points to the specified user's account
- Here the new user is created if it doesn't exist, or the existing user with specified userId is retrieved to add points.

EndPoint to add points to user with userid 1 :
```
http://localhost:8080/api/addPoints/1
```

Request to add points :
```
curl -H "Content-Type: application/json" -d "{\"payerName\":\"DANNON\",\"points\":300}" http://localhost:8080/api/addPoints/1
```

Response to add points :
```
{
    "transactionId": 1,
    "payerName": "DANNON",
    "points": 300,
    "timestamp": "2021-03-01T20:17:34.989"
}
```

2. Retrieve Transactions details of all the users
- Here 5 transactions are shown as example given in the problem description.

EndPoint to get transaction details of all users :
```
http://localhost:8080/api/transactions
```

Request to get transaction details of all users :
```
curl http://localhost:8080/api/transactions
```

Response :
```
{
    "1": [
        {
            "transactionId": 1,
            "payerName": "DANNON",
            "points": 100,
            "timestamp": "2021-03-01T20:17:34.989"
        },
        {
            "transactionId": 2,
            "payerName": "UNILEVER",
            "points": 200,
            "timestamp": "2021-03-01T20:19:36.901"
        },
        {
            "transactionId": 4,
            "payerName": "MILLER COORS",
            "points": 10000,
            "timestamp": "2021-03-01T20:20:05.602"
        },
        {
            "transactionId": 5,
            "payerName": "DANNON",
            "points": 1000,
            "timestamp": "2021-03-01T20:20:14.376"
        }
    ]
}
```


3. Retrieve Transactions details of a specific user with given userId
- Here just for example transaction detail of user with userId 1 is shown.

EndPoint for userId 1 :
```
http://localhost:8080/api/transactions/1
```

Request for userId 1 :
```
curl http://localhost:8080/api/transactions/1
```

Response :
```
[
    {
        "transactionId": 1,
        "payerName": "DANNON",
        "points": 100,
        "timestamp": "2021-03-01T20:17:34.989"
    },
    {
        "transactionId": 2,
        "payerName": "UNILEVER",
        "points": 200,
        "timestamp": "2021-03-01T20:19:36.901"
    },
    {
        "transactionId": 4,
        "payerName": "MILLER COORS",
        "points": 10000,
        "timestamp": "2021-03-01T20:20:05.602"
    },
    {
        "transactionId": 5,
        "payerName": "DANNON",
        "points": 1000,
        "timestamp": "2021-03-01T20:20:14.376"
    }
]
```

4. Spend points for a given user from the user account
- Here just for example 5000 points are spent for user with userId 1.

EndPoint :
```
http://localhost:8080/api/spend/5000/1
```

Request :
```
curl http://localhost:8080/api/spend/5000/1
```

Response :
```
[
    "DANNON -100",
    "UNILEVER -200",
    "MILLER COORS -4700"
]
```

5. Balance of each user with given userId
- Here just for example balance of user with userId 1 is shown.

EndPoint : 
```
http://localhost:8080/api/balance/1
```

Request : 
```
curl http://localhost:8080/api/balance/1
```

Response : 
```
[
    "UNILEVER : 0",
    "MILLER COORS : 5300",
    "DANNON : 1000"
]
```


Demo of the Project : [Link](https://www.youtube.com/watch?v=A5TCGTH_9FM&ab_channel=gautamojha)
