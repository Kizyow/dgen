package fr.charlemagne.dgen.fleches;

import fr.charlemagne.dgen.diagrammes.Diagramme;
import fr.charlemagne.dgen.utils.FlecheType;

/**
 * Classe permettant de créer les fleches qui relieront les diagrammes entre eux
 */
public class Fleche {

    protected Diagramme source;
    protected Diagramme destination;
    protected FlecheType flecheType;

    protected String nom;

    /**
     * Constructeur de la classe
     * @param source Diagramme d'où part la fleche
     * @param destination Diagramme qui dont la fleche pointe
     * @param flecheType type de la fleche
     */
    public Fleche(Diagramme source, Diagramme destination, FlecheType flecheType) {
        this.source = source;
        this.destination = destination;
        this.flecheType = flecheType;
        this.nom = "";
    }


    /**
     * Methode qui retourne le diagramme source
     * @return le diagramme d'où la fleche part
     */
    public Diagramme getSource() {
        return source;
    }

    /**
     * Methode qui retourne le diagramme destination
     * @return le diagramme dont la fleche pointe
     */
    public Diagramme getDestination() {
        return destination;
    }

    /**
     * Methode qui retourne le type de la fleche
     * @return
     */
    public FlecheType getFlecheType() {
        return flecheType;
    }

    /**
     * Methode qui retourne le nom de la fleche
     * @return le nom de la fleche
     */
    public String getNom() {
        return nom;
    }

    /**
     * Methode permettant de changer le nom de la fleche
     * @param nom : nom de la fleche
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
}
