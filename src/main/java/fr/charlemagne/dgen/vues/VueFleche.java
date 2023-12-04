package fr.charlemagne.dgen.vues;

import fr.charlemagne.dgen.controllers.Controller;
import fr.charlemagne.dgen.diagrammes.Attribut;
import fr.charlemagne.dgen.diagrammes.Diagramme;
import fr.charlemagne.dgen.fleches.Fleche;
import fr.charlemagne.dgen.modeles.DiagrammeModele;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.Optional;

/**
 * Classe permettant d'afficher une fleche
 */
public class VueFleche extends Group {


    private Fleche fleche;
    private double sx, sy, ex, ey;
    private DiagrammeModele modele;

    /**
     * Constructeur de la fleche
     * @param fleche donnees de la fleche
     * @param sx position x du depart de la fleche
     * @param sy position y du depart de la fleche
     * @param ex position x de l'arriver de la fleche
     * @param ey position y de l'arriver de la fleche
     */
    public VueFleche(Fleche fleche, double sx, double sy, double ex, double ey, DiagrammeModele modele) {
        this.fleche = fleche;
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
        this.modele = modele;
        //creer la fleche
        creerFleche();

    }

    /**
     * Methode qui creer la fleche
     */
    public void creerFleche() {
        // si la fleche existe deja, on la supprime
        for (int i = 0; i < this.getChildren().size(); i++) {
            this.getChildren().remove(i);
        }
        // " corps de la fleche"
        Line line1 = new Line(sx, sy, ex, ey);
        // taille de la fleche
        int size = 10;
        //fleche
        Polygon poly = new Polygon(ex, ey, ex - size, ey + size, ex - size, ey - size);
        // positionnement de la tete de la fleche dans l espace
        double angle = Math.atan2(ex - sx, ey - sy);
        poly.setRotate(90 - Math.toDegrees(angle));

        poly.setStrokeWidth(1);

        Polyline poli = new Polyline(ex - size, ey + size, ex, ey, ex - size, ey - size);

        poli.setRotate(90 - Math.toDegrees(angle));
        if (Controller.isLightMode) {
            line1.setStroke(Color.BLACK);
            poli.setStroke(Color.BLACK);
            poly.setStroke(Color.BLACK);
        } else {
            line1.setStroke(Color.WHITE);
            poli.setStroke(Color.WHITE);
            poly.setStroke(Color.WHITE);
        }
        poli.setStrokeWidth(1);




        switch (fleche.getFlecheType()) {
            case IMPLEMENTATION:
                poly.setFill(Color.TRANSPARENT);
                line1.getStrokeDashArray().addAll(5d, 5d);
                getChildren().addAll(line1, poly);
                break;

            case HERITAGE:
                poly.setFill(Color.TRANSPARENT);
                getChildren().addAll(line1, poly);
                break;

            case DEPENDANCE:
                //prendre le diagramme source
                Diagramme diagrammeSrc = fleche.getSource();
                //prendre le diagramme destination
                Diagramme diagrammeDest = fleche.getDestination();
                // verifie si l'attribut existe ou pas
                Optional<Attribut> attribut = diagrammeSrc.getAttributs().stream().filter(attribut1 -> attribut1.getType().equals(diagrammeDest.getNomClasse())).findFirst();

                Line linetete1 = new Line();
                Line linetete2 = new Line();
                //faire la tete de la fleche avec 2 lignes
                linetete1.setStartX(ex);
                linetete1.setStartY(ey);

                linetete1.setEndX(ex - size);
                linetete1.setEndY(ey + size);

                linetete2.setStartX(ex);
                linetete2.setStartY(ey);

                linetete2.setEndX(ex - size);
                linetete2.setEndY(ey - size);

                if (Controller.isLightMode) {
                    linetete1.setStroke(Color.BLACK);
                    linetete2.setStroke(Color.BLACK);
                } else {
                    linetete1.setStroke(Color.WHITE);
                    linetete2.setStroke(Color.WHITE);
                }

                Group temp = new Group();
                temp.getChildren().addAll(linetete1, linetete2);
                temp.setRotate(90 - Math.toDegrees(angle));
                TextField textField= new TextField();

                //verifie si le nom de la fleche est vide
                if (!fleche.getNom().equals("")){
                    textField.setText(fleche.getNom());
                }else{
                    textField.setText("New attribut");
                    attribut.ifPresent(attribut1 ->{
                        textField.setText(attribut1.getVisibilite()+ " " + attribut1.getNom());
                        fleche.setNom(textField.getText());
                    });
                }

                textField.setEditable(true);
                textField.setLayoutX((sx+ex)/2);
                textField.setLayoutY((sy+ey)/2);
                textField.setStyle("-fx-background-color: transparent;");


                //Evenement lorsque l'utilisateur appuie sur la touche "entrer"
                textField.setOnKeyPressed(event->{
                    if(event.getCode().toString().equals("ENTER")){
                        fleche.setNom(textField.getText());
                    }
                });
                getChildren().addAll(line1, temp, textField);
                break;

            default:
                getChildren().addAll(line1, poli);
                break;

        }

        ContextMenu contextMenu = new ContextMenu();
        MenuItem item = new MenuItem("Supprimer");
        contextMenu.getItems().add(item);

        item.setOnAction(event -> {
            Pane pane = (Pane) getParent();
            pane.getChildren().remove(this);
            VueDessin.vueFlecheMap.remove(fleche);
            modele.getFleches().remove(fleche);
        });


        setOnContextMenuRequested(event -> {
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        });

        // deplacement de la fleche quand onclique dessus, quand on relache la souris elle se remet sur le point d encrage le plus proche

        setOnMouseDragOver(event -> {
            setCursor(Cursor.CLOSED_HAND);
        });
        setOnMouseDragged(event -> {
            // le curseur se transforme en main fermee
            setCursor(Cursor.CLOSED_HAND);
            double x = event.getX();
            double y = event.getY();
            // si shift est presse on bouge le point de depart
            if ( event.isShiftDown()) {
                setStartX(x);
                setStartY(y);
            } else {
                setEndX(x);
                setEndY(y);
            }
            // on deplace la fleche aux nouvelles coordonnees
            // pour cela on recupere les coordonnees du point de depart et du point d arrivee
            // on les envoie a la methode creerFleche
            creerFleche();
        });
    }



    public void setStartX(double sx) {
        this.sx = sx;
    }

    public void setStartY(double sy) {
        this.sy = sy;
    }

    public void setEndX(double ex) {
        this.ex = ex;
    }

    public void setEndY(double ey) {
        this.ey = ey;
    }


    public double getStartX() {
        return this.sx;
    }

    public double getStartY() {
        return this.sy;
    }

    public double getEndX() {
        return this.ex;
    }

    public double getEndY() {
        return this.ey;
    }
}
