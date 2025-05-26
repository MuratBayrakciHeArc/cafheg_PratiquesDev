package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  private static final String PARENT_1 = "PARENT_1";
  private static final String PARENT_2 = "PARENT_2";

// TODO clean code et todos avant commit.
// TODO quand j'ai fini d'écrire ces tests, lui demander de vérifier selon schémà car c'est vite casse tete.

  @Test
  void getParentDroitAllocation_casA_parent1SeulActif_returnsParent1() {
    //Map<String, Object> params = new HashMap<>();
    ParentAllocationRequest req = new ParentAllocationRequest();
   // params.put("parent1ActiviteLucrative", true);
   // params.put("parent2ActiviteLucrative", false);
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(false);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_1, result);
  }

  @Test
  void getParentDroitAllocation_casA_parent2SeulActif_returnsParent2() {
    //Map<String, Object> params = new HashMap<>();
    ParentAllocationRequest req = new ParentAllocationRequest();
   // params.put("parent1ActiviteLucrative", false);
   // params.put("parent2ActiviteLucrative", true);
    req.setParent1ActiviteLucrative(false);
    req.setParent2ActiviteLucrative(true);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_2, result);
  }


//  @Test
//  void getParentDroitAllocation_lesDeuxActifs_parent1SalairePlusHaut_returnsParent1() {
//   // Map<String, Object> params = new HashMap<>();
//    ParentAllocationRequest req = new ParentAllocationRequest();
//  /*  params.put("parent1ActiviteLucrative", true);
//    params.put("parent2ActiviteLucrative", true);
//    params.put("parent1Salaire", 5000);
//    params.put("parent2Salaire", 3000);
//
//   */
//  req.setParent1ActiviteLucrative(true);
//  req.setParent2ActiviteLucrative(true);
//  req.setParent1Salaire(5000);
//  req.setParent2Salaire(3000);
//
//
//    String result = allocationService.getParentDroitAllocation(req);
//    assertEquals(PARENT_1, result);
//  }

//  @Test
//  void getParentDroitAllocation_lesDeuxActifs_parent2SalairePlusHaut_returnsParent2() {
//  //  Map<String, Object> params = new HashMap<>();
//    ParentAllocationRequest req = new ParentAllocationRequest();
////    params.put("parent1ActiviteLucrative", true);
////    params.put("parent2ActiviteLucrative", true);
////    params.put("parent1Salaire", 3000);
////    params.put("parent2Salaire", 5000);
//    req.setParent1ActiviteLucrative(true);
//    req.setParent2ActiviteLucrative(true);
//    req.setParent1Salaire(3000);
//    req.setParent2Salaire(5000);
//
//    String result = allocationService.getParentDroitAllocation(req);
//    assertEquals(PARENT_2, result);
//  }

//  @Test
//  void getParentDroitAllocation_aucunActif_parent1SalairePlusHaut_returnsParent1() {
////    Map<String, Object> params = new HashMap<>();
//    ParentAllocationRequest req = new ParentAllocationRequest();
////    params.put("parent1ActiviteLucrative", false);
////    params.put("parent2ActiviteLucrative", false);
////    params.put("parent1Salaire", 4500);
////    params.put("parent2Salaire", 3000);
//    req.setParent1ActiviteLucrative(false);
//    req.setParent2ActiviteLucrative(false);
//    req.setParent1Salaire(4500);
//    req.setParent2Salaire(3000);
//
//    String result = allocationService.getParentDroitAllocation(req);
//    assertEquals(PARENT_1, result);
//  }


//  @Test
//  void getParentDroitAllocation_salaireEgal_returnsParent2() {
////    Map<String, Object> params = new HashMap<>();
//    ParentAllocationRequest req = new ParentAllocationRequest();
////    params.put("parent1ActiviteLucrative", false);
////    params.put("parent2ActiviteLucrative", false);
////    params.put("parent1Salaire", 4000);
////    params.put("parent2Salaire", 4000);
//    req.setParent1ActiviteLucrative(false);
//    req.setParent2ActiviteLucrative(false);
//    req.setParent1Salaire(4000);
//    req.setParent2Salaire(4000);
//
//    String result = allocationService.getParentDroitAllocation(req);
//    assertEquals(PARENT_2, result); // car return PARENT_2 si égalité
//  }



