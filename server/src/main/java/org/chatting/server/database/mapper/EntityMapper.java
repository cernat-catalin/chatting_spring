package org.chatting.server.database.mapper;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface EntityMapper<T> {
    Optional<T> extractSingle(ResultSet resultSet);

    List<T> extractList(ResultSet resultSet);
}
