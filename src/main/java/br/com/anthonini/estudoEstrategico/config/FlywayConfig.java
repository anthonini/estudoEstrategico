package br.com.anthonini.estudoEstrategico.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .outOfOrder(false)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .sqlMigrationPrefix("V")
                .sqlMigrationSeparator("__")
                .sqlMigrationSuffixes(".sql")
                .locations("classpath:db/migration")
                .table("schema_version_estudo_estrategico_migration")
                .load();
        
        return flyway;
    }
}	
