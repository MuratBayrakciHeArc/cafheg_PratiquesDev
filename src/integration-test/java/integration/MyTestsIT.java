package integration;


import ch.hearc.cafheg.infrastructure.persistance.Database;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ch.hearc.cafheg.business.allocations.AllocataireService;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import static org.assertj.core.api.Assertions.assertThatNoException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;



@SpringBootTest(classes = ch.hearc.cafheg.infrastructure.application.Application.class)
public class MyTestsIT {

    //Test utilisé à la partie 2
//    @Test
//    void simpleTest() {
//        assertThat(1).isEqualTo(1);
//    }


    //private final DataSource dataSource;

//    public MyTestsIT(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//    @Autowired
//    private DataSource dataSource;
    private DataSource dataSource;


    @BeforeEach
    void setup() throws Exception {
//        try (Connection connection = dataSource.getConnection()) {
//            IDatabaseConnection dbUnitConnection = new DatabaseConnection(connection);
//
//            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
//            IDataSet dataSet = builder.build(new File("src/integration-test/resources/allocataires_base.xml"));
//
//            DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
//        }
        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:h2:mem:sample;DB_CLOSE_DELAY=-1");
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");
        this.dataSource = new HikariDataSource(config);

        // ⚠️ EXÉCUTER LES MIGRATIONS D'ABORD
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/ddl", "classpath:db/dml")
                .load()
                .migrate();

        Database.setDataSource(this.dataSource);


        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DELETE FROM VERSEMENTS_ALLOCATIONS");
            conn.createStatement().execute("DELETE FROM VERSEMENTS");
            conn.createStatement().execute("DELETE FROM ALLOCATIONS_ENFANTS");
            conn.createStatement().execute("DELETE FROM ALLOCATIONS");
            conn.createStatement().execute("DELETE FROM ENFANTS");
            conn.createStatement().execute("DELETE FROM ALLOCATAIRES");
        }



        IDatabaseConnection dbUnitConnection = new DatabaseConnection(dataSource.getConnection());

        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(new File("src/integration-test/resources/allocataires_base.xml"));

 //       DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
        DatabaseOperation.INSERT.execute(dbUnitConnection, dataSet);

//        DatabaseOperation.DELETE_ALL.execute(dbUnitConnection, dataSet);
//        DatabaseOperation.INSERT.execute(dbUnitConnection, dataSet);



    }

//    @Test
//    void testSuppressionAllocataireSansVersement() {
//        // Arrange
//        AllocataireService service = new AllocataireService(new AllocataireMapper(), new VersementMapper());
//        String noAVS = "7561234567890";
//
//
//        // Act & Assert
//        assertThatNoException()
//                .isThrownBy(() -> service.supprimerAllocataireParNoAVS(noAVS));
//    }

    @Test
    void testSuppressionAllocataireSansVersement() {
        String noAVS = "756.6324.3723.75";

        assertThatNoException().isThrownBy(() -> {
            Database.inTransaction(() -> {
                AllocataireService service = new AllocataireService(new AllocataireMapper(), new VersementMapper());
                service.supprimerAllocataireParNoAVS(noAVS);
                return null;
            });
        });
    }













}

