package org.chatting.server.database;

import org.chatting.server.database.mapper.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class QueryExecutor {

    private final DatabaseSource databaseSource;

    public QueryExecutor(DatabaseSource databaseSource) {
        this.databaseSource = databaseSource;
    }

    public <T> Optional<T> getResultSingle(String query, EntityMapper<T> entityMapper) {
        try {
            final DatabaseConnection databaseConnection = databaseSource.createConnection();
            final Statement statement = databaseConnection.createStatement();
            final ResultSet resultSet = statement.executeQuery(query);
            final Optional<T> result = entityMapper.extractSingle(resultSet);

            resultSet.close();
            statement.close();
            databaseConnection.closeConnection();

            return result;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    public <T> List<T> getResultList(String query, EntityMapper<T> entityMapper) {
        try {
            final DatabaseConnection databaseConnection = databaseSource.createConnection();
            final Statement statement = databaseConnection.createStatement();
            final ResultSet resultSet = statement.executeQuery(query);
            final List<T> result = entityMapper.extractList(resultSet);

            resultSet.close();
            statement.close();
            databaseConnection.closeConnection();

            return result;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    public void executeQuery(String query) {
        try {
            final DatabaseConnection databaseConnection = databaseSource.createConnection();
            final Statement statement = databaseConnection.createStatement();
            statement.executeUpdate(query);

            statement.close();
            databaseConnection.closeConnection();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }
}
