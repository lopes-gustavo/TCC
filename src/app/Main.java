package app;

import processing.core.PApplet;
import processing.opengl.PJOGL;
import processing.serial.Serial;
import tests.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;


//TODO: Use app.Config properly (make it more generic)

public class Main extends Applet {
    private static String SERIAL_PORT = "COM1";
    private static int SERIAL_BAUD = 9600;

    public static float FRAME_RATE = 100;
    public static float SPEED_CORRECTION_FACTOR = 30 / FRAME_RATE;

    private ConveyorBelt conveyorBelt;
    private double speed = 1 * SPEED_CORRECTION_FACTOR;
    private Serial myPort;
    private Timer timer;
    private int baud = 60;
    private int digitalInput = Config.getDigitalInput();
    private int analogInput = Config.getAnalogInput();
    protected final ArrayList<Boolean> digitalList = new ArrayList<>(Collections.nCopies(digitalInput, false));
    protected final ArrayList<Integer> analogList = new ArrayList<>(Collections.nCopies(analogInput, 0));

    float rotX, rotY;

    public static void main(String... args) {
        PApplet.main("app.Main");
    }

    @Override
    public void settings() {
        PJOGL.setIcon("assets/icon.png");

        size(1200, 800, P3D);
    }

    @Override
    public void setup() {
        new Test();

        surface.setResizable(true);
        surface.setLocation(2000, 0);
        surface.setTitle("Trabalho de TCC");

        frameRate(FRAME_RATE);

        myPort = new Serial(this, SERIAL_PORT, SERIAL_BAUD);

        startThreadSendData();
        startThreadReceiveData();

        conveyorBelt = new ConveyorBelt("conveyor belt", this);
        conveyorBelt.displayControllers();
    }

    @Override
    public void draw() {
        super.draw();

        background(Color.BACKGROUND);
        smooth();

        debug(true);

        rotateX(rotX);
        rotateY(-rotY);

        center();


        conveyorBelt.init();
    }

    @Override
    public void mousePressed() {
        super.mousePressed();
    }

    private void startThreadSendData() {
        new Thread(() -> {
            timer = new Timer(1000 / baud, actionEvent -> {
                myPort.write('D');
                myPort.write(conveyorBelt.getSensorsActive());
                myPort.write("\r\n");
            });
            timer.start();
        }).start();
    }

    private void startThreadReceiveData() {
        new Thread(() -> new Timer(1000 / baud, actionEvent -> {
            String buffer = myPort.readString();
            if (buffer != null) {
                buffer = buffer.trim().toUpperCase();

                updateReceivedData(buffer);
                updateControllers();
            }
        }).start()).start();
    }

    private void updateReceivedData(String buffer) {
        String digital;
        String analog;

        int indexOfDigital = buffer.indexOf("D");
        int indexOfAnalog = buffer.indexOf("A");

        // Para ao receber valores estranhos
        if (!buffer.matches("^[0-9AD]+$")) {
            System.err.println("Protocolo aceita somente números [0-9] e literais 'A' e 'D'. Recebido: " + buffer);
            return;
        }

        // Atualiza as variáveis locais 'digital' e 'analog' de acordo com o que receber no buffer
        if (indexOfAnalog < 0 || indexOfDigital < 0) {
            System.err.println("Protocolo não cumprido. Protocolo deve seguir o seguinte padrão: 'D'xx...xx'A'xx...xxx'\\r\\n");
            return;
        } else {
            digital = buffer.substring(indexOfDigital + 1, indexOfAnalog);
            analog = buffer.substring(indexOfAnalog + 1);
        }

        if (digital.length() != digitalInput) {
            System.err.println("Número de bytes recebidos para valores digitais diverge do anotado em 'sensors.properties'!"); //TODO: Não usar string hardcoded em 'sensors.properties'
            return;
        }

        if (analog.length() != analogInput * 4) {
            System.err.println("Número de bytes recebidos para valores analógicos diverge do anotado em 'sensors.properties'!"); //TODO: Não usar string hardcoded em 'sensors.properties'
            return;
        }

        // Valores digitais sem ser 0 ou 1
        if (!digital.matches("^[0-1]+$")) {
            System.err.println("Valores digitais devem ser 0 ou 1");
            return;
        }

        // Valores analógicos devem ser de tamanho 4 (decimal), logo múltiplos de 4
        if (!analog.isEmpty() && analog.length() % 4 != 0) {
            System.err.println("Valores analógicos devem ser de tamanho 4 (decimal). [0000 - 9999]");
            return;
        }

        // Caso esteja tudo certo, atualiza as variáveis globais dos sensores
        // Digital
        for (int i = 0; i < digital.length(); i++) {
            boolean isTrue = digital.charAt(i) == '1';
            try {
                digitalList.set(i, isTrue);
            } catch (IndexOutOfBoundsException e) {
                digitalList.add(isTrue);
            }
        }
        // Analógico
        for (int i = 0; i < analog.length(); i+=4) {
            int whichInt = Integer.parseInt(analog.substring(i, i+4));
            try {
                analogList.set(i, whichInt);
            } catch (IndexOutOfBoundsException e) {
                analogList.add(whichInt);
            }
        }
    }

    public void updateControllers() {
        updateDigitalControllers();
        updateAnalogControllers();
    }

    private void updateDigitalControllers() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setActive(digitalList.get(i));
        }
    }

    private void updateAnalogControllers() {
        setSpeed(analogList.get(0)/9999f);
    }

    public void setSpeed(double speed) {
        this.speed = speed * SPEED_CORRECTION_FACTOR;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * Mostra o quadro preto no final da tela para debugging
     */
    public void debug(boolean debug) {
        if (debug) {
            DEBUG = true;

            insideMatrix(() -> {
                noStroke();
                fill(Color.BLACK);
                rect(0, height, width, -100);

                showGrid();
                displayButtons();
                conveyorBelt.displaySensorsTable();
            });

        }
    }

    public void mouseDragged(){
        rotY -= (mouseX - pmouseX) * 0.01;
        rotX -= (mouseY - pmouseY) * 0.01;
    }
}

