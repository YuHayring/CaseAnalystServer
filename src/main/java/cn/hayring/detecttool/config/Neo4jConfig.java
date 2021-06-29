package cn.hayring.detecttool.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "cn.hayring.detecttool.dao")
@EnableTransactionManagement
public class Neo4jConfig{
    @Value("${spring.neo4j.uri}")
    private String databaseUrl;

    @Value("${spring.neo4j.authentication.username}")
    private String userName;

    @Value("${spring.neo4j.authentication.password}")
    private String password;

    @Bean
    public SessionFactory sessionFactory() {
        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder()
                .uri(databaseUrl)
                .credentials(userName, password)
                .build();
        SessionFactory sessionFactory = new SessionFactory(configuration, "cn.hayring.detecttool.domain");
        return sessionFactory;
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }


}

