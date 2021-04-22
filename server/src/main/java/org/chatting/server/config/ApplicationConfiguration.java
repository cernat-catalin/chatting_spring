package org.chatting.server.config;

import org.chatting.server.database.DatabaseService;
import org.chatting.server.database.QueryExecutor;
import org.chatting.server.network.NetworkService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {

    private final ApplicationProperties properties;

    public ApplicationConfiguration(ApplicationProperties properties) {
        this.properties = properties;
    }

    public QueryExecutor queryExecutor() {
        return new QueryExecutor();
    }

    @Bean
    public DatabaseService databaseService() {
        return new DatabaseService(queryExecutor());
    }

    @Bean
    public NetworkService networkService() {
        return new NetworkService(properties.getNetwork().getPort(), databaseService());
    }
}
