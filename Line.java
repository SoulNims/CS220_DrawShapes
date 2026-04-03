import java.awt.*;

public class Line extends MyShape {
    public Line(Point start, Color color, float strokeWidth) {
        super(start, color, strokeWidth);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }

    @Override
    public boolean contains(Point p) {
        return java.awt.geom.Line2D.ptSegDist(start.x, start.y, end.x, end.y, p.x, p.y) < strokeWidth + 2;
    }
}
