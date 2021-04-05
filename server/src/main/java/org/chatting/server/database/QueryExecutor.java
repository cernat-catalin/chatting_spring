package org.chatting.server.database;

import org.chatting.server.database.mapper.EntityMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class QueryExecutor {

    public <T> Optional<T> getResultSingle(String query, EntityMapper<T> entityMapper) {
        try {
            final Connection connection = DatabaseSource.createConnection();
            final Statement statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(query);
            final Optional<T> result = entityMapper.extractSingle(resultSet);

            resultSet.close();
            statement.close();
            connection.close();

            return result;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    public <T> List<T> getResultList(String query, EntityMapper<T> entityMapper) {
        try {
            final Connection connection = DatabaseSource.createConnection();
            final Statement statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(query);
            final List<T> result = entityMapper.extractList(resultSet);

            resultSet.close();
            statement.close();
            connection.close();

            return result;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    public void executeQuery(String query) {
        try {
            final Connection connection = DatabaseSource.createConnection();
            final Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }
}
