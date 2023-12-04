package fr.charlemagne.dgen.diagrammes;

import fr.charlemagne.dgen.utils.Visibilite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * classe qui permet de gerer toutes les methodes de la classe courante
 */
public class Methode {

    //nom de la methode
    private String nom;
    //type de la methode
    private String visibilite;
    //retour de la methode
    private String typeRetour;
    //parametre de la methode
    private String parametres;

    /**
     * Constructeur de la methode
     * @param method methode de la classe
     */
    public Methode(Method method) {
        this.nom = method.getName();
        this.visibilite = Visibilite.toString(method.getModifiers());
        this.typeRetour = method.getReturnType().getSimpleName();

        StringBuilder params = new StringBuilder();
        for (Class<?> param : method.getParameterTypes()) {
            params.append(param.getSimpleName()).append(",");
        }

        if (params.length() > 0) {
            this.parametres = params.substring(0, params.length() - 1);
        } else {
            this.parametres = "";
        }

    }

    /**
     * Methode qui permet d afficher toutes les donnees de la methode
     * @return les donnees de la methode en chaine de caractere
     */
    @Override
    public String toString() {
        return visibilite + " " + nom + "(" + parametres + ") : " + typeRetour;
    }

    public static List<Methode> listOf(Method[] methods) {
        List<Methode> methodeList = new ArrayList<>();
        for (Method method : methods) {
            Methode methode = new Methode(method);
            methodeList.add(methode);
        }
        return methodeList;
    }

}
