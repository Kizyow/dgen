package fr.charlemagne.dgen.vues;

import fr.charlemagne.dgen.controllers.Controller;
import fr.charlemagne.dgen.diagrammes.Diagramme;
import fr.charlemagne.dgen.modeles.DiagrammeModele;
import fr.charlemagne.dgen.utils.FlecheType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class VueDiagramme extends VBox {

    private Diagramme diagramme;
    private DiagrammeModele modele;

    public VueDiagramme(Diagramme diagramme, DiagrammeModele modele) {
        this.setAlignment(Pos.CENTER);
        this.diagramme = diagramme;
        this.modele = modele;
        creer();
    }


    /**
     * Méthode qui permet de créer un diagramme
     */
    public void creer() {

        VBox titre = new VBox();
        VBox attributs = new VBox();
        VBox methodes = new VBox();



        HBox titreImages = new HBox();

        ImageView image = new ImageView(diagramme.getLogoType());
        image.setFitHeight(20);
        image.setFitWidth(20);
        image.setSmooth(true);
        image.setPreserveRatio(true);

        Label titreLabel = new Label(diagramme.getNomClasse());
        Label nomPackage = new Label(diagramme.getNomPackage());
        Label nomType = new Label("<<" + diagramme.getType() + ">>");

        titreImages.setSpacing(5);

        titreImages.getChildren().addAll(image, titreLabel);
        titreImages.setAlignment(Pos.CENTER);

        titre.getChildren().addAll(nomType, titreImages, nomPackage);

        titre.setAlignment(Pos.CENTER);

        for (int i = 0; i < diagramme.getAttributs().size(); i++) {
            if (diagramme.getAttributs().get(i).isPrimitive()) {
                Label attribut = new Label(diagramme.getAttributs().get(i).toString());
                attributs.getChildren().add(attribut);
            }
        }

        for (int i = 0; i < diagramme.getMethodes().size(); i++) {
            Label methode = new Label(" "+diagramme.getMethodes().get(i).toString());
            methodes.getChildren().add(methode);
        }

        //titre.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FAAF03"), null, null)));
        titre.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        //attributs.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FAAF03"), null, null)));
        attributs.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        //methodes.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FAAF03"), null, null)));
        methodes.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, null, new BorderWidths(1))));


        getChildren().addAll(titre, attributs, methodes);

//        attributs.setPadding(new Insets(0, 5, 0, 5));
//        methodes.setPadding(new Insets(0, 5, 0, 5));
//        setPadding(new Insets(0, 5, 0, 5));

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && VueDessin.diagrammeData != null) {
                Diagramme diagrammeDest = diagramme;
                Diagramme diagrammeSource = VueDessin.diagrammeData.getDiagramme();
                FlecheType type = VueDessin.diagrammeData.getFlecheType();

                if (diagrammeDest != null) {
                    relierDiagramme(diagrammeSource, diagrammeDest, type);
                    VueDessin.diagrammeData = null;
                    modele.notifierObservateur();
                }
            }
        });

        VueDessin.changeMode();

    }

    public void relierDiagramme(Diagramme src, Diagramme dest, FlecheType type) {
        modele.ajouterFleche(src, dest, type);
    }
}
