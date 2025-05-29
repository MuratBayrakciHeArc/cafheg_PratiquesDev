package integration;

import ch.hearc.cafheg.business.allocations.Allocataire;
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
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import static org.assertj.core.api.Assertions.assertThatNoException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import static org.assertj.core.api.Assertions.assertThatThrownBy;



@SpringBootTest(classes = ch.hearc.cafheg.infrastructure.application.Application.class)
public class MyTestsIT {


    //Test utilisé à la partie 2

//    @Test
//    void simpleTest() {
//        assertThat(1).isEqualTo(1);
//    }


    private DataSource dataSource;

    @BeforeEach
    void setup() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");
        this.dataSource = new HikariDataSource(config);

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
        DatabaseOperation.INSERT.execute(dbUnitConnection, dataSet);
    }

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

    @Test
    void testSuppressionAllocataireAvecVersement() {
        String noAVS = "756.8888.9999.11"; // Marie, qui a un versement

        assertThatThrownBy(() -> {
            Database.inTransaction(() -> {
                AllocataireService service = new AllocataireService(new AllocataireMapper(), new VersementMapper());
                service.supprimerAllocataireParNoAVS(noAVS);
                return null;
            });
        }).hasCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("Impossible de supprimer : l'allocataire a des versements.");
    }

    @Test
    void testModificationNomPrenomEffectif() {
        Database.inTransaction(() -> {
            AllocataireMapper mapper = new AllocataireMapper();
            Allocataire allocataire = mapper.findById(mapper.findNumeroByNoAVS("756.6324.3723.75"));

            AllocataireService service = new AllocataireService(mapper, new VersementMapper());

            boolean result = service.modifierNomPrenom(allocataire, "Martin", "Jean-Luc");

            assertThat(result).isTrue();

            Allocataire allocataireModifie = mapper.findById(mapper.findNumeroByNoAVS("756.6324.3723.75"));
            assertThat(allocataireModifie.getNom()).isEqualTo("Martin");
            assertThat(allocataireModifie.getPrenom()).isEqualTo("Jean-Luc");

            return null;
        });
    }

    @Test
    void testModificationNomPrenomInutile() {
        Database.inTransaction(() -> {
            AllocataireMapper mapper = new AllocataireMapper();
            Allocataire allocataire = mapper.findById(mapper.findNumeroByNoAVS("756.0024.0023.00"));

            AllocataireService service = new AllocataireService(mapper, new VersementMapper());

            boolean result = service.modifierNomPrenom(allocataire, "Martin", "Enzo");

            assertThat(result).isFalse();

            return null;
        });
    }
}

