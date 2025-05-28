package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AllocataireMapper extends Mapper {

  private static final Logger logger = LogManager.getLogger(AllocataireMapper.class);

  private static final String QUERY_FIND_ALL = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES";
  private static final String QUERY_FIND_WHERE_NOM_LIKE = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?";
  private static final String QUERY_FIND_WHERE_NUMERO = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?";

  public List<Allocataire> findAll(String likeNom) {
    logger.debug("findAll() appelé avec filtre: {}", likeNom);
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement;
      if (likeNom == null) {
        logger.debug("SQL: {}", QUERY_FIND_ALL);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_ALL);
      } else {

        logger.debug("SQL: {}", QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement.setString(1, likeNom + "%");
      }
      logger.debug("Allocation d'un nouveau tableau pour les résultats");
      List<Allocataire> allocataires = new ArrayList<>();

      logger.debug("Exécution de la requête");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {

        logger.debug("Début du mapping des résultats");
        while (resultSet.next()) {
          logger.debug("ResultSet#next");
          allocataires
              .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                  resultSet.getString(1)));
        }
      }
      logger.debug("Allocataires trouvés : {}", allocataires.size());
      return allocataires;
    } catch (SQLException e) {
      logger.error("Erreur lors de l'exécution de findAll()", e);
      throw new RuntimeException(e);
    }
  }

  public Allocataire findById(long id) {
    logger.debug("findById() appelé avec id: {}", id);
    Connection connection = activeJDBCConnection();
    try {
      logger.debug("SQL: {}", QUERY_FIND_WHERE_NUMERO);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      logger.debug("ResultSet#next");
      resultSet.next();
      logger.debug("Mapping de l'allocataire depuis la base");
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      logger.error("Erreur lors de findById({})", id, e);
      throw new RuntimeException(e);
    }
  }

  public void delete(long id) {
    String DELETE_QUERY = "DELETE FROM ALLOCATAIRES WHERE NUMERO=?";
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
      preparedStatement.setLong(1, id);
      preparedStatement.executeUpdate();
      logger.info("Allocataire supprimé : {}", id);
    } catch (SQLException e) {
      logger.error("Erreur lors de la suppression de l'allocataire {}", id, e);
      throw new RuntimeException(e);
    }
  }

  public void updateNomPrenom(String noAVS, String nouveauNom, String nouveauPrenom) {
    String UPDATE_QUERY = "UPDATE ALLOCATAIRES SET NOM = ?, PRENOM = ? WHERE NO_AVS = ?";
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
      preparedStatement.setString(1, nouveauNom);
      preparedStatement.setString(2, nouveauPrenom);
      preparedStatement.setString(3, noAVS);
      int rows = preparedStatement.executeUpdate();
      logger.info("Nom et prénom mis à jour pour {}. Lignes affectées : {}", noAVS, rows);
    } catch (SQLException e) {
      logger.error("Erreur lors de la mise à jour du nom/prénom pour {}", noAVS, e);
      throw new RuntimeException(e);
    }
  }

  public long findNumeroByNoAVS(String noAVS) {
    String QUERY = "SELECT NUMERO FROM ALLOCATAIRES WHERE NO_AVS = ?";
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
      preparedStatement.setString(1, noAVS);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        long numero = resultSet.getLong("NUMERO");
        logger.debug("Numéro trouvé pour {} : {}", noAVS, numero);
        return numero;
      } else {
        logger.warn("Aucun allocataire trouvé avec le No AVS : {}", noAVS);
        throw new IllegalArgumentException("Aucun allocataire trouvé avec le numéro AVS : " + noAVS);
      }
    } catch (SQLException e) {
      logger.error("Erreur lors de findNumeroByNoAVS({})", noAVS, e);
      throw new RuntimeException(e);
    }
  }
}
