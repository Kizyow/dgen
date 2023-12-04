package fr.charlemagne.dgen;

import fr.charlemagne.dgen.diagrammes.Diagramme;
import fr.charlemagne.dgen.modeles.Introspection;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class IntrospectionTest {

    /**
     * Test de l'introspection
     * On vérifie que la nom de la classe correspond bien à la classe passée en paramètre
     */
    @Test
    public void inspectTest() {
        File file = new File(this.getClass().getResource("/pkge/").getPath());
        Introspection introspection = new Introspection(file);
        Diagramme d = introspection.inspectClass(Integer.class);

        assertEquals("Integer", d.getNomClasse(), "Le diagramme n'a pas été ajouté");
    }


    /**
     * Test pour charger une classe
     * Le chemin du fichier est a modifier en fonction de l'appareil sur lequel le test est lancé
     */
    @Test
    public void loadClassTest(){
        File file = new File(this.getClass().getResource("/pkge/").getPath());
        Introspection introspection = new Introspection(file);

        File f = new File(this.getClass().getResource("/pkge/A.class").getPath());

        Class<?> loadedClass = introspection.loadClass(f);

        assertNotNull(loadedClass, "Le diagramme n'a pas été chargé");
    }

}