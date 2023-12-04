package fr.charlemagne.dgen.arborescence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent a file tree
 */
public class FileTree extends BaseFile {

    private final List<FileTree> children;

    /**
     * Constructor of File Node
     *
     * @param file File
     */
    public FileTree(File file) {
        super(file);
        this.children = new ArrayList<>();
    }

    /**
     * Get the children of this folder if is it
     *
     * @return A List of the children
     */
    public List<FileTree> getChildren() {
        return this.children;
    }


    /**
     * Create a tree folder with a File input
     *
     * @param folder A File input
     * @return A FileNode that contains all elements of thisfolder
     */
    public static FileTree of(File folder) {

        FileTree fileTree = new FileTree(folder);

        if (folder.isDirectory()) {

            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileTree nextFileTree = FileTree.of(file);
                    fileTree.getChildren().add(nextFileTree);
                }

            } else {
                return fileTree;
            }
        }

        return fileTree;
    }

}

