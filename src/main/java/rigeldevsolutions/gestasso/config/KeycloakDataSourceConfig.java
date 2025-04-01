package rigeldevsolutions.gestasso.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = "rigeldevsolutions.gestasso.authmodule.keycloak",
        entityManagerFactoryRef = "keycloakEntityManagerFactory",
        transactionManagerRef = "keycloakTransactionManager")
public class KeycloakDataSourceConfig
{
    @Bean(name = "keycloakDataSource")
    public DataSource keycloakDataSource(@Value("${spring.datasource.driver-class-name}") String driverClass,
                                     @Value("${spring.datasource.keycloak.url}") String url,
                                     @Value("${spring.datasource.keycloak.username}") String username,
                                     @Value("${spring.datasource.keycloak.password}") String password)
    {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(driverClass)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "keycloakEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("keycloakDataSource") DataSource dataSource,
                                                                       Environment env)
    {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("rigeldevsolutions.gestasso.authmodule.keycloak");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.keycloak.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
        jpaProperties.put("hibernate.physical_naming_strategy", env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));

        emf.setJpaPropertyMap(jpaProperties);
        return emf;
    }

    @Bean(name = "keycloakTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("keycloakEntityManagerFactory") EntityManagerFactory emf)
    {
        return new JpaTransactionManager(emf);
    }
}
