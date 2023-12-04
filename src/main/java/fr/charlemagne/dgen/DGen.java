package fr.charlemagne.dgen;

import fr.charlemagne.dgen.controllers.Controller;
import fr.charlemagne.dgen.controllers.DragController;
import fr.charlemagne.dgen.modeles.DiagrammeModele;
import fr.charlemagne.dgen.utils.Resource;
import fr.charlemagne.dgen.vues.VueDessin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * La classe principale de l'application
 * DGen est le nom de notre projet
 */
public class DGen extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        DiagrammeModele modele = new DiagrammeModele();

        FXMLLoader fxmlLoader = new FXMLLoader(Resource.DGEN_VIEW_FXML.toURL());
        BorderPane container = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.initData(modele);

        VueDessin dessin = new VueDessin();
        container.setCenter(dessin);

        modele.enregistrerObservateur(dessin);

        DragController dragController = new DragController(modele);
        dessin.addEventHandler(DragEvent.ANY, dragController);

        Scene scene = new Scene(container, 700, 500);
        stage.setTitle("DGen");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        stage.setMaximized(true);

        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }

}