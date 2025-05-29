# Projet Java

Ce projet Java a été réalisé dans le cadre du cours de pratiques de développement. Il met en œuvre des services REST, des tests unitaires et d’intégration, ainsi qu'une configuration du logging.

## Fonctionnalités principales

### Exercice 1 – Analyse et refactoring
- Exploration initiale de l'application et des classes existantes.
- Test du endpoint REST `/droits/quel-parent` à l’aide d’un outil d’API.
- Réalisation de tests unitaires complets (100%) sur la méthode `AllocationService#getParentDroitAllocation`.
- Refactoring de cette méthode pour remplacer l’utilisation de `Map<String, Object>` par une classe dédiée.
- Maintien de la couverture de test à 100% tout au long du TDD.

### Exercice 2 – Services REST
- **Suppression d’un allocataire** : uniquement possible si aucun versement n’est associé.
- **Modification d’un allocataire** : modification du nom et prénom uniquement si changement détecté. Le numéro AVS reste inchangé.
- Exposition de ces fonctionnalités via des endpoints REST.

### Exercice 4 – Logging
- Remplacement de tous les `System.out.println` et `printStackTrace` par des loggers configurés avec des niveaux pertinents :
    - `error` pour les exceptions (avec la cause)
    - `warn`, `info`, `debug`, `trace` selon le contexte
- Mise en place de 3 appenders :
    - `err.log` pour les erreurs dans les packages `ch.*`
    - `cafheg_{date}.log` pour les logs `info` des services
    - Affichage `debug` dans la console

### Exercice 5 – Tests d’intégration
- Création d’une structure dédiée aux tests d’intégration (`src/integration-test/...`).
- Mise en place de tests d’intégration avec DBUnit, JUnit 5 et AssertJ.
- Rédaction de deux tests d’intégration : suppression et modification d’un allocataire.
- Configuration de l’environnement avec DBUnit pour peupler une base de test à partir de fichiers XML.

## Technologies utilisées

- Java 11
- Spring Boot
- JUnit 5, AssertJ, DBUnit
- Log4j
- IntelliJ Idea