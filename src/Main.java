import processing.serial.Serial;

import javax.swing.*;


public class Main extends Applet {

    private ConveyorBelt conveyorBelt;
    private double speed = 1;
    private Serial myPort;
    private Timer timer;
    private int baud = 20;

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        surface.setLocation(2500, 0);
        frameRate(30);

        myPort = new Serial(this, "COM1", 9600);
        sendData();

        conveyorBelt = new ConveyorBelt("conveyor belt", this);
        conveyorBelt.displayControllers();
    }

    @Override
    public void draw() {
        super.draw();

        background(Color.BACKGROUND);
        smooth();
        showGrid();
        showBlackDebugBox();
        center();


        displayButtons();

        conveyorBelt.display();
        conveyorBelt.turnSensorsOn();

        conveyorBelt.moveObject(speed);

        conveyorBelt.displaySensorsTable();
    }


    @Override
    public void mousePressed() {
        super.mousePressed();
    }

    @SuppressWarnings("unused")
    public void sendData() {
        new Thread(() -> {
            timer = new Timer(1000 / baud, actionEvent -> {
                myPort.write('D');
                myPort.write(conveyorBelt.getSensorsActive());
                myPort.write("\r\n");
            });
            timer.start();
        }).start();
    }
}

