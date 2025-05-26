package ch.hearc.cafheg.business.allocations;

public class ParentAllocationRequest {
    private String enfantResidence;
    private String parent1Residence;
    private String parent2Residence;
    private boolean parent1ActiviteLucrative;
    private boolean parent2ActiviteLucrative;
    private boolean parentsEnsemble;
    private boolean parent1AutoriteParentale;
    private boolean parent2AutoriteParentale;
    private boolean parent1TravailleDansCantonEnfant;
    private boolean parent2TravailleDansCantonEnfant;
    private boolean parent1EstIndependant;
    private boolean parent2EstIndependant;
    private String enfantAvecParent; // "PARENT_1" ou "PARENT_2
    private Number parent1Salaire;
    private Number parent2Salaire;


    //todo tout ca si le temps le permet.
    // TODO les résidences ne sont pas utiliséés?? mais en fait il faudrait
    // prendre en compte les lieux de résidence enfant/parents...
    //et aussi lieu de travail parents / lieu de résidence enfant...

    public String getEnfantResidence() {
        return enfantResidence;
    }

    public void setEnfantResidence(String enfantResidence) {
        this.enfantResidence = enfantResidence;
    }

    public String getParent1Residence() {
        return parent1Residence;
    }

    public void setParent1Residence(String parent1Residence) {
        this.parent1Residence = parent1Residence;
    }

    public String getParent2Residence() {
        return parent2Residence;
    }

    public void setParent2Residence(String parent2Residence) {
        this.parent2Residence = parent2Residence;
    }

    public boolean isParent1ActiviteLucrative() {
        return parent1ActiviteLucrative;
    }

    public void setParent1ActiviteLucrative(boolean parent1ActiviteLucrative) {
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
    }

    public boolean isParent2ActiviteLucrative() {
        return parent2ActiviteLucrative;
    }

    public void setParent2ActiviteLucrative(boolean parent2ActiviteLucrative) {
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
    }

    public boolean isParentsEnsemble() {
        return parentsEnsemble;
    }

    public void setParentsEnsemble(boolean parentsEnsemble) {
        this.parentsEnsemble = parentsEnsemble;
    }

    public Number getParent1Salaire() {
        return parent1Salaire;
    }

    public void setParent1Salaire(Number parent1Salaire) {
        this.parent1Salaire = parent1Salaire;
    }

    public Number getParent2Salaire() {
        return parent2Salaire;
    }

    public void setParent2Salaire(Number parent2Salaire) {
        this.parent2Salaire = parent2Salaire;
    }

    public boolean isParent1AutoriteParentale() {
        return parent1AutoriteParentale;
    }

    public void setParent1AutoriteParentale(boolean parent1AutoriteParentale) {
        this.parent1AutoriteParentale = parent1AutoriteParentale;
    }

    public boolean isParent2AutoriteParentale() {
        return parent2AutoriteParentale;
    }

    public void setParent2AutoriteParentale(boolean parent2AutoriteParentale) {
        this.parent2AutoriteParentale = parent2AutoriteParentale;
    }

    public boolean isParent1TravailleDansCantonEnfant() {
        return parent1TravailleDansCantonEnfant;
    }

    public void setParent1TravailleDansCantonEnfant(boolean parent1TravailleDansCantonEnfant) {
        this.parent1TravailleDansCantonEnfant = parent1TravailleDansCantonEnfant;
    }

    public boolean isParent2TravailleDansCantonEnfant() {
        return parent2TravailleDansCantonEnfant;
    }

    public void setParent2TravailleDansCantonEnfant(boolean parent2TravailleDansCantonEnfant) {
        this.parent2TravailleDansCantonEnfant = parent2TravailleDansCantonEnfant;
    }

    public boolean isParent1EstIndependant() {
        return parent1EstIndependant;
    }

    public void setParent1EstIndependant(boolean parent1EstIndependant) {
        this.parent1EstIndependant = parent1EstIndependant;
    }

    public boolean isParent2EstIndependant() {
        return parent2EstIndependant;
    }

    public void setParent2EstIndependant(boolean parent2EstIndependant) {
        this.parent2EstIndependant = parent2EstIndependant;
    }

    public String getEnfantAvecParent() {
        return enfantAvecParent;
    }

    public void setEnfantAvecParent(String enfantAvecParent) {
        this.enfantAvecParent = enfantAvecParent;
    }



}

