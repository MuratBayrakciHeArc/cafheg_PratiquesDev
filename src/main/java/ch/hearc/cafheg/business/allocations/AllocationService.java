package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AllocationService {

  private static final Logger logger = LogManager.getLogger(AllocationService.class);

  private static final String PARENT_1 = "PARENT_1";
  private static final String PARENT_2 = "PARENT_2";

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;

  public AllocationService(
      AllocataireMapper allocataireMapper,
      AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

//  public List<Allocataire> findAllAllocataires(String likeNom) {
//    System.out.println("Rechercher tous les allocataires");
//    return allocataireMapper.findAll(likeNom);
//  }
public List<Allocataire> findAllAllocataires(String likeNom) {
  logger.info("Appel du service : recherche de tous les allocataires avec filtre '{}'", likeNom);
  return allocataireMapper.findAll(likeNom);
}



  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public String getParentDroitAllocation(ParentAllocationRequest request) {

//    System.out.println("Déterminer quel parent a le droit aux allocations");
    logger.info("Appel du service : détermination du parent ayant droit aux allocations");


    //Code de base du projet
    /*  String eR = (String)parameters.getOrDefault("enfantResidence", "");
    Boolean p1AL = (Boolean)parameters.getOrDefault("parent1ActiviteLucrative", false);
    String p1Residence = (String)parameters.getOrDefault("parent1Residence", "");
    Boolean p2AL = (Boolean)parameters.getOrDefault("parent2ActiviteLucrative", false);
    String p2Residence = (String)parameters.getOrDefault("parent2Residence", "");
    Boolean pEnsemble = (Boolean)parameters.getOrDefault("parentsEnsemble", false);
    Number salaireP1 = (Number) parameters.getOrDefault("parent1Salaire", BigDecimal.ZERO);
    Number salaireP2 = (Number) parameters.getOrDefault("parent2Salaire", BigDecimal.ZERO);
    if(p1AL && !p2AL) {
      return PARENT_1;
    }

    if(p2AL && !p1AL) {
      return PARENT_2;
    }

    return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;
     */


    boolean p1AL = safeBoolean(request.isParent1ActiviteLucrative());
    boolean p2AL = safeBoolean(request.isParent2ActiviteLucrative());
    boolean p1Autorite = safeBoolean(request.isParent1AutoriteParentale());
    boolean p2Autorite = safeBoolean(request.isParent2AutoriteParentale());
    boolean pEnsemble = safeBoolean(request.isParentsEnsemble());
    boolean p1Independant = safeBoolean(request.isParent1EstIndependant());
    boolean p2Independant = safeBoolean(request.isParent2EstIndependant());
    Number salaireP1 = safeNumber(request.getParent1Salaire());
    Number salaireP2 = safeNumber(request.getParent2Salaire());
    String enfantResidence = safeString(request.getEnfantResidence());
    String parent1Residence = safeString(request.getParent1Residence());
    String parent2Residence = safeString(request.getParent2Residence());
    String parent1LieuTravail = safeString(request.getParent1LieuTravail());
    String parent2LieuTravail = safeString(request.getParent2LieuTravail());


    // Cas A : Un seul parent actif
    if (p1AL && !p2AL) return PARENT_1;
    if (p2AL && !p1AL) return PARENT_2;

    // Cas B : Un seul a l'autorité parentale
    if (p1Autorite && !p2Autorite) return PARENT_1;
    if (p2Autorite && !p1Autorite) return PARENT_2;

    // Cas C : Parents séparés
    if (!pEnsemble) {
      boolean enfantAvecP1 = enfantResidence.equalsIgnoreCase(parent1Residence);
      boolean enfantAvecP2 = enfantResidence.equalsIgnoreCase(parent2Residence);

      if (enfantAvecP1 && !enfantAvecP2) return PARENT_1;
      if (enfantAvecP2 && !enfantAvecP1) return PARENT_2;
    }

    // Cas D : Celui qui travaille dans le canton de l’enfant
    boolean p1TravailleDansCantonEnfant = enfantResidence.equalsIgnoreCase(parent1LieuTravail);
    boolean p2TravailleDansCantonEnfant = enfantResidence.equalsIgnoreCase(parent2LieuTravail);

    if (p1TravailleDansCantonEnfant && !p2TravailleDansCantonEnfant) return PARENT_1;
    if (p2TravailleDansCantonEnfant && !p1TravailleDansCantonEnfant) return PARENT_2;

// Cas E et F :
    boolean p1Sala = !p1Independant;
    boolean p2Sala = !p2Independant;

// Un seul salarié → priorité au salarié
    if (p1Sala && !p2Sala) return PARENT_1;
    if (p2Sala && !p1Sala) return PARENT_2;

// Deux salariés → comparer les salaires
    if (p1Sala && p2Sala) {
      return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;
    }

// Deux indépendants → comparer les salaires
    return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;
  }

  private boolean safeBoolean(Boolean value) {
    return value != null && value;
  }

  private Number safeNumber(Number value) {
    return value != null ? value : BigDecimal.ZERO;
  }

  private String safeString(String value) {
    return value != null ? value : "";
  }
}