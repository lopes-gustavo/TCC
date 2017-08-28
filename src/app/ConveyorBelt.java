package app;

import processing.core.PConstants;

import javax.swing.*;
import java.util.ArrayList;

public class ConveyorBelt extends Shape {

    private static final int size = 500;
    private static final int footHeight = 100;

    float[] dropDownParams = getDropDownParams();

    private ArrayList<Sensor> sensors = new ArrayList<>();

    private MovingObject object;
    private boolean dropSelected = false;

    /**
     * Construtor
     * @param name Nome do Shape
     * @param context Contexto da aplicação. {@link Applet} que costuma ser a classe {@link Main}
     */
    public ConveyorBelt (String name, Applet context) {
        super.context = context;
        setName(name);

        addBase();
        addFeet(0.1, 0.9);
        addMast();
        addDropDown();
        addBaseSensors(0.25, 0.5, 0.9);
        addDropDownSensor();
        addMovingObject();
        reposition();

        //createDrop();
        drop();
    }

    /**
     * Adiciona a caixa que irá mover
     */
    private void addMovingObject() {
        object = new MovingObject("moving object", context, size);
    }

    /**
     * Adiciona a base da esteira
     */
    private void addBase() {
        Shape base = context.createShape(PConstants.LINE, 0, 0, size, 0);
        base.setStroke(Color.WHITE);
        addChild("base", base);
    }

    /**
     * TODO: Adicionar validação para valores menores que 1
     * Adiciona os pés da esteira
     * @param feetPosition As posições dos pés, em porcentagem
     */
    private void addFeet(double... feetPosition) {
        for (double footPosition : feetPosition) {
            float position = (float) footPosition * size;
            Shape feet = context.createShape(PConstants.LINE, position, footHeight, position, 0);
            feet.setStroke(Color.WHITE);
            addChild("feet " + footPosition, feet);
        }
    }

    /**
     * Adiciona o poste que contém o dropDown
     */
    private void addMast() {
        float mastPosition = 0.5f * size;
        float mastHeight = 200;
        Shape mast = context.createShape(PConstants.LINE, mastPosition, footHeight, mastPosition, -mastHeight);
        mast.setStroke(Color.WHITE);
        addChild("mast", mast);
    }

    /**
     * TODO: Fazer verificação para que os valores sejam menores que 1
     * Adiciona os sensores da base
     * @param sensorPositions A posição dos sensores, em porcentagem
     */
    private void addBaseSensors(double... sensorPositions) {
        for (double sensorPosition : sensorPositions) {
            Sensor sensor = new Sensor(context.createShape(PConstants.ELLIPSE, (float) sensorPosition * size, 0, 5, 5), Sensor.VERTICAL);
            this.sensors.add(sensor);
            addChild("sensor " + String.valueOf(sensorPosition), sensor);
        }
    }

    /**
     * Adiciona o sensor do dropdown
     */
    private void addDropDownSensor() {
        Sensor sensor = new Sensor(context.createShape(PConstants.ELLIPSE, size/2, -70, 5, 5), Sensor.HORIZONTAL);
        this.sensors.add(sensor);
        addChild("sensor " + size/2, sensor);
    }

    /**
     * Adiciona o dropdown
     */
    private void addDropDown() {
        Shape dropDown = context.createShape(PConstants.RECT, dropDownParams);
        addChild("dropDown", dropDown);
    }

    /**
     * Método helper para salvar e manter "genérico" os parâmetros do dropdown
     * @return
     */
    private float[] getDropDownParams() {
        float dropDownWidth = 50;
        float dropDownHeight = 80;
        float dropDownStartPosition = size/2 - dropDownWidth;

        return new float[]{dropDownStartPosition, -80.0f, dropDownWidth, -dropDownHeight};
    }

    /**
     * Atualiza a posição da esteira na tela
     */
    private void reposition() {
        translate(-size/2, 0);
    }

    /**
     * Movimenta a caixa
     * @param dx A "velocidade" do objeto. Costuma ser entre [-1, 1], mas nada impede de ser maior que 1
     */
    public void moveObject(double dx) {
        object.move(dx);
    }

    /**
     * @return Retorna o tamanho da esteira
     */
    @Override
    public float getWidth() {
        return size;
    }

    /**
     * Mostra a esteira na tela
     */
    public void display() {
        context.shape(this);
        context.shape(object);
    }

