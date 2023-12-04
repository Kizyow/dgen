package fr.charlemagne.dgen.controllers;

import fr.charlemagne.dgen.utils.Resource;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StyleController {

    private BorderPane parent;
    private Text title;
    private TreeView<?> treeView;
    private ImageView imgMode;

    public StyleController(BorderPane parent, Text title, TreeView<?> treeView, ImageView imgMode) {
        this.parent = parent;
        this.title = title;
        this.treeView = treeView;
        this.imgMode = imgMode;
    }

    /**
     * Change la couleur de l'application en blanc
     */
    public void setLightMode() {
        parent.getStylesheets().remove(Resource.DARK_MODE_CSS.toPath());
        parent.getStylesheets().add(Resource.LIGHT_MODE_CSS.toPath());
        title.setFill(Color.BLACK);
        treeView.getStylesheets().remove(Resource.DARK_MODE_CSS.toPath());
        treeView.getStylesheets().add(Resource.LIGHT_MODE_CSS.toPath());
        Image image = new Image(Resource.NIGHT_MODE_PNG.toPath());
        imgMode.setImage(image);
    }

    /**
     * Change la couleur de l'application en noir
     */
    public void setDarkMode() {
        parent.getStylesheets().remove(Resource.LIGHT_MODE_CSS.toPath());
        parent.getStylesheets().add(Resource.DARK_MODE_CSS.toPath());
        title.setFill(Color.WHITE);
        treeView.getStylesheets().remove(Resource.LIGHT_MODE_CSS.toPath());
        treeView.getStylesheets().add(Resource.DARK_MODE_CSS.toPath());
        Image image = new Image(Resource.LIGHT_MODE_PNG.toPath());
        imgMode.setImage(image);
    }

}
