package org.chatting.server;

import org.chatting.server.database.DatabaseConnection;

import java.util.Stack;

public class ThreadTransactionsState {

    private final long threadId;
    private final Stack<Integer> transactionIds = new Stack<>();

    private Integer topLevelTransactionId;
    private DatabaseConnection connection;

    public ThreadTransactionsState(long threadId) {
        this.threadId = threadId;
    }

    public Stack<Integer> getTransactionIds() {
        return transactionIds;
    }

    public DatabaseConnection getConnection() {
        return connection;
    }

    public void setConnection(DatabaseConnection connection) {
        this.connection = connection;
    }

    public Integer getTopLevelTransactionId() {
        return topLevelTransactionId;
    }

    public void setTopLevelTransactionId(Integer topLevelTransactionId) {
        this.topLevelTransactionId = topLevelTransactionId;
    }

    public void clean() {
        while (!transactionIds.empty()) {
            transactionIds.pop();
        }
        connection = null;
    }
}
