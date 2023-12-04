package fr.charlemagne.dgen.utils;

import fr.charlemagne.dgen.diagrammes.Diagramme;

/**
 * Classe qui permet de relier le diagramme avec le type de fleche qu'il appartient
 */
public class DiagrammeData {

    private Diagramme diagramme;
    private FlecheType flecheType;

    /**
     * Constructeur
     * @param diagramme diagramme courant
     * @param flecheType type de la fleche que le diagramme possede
     */
    public DiagrammeData(Diagramme diagramme, FlecheType flecheType) {
        this.diagramme = diagramme;
        this.flecheType = flecheType;
    }

    /**
     * Methode qui retourne le diagramme
     * @return le diagramme
     */
    public Diagramme getDiagramme() {
        return diagramme;
    }

    /**
     * Methode qui retourne le type de fleche
     * @return le type de fleche que le diagramme appartient
     */
    public FlecheType getFlecheType() {
        return flecheType;
    }

    /**
     * permet de changer de diagramme
     * @param diagramme le nouveau diagramme
     */
    public void setDiagramme(Diagramme diagramme) {
        this.diagramme = diagramme;
    }
}
