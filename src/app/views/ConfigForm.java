package app.views;

import app.processing.Main;
import processing.core.PApplet;
import processing.serial.Serial;

import javax.swing.*;

public class ConfigForm {
    private JFrame frame = new JFrame("TCC");

    private JPanel mainPanel;
    private JButton restartButton;
    private JTextField baudTextField;
    private JButton startButton;
    private JComboBox<String> comPortComboBox;

    final Main main = new Main();

    public ConfigForm() {
        startButton.addActionListener(e -> PApplet.runSketch( new String[] { "" }, main));
        restartButton.addActionListener(e -> SwingUtilities.invokeLater(main::finish));
    }

    public void init() {
        frame.setContentPane(new ConfigForm().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        for(String comPort : Serial.list()) {
            System.out.println(comPort);
            comPortComboBox.addItem(comPort);
        }

        frame.pack();
        frame.setVisible(true);
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

}
