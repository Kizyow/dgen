package fr.charlemagne.dgen.diagrammes;

import fr.charlemagne.dgen.utils.Resource;
import javafx.scene.image.Image;

/**
 * Classe permettant de representer le diagramme d'une classe
 */
public class Classe extends Diagramme {

    /**
     * Constructeur
     * @param pClass classe compiler
     */
    public Classe(Class<?> pClass) {
        super(pClass, "Classe");
    }

    /**
     * Methode permetant d'afficher le logo de classe
     * @return le logo d'une classe
     */
    @Override
    public Image getLogoType() {
        return new Image(Resource.LOGO_CLASSE_PNG.toPath());
    }

}
