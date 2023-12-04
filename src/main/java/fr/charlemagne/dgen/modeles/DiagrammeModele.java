package fr.charlemagne.dgen.modeles;

import fr.charlemagne.dgen.diagrammes.Diagramme;
import fr.charlemagne.dgen.observateurs.Observateur;
import fr.charlemagne.dgen.observateurs.Sujet;
import fr.charlemagne.dgen.fleches.Fleche;
import fr.charlemagne.dgen.utils.FlecheType;
import fr.charlemagne.dgen.utils.Resource;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui est le modele de l'application
 */
public class DiagrammeModele implements Sujet {

    //liste des vues
    private List<Observateur> observateurs;

    //Liste des diagrammes dans l'application
    private List<Diagramme> diagrammes;

    //Liste des fleches dans l'application
    private List<Fleche> fleches;

    //Introspection du projet
    private Introspection introspection;

    //position du diagramme courant
    private double positionX = 0;
    private double positionY = 0;

    //son d'erreur en cas de mauvaise utilisation de l'application
    private final AudioClip errorSound;

    /**
     * Constructeur du modele
     */
    public DiagrammeModele() {
        this.diagrammes = new ArrayList<>();
        this.observateurs = new ArrayList<>();
        this.fleches = new ArrayList<>();
        this.errorSound = new AudioClip(Resource.ERROR_SOUND_MP3.toPath());
    }

    /**
     * Methode qui permet d'ajouter un diagramme dans l'application
     * @param path chemin du fichier .class
     */
    public void ajouterDiagramme(String path) {

        File file = new File(path);

        Class<?> loadClass = introspection.loadClass(file);
        if (loadClass != null) {

            Diagramme diagramme = introspection.inspectClass(loadClass);
            //verifie si le diagramme est deja afficher dans l'application
            if (inDiagrammes(diagramme)) {
                errorSound.play();
                return;
            }
            //ajout du diagramme dans la liste des diagrammes
            diagrammes.add(diagramme);
            //creer les fleche qui sont potentiellement a relier avec le diagramme qui vient d'etre ajouter
            creerFleche(diagramme);
            //creer les fleche qui sont potentiellement a relier avec le diagramme qui vient d'etre ajouter par rapport aux anciens
            creerFlecheInverser(diagramme);
            //actualiser toutes les vues relier au modele
            notifierObservateur();
        } else {
            errorSound.play();
        }

    }

    /**
     * Methode qui permet de creer les fleches dont le diagramme qui vient d etre ajouter est le diagramme source
     * @param diagramme diagramme qui vient d'etre ajouter dans l'application
     */
    public void creerFleche(Diagramme diagramme) {

        //parcours de tous les diagrammes present dans l'application
        for (Diagramme d : diagrammes) {

            if (d.getNomClasse().equals(diagramme.getNomHeritage())) {
                fleches.add(new Fleche(diagramme, d, FlecheType.HERITAGE));
            }

            if (diagramme.getNomInterfaces().contains(d.getNomClasse())) {
                fleches.add(new Fleche(diagramme, d, FlecheType.IMPLEMENTATION));
            }

            if (diagramme.getAttributs().stream().anyMatch(attribut -> attribut.getType().equals(d.getNomClasse()))) {
                fleches.add(new Fleche(diagramme, d, FlecheType.DEPENDANCE));
            }
        }
    }

    /**
     * Methode qui permet de creer les fleches dont le diagramme qui vient d'etre ajouter est le diagramme destination
     * @param diagramme
     */
    public void creerFlecheInverser(Diagramme diagramme) {
        for (Diagramme d : diagrammes) {
            if (diagramme.getNomClasse().equals(d.getNomHeritage())) {
                fleches.add(new Fleche(d, diagramme, FlecheType.HERITAGE));
            }

            if (d.getNomInterfaces().contains(diagramme.getNomClasse())) {
                fleches.add(new Fleche(d, diagramme, FlecheType.IMPLEMENTATION));
            }

            if (d.getAttributs().stream().anyMatch(attribut -> attribut.getType().equals(diagramme.getNomClasse()))) {
                fleches.add(new Fleche(d, diagramme, FlecheType.DEPENDANCE));
            }
        }
    }

    /**
     * Methode permettant d'ajouter une fleche entre 2 diagrammes
     * @param diagrammeSource digramme d'où part la fleche
     * @param diagrammeDestination digramme où arrive la fleche
     * @param flecheType type de la fleche
     */
    public void ajouterFleche(Diagramme diagrammeSource, Diagramme diagrammeDestination, FlecheType flecheType) {
        fleches.add(new Fleche(diagrammeSource, diagrammeDestination, flecheType));
    }

    /**
     * Methode permettant de verifier si le diagramme en parametre est deja dans l'application
     * @param diagramme diagramme a verifier si il est dans l'application ou non
     * @return un booleen
     */
    public boolean inDiagrammes(Diagramme diagramme) {
        for (Diagramme d : diagrammes) {
            if (d.getNomClasse().equals(diagramme.getNomClasse())) {
                return true;
            }

        }
        return false;
    }


    /**
     * Methode qui permet de relier le modele avec les vues
     * @param o la vue courante
     */
    @Override
    public void enregistrerObservateur(Observateur o) {
        observateurs.add(o);
    }

    /**
     * Methode qui permet de supprimer une vue du modele
     * @param o vue courante
     */
    @Override
    public void supprimerObservateur(Observateur o) {
        observateurs.remove(o);
    }

    /**
     * Methode permettant d'actualiser toutes les vues en lien avec le modele et donc d'actualiser l'interface graphique
     */
    @Override
    public void notifierObservateur() {
        for (Observateur o : observateurs) {
            o.actualiser(this);
        }
    }

    /**
     * Methode qui retourne les diagrammes presents dans l'application
     * @return la liste de diagramme
     */
    public List<Diagramme> getDiagrammes() {
        return diagrammes;
    }

    /**
     * Methode qui retourne les fleches presentes dans l'application
     * @return la liste des fleches
     */
    public List<Fleche> getFleches() {
        return fleches;
    }

    /**
     * Récuperer l'introspection
     * @return L'introspection
     */
    public Introspection getIntrospection() {
        return this.introspection;
    }

    /**
     * Permet de redéfinir le dossier parent pour l'introspection
     * (Par exemple: choisir un nouveau dossier sur lequel travailler...)
     * @param file Le dossier parent
     */
    public void defineIntrospection(File file) {
        this.introspection = new Introspection(file);
    }

    /**
     * Methode qui affiche la position X du diagramme courant
     * @return position X du diagramme courant
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * Methode qui permet de changer la position X du diagramme courant
     * @param positionX nouvelle position X du diagramme courant
     */
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    /**
     * Methode qui affiche la position Y du diagramme courant
     * @return position Y du diagramme courant
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * Methode qui permet de changer la position Y du diagramme courant
     * @param positionY nouvelle position Y du diagramme courant
     */
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

}
