package org.chatting.server.database.mapper;

import org.chatting.server.database.DatabaseException;
import org.chatting.server.entity.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserMapper implements EntityMapper<UserEntity> {

    @Override
    public Optional<UserEntity> extractSingle(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return Optional.of(extractUser(resultSet));
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<UserEntity> extractList(ResultSet resultSet) {
        try {
            final List<UserEntity> userEntities = new ArrayList<>();

            while (resultSet.next()) {
                final UserEntity userEntity = extractUser(resultSet);
                userEntities.add(userEntity);
            }

            return userEntities;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    private UserEntity extractUser(ResultSet resultSet) throws SQLException {
        final int id = resultSet.getInt("id");
        final String username = resultSet.getString("username");
        final String password = resultSet.getString("password");
        return new UserEntity(id, username, password);
    }
}
