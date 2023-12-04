package fr.charlemagne.dgen.modeles;

import fr.charlemagne.dgen.diagrammes.*;
import fr.charlemagne.dgen.diagrammes.Enum;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Classe permettant de faire l'introspection du projet de l'utilisateur
 */
public class Introspection {

    //Dossier parent du projet
    private File parentDirectory;
    //Chargeur des fichiers ".class"
    private URLClassLoader classLoader;

    /**
     * Constructeur
     * @param parentDirectory dossier parent
     */
    public Introspection(File parentDirectory) {
        this.initialize(parentDirectory);
    }

    /**
     * Methode qui permet d'initialiser l'introspection
     * @param parentDirectory dossier parent
     */
    private void initialize(File parentDirectory) {
        this.parentDirectory = parentDirectory;
        try {
            // récupère le chemin du dossier parent
            URL[] classPath = new URL[]{parentDirectory.toURI().toURL()};
            // creation d'un seul et unique class loader pour ce dossier parent
            this.classLoader = new URLClassLoader(classPath);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Methode qui permet de charger les ".class"
     * @param file fichier courant
     * @return La classe chargée du ".class"
     */
    public Class<?> loadClass(File file) {
        // si le fichier est un dossier ou qu'il ne finit pas par un ".class", on ne fait rien
        if (file.isDirectory() || !file.getName().endsWith(".class")) return null;

        // Permet de recuperer le chemin relatif entre le fichier courant et le dossier parent
        // et on remplace tout les "/" par des "." car on part du principe que c'est le package du fichier
        String relativePath = parentDirectory.toURI().relativize(file.toURI()).getPath().replaceAll("/", ".");
        // permet de recuperer le chemin du relativePath tout en enlevant le ".class" a la fin
        String filePath = relativePath.replace(".class", "");

        try {
            // retourne le ".class" charger
            return classLoader.loadClass(filePath);

            // si on a tenté de charger un ".class" incorrectement
            // càd: mauvais dossier parent, mauvais package etc..
        } catch (NoClassDefFoundError e) {
            // on re-initialise mais avec le dossier parent du dossier parent
            this.initialize(parentDirectory.getParentFile());
            // et on retente de charger la classe
            return this.loadClass(file);
            //si la classe n'est pas trouvée
        } catch (ClassNotFoundException e) {
            System.err.println("Class file not found, broken classpath?");
            throw new RuntimeException(e);
        }

    }

    /**
     * Methode qui permet de determiner le type de la classe courante
     * @param pClass classe courante compiler
     * @return le diagramme representant la classe
     */
    public Diagramme inspectClass(Class<?> pClass) {

        Diagramme diagramme;

        if (pClass.isInterface()) {
            diagramme = new Interface(pClass);
        } else if (Modifier.isAbstract(pClass.getModifiers())) {
            diagramme = new Abstract(pClass);
        } else if (pClass.isEnum()) {
            diagramme = new Enum(pClass);
        } else {
            diagramme = new Classe(pClass);
        }

        return diagramme;

    }

    /**
     * Recuperer le dossier parent
     * @return File du dossier parent
     */
    public File getParentDirectory() {
        return parentDirectory;
    }

}
