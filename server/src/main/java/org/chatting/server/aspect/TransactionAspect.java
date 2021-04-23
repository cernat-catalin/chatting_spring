package org.chatting.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.chatting.server.ThreadTransactionsState;
import org.chatting.server.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
public class TransactionAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionAspect.class);
    private static final Set<Integer> TRANSACTION_IDS = new HashSet<>();
    private static final Map<Long, ThreadTransactionsState> THREADS_STATE = new Hashtable<>();

    @Pointcut("execution(* org.chatting.server.database.DatabaseSource.createConnection())")
    private void createConnection() {
    }

    @Pointcut("execution(void org.chatting.server.database.DatabaseConnection.closeConnection())")
    private void closeConnection() {
    }

    @Pointcut("execution(* org.chatting.server.database.DatabaseService.*(..)) || execution(* org.chatting.server.database.DatabaseStatisticsService.*(..))")
    private void transaction() {
    }

    @Around("createConnection()")
    private DatabaseConnection aroundCreateConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        final ThreadTransactionsState threadState = getCurrentThreadState();

        DatabaseConnection databaseConnection = threadState.getConnection();
        if (databaseConnection == null) {
            final Integer topLevelTransactionId = threadState.getTopLevelTransactionId();
            LOGGER.info(String.format("[TRANSACTION %d] DB Connection acquired", topLevelTransactionId));

            databaseConnection = (DatabaseConnection) joinPoint.proceed();
            databaseConnection.getConnection().setAutoCommit(false);

            threadState.setConnection(databaseConnection);
        }

        return databaseConnection;
    }

    @Around("transaction()")
    private Object aroundTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        final ThreadTransactionsState currentThreadState = getCurrentThreadState();
        if (currentThreadState.getTransactionIds().empty()) {
            // We are in a top level transaction
            final int transactionId = generateNewTransactionId();
            final Signature signature = joinPoint.getSignature();
            LOGGER.info(String.format("[TRANSACTION %d] Started from %s", transactionId, signature));

            currentThreadState.setTopLevelTransactionId(transactionId);
            currentThreadState.getTransactionIds().push(transactionId);

            try {
                Object result = joinPoint.proceed();
                currentThreadState.getConnection().commit();
                LOGGER.info(String.format("[TRANSACTION %d] Commit", transactionId));
                return result;
            } catch (Exception ex) {
                LOGGER.info(String.format("[TRANSACTION %d] Rollback", transactionId));
                currentThreadState.getConnection().rollback();
                throw new TransactionException(ex);
            } finally {
                LOGGER.info(String.format("[TRANSACTION %d] DB Connection closed", transactionId));
                currentThreadState.getConnection().closeConnection();
                currentThreadState.clean();
                removeTransactionId(transactionId);
            }

        } else {
            final Integer topLevelTransactionId = currentThreadState.getTopLevelTransactionId();
            final int transactionId = generateNewTransactionId();
            final Signature signature = joinPoint.getSignature();
            LOGGER.info(String.format("[TRANSACTION %d] Started inner transaction %d from %s", topLevelTransactionId,
                    transactionId, signature));

            currentThreadState.getTransactionIds().push(transactionId);
            Object result = joinPoint.proceed();
            removeTransactionId(transactionId);
            return result;
        }
    }

    @Around("closeConnection() && !within(org.chatting.server.aspect.TransactionAspect)")
    private void ignoreCloseConnection() {

    }

    private ThreadTransactionsState getCurrentThreadState() {
        final long threadId = Thread.currentThread().getId();
        ThreadTransactionsState threadState = THREADS_STATE.get(threadId);

        if (threadState == null) {
            threadState = new ThreadTransactionsState(threadId);
            THREADS_STATE.put(threadId, threadState);
        }

        return threadState;
    }

    private Integer generateNewTransactionId() {
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

    private void removeTransactionId(Integer transactionId) {
        synchronized (TRANSACTION_IDS) {
            TRANSACTION_IDS.remove(transactionId);
        }
    }
}
