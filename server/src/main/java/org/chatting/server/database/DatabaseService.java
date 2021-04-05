package org.chatting.server.database;

import org.chatting.server.database.mapper.UserMapper;
import org.chatting.server.database.mapper.UserStatisticsMapper;
import org.chatting.server.entity.UserEntity;
import org.chatting.server.entity.UserStatisticsEntity;

import java.text.MessageFormat;
import java.util.Optional;

public class DatabaseService {
    private final QueryExecutor queryExecutor = new QueryExecutor();

    public Optional<UserEntity> getUserByUsername(String username) {
        final String query = MessageFormat.format("SELECT id, username, password FROM user WHERE username = ''{0}''",
                username);
        final UserMapper mapper = new UserMapper();
        return queryExecutor.getResultSingle(query, mapper);
    }

    public void addUser(String username, String password) {
        final String query = MessageFormat.format("INSERT INTO user (username, password) VALUES (''{0}'', ''{1}'')",
                username, password);

        queryExecutor.executeQuery(query);
        final Optional<UserEntity> userOpt = getUserByUsername(username);
        userOpt.ifPresent(user -> addUserStatistics(user.getId()));
    }

    private void addUserStatistics(int userId) {
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
