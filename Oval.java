import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Oval extends MyShape {
    public Oval(Point start, Color color, float strokeWidth) {
        super(start, color, strokeWidth);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth));
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        g2d.drawOval(x, y, width, height);
    }

    @Override
    public boolean contains(Point p) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        return new Ellipse2D.Float(x, y, width, height).contains(p);
    }
}
