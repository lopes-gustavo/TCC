public class Main extends Applet {

    private ConveyorBelt conveyorBelt;
    private double speed = 1;

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        surface.setLocation(2000, 0);

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

}

