package fr.charlemagne.dgen.utils;

import fr.charlemagne.dgen.DGen;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Permet de réferencer tout les fichiers utilisés dans l'application
 * Cela permet d'éviter de tout hardcode et si on veut changer le chemin d'un fichier
 * on n'a qu'a modifier ici !
 */
public enum Resource {

    DARK_MODE_CSS("css/dark-mode.css"),
    LIGHT_MODE_CSS("css/light-mode.css"),

    DGEN_VIEW_FXML("fxml/dgen-view.fxml"),

    FOLDER_PNG("images/folder.png"),
    FILE_PNG("images/file.png"),

    EXPORT_LIGHT_PNG("images/export-light.png"),

    LIGHT_MODE_PNG("images/light-mode.png"),
    NIGHT_MODE_PNG("images/night-mode.png"),

    LOGO_ENUM_PNG("images/logo-enum.png"),
    LOGO_CLASSE_PNG("images/logo-classe.png"),
    LOGO_ABSTRACT_PNG("images/logo-abstract.png"),
    LOGO_INTERFACE_PNG("images/logo-interface.png"),

    OPEN_LIGHT_PNG("images/open-light.png"),
    OPEN_DARK_PNG("images/open-dark.png"),

    ERROR_SOUND_MP3("sounds/error-sound.mp3");

    private final String resourcePath;

    Resource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String toPath(){
        URL resourceUrl = DGen.class.getResource("/" + this.resourcePath);

        if(resourceUrl != null){
            return resourceUrl.toExternalForm();
        } else {
            try {
                throw new MalformedURLException("La resource n'a pas été trouvée dans le fichier 'resources'");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public URL toURL(){
        URL resourceUrl = DGen.class.getResource("/" + this.resourcePath);

        if(resourceUrl != null){
            return resourceUrl;
        } else {
            try {
                throw new MalformedURLException("La resource n'a pas été trouvée dans le fichier 'resources'");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public InputStream toInputStream(){
        InputStream resourceUrl = DGen.class.getResourceAsStream("/" + this.resourcePath);

        if(resourceUrl != null){
            return resourceUrl;
        } else {
            try {
                throw new MalformedURLException("La resource n'a pas été trouvée dans le fichier 'resources'");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