    /**
     * Inicializa os sensores
     */
    public void turnSensorsOn() {
        new Thread(() -> {
            for (Sensor sensor : sensors) {

                boolean isActive = false;
                switch (sensor.getType()) {
                    case Sensor.HORIZONTAL:
                        Shape drop = getChild("drop");
                        isActive = drop != null && Math.abs(sensor.getVerticalPosition() - drop.getY()) < drop.getHeight();
                        break;
                    case Sensor.VERTICAL:
                        isActive = (sensor.getHorizontalPosition() - object.getPosX()) > 0 && (sensor.getHorizontalPosition() - object.getPosX()) < object.getSizeX();
                        break;
                }

                if (isActive) {
                    sensor.setActive(true);
                    sensor.setStroke(Color.YELLOW);
                } else {
                    sensor.setActive(false);
                    sensor.setStroke(Color.RED);
                }
            }
        }).start();
    }

    /**
     * Mostra os valores dos sensores na tela
     */
    public void displaySensorsTable() {
        if (Applet.DEBUG) {
            context.insideMatrix(() -> {
                context.center();
                for (int i = 0; i < sensors.size(); i++) {
                    context.writeInPage("S" + i, 30 + 40 * i, 10);
                    context.writeInPage(sensors.get(i).isActive() ? 1 : 0, 30 + 40 * i, 40);
                }
            });
        }
    }

    /**
     * Inicializa e mostra os botões dos controladores na tela
     */
    public void displayControllers() {
        int spacing = 60;
        Button forw = new Button(context, 200 + spacing*0, context.height - 70, 40, 40, button -> object.setDirection(MovingObject.Direction.POSITIVE));
        Button back = new Button(context, 200 + spacing*1, context.height - 70, 40, 40, button -> object.setDirection(MovingObject.Direction.NEGATIVE));
        Button stop = new Button(context, 200 + spacing*2, context.height - 70, 40, 40, button -> object.setDirection(MovingObject.Direction.STOP));
        //Button drop = new Button(context, 200 + spacing*3, context.height - 70, 40, 40, button -> dropSelected = button.isSelected());
        Button drop = new Button(context, 200 + spacing*3, context.height - 70, 40, 40, button -> {
            isDrop = !isDrop;
            //button.setActive();
        });

        forw.setText(">>");
        back.setText("<<");
        stop.setText("||");
        drop.setText("\\/");

        context.addButton(forw);
        context.addButton(back);
        context.addButton(stop);
        context.addButton(drop);
    }

    /**
     * @return Uma {@link String} com os valores dos sensores. 1 para ativo, 0 para inativo
     */
    public String getSensorsActive() {
        StringBuilder out = new StringBuilder();
        for (Sensor sensor : sensors) {
            out.append(sensor.isActive() ? 1 : 0);
        }
        return out.toString();
    }

    /**
     * Cria a bola que cai do dropdown
     */
    float speed = 0;
    float t = 0;
    private void createDrop() {
        float width = dropDownParams[2] / 2;
        float x = dropDownParams[0] + width;

        Shape drop = context.createShape(PConstants.ELLIPSE, x, dropDownParams[1], width, width);

        speed = 0;
        t = 0;
        addChild("drop", drop, true);

        reorderChildren(drop, getChild("dropDown"));
    }

    /**
     * Move a bola que cai do dropdown
     */
    public void moveDrop () {
        try {
            Shape drop = getChild("drop");

            if (drop.getY() + drop.getHeight() + speed > 0) {
                removeChild(drop.getName());
            }

            // Simula a gravidade
            speed += 10 * t;
            t += 1 / 30f * Main.SPEED_CORRECTION_FACTOR;

            drop.translate(0, speed);


        } catch (Exception ignored) {}
    }

    /**
     * Faz a bola cair em intervalos constantes.
     */
    Timer timer;
    boolean isDrop = false;
    public void drop() {
        new Thread(() -> {
            timer = new Timer(1000, actionEvent -> {
                if (context.buttons.get(3).isActive()) {
                    createDrop();
                    moveDrop();
                }
            });
            timer.start();
        }).start();

    }

    /**
     * Inicializa a classe
     */
    public void init() {
        display();
        turnSensorsOn();
        moveObject(((Main) context).getSpeed());
        moveDrop();
    }


    /**
     * Classe helper para criar os sensores
     */
    private static class Sensor extends Shape {
        public static final int VERTICAL = 0;
        public static final int HORIZONTAL = 1;

        private float horizontalPosition;
        private float verticalPosition;
        private boolean active;
        private int type;

        Sensor(Shape shape, int type) {
            super(shape.context, shape.context.g, shape.getKind(), shape.getParams());
            this.horizontalPosition = shape.getParam(0);
            this.verticalPosition = shape.getParam(1);
            this.active = false;
            this.type = type;

            setStroke(Color.RED);
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public float getHorizontalPosition() {
            return horizontalPosition;
        }

        public float getVerticalPosition() {
            return verticalPosition;
        }

        public int getType() {
            return type;
        }
    }
}
