package fr.charlemagne.dgen.diagrammes;

import fr.charlemagne.dgen.utils.Resource;
import javafx.scene.image.Image;

/**
 * Classe qui permet de representer le diagramme d'une interface
 */
public class Interface extends Diagramme {
    /**
     * Constructeur
     * @param pClass classe compiler
     */
    public Interface(Class<?> pClass) {
        super(pClass, "Interface");
    }

    /**
     * Methode qui permet d'afficher le logo d'une interface
     * @return le logo de l'interface
     */
    @Override
    public Image getLogoType() {
        return new Image(Resource.LOGO_INTERFACE_PNG.toPath());
    }

}

