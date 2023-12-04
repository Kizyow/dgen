package fr.charlemagne.dgen.utils;

import java.lang.reflect.Modifier;
import java.util.StringJoiner;

/**
 * class permettant de afficher un caractere unique pour chaque type d'attribut
 */
public class Visibilite {

    /**
     * Methode qui permet de retourner le caractere unique du type de l'attribut
     *
     * @param mod nombre binaire de la visibilite l'attribut courant
     * @return le caractere unique de la visibilite de l'attribut
     */
    public static String toString(int mod) {
        StringJoiner sj = new StringJoiner(" ");

        if ((mod & Modifier.PUBLIC) != 0) sj.add("+");
        if ((mod & Modifier.PROTECTED) != 0) sj.add("#");
        if ((mod & Modifier.PRIVATE) != 0) sj.add("-");
        if ((mod & Modifier.STATIC) != 0) sj.add("(static)");
        if ((mod & Modifier.FINAL) != 0) sj.add("(final)");
        if ((mod & Modifier.ABSTRACT) != 0) sj.add("abstract");

        return sj.toString();
    }

}
