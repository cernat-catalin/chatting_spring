package org.chatting.server.database.mapper;

import org.chatting.server.database.DatabaseException;
import org.chatting.server.entity.UserStatisticsEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserStatisticsMapper implements EntityMapper<UserStatisticsEntity> {

    @Override
    public Optional<UserStatisticsEntity> extractSingle(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return Optional.of(extractUserStatistics(resultSet));
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<UserStatisticsEntity> extractList(ResultSet resultSet) {
        try {
            final List<UserStatisticsEntity> userStatisticsEntities = new ArrayList<>();

            while (resultSet.next()) {
                final UserStatisticsEntity userStatisticsEntity = extractUserStatistics(resultSet);
                userStatisticsEntities.add(userStatisticsEntity);
            }

            return userStatisticsEntities;
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    private UserStatisticsEntity extractUserStatistics(ResultSet resultSet) throws SQLException {
        final int nLogins = resultSet.getInt("n_logins");
        final int nMessages = resultSet.getInt("n_messages");
        return new UserStatisticsEntity(nLogins, nMessages);
    }
}
