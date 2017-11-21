package app;

import app.views.ConfigForm;

import javax.swing.*;

public class MainApp {
    public static void main(String... args) {
        final ConfigForm configForm = new ConfigForm();
        SwingUtilities.invokeLater(configForm::init);
    }
}
