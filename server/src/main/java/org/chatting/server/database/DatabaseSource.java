package org.chatting.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSource {
    private static final String DATABASE_PATH = "/home/catalin/Documents/master/projects/aspects/chatting/database/server.db";

    public static Connection createConnection() throws SQLException {
        final String url = String.format("jdbc:sqlite:%s", DATABASE_PATH);
        return DriverManager.getConnection(url);
    }
}
