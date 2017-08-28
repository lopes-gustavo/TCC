package tests;

import app.Config;
import processing.core.PApplet;
import processing.serial.Serial;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Test extends PApplet {
    private Serial myPort;
    private Timer timer;
    private int baud = 20;
    private int digitalOutput = Config.getDigitalOutput();
    private ArrayList<Boolean> digitalList = new ArrayList<>(Collections.nCopies(digitalOutput, false));
    private int balls = 0;

    public Test() {
        setup();
    }

    @Override
    public void setup() {
        try {
            myPort = new Serial(this, "COM2", 9600);
        } catch (RuntimeException e) {
            System.err.println("A porta está correta?");
            System.exit(1);
        }
        //startThreadSendData();

        startThreadReceiveData();
    }

    private void answer(String answer) {
        myPort.write(answer);
        myPort.write("\r\n");
    }

    private void startThreadReceiveData() {
        new Thread(() -> new Timer(1000 / baud, actionEvent -> {
            String buffer = myPort.readString();

            if (buffer != null) {
                buffer = buffer.trim().toUpperCase();

                countBalls(buffer);
                reply(buffer);
            }
        }).start()).start();
    }

    private boolean isOn = false;
    private boolean hasStopped = false;
    private void countBalls(String buffer) {
        try {
            String sensorDrop = buffer.substring(4);

            println("isOn: " + isOn);
            println("sensorDrop: " + sensorDrop);
            println("balls: " + balls);

            if (sensorDrop.equals("1") && !isOn) {
                balls++;
                isOn = true;
            }

            if (sensorDrop.equals("0") && isOn) {
                isOn = false;

                if (balls >= 4) {
                    balls = 0;
                    answer("D1000A9000");
                }
            }
        }catch (Exception ignored) {}
    }

    private void reply(String buffer) {
        try {
            String digital = buffer.substring(1);

            for (int i = 0; i < digitalOutput; i++) {
                digitalList.set(i, digital.charAt(i) == '1');
            }

/*
            if (digitalList.get(0)) {
                answer("D1001A5000");
            }
*/

            if (digitalList.get(1)) {
                if (!hasStopped) {
                    answer("D0011A9999");
                    hasStopped = true;
                }
            }

            if (digitalList.get(2)) {
                answer("D0010A0000");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
