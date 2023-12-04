package fr.charlemagne.dgen.diagrammes;

import fr.charlemagne.dgen.utils.Resource;
import javafx.scene.image.Image;

/**
 * Classe qui permet de representer un diagramme d'un enum
 */
public class Enum extends Diagramme {

    /**
     * Constructeur
     * @param pClass classe compiler
     */
    public Enum(Class<?> pClass) {
        super(pClass, "Enum");
    }

    /**
     * Methode qui permet d'afficher le logo d'un enum
     * @return le logo de l'enum
     */
    @Override
    public Image getLogoType() {
        return new Image(Resource.LOGO_ENUM_PNG.toPath());
    }

}
