module fr.charlemagne.dgen {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;
    requires itextpdf;

    opens fr.charlemagne.dgen to javafx.fxml;
    exports fr.charlemagne.dgen;

    opens fr.charlemagne.dgen.arborescence to javafx.fxml;
    exports fr.charlemagne.dgen.arborescence;

    opens fr.charlemagne.dgen.controllers to javafx.fxml;
    exports fr.charlemagne.dgen.controllers;

    opens fr.charlemagne.dgen.utils to javafx.fxml;
    exports fr.charlemagne.dgen.utils;
    exports fr.charlemagne.dgen.modeles;
    opens fr.charlemagne.dgen.modeles to javafx.fxml;
}