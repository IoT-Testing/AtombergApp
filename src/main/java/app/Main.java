package app;

import app.util.ReadFromCSV;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        AutomatedTest automationTest = new AutomatedTest();
        automationTest.run();
    }
}
