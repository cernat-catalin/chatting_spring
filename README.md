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


Server differences (from AspectJ):
* Because both join and leave were called internally, had to create another level of indirection
  * User Join - No call only execution. Can not capture both this & target (luckily user thread has reference to the server)
    * had to create a getter for the server
* NetworkLogger
  * Because UserThreads were created with new inside Network Service AOP would not work. I had to autowire
  the application context, make UserThead a managed bean (scope prototype) and create it this way
  * had to make the processMessage and sendMessage methods on UserThead public  
  * Had a problem with a set of User Threads. As I didn't override hashCode / equals, I had to be careful when
  using add / remove (this is without proxy while reference from other objects are usually proxies). Ended up using
    a hashmap instead.
* Security
    * Had to switch to another method (internal call)