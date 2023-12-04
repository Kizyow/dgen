package fr.charlemagne.dgen;

import fr.charlemagne.dgen.diagrammes.Classe;
import fr.charlemagne.dgen.modeles.DiagrammeModele;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DiagrammeModeleTest {
    private DiagrammeModele modele;
    private Classe classe;
    private Classe secondClasse;


    /**
     * Initilisation des données utilisés lors des tests
     */
    @BeforeEach
    public void dataSetUp() {
        this.modele = new DiagrammeModele();
        this.classe = new Classe(Integer.class);
        this.secondClasse = new Classe(Number.class);
    }


    /**
     * Test de l'ajout du diagramme
     * Le chemin du fichier est a modifier en fonction de l'appareil sur lequel le test est lancé
     */
    @Test
    public void ajouterDiagrammeTest() {
        File file = new File(this.getClass().getResource("/pkge/").getPath());
        this.modele.defineIntrospection(file);
        this.modele.ajouterDiagramme(this.getClass().getResource("/pkge/A.class").getPath());

        assertEquals(1, modele.getDiagrammes().size(), "Le diagramme n'a pas été ajouté");
    }


    /**
     * Test de la création de flèches
     */
    @Test
    public void creerFlecheTest() {
        this.modele.getDiagrammes().add(classe);
        this.modele.getDiagrammes().add(secondClasse);

        this.modele.creerFleche(classe);

        assertEquals(1, modele.getFleches().size(), "La liste de fleches ne doit pas etre vide");
    }


    /**
     * Test de la correspondance des noms des diagrammes si déjà ajoutés
     */
    @Test
    public void inDiagrammesTest() {
        this.modele.inDiagrammes(classe);

        assertEquals("Integer", classe.getNomClasse(), "Le diagramme n'a pas le bon nom");
    }
}