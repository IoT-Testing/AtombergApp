package app;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        AutomatedTest test = new AutomatedTest();
        test.run();
    }
}
