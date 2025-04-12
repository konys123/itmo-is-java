import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractTest {
    protected static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15.2")
                    .withDatabaseName("test_db")
                    .withUsername("user")
                    .withPassword("password");

    protected static SessionFactory sessionFactory;

    @BeforeAll
    public static void setup() {
        postgresContainer.start();
        Configuration configuration = new Configuration().configure("hibernate-docker.cfg.xml");
        configuration.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    public static void stop() {
        sessionFactory.close();
        postgresContainer.stop();
    }

}
