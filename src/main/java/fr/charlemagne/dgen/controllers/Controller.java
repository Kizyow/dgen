package fr.charlemagne.dgen.controllers;

import fr.charlemagne.dgen.arborescence.FileTree;
import fr.charlemagne.dgen.modeles.DiagrammeModele;
import fr.charlemagne.dgen.utils.Format;
import fr.charlemagne.dgen.vues.VueDessin;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Controller {

    @FXML
    private BorderPane parent;

    @FXML
    private Text title;

    @FXML
    private Button btnMode;
    @FXML
    private Button exportButton;

    @FXML
    private ImageView imgMode;

    @FXML
    private Button openButton;
    @FXML
    private TreeView<FileTree> treeView;

    public static boolean isLightMode = true;
    private DiagrammeModele diagrammeModele;

    public void initData(DiagrammeModele diagrammeModele) {
        this.diagrammeModele = diagrammeModele;
    }

    /**
     * Permet d'exporter un diagramme sous les formats disponible (JPG, PDF, PNG)
     */
    public void export() {

        // get the diagram class
        Pane pane = (Pane) parent.getCenter();

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        // Pane is the JavaFX pane that you want to export
        WritableImage writableImage = pane.snapshot(params, null);

        // Show a file chooser to select where to save the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder votre diagramme");

        for (Format format : Format.values()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(format.getNom(), format.getExtension()));
        }

        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());

        if (file != null) {

            // Save the image to the selected file
            Format format = Format.fromExtension(fileChooser.getSelectedExtensionFilter().getExtensions().get(0));

            // si le format n'existe pas, on n'exporte pas
            if (format == null) {
                return;
            }

            String formatName = format.getExtension().substring(1);

            try {

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

                if (format == Format.PNG) {
                    ImageIO.write(bufferedImage, formatName, file);
                } else if (format == Format.PDF) {

                    Document document = new Document(PageSize.A4, 20, 20, 20, 20);
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();
                    Image image = Image.getInstance(bufferedImage, null);
                    document.add(image);
                    document.close();

                } else if (format == Format.JPEG) {
                    BufferedImage newIm = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                    newIm.createGraphics().drawImage(bufferedImage, 0, 0, java.awt.Color.WHITE, null);
                    ImageIO.write(newIm, formatName, file);
                }

            } catch (IOException | DocumentException e) {
                System.err.println("Erreur lors de la sauvegarde du diagramme");
                throw new RuntimeException(e);
            }

        }

    }

    /**
     * Permet de changer la couleur du thème de l'interface (Blanc/Noir)
     * @param event
     */
    @FXML
    public void changeMode(Event event) {
        Button button = (Button) event.getSource();
        if (button.getId().equals("btnMode")) {
            StyleController styleController = new StyleController(parent, title, treeView, imgMode);
            isLightMode = !isLightMode;
            if (isLightMode) {
                styleController.setLightMode();
            } else {
                styleController.setDarkMode();
            }
        }
        VueDessin.changeMode();
    }

    /**
     * Permet de sélectionner un dossier sur lequel travailler.
     * @param event
     */
    @FXML
    public void chooseFolder(ActionEvent event) {
        Button button = (Button) event.getSource();
        if (button.getId().equals("openButton")) {
            MenuController menuController = new MenuController(diagrammeModele, treeView);
            menuController.chooseFolderPath();
        }
    }

    /**
     * Lorsqu'un utilisateur tente de drag un élement de l'arborescence, alors cette méthode est appelé
     *
     * @param event
     */
    @FXML
    private void dragDetected(MouseEvent event) {

        //recuperer l'item selectionné
        TreeItem<FileTree> item = treeView.getSelectionModel().getSelectedItem();

        Dragboard dragboard = treeView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(item.getValue().getPath());
        dragboard.setContent(content);

        event.consume();

    }


    /**
     * Methode qui permet d'actualiser le positionnement des fleches
     * @param event touche appuyer par l'utilisateur
     */
    @FXML
    private void keyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            VueDessin vueDessin = (VueDessin) parent.getCenter();
            vueDessin.attacherFleches(diagrammeModele);
        }
    }
}