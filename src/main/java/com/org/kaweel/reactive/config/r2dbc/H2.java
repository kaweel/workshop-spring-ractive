package com.org.kaweel.reactive.config.r2dbc;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.h2.H2ConnectionOption;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
@EnableR2dbcRepositories
@Profile("h2")
public class H2 {

    @Bean
    public ConnectionFactory connectionFactory() {
        H2ConnectionConfiguration h2ConnectionConfiguration = H2ConnectionConfiguration.builder()
                .inMemory("rookie")
                .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
                .build();
        return new H2ConnectionFactory(h2ConnectionConfiguration);
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

}
