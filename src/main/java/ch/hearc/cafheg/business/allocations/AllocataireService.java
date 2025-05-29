package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import ch.hearc.cafheg.business.versements.VersementParentEnfant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class AllocataireService {

    private static final Logger logger = LogManager.getLogger(AllocataireService.class);

    private final AllocataireMapper allocataireMapper;
    private final VersementMapper versementMapper;

    public AllocataireService(AllocataireMapper allocataireMapper, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.versementMapper = versementMapper;
    }

    public void supprimerAllocataireParNoAVS(String noAVS) {
        long numero;
        try {
            numero = allocataireMapper.findNumeroByNoAVS(noAVS);
            logger.info("Allocataire à supprimer : {}", numero);
        } catch (IllegalArgumentException e) {
            logger.error("Aucun allocataire trouvé avec le No AVS : {}", noAVS, e);
            throw new IllegalArgumentException("Aucun allocataire trouvé avec le No AVS : " + noAVS, e);
        }


        List<VersementParentEnfant> versements = versementMapper.findVersementParentEnfant();
        boolean aDesVersements = versements.stream()
                .anyMatch(v -> v.getParentId() == numero);

        if (aDesVersements) {
            logger.warn("Tentative de suppression refusée : l'allocataire {} a des versements.", numero);
            throw new IllegalStateException("Impossible de supprimer : l'allocataire a des versements.");
        }

        allocataireMapper.delete(numero);
        logger.info("Allocataire supprimé : {}", numero);
    }

    public boolean modifierNomPrenom(Allocataire allocataire, String nouveauNom, String nouveauPrenom) {
        String nomActuel = allocataire.getNom();
        String prenomActuel = allocataire.getPrenom();

        if (nomActuel.equals(nouveauNom) && prenomActuel.equals(nouveauPrenom)) {
            logger.info("Aucune modification nécessaire pour l'allocataire {}", allocataire.getNoAVS().getValue());
            return false; // rien modifié
        }

        allocataireMapper.updateNomPrenom(allocataire.getNoAVS().getValue(), nouveauNom, nouveauPrenom);
        logger.info("Allocataire mis à jour : {}", allocataire.getNoAVS().getValue());
        return true; // modifié
    }
}

