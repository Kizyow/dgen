package fr.charlemagne.dgen.observateurs;

/**
 * Interface permettant de gerer les vues avec le model
 */
public interface Sujet {
    void enregistrerObservateur(Observateur o);
    void supprimerObservateur(Observateur o);
    void notifierObservateur();

}
