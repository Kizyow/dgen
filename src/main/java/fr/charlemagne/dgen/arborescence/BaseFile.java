package fr.charlemagne.dgen.arborescence;


import java.io.File;

public class BaseFile {

    protected final File file;

    public BaseFile(File file) {
        this.file = file;
    }

    /**
     * Get the File object
     *
     * @return A File
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Get the name of the File
     *
     * @return String of the name
     */
    public String getFileName() {
        return this.file.getName();
    }

    /**
     * Get the path of the File
     *
     * @return String of the path
     */
    public String getPath() {
        return this.file.getAbsolutePath();
    }

    /**
     * Return the String of a tree folder
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.getFileName();
    }

}

