package fr.charlemagne.dgen.vues;

import fr.charlemagne.dgen.controllers.Controller;
import fr.charlemagne.dgen.diagrammes.Diagramme;
import fr.charlemagne.dgen.fleches.Fleche;
import fr.charlemagne.dgen.modeles.DiagrammeModele;
import fr.charlemagne.dgen.observateurs.Observateur;
import fr.charlemagne.dgen.observateurs.Sujet;
import fr.charlemagne.dgen.utils.DiagrammeData;
import fr.charlemagne.dgen.utils.FlecheType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * classe permettant de representer la zone de dessin du diagramme de classe du projet de l'utilisateur
 */
public class VueDessin extends Pane implements Observateur {

    //dictionnaire associant le diagramme avec sa representation graphique
    public static Map<Diagramme, VueDiagramme> vueDiagrammeMap = new HashMap<>();
    // dictionnaire associant chaque flèche à sa representation graphique
    public static Map<Fleche, VueFleche> vueFlecheMap = new HashMap<>();
    //donnee du diagramme courant
    public static DiagrammeData diagrammeData;

    /**
     * Methode qui sera actualiser plusieurs fois
     *
     * @param s modele de l'application
     */
    @Override
    public void actualiser(Sujet s) {
        DiagrammeModele modele = (DiagrammeModele) s;

        // s'il y a un nouveau diagramme qui n'a pas de représentation graphique, alors on le cree
        if (modele.getDiagrammes().size() > vueDiagrammeMap.size()) {
            //prendre le dernier diagramme ajouter dans l'application
            Diagramme diagramme = modele.getDiagrammes().get(modele.getDiagrammes().size() - 1);
            //creer la representation graphique de ce dernier diagramme
            VueDiagramme vueDiagramme = new VueDiagramme(diagramme, modele);

            //ajout d'un conteneur menu
            ContextMenu contextMenu = new ContextMenu();

            //ajout d'un item a mettre dans le conteneur menu
            MenuItem modifier = new MenuItem("Modifier");

            // ouvre un éditeur de texte integré
            modifier.setOnAction(event -> {

                String nomClasse = diagramme.getNomClasse();
                File parentDirectory = modele.getIntrospection().getParentDirectory();

                String nomPackage = "";

                if (!diagramme.getNomPackage().isEmpty()) {
                    nomPackage = diagramme.getNomPackage() + "/";
                }

                //prendre le chemin du fichier courant
                String path = parentDirectory.getAbsolutePath() + "/" + nomPackage + nomClasse + ".java";


                OutputStream os = new OutputStream() {
                    private final StringBuilder string = new StringBuilder();

                    @Override
                    public void write(int b) {
                        this.string.append((char) b);
                    }

                    @Override
                    public String toString() {
                        return this.string.toString();
                    }
                };

                // copie le contenu du fichier java dans le text area qu'on affiche dans la vue
                // pour modifier le code

                try {
                    Files.copy(Paths.get(path), os);
                    TextArea textArea = new TextArea(os.toString());
                    textArea.setPrefSize(500, 500);
                    textArea.setEditable(true);
                    textArea.setWrapText(true);
                    textArea.setStyle("-fx-font-family: monospace");
                    textArea.setStyle("-fx-font-size: 12px");
                    textArea.setStyle("-fx-text-fill: #000000");
                    textArea.setStyle("-fx-background-color: #101010");

                    HBox hbox = new HBox();
                    VBox vbox = new VBox();

                    Button saveButton = new Button("Save");
                    String finalNomPackage = nomPackage;
                    saveButton.setOnAction(event1 -> {
                        try {

                            Files.write(Paths.get(path), textArea.getText().getBytes());

                            //recompile le fichier java

                            System.out.println(finalNomPackage);

                            String[] command = new String[]{"javac", finalNomPackage + nomClasse + ".java"};
                            ProcessBuilder builder = new ProcessBuilder(command);
                            builder.directory(Paths.get(parentDirectory.getAbsolutePath()).toFile());
                            builder.inheritIO();
                            Process process = builder.start();
                            process.waitFor();

                            // on recrée l'introspection car on a recompilé les .class
                            modele.defineIntrospection(parentDirectory);

                            // on supprime les fleches associés a ce fichier

                            this.getChildren().remove(vueDiagramme);
                            vueDiagrammeMap.remove(diagramme);
                            modele.getDiagrammes().remove(diagramme);

                            modele.getFleches().stream().filter(fleche -> fleche.getSource().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> {
                                getChildren().remove(vueFlecheMap.get(fleche));
                                vueFlecheMap.remove(fleche);
                            });

                            modele.getFleches().stream().filter(fleche -> fleche.getDestination().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> {
                                getChildren().remove(vueFlecheMap.get(fleche));
                                vueFlecheMap.remove(fleche);
                            });

                            new ArrayList<>(modele.getFleches()).stream().filter(fleche -> fleche.getSource().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> modele.getFleches().remove(fleche));
                            new ArrayList<>(modele.getFleches()).stream().filter(fleche -> fleche.getDestination().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> modele.getFleches().remove(fleche));

                            this.getChildren().remove(vbox);

                            // on réajoute le diagramme
                            String path2 = parentDirectory.getAbsolutePath() + "/" + finalNomPackage + nomClasse + ".class";
                            modele.ajouterDiagramme(path2);

                            VueDessin.changeMode();

                            // actualisation du modele
                            modele.notifierObservateur();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });


                    Button clean = new Button("Close");
                    clean.setOnAction(event1 -> {
                        this.getChildren().remove(vbox);
                    });
                    // crée la vue graphique de l'editeur de texte
                    vbox.getChildren().addAll(textArea, hbox);
                    hbox.getChildren().addAll(saveButton, clean);
                    this.getChildren().add(vbox);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            //on ajoute l'item au conteneur de menu
            MenuItem supprimer = new MenuItem("Supprimer");

            supprimer.setOnAction(event -> {
                //si l'utilisateur clique sur supprimer alors on supprime le diagramme ainsi que les fleches qui le relie
                this.getChildren().remove(vueDiagramme);
                vueDiagrammeMap.remove(diagramme);
                modele.getDiagrammes().remove(diagramme);

                modele.getFleches().stream().filter(fleche -> fleche.getSource().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> {
                    getChildren().remove(vueFlecheMap.get(fleche));
                    vueFlecheMap.remove(fleche);
                });

                modele.getFleches().stream().filter(fleche -> fleche.getDestination().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> {
                    getChildren().remove(vueFlecheMap.get(fleche));
                    vueFlecheMap.remove(fleche);
                });

                new ArrayList<>(modele.getFleches()).stream().filter(fleche -> fleche.getSource().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> modele.getFleches().remove(fleche));
                new ArrayList<>(modele.getFleches()).stream().filter(fleche -> fleche.getDestination().getNomClasse().equals(diagramme.getNomClasse())).forEach(fleche -> modele.getFleches().remove(fleche));
            });

            // on ajoute l'item au conteneur de menu
            Menu ajouterRelation = new Menu("Ajouter une relation");

            MenuItem implementation = new MenuItem("Implémentation");
            MenuItem heritage = new MenuItem("Héritage");
            MenuItem association = new MenuItem("Association");

            // permet d'ajouter de nouvelles relations
            implementation.setOnAction(event -> diagrammeData = new DiagrammeData(diagramme, FlecheType.IMPLEMENTATION));
            heritage.setOnAction(event -> diagrammeData = new DiagrammeData(diagramme, FlecheType.HERITAGE));
            association.setOnAction(event -> diagrammeData = new DiagrammeData(diagramme, FlecheType.DEPENDANCE));

            ajouterRelation.getItems().addAll(implementation, heritage, association);

            // on ajoute tout les items au menu
            contextMenu.getItems().add(ajouterRelation);
            contextMenu.getItems().add(supprimer);
            contextMenu.getItems().add(modifier);

            //ajouter un evenement lorsque l'utilisateur fait un clic droit sur la VBox pour la supprimer du Pane et du modèle
            vueDiagramme.setOnContextMenuRequested(event -> {
                contextMenu.show(vueDiagramme, event.getScreenX(), event.getScreenY());
            });


            // ce code permet de déplacer les diagrammes avec beauté
            AtomicReference<Double> deltaX = new AtomicReference<>();
            AtomicReference<Double> deltaY = new AtomicReference<>();

            vueDiagramme.setOnMousePressed(mouseEvent -> {
                deltaX.set(vueDiagramme.getLayoutX() - mouseEvent.getSceneX());
                deltaY.set(vueDiagramme.getLayoutY() - mouseEvent.getSceneY());
                vueDiagramme.setCursor(Cursor.MOVE);
            });

            vueDiagramme.setOnMouseReleased(mouseEvent -> vueDiagramme.setCursor(Cursor.HAND));
            vueDiagramme.setOnMouseEntered(mouseEvent -> vueDiagramme.setCursor(Cursor.HAND));

            vueDiagramme.setOnMouseDragged(event -> {
                if (event.getSceneX() > 325 && event.getSceneX() < getScene().getWidth() && event.getSceneY() > 60 && event.getSceneY() < getScene().getHeight()) {
                    vueDiagramme.setLayoutX(event.getSceneX() + deltaX.get());
                    vueDiagramme.setLayoutY(event.getSceneY() + deltaY.get());
                    modele.notifierObservateur();
                }
            });

            //permet de faire apparaitre le nouveau diagramme a la position de la souris de l'utilisateur 
            vueDiagramme.setLayoutX(modele.getPositionX());
            vueDiagramme.setLayoutY(modele.getPositionY());

            //on ajoute le nouveau diagramme dans l'application et dans le dictionnaire de diagrammes
            this.getChildren().add(vueDiagramme);
            vueDiagrammeMap.put(diagramme, vueDiagramme);
        }

        // permet de crée les fleches
        for (Fleche fleche : modele.getFleches()) {
            VueDiagramme vSrc = vueDiagrammeMap.get(fleche.getSource());
            VueDiagramme vDest = vueDiagrammeMap.get(fleche.getDestination());

            if (!vueFlecheMap.containsKey(fleche)) {

                double sx = vSrc.getLayoutX() + vSrc.getWidth();
                double sy = vSrc.getLayoutY() + vSrc.getHeight();
                double ex = vDest.getLayoutX() + vDest.getWidth();
                double ey = vDest.getLayoutY() + vDest.getHeight();
                VueFleche vueFleche = new VueFleche(fleche, sx, sy, ex, ey, modele);
                getChildren().add(vueFleche);
                vueFlecheMap.put(fleche, vueFleche);
            } else {
                VueFleche vueFleche = vueFlecheMap.get(fleche);
                getChildren().remove(vueFleche);
                vueFlecheMap.remove(fleche);




                // definition de points d encrages et auto placement
                // defintion des points d encrages ( 16 c est pas mal ca fiat utilise des 1/4 de case)
                // utilisation d une array list pour stocker les points d encrages de chaque case
                ArrayList<ArrayList<Integer>> pointsEncrageSrc;
                ArrayList<ArrayList<Integer>> pointsEncrageDest;
                // refonte de la definition des points d encrages en utilisant des boucles for
                // on appelle la methode de la classe diagramme qui renvoie les points d encrages (une array list) en fonction de la position de depart de la case
                pointsEncrageSrc = this.getPointsEncrage(vSrc, vSrc.getLayoutX(), vSrc.getLayoutY());
                pointsEncrageDest = this.getPointsEncrage(vDest, vDest.getLayoutX(), vDest.getLayoutY());

                // on utilise une methode qui va trouver les points les plus proches entre deux listes de points
                ArrayList<Integer> indicePointsFleche = this.getPointsProche(pointsEncrageSrc, pointsEncrageDest);
                int indexSrc = indicePointsFleche.get(0);
                int indexDest = indicePointsFleche.get(1);
                int xSrc = pointsEncrageSrc.get(indexSrc).get(0);
                int ySrc = pointsEncrageSrc.get(indexSrc).get(1);
                int xDest = pointsEncrageDest.get(indexDest).get(0);
                int yDest = pointsEncrageDest.get(indexDest).get(1);


                vueFleche = new VueFleche(fleche, xSrc, ySrc, xDest, yDest, modele);
                getChildren().add(vueFleche);
                vueFlecheMap.put(fleche, vueFleche);
            }

        }

    }


    public void attacherFleches(DiagrammeModele modele) {
        {
            // pour chaque fleche
            for (Fleche fleche : vueFlecheMap.keySet()) {
                VueFleche vueFleche = vueFlecheMap.get(fleche);
                // on recupere les points de depart et d arrivee de la fleche
                double sx = vueFleche.getStartX();
                double sy = vueFleche.getStartY();
                double ex = vueFleche.getEndX();
                double ey = vueFleche.getEndY();
                // on recupere les cases de depart et d arrivee de la fleche
                VueDiagramme vSrc = vueDiagrammeMap.get(fleche.getSource());
                VueDiagramme vDest = vueDiagrammeMap.get(fleche.getDestination());

                // on recupere les points d encrages de chaque case
                ArrayList<ArrayList<Integer>> pointsEncrageSrc = new ArrayList<>();
                ArrayList<ArrayList<Integer>> pointsEncrageDest = new ArrayList<>();
                pointsEncrageSrc = getPointsEncrage(vSrc, vSrc.getLayoutX(), vSrc.getLayoutY());
                pointsEncrageDest = getPointsEncrage(vDest, vDest.getLayoutX(), vDest.getLayoutY());

                // on recupere les points les plus proches entre le point de depart et d arrivee de la fleche et les points d encrages de chaque case
                ArrayList<ArrayList<Integer>> startFleche = new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList((int) sx, (int) sy))));
                ArrayList<ArrayList<Integer>> endFleche = new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList((int) ex, (int) ey))));
                ArrayList<Integer> indicePointsDepFleche = getPointsProche(startFleche, pointsEncrageSrc);
                ArrayList<Integer> indicePointsArrFleche = getPointsProche(endFleche, pointsEncrageDest);
                int indexSrc = indicePointsDepFleche.get(1);
                int indexDest = indicePointsArrFleche.get(1);
                int xSrc = pointsEncrageSrc.get(indexSrc).get(0);
                int ySrc = pointsEncrageSrc.get(indexSrc).get(1);
                int xDest = pointsEncrageDest.get(indexDest).get(0);
                int yDest = pointsEncrageDest.get(indexDest).get(1);
                // suppression de l ancienne fleche
                getChildren().remove(vueFleche);
                // on met a jour les points de depart et d arrivee de la fleche
                vueFleche = new VueFleche(fleche, xSrc, ySrc, xDest, yDest, modele);
                getChildren().add(vueFleche);
                vueFlecheMap.put(fleche, vueFleche);
            }
        }
    }

    private ArrayList<Integer> getPointsProche(ArrayList<ArrayList<Integer>> pointsEncrageSrc, ArrayList<ArrayList<Integer>> pointsEncrageDest) {
        int min = 1000000;
        int minSrc = 0;
        int minDest = 0;
        for (int i = 0; i < pointsEncrageSrc.size(); i++) {
            for (int j = 0; j < pointsEncrageDest.size(); j++) {
                int dist = (int) Math.sqrt(Math.pow(pointsEncrageSrc.get(i).get(0) - pointsEncrageDest.get(j).get(0), 2) + Math.pow(pointsEncrageSrc.get(i).get(1) - pointsEncrageDest.get(j).get(1), 2));
                if (dist < min) {
                    min = dist;
                    minSrc = i;
                    minDest = j;
                }
            }
        }
        return new ArrayList<>(Arrays.asList(minSrc, minDest));

    }

    private ArrayList<ArrayList<Integer>> getPointsEncrage(VueDiagramme vDiag, double layoutX, double layoutY) {
        ArrayList<ArrayList<Integer>> pointsEncrage = new ArrayList<>();
        // on utilise des boucles for pour definir les points d encrages
        double width = vDiag.getWidth();
        double height = vDiag.getHeight();
        // on s occupe
        for (int i = 0; i <= 4; i++) {
            // ajout des
            pointsEncrage.add(new ArrayList<>(Arrays.asList((int) (layoutX + width), (int) (layoutY + height / 4 * i))));
            pointsEncrage.add(new ArrayList<>(Arrays.asList((int) (layoutX), (int) (layoutY + height / 4 * i))));

            // si ligne horizontale
            if (i == 0 || i == 4) {
                for (int j = 1; j < 4; j++) {
                    ArrayList<Integer> pointEncrage = new ArrayList<>();
                    pointEncrage.add((int) (layoutX + width / 4 * j));
                    pointEncrage.add((int) (layoutY + height / 4 * i));
                    pointsEncrage.add(pointEncrage);
                }

            }
        }
        return pointsEncrage;
    }

    /**
     * Permet de changer la couleur de l'interface
     */
    public static void changeMode() {

        String color = Controller.isLightMode ? "black" : "white";
        for (VueDiagramme vBox : vueDiagrammeMap.values()) {

            for (int i = 0; i < vBox.getChildren().size(); i++) {
                if (vBox.getChildren().get(i) instanceof VBox) {
                    for (Node label : ((VBox) vBox.getChildren().get(i)).getChildren()) {
                        label.setStyle("-fx-text-fill: " + color);
                    }
                    vBox.getChildren().get(i).setStyle("-fx-border-color: " + color);
                } else if (vBox.getChildren().get(i) instanceof HBox) {
                    ((HBox) vBox.getChildren().get(i)).getChildren().get(1).setStyle("-fx-text-fill: " + color);
                }
            }
            VBox title = (VBox) vBox.getChildren().get(0);
            HBox hBox = (HBox) title.getChildren().get(1);
            Label label = (Label) hBox.getChildren().get(1);
            label.setStyle("-fx-text-fill: " + color);
        }

        for(VueFleche fleche : vueFlecheMap.values()){
            for (int i = 0; i < fleche.getChildren().size(); i++) {
                if (fleche.getChildren().get(i) instanceof Group) {
                    for (Node label : ((Group) fleche.getChildren().get(i)).getChildren()) {
                        //changer la couleur des strokes des fleches
                        label.setStyle("-fx-stroke: " + color);
                    }
                }
                if ( !(fleche.getChildren().get(i) instanceof TextField)) {
                    fleche.getChildren().get(i).setStyle("-fx-stroke: " + color);
                }else{
                    fleche.getChildren().get(i).setStyle("-fx-text-fill: " + color+"; -fx-background-color: transparent");
                }
            }
        }
    }


}

