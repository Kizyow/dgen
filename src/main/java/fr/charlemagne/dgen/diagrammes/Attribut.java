package fr.charlemagne.dgen.diagrammes;

import fr.charlemagne.dgen.utils.Visibilite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de gerer les attributs de la classe courante
 */
public class Attribut {

    //nom de l'attribut
    private String nom;
    //representation du type de l'attribut
    private String visibilite;
    //type de l'attribut
    private String type;

    private boolean primitive = false;

    /**
     * Constructeur de l'attribut
     * @param field
     */
    public Attribut(Field field) {
        this.nom = field.getName();
        this.visibilite = Visibilite.toString(field.getModifiers());
        this.type = field.getType().getSimpleName();
        //verifie si le type de l'attribut est primitif ou un string
        if (field.getType().isPrimitive()||field.getType().equals(String.class)) {
            this.primitive = true;
        }
    }

    public String getNom() {
        return nom;
    }


    public String getVisibilite() {
        return visibilite;
    }

    public String getType() {
        return type;
    }

    /**
     * Methode qui affiche les donnees de l'attribut en chaine de caractere
     * @return
     */
    @Override
    public String toString() {
        return visibilite + " " + getNom() + " : " + type;
    }

    public static List<Attribut> listOf(Field[] fields) {
        List<Attribut> attributList = new ArrayList<>();
        for (Field field : fields) {
            Attribut attribut = new Attribut(field);
            attributList.add(attribut);
        }

        return attributList;
    }

    /**
     * Methode permettant de verifier si l'atribut courant est primitive ou non
     * @return un booleen
     */
    public boolean isPrimitive() {
        return primitive;
    }

}
