package ch.hearc.cafheg.infrastructure.application;

import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ch.hearc.cafheg")
public class Application extends SpringBootServletInitializer {

  /**
   * Démarrage de l'application en mode standalone (java -jar ...)
   * @param args Arguments du programme
   */
  public static void main(String[] args) {

//    Logger logger = LogManager.getLogger("ch.hearc.cafheg.infrastructure.api");
//    logger.error("Message test dans err.log");
//    logger.info("Message test dans cafheg_{date}.log");



    start();
    SpringApplication.run(Application.class, args);
  }

  private static void start() {
    Database database = new Database();
    Migrations migrations = new Migrations(database);

    database.start();
    migrations.start();
  }

  /**
   * Démarrage de l'application dans un serveur d'applications (Tomcat, Glassfish, Websphere...)
   */
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);
    start();
  }
}
