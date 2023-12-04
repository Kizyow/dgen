package fr.charlemagne.dgen.diagrammes;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite permettant de representer un diagramme
 */
public abstract class Diagramme {
    //classe compiler
    protected Class<?> classe;
    //type du diagramme (interface, classe, enum, abstract)
    protected String type;
    //nom de la classe
    protected String nomClasse;
    //nom du package
    protected String nomPackage;
    //Liste des attributs de la classe
    protected List<Attribut> attributs;
    //liste des methode de la classe
    protected List<Methode> methodes;
    //nom de la classe qui herite
    protected String nomHeritage;
    //liste des noms d'interface
    protected List<String> nomInterfaces;

    /**
     * Constructeur d'un diagramme
     * @param pClass classe compiler
     * @param typeC type du diagramme (interface, classe, enum, abstract)
     */
    public Diagramme(Class<?> pClass, String typeC) {
        this.nomClasse = pClass.getSimpleName();
        this.type = typeC;
        this.nomPackage = pClass.getPackage().getName();
        this.attributs = Attribut.listOf(pClass.getDeclaredFields());
        this.methodes = Methode.listOf(pClass.getDeclaredMethods());
        //Verifie si la classe herite d'une autre classe
        if (pClass.getSuperclass() != null) {
            this.nomHeritage = pClass.getSuperclass().getSimpleName();
        }
        List<String> listNomInterfaces = new ArrayList<>();
        //verifie si la classe implemente des interfaces
        for (Class<?> interfaces : pClass.getInterfaces()) {
            listNomInterfaces.add(interfaces.getSimpleName());
        }
        this.nomInterfaces = listNomInterfaces;
    }

    /**
     * Methode qui permet de retourner l'image du type de diagramme
     * @return l'image
     */
    public abstract Image getLogoType();

    public String getType() {
        return this.type;
    }

    public String getNomClasse() {
        return this.nomClasse;
    }

    public String getNomPackage() {
        return this.nomPackage;
    }

    public List<Attribut> getAttributs() {
        return this.attributs;
    }

    public List<Methode> getMethodes() {
        return this.methodes;
    }

    public String getNomHeritage() {
        return nomHeritage;
    }

    public List<String> getNomInterfaces() {
        return nomInterfaces;
    }

    public String toString() {
        return "Nom de la classe : " + this.nomClasse + " Type de la classe : " + this.type + " Nom du package : " + this.nomPackage + " Attributs : " + this.attributs + " Methodes : " + this.methodes;
    }

}
