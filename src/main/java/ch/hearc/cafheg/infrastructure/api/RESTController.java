package ch.hearc.cafheg.infrastructure.api;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;

import ch.hearc.cafheg.business.allocations.*;
import ch.hearc.cafheg.business.versements.VersementService;
import ch.hearc.cafheg.infrastructure.pdf.PDFExporter;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.EnfantMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class RESTController {

  private final AllocationService allocationService;
  private final VersementService versementService;

  private static final Logger logger = LogManager.getLogger(RESTController.class);


  public RESTController() {
    this.allocationService = new AllocationService(new AllocataireMapper(), new AllocationMapper());
    this.versementService = new VersementService(new VersementMapper(), new AllocataireMapper(),
        new PDFExporter(new EnfantMapper()));
  }

  /*
  // Headers de la requête HTTP doit contenir "Content-Type: application/json"
  // BODY de la requête HTTP à transmettre afin de tester le endpoint
  {
      "enfantResidence" : "Neuchâtel",
      "parent1Residence" : "Neuchâtel",
      "parent2Residence" : "Bienne",
      "parent1ActiviteLucrative" : true,
      "parent2ActiviteLucrative" : true,
      "parent1Salaire" : 2500,
      "parent2Salaire" : 3000
  }
   */
  @PostMapping("/droits/quel-parent")
  public String getParentDroitAllocation(@RequestBody ParentAllocationRequest params) {
    return inTransaction(() -> allocationService.getParentDroitAllocation(params));
  }

  @GetMapping("/allocataires")
  public List<Allocataire> allocataires(
      @RequestParam(value = "startsWith", required = false) String start) {
    return inTransaction(() -> allocationService.findAllAllocataires(start));
  }

  @GetMapping("/allocations")
  public List<Allocation> allocations() {
    return inTransaction(allocationService::findAllocationsActuelles);
  }

  @GetMapping("/allocations/{year}/somme")
  public BigDecimal sommeAs(@PathVariable("year") int year) {
    return inTransaction(() -> versementService.findSommeAllocationParAnnee(year).getValue());
  }

  @GetMapping("/allocations-naissances/{year}/somme")
  public BigDecimal sommeAns(@PathVariable("year") int year) {
    return inTransaction(
        () -> versementService.findSommeAllocationNaissanceParAnnee(year).getValue());
  }

  @GetMapping(value = "/allocataires/{allocataireId}/allocations", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfAllocations(@PathVariable("allocataireId") int allocataireId) {
    return inTransaction(() -> versementService.exportPDFAllocataire(allocataireId));
  }

  @GetMapping(value = "/allocataires/{allocataireId}/versements", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfVersements(@PathVariable("allocataireId") int allocataireId) {
    return inTransaction(() -> versementService.exportPDFVersements(allocataireId));
  }

//  @DeleteMapping("/allocataires/{noAVS}")
//  public ResponseEntity<?> supprimerAllocataire(@PathVariable String noAVS) {
//    return inTransaction(() -> {
//      try {
//        AllocataireService service = new AllocataireService(new AllocataireMapper(), new VersementMapper());
//        service.supprimerAllocataireParNoAVS(noAVS);
//        return ResponseEntity.noContent().build();
//      } catch (IllegalArgumentException e) {
//        return ResponseEntity.status(404).body(e.getMessage());
//      } catch (IllegalStateException e) {
//        return ResponseEntity.status(409).body(e.getMessage());
//      }
//    });
//  }
  @DeleteMapping("/allocataires/{noAVS}")
  public ResponseEntity<?> supprimerAllocataire(@PathVariable String noAVS) {
    return inTransaction(() -> {
      try {
        AllocataireService service = new AllocataireService(new AllocataireMapper(), new VersementMapper());
        service.supprimerAllocataireParNoAVS(noAVS);
        return ResponseEntity.noContent().build();
      } catch (IllegalArgumentException e) {
        logger.error("Erreur 404 : Allocataire non trouvé pour le No AVS {}", noAVS, e);
        return ResponseEntity.status(404).body(e.getMessage());
      } catch (IllegalStateException e) {
        logger.error("Erreur 409 : Suppression impossible, l'allocataire {} a des versements", noAVS, e);
        return ResponseEntity.status(409).body(e.getMessage());
      }
    });
  }


  @PutMapping("/allocataires/{noAVS}")
  public ResponseEntity<?> modifierAllocataire(
          @PathVariable String noAVS,
          @RequestBody Map<String, String> body
  ) {
    return inTransaction(() -> {
      try {
        String nom = body.get("nom");
        String prenom = body.get("prenom");

        AllocataireMapper mapper = new AllocataireMapper();
        long id = mapper.findNumeroByNoAVS(noAVS);
        Allocataire allocataire = mapper.findById(id);

        AllocataireService service = new AllocataireService(mapper, new VersementMapper());
        boolean updated = service.modifierNomPrenom(allocataire, nom, prenom);

        if (updated) {
          return ResponseEntity.ok("Allocataire mis à jour");
        } else {
          return ResponseEntity.ok("Aucune modification effectuée");
        }

      } catch (IllegalArgumentException e) {
        logger.error("Erreur lors de la modification de l’allocataire {} : {}", noAVS, e.getMessage(), e);
        return ResponseEntity.status(404).body(e.getMessage());
      }
    });
  }
}
