package fr.charlemagne.dgen.diagrammes;

import fr.charlemagne.dgen.utils.Resource;
import javafx.scene.image.Image;

/**
 * Classe permettant de representer le diagramme d'une classe abstraite
 */
public class Abstract extends Diagramme {

    /**
     * Constructeur
     * @param pClass classe compiler
     */
    public Abstract(Class<?> pClass) {
        super(pClass, "Abstract");
    }

    /**
     * Methode permetant d'afficher le logo d'une classe abstraite
     * @return le logo d'une classe abstraite
     */
    @Override
    public Image getLogoType() {
        return new Image(Resource.LOGO_ABSTRACT_PNG.toPath());
    }


}

