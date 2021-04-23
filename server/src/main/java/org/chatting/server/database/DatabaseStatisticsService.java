package org.chatting.server.database;

import org.chatting.server.database.mapper.UserStatisticsMapper;
import org.chatting.server.entity.UserStatisticsEntity;

import java.text.MessageFormat;
import java.util.Optional;

public class DatabaseStatisticsService {

    private final QueryExecutor queryExecutor;

    public DatabaseStatisticsService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public void addUserStatistics(int userId) {
        final String query = MessageFormat.format("INSERT INTO user_statistics (user_id) VALUES ({0});", userId);
        queryExecutor.executeQuery(query);
    }

    public Optional<UserStatisticsEntity> getUserStatistics(String username) {
        final String query = MessageFormat.format("SELECT n_logins, n_messages FROM user_statistics where user_id IN (SELECT id FROM user WHERE username = ''{0}'')",
                username);
        final UserStatisticsMapper mapper = new UserStatisticsMapper();
        return queryExecutor.getResultSingle(query, mapper);
    }

    public void incrementUserLogins(String username) {
        final String query = MessageFormat.format("UPDATE user_statistics SET n_logins = n_logins + 1 WHERE user_id IN (SELECT id FROM user WHERE username = ''{0}'')",
                username);
        queryExecutor.executeQuery(query);
    }

    public void incrementUserMessages(String username) {
        final String query = MessageFormat.format("UPDATE user_statistics SET n_messages = n_messages + 1 WHERE user_id IN (SELECT id FROM user WHERE username = ''{0}'')",
                username);
        queryExecutor.executeQuery(query);
    }
}
