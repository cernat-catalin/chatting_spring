package org.chatting.server.database;

import org.chatting.server.database.mapper.UserMapper;
import org.chatting.server.entity.UserEntity;

import java.text.MessageFormat;
import java.util.Optional;

public class DatabaseService {

    private final QueryExecutor queryExecutor;
    private final DatabaseStatisticsService statisticsService;

    public DatabaseService(QueryExecutor queryExecutor, DatabaseStatisticsService statisticsService) {
        this.queryExecutor = queryExecutor;
        this.statisticsService = statisticsService;
    }

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
        userOpt.ifPresent(user -> statisticsService.addUserStatistics(user.getId()));
    }

    public boolean areCorrectUserCredentials(String username, String password) {
        return true;
    }
}
