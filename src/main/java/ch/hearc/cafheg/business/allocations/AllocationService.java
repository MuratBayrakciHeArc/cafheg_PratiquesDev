package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AllocationService {

//  private static final String PARENT_1 = "Parent1";
//  private static final String PARENT_2 = "Parent2";
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

  public List<Allocataire> findAllAllocataires(String likeNom) {
    System.out.println("Rechercher tous les allocataires");
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public String getParentDroitAllocation(ParentAllocationRequest request) {
    System.out.println("Déterminer quel parent a le droit aux allocations");
  /*  String eR = (String)parameters.getOrDefault("enfantResidence", "");
    Boolean p1AL = (Boolean)parameters.getOrDefault("parent1ActiviteLucrative", false);
    String p1Residence = (String)parameters.getOrDefault("parent1Residence", "");
    Boolean p2AL = (Boolean)parameters.getOrDefault("parent2ActiviteLucrative", false);
    String p2Residence = (String)parameters.getOrDefault("parent2Residence", "");
    Boolean pEnsemble = (Boolean)parameters.getOrDefault("parentsEnsemble", false);
    Number salaireP1 = (Number) parameters.getOrDefault("parent1Salaire", BigDecimal.ZERO);
    Number salaireP2 = (Number) parameters.getOrDefault("parent2Salaire", BigDecimal.ZERO);
*/
//    String eR = request.getEnfantResidence();
//    boolean p1AL = request.isParent1ActiviteLucrative();
//    String p1Residence = request.getParent1Residence();
//    boolean p2AL = request.isParent2ActiviteLucrative();
//    String p2Residence = request.getParent2Residence();
//    boolean pEnsemble = request.isParentsEnsemble();
//    Number salaireP1 = request.getParent1Salaire();
//    Number salaireP2 = request.getParent2Salaire();

//    String eR = safeString(request.getEnfantResidence());
//    boolean p1AL = safeBoolean(request.isParent1ActiviteLucrative());
//    String p1Residence = safeString(request.getParent1Residence());
//    boolean p2AL = safeBoolean(request.isParent2ActiviteLucrative());
//    String p2Residence = safeString(request.getParent2Residence());
//    boolean pEnsemble = safeBoolean(request.isParentsEnsemble());
//    Number salaireP1 = safeNumber(request.getParent1Salaire());
//    Number salaireP2 = safeNumber(request.getParent2Salaire());



//    if(p1AL && !p2AL) {
//      return PARENT_1;
//    }
//
//    if(p2AL && !p1AL) {
//      return PARENT_2;
//    }
//
//    return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;


    // TODO vérifier que c'est bien juste selon schémà
    // TODO vérifier que l'api reste fonctionne toujours
    // TODO vérifier ques les tests fonctionnent toujours.


    boolean p1AL = safeBoolean(request.isParent1ActiviteLucrative());
    boolean p2AL = safeBoolean(request.isParent2ActiviteLucrative());
    boolean p1Autorite = safeBoolean(request.isParent1AutoriteParentale());
    boolean p2Autorite = safeBoolean(request.isParent2AutoriteParentale());
    boolean pEnsemble = safeBoolean(request.isParentsEnsemble());
    boolean p1TravCanton = safeBoolean(request.isParent1TravailleDansCantonEnfant());
    boolean p2TravCanton = safeBoolean(request.isParent2TravailleDansCantonEnfant());
    boolean p1Independant = safeBoolean(request.isParent1EstIndependant());
    boolean p2Independant = safeBoolean(request.isParent2EstIndependant());
    String enfantAvec = safeString(request.getEnfantAvecParent()); // "PARENT_1" ou "PARENT_2"

    Number salaireP1 = safeNumber(request.getParent1Salaire());
    Number salaireP2 = safeNumber(request.getParent2Salaire());

    // Cas A : Un seul parent actif
    if (p1AL && !p2AL) return PARENT_1;
    if (p2AL && !p1AL) return PARENT_2;

    // Cas B : Un seul a l'autorité parentale
    if (p1Autorite && !p2Autorite) return PARENT_1;
    if (p2Autorite && !p1Autorite) return PARENT_2;

    // Cas C : Parents séparés
    if (!pEnsemble) {
      // Cas C : celui qui vit avec l’enfant
      if (enfantAvec.equals(PARENT_1)) return PARENT_1;
      if (enfantAvec.equals(PARENT_2)) return PARENT_2;
    }

    // Cas D : celui qui travaille dans le canton de l’enfant
    if (p1TravCanton && !p2TravCanton) return PARENT_1;
    if (p2TravCanton && !p1TravCanton) return PARENT_2;

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
