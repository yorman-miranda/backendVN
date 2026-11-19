package taller.velvetnails;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class VelvetNailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(VelvetNailsApplication.class, args);

    }

    @Bean
    CommandLineRunner testNeonConnection(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("✅ Conexión a Neon exitosa: " + conn.getMetaData().getURL());
            }
        };
    }

}
