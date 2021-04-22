# Aspect Oriented Programming - Chat Application

## Common

Install before compiling the client and server: `mvn clean install`

## Client

From `/client`:

* Compile: `mvn clean compile`
* Execute: `mvn exec:java -Dexec.mainClass="org.chatting.client.SpringBootMain"`
* Compile and execute: `mvn clean compile exec:java -Dexec.mainClass="org.chatting.client.Main"`

## Server

From `/server`:

* Compile: `mvn clean compile`
* Execute: `mvn exec:java -Dexec.mainClass="org.chatting.server.Main"`
* Compile and execute: `mvn clean compile exec:java -Dexec.mainClass="org.chatting.server.Main"`



Client Differences (from AspectJ):
* EventLoggerAspect
  * `org.chatting.client.gui.EventProcessor.processEvent` chould no longer advise it as it was called inside the class.
    Had to advise a method another point `org.chatting.client.gui.EventQueue.popEvent`
    * Also, I couldn't nicely (short of declaring a throwable and walking the call stack) know who called the method
* NetworkLoggerAspect:
  * The received network messages are read and transformed in the same class (internal call). I had to create another
    class to transform the messages and push them to the internal event queue in order to log incoming network messages
  * The method to send messages over the network is internal. Has to switch from monitoring that method to monitoring
  pops from the network messages queue
