package org.chatting.server.database;

import org.springframework.context.ApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSource {
    private static final String DATABASE_PATH = "/home/catalin/Documents/master/projects/aspects/chatting_spring/database/server.db";

    private final ApplicationContext applicationContext;

    public DatabaseSource(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public DatabaseConnection createConnection() throws SQLException {
        final String url = String.format("jdbc:sqlite:%s", DATABASE_PATH);
        final Connection connection = DriverManager.getConnection(url);


        final DatabaseConnection databaseConnection = applicationContext.getBean(DatabaseConnection.class);
        databaseConnection.setConnection(connection);

        return databaseConnection;
    }
}
