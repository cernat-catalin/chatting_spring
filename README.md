# Aspect Oriented Programming - Chat Application

## Common

Install before compiling the client and server: `mvn clean install`

## Client

From `/client`:

* Compile: `mvn clean compile`
* Execute: `mvn exec:java -Dexec.mainClass="org.chatting.client.Main"`
* Compile and execute: `mvn clean compile exec:java -Dexec.mainClass="org.chatting.client.Main"`

## Server

From `/server`:

* Compile: `mvn clean compile`
* Execute: `mvn exec:java -Dexec.mainClass="org.chatting.server.Main"`
* Compile and execute: `mvn clean compile exec:java -Dexec.mainClass="org.chatting.server.Main"`
