import java.awt.*;
import java.io.Serializable;

public abstract class MyShape implements Serializable {
    protected Color color;
    protected Point start;
    protected Point end;
    protected float strokeWidth;

    public MyShape(Point start, Color color, float strokeWidth) {
        this.start = start;
        this.end = start;
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public abstract void draw(Graphics2D g2d);

    public abstract boolean contains(Point p);
}
