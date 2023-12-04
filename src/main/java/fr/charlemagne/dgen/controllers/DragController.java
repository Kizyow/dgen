package fr.charlemagne.dgen.controllers;

import fr.charlemagne.dgen.modeles.DiagrammeModele;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;

public class DragController implements EventHandler<DragEvent> {

    private final DiagrammeModele modele;

    public DragController(DiagrammeModele mod) {
        this.modele = mod;
    }

    /**
     * Methode qui permet de déplacer un fichier .class vers la zone de dessin pour créer les diagrammes
     * @param event
     */
    @Override
    public void handle(DragEvent event) {

        // si le drag est en cours
        if (event.getEventType() == DragEvent.DRAG_OVER) {

            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();

            // sinon si le drag est terminé
        } else if (event.getEventType() == DragEvent.DRAG_DROPPED) {

            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            // si c'est un répertoire, on parcours tout les fichiers .class et on les ajoute
            if (dragboard.hasString()) {

                modele.setPositionX(event.getX() - 50);
                modele.setPositionY(event.getY() - 50);
                File file = new File(dragboard.getString());

                if (file.isDirectory()) {

                    File[] files = file.listFiles();

                    for (File f : files) {
                        if (f.isFile() && f.getName().endsWith(".class")) {
                            modele.ajouterDiagramme(f.getAbsolutePath());
                        }
                    }

                } else {
                    modele.ajouterDiagramme(dragboard.getString());
                }

                success = true;

            }

            event.setDropCompleted(success);
            event.consume();

        }

    }

}
