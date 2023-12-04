package fr.charlemagne.dgen.controllers;

import fr.charlemagne.dgen.modeles.DiagrammeModele;
import fr.charlemagne.dgen.arborescence.FileTree;
import fr.charlemagne.dgen.utils.Resource;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class MenuController {

    private DiagrammeModele diagrammeModele;
    private TreeView<FileTree> treeView;
    private Image folderIcon, fileIcon;

    public MenuController(DiagrammeModele diagrammeModele, TreeView<FileTree> treeView) {
        this.diagrammeModele = diagrammeModele;
        this.treeView = treeView;
        this.folderIcon = new Image(Resource.FOLDER_PNG.toPath());
        this.fileIcon = new Image(Resource.FILE_PNG.toPath());
    }

    /**
     * Demande a l'utilisateur le dossier sur lequel les diagrammes doivent etre pris
     */
    public void chooseFolderPath() {
        DirectoryChooser dc = new DirectoryChooser();
        Window window = treeView.getScene().getWindow();
        File file = dc.showDialog(window);

        if (file != null) {
            diagrammeModele.defineIntrospection(file);
            TreeItem<FileTree> root = this.createTreeViewFolder(file);
            root.setExpanded(true);
            treeView.setRoot(root);
        }
        
    }

    /**
     * Permet de créer l'arborescence du dossier
     * @param folder Le dossier racine
     * @return Un TreeItem
     */
    public TreeItem<FileTree> createTreeViewFolder(File folder) {
        FileTree fileTree = FileTree.of(folder);
        TreeItem<FileTree> treeItem = new TreeItem<>(fileTree);
        ImageView temp;

        for (FileTree child : fileTree.getChildren()) {
            File nextChild = child.getFile();
            TreeItem<FileTree> nextTreeItem = this.createTreeViewFolder(nextChild);
            treeItem.getChildren().add(nextTreeItem);
        }

        if (treeItem.getValue().getFile().isDirectory()) {
            temp = new ImageView(folderIcon);
        } else {
            temp = new ImageView(fileIcon);
        }

        temp.setFitHeight(20);
        temp.setFitWidth(20);
        treeItem.setGraphic(temp);

        return treeItem;

    }

}