//  @Test
//  void getParentDroitAllocation_casA_parent1SeulActif_returnsParent1() {
//    ParentAllocationRequest req = new ParentAllocationRequest();
//    req.setParent1ActiviteLucrative(true);
//    req.setParent2ActiviteLucrative(false);
//
//    String result = allocationService.getParentDroitAllocation(req);
//    assertEquals(PARENT_1, result);
//  }

  @Test
  void getParentDroitAllocation_casB_parent1SeulAvecAutorite_returnsParent1() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(false);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_1, result);
  }


  @Test
  void getParentDroitAllocation_casB_parent2SeulAvecAutorite_returnsParent2() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(false);
    req.setParent2AutoriteParentale(true);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_2, result);
  }




  @Test
  void getParentDroitAllocation_casC_parentsSepares_enfantAvecParent2_returnsParent2() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(false);
    req.setEnfantAvecParent(PARENT_2);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_2, result);
  }


  @Test
  void getParentDroitAllocation_casC_parentsSepares_enfantAvecParent1_returnsParent1() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(false);
    req.setEnfantAvecParent(PARENT_1);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_1, result);
  }





  @Test
  void getParentDroitAllocation_casD_parentsSepares_parent1TravailleDansCanton_returnsParent1() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setEnfantAvecParent(""); // personne spécifié       //besoin de cette ligne?
    req.setParent1TravailleDansCantonEnfant(true);
    req.setParent2TravailleDansCantonEnfant(false);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_1, result);
  }


  @Test
  void getParentDroitAllocation_casD_parentsSepares_parent2TravailleDansCanton_returnsParent2() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setEnfantAvecParent(""); // personne spécifié
    req.setParent1TravailleDansCantonEnfant(false);
    req.setParent2TravailleDansCantonEnfant(true);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_2, result);
  }




  @Test
  void getParentDroitAllocation_casE_parentsEnsemble_unSalaireUnIndependant_parent2Salaire_returnsParent2() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setParent1EstIndependant(true); // indé
    req.setParent2EstIndependant(false);  // salarié

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_2, result);
  }

  @Test
  void getParentDroitAllocation_casE_parentsEnsemble_unSalaireUnIndependant_parent1Salaire_returnsParent1() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setParent1EstIndependant(false); // salarié
    req.setParent2EstIndependant(true);  // indépendant

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_1, result);
  }

  @Test
  void getParentDroitAllocation_casE_parentsEnsemble_deuxSalaires_parent2SalairePlusHaut_returnsParent2() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setParent1EstIndependant(false); // salarié
    req.setParent2EstIndependant(false);  // salarié
    req.setParent1Salaire(3000);
    req.setParent2Salaire(5000);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_2, result);
  }

  @Test
  void getParentDroitAllocation_casE_parentsEnsemble_deuxSalaires_parent1SalairePlusHaut_returnsParent1() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setParent1EstIndependant(false); // salarié
    req.setParent2EstIndependant(false);  // salarié
    req.setParent1Salaire(5000);
    req.setParent2Salaire(3000);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_1, result);
  }







  @Test
  void getParentDroitAllocation_casF_parentsEnsemble_deuxIndependants_parent1SalairePlusHaut_returnsParent1() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setParent1EstIndependant(true);
    req.setParent2EstIndependant(true);
    req.setParent1Salaire(6000);
    req.setParent2Salaire(5500);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_1, result);
  }

  @Test
  void getParentDroitAllocation_casF_parentsEnsemble_deuxIndependants_parent2SalairePlusHaut_returnsParent2() {
    ParentAllocationRequest req = new ParentAllocationRequest();
    req.setParent1ActiviteLucrative(true);
    req.setParent2ActiviteLucrative(true);
    req.setParent1AutoriteParentale(true);
    req.setParent2AutoriteParentale(true);
    req.setParentsEnsemble(true);
    req.setParent1EstIndependant(true);
    req.setParent2EstIndependant(true);
    req.setParent1Salaire(5500);
    req.setParent2Salaire(6000);

    String result = allocationService.getParentDroitAllocation(req);
    assertEquals(PARENT_2, result);
  }





  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);

    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
        .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
            LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
            LocalDate.now(), null)));
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
        () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
        () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(0).getFin()).isNull(),
        () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
        () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
        () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(1).getFin()).isNull());
  }

}