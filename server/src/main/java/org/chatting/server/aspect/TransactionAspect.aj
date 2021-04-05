package org.chatting.server.aspect;

import org.aspectj.lang.Signature;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public aspect TransactionAspect percflow(topLevelTransaction()) {

    private static final Logger LOGGER = Logger.getLogger("TransactionLogger");
    private static final Set<Integer> TRANSACTION_IDS = new HashSet<>();

    private Connection connection;
    private int transactionId;

    pointcut createConnection():
            call(Connection org.chatting.server.database.DatabaseSource.createConnection());

    pointcut closeConnection():
            call(void Connection.close());

    pointcut transaction():
            call(* org.chatting.server.database.DatabaseService.*(..));

    pointcut topLevelTransaction():
            transaction() && !cflowbelow(transaction());


    Connection around(): createConnection() && cflow(transaction()) {
        if (connection == null) {
            LOGGER.info(String.format("[TRANSACTION %d] DB Connection acquired", transactionId));
            connection = proceed();
            connection.setAutoCommit(false);
        }

        return connection;
    }

    Object around(): topLevelTransaction() {
        transactionId = generateNewTransactionId();
        final Signature signature = thisJoinPoint.getSignature();
        LOGGER.info(String.format("[TRANSACTION %d] Started from %s", transactionId, signature));

        try {
            Object result = proceed();
            connection.commit();
            LOGGER.info(String.format("[TRANSACTION %d] Commit", transactionId));
            return result;
        } catch (Exception ex) {
            LOGGER.info(String.format("[TRANSACTION %d] Rollback", transactionId));
            connection.rollback();
            throw new TransactionException(ex);
        } finally {
            LOGGER.info(String.format("[TRANSACTION %d] DB Connection closed", transactionId));
            connection.close();
            removeTransactionId(transactionId);
        }
    }

    void around(): closeConnection() && !within(org.chatting.server.aspect.TransactionAspect) {
    }

    declare soft:java.sql.SQLException:
            call (void Connection.close())
                    || call (void Connection.setAutoCommit(boolean))
                    || call (void Connection.rollback())
                    && within(org.chatting.server.aspect.TransactionAspect);

    private static Integer generateNewTransactionId() {
        synchronized (TRANSACTION_IDS) {
            final Random random = new Random();

            Integer transactionId;
            do {
                transactionId = Math.abs(random.nextInt());
            } while (TRANSACTION_IDS.contains(transactionId));

            TRANSACTION_IDS.add(transactionId);
            return transactionId;
        }
    }

    private static void removeTransactionId(Integer transactionId) {
        synchronized (TRANSACTION_IDS) {
            TRANSACTION_IDS.remove(transactionId);
        }
    }
}
