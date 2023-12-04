package fr.charlemagne.dgen.utils;

/**
 * Enum permettant de mettre les differents type de format
 */
public enum Format {

    PNG("PNG", ".png"),
    JPEG("JPEG", ".jpeg"),
    PDF("PDF", ".pdf");

    private final String nom;
    private final String extension;

    Format(String nom, String extension) {
        this.nom = nom;
        this.extension = extension;
    }

    public String getNom() {
        return nom;
    }

    public String getExtension() {
        return extension;
    }

    public static Format fromExtension(String ext) {
        for (Format format : values()) {
            if (format.getExtension().equalsIgnoreCase(ext)) {
                return format;
            }
        }
        return null;
    }

}
