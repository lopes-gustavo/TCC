import static processing.core.PConstants.CENTER;

public class Button {
    private final int x, y, w, h;
    private final Applet context;
    private OnClickListener listener;
    private String text = null;

    Button(Applet context, int xx, int yy, int ww, int hh, OnClickListener listener) {
        this.context = context;
        this.x = xx;
        this.y = yy;
        this.w = ww;
        this.h = hh;
        this.listener = listener;
    }

    public void display() {
        context.fill(Color.WHITE);
        context.stroke(Color.YELLOW);
        context.rect(x - context.width / 2, y - context.height / 2, w, h);

        if (text != null) {
            context.textSize(15);
            context.textAlign(CENTER, CENTER);
            context.fill(Color.BLACK);
            context.text(text, x + w/2 - context.width / 2, y - context.height / 2 + h/2);
        }
    }

    public void callListener() {
        listener.onClickListener();
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isMouseInside() {
        return context.mouseX > x & context.mouseX < x + w & context.mouseY > y & context.mouseY < y + h;
    }

    public interface OnClickListener {
        void onClickListener();
    }
}
