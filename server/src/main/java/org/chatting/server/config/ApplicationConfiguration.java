package org.chatting.server.config;

import org.chatting.server.database.DatabaseService;
import org.chatting.server.database.DatabaseSource;
import org.chatting.server.database.DatabaseStatisticsService;
import org.chatting.server.database.QueryExecutor;
import org.chatting.server.model.ServerModel;
import org.chatting.server.network.NetworkService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {

    private final ApplicationContext applicationContext;
    private final ApplicationProperties properties;

    public ApplicationConfiguration(ApplicationContext applicationContext, ApplicationProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @Bean
    public DatabaseSource databaseSource() {
        return new DatabaseSource(applicationContext);
    }

    @Bean
    public QueryExecutor queryExecutor() {
        return new QueryExecutor(databaseSource());
    }

    @Bean
    public DatabaseStatisticsService databaseStatisticsService() {
        return new DatabaseStatisticsService(queryExecutor());
    }

    @Bean
    public DatabaseService databaseService() {
        return new DatabaseService(queryExecutor(), databaseStatisticsService());
    }

    @Bean
    public ServerModel serverModel() {
        return new ServerModel();
    }

    @Bean
    public NetworkService networkService() {
        return new NetworkService(applicationContext, properties.getNetwork().getPort(), serverModel());
    }
}
