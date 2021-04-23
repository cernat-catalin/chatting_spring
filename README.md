# Aspect Oriented Programming - Chat Application

## Common

Install before compiling the client and server: `mvn clean install`

## Client

From `/client`:

* Compile: `mvn clean compile`
* Execute: `mvn exec:java -Dexec.mainClass="org.chatting.client.SpringBootMain"`
* Compile and execute: `mvn clean compile exec:java -Dexec.mainClass="org.chatting.client.SpringBootMain"`

## Server

From `/server`:

* Compile: `mvn clean compile`
* Execute: `mvn exec:java -Dexec.mainClass="org.chatting.server.SpringBootMain"`
* Compile and execute: `mvn clean compile exec:java -Dexec.mainClass="org.chatting.server.SpringBootMain"`

## Differences from AspectJ

### Client

* EventLoggerAspect
    * [SameClassCall] & [Visibility]
        * Had to advise another method for event processing
        * [NoCaller] Also, I couldn't nicely (short of declaring a throwable and walking the call stack) know who called
          the method
* NetworkLoggerAspect:
    * [SameClassCall] & [Visibility] x2
        * Had to create another class that handles incoming messages

### Server

* UserLoggerAspect
    * [SameClassCall] x2
    * [NoCaller] Luckily there was a reference to the parent object
* NetworkLogger
    * [DynamicCreation] Had to change the scope of UserThread to Prototype and create it using the Application Context
    * [Visibility]
    * [Proxy HashCode] Had to change from a Set to a Map when keeping track of UserThreads
* ProfileLogger
    * No changes
* CachingLogger
    * No changes
* SecurityLogger
    * [SameClassCall] & [Visibility] Had to advise another method
* TransactionLogger
    * [DynamicCreation] Had to create an intermediary object (DatabaseConnection) & Prototype & creation using
      Application Context
    * [CFlowBelow] In order to identify the top level transaction, I advised all transactions and kept a stack of
      transactions for each thread