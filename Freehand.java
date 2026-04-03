import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Freehand extends MyShape {
    private List<Point> points = new ArrayList<>();

    public Freehand(Point start, Color color, float strokeWidth) {
        super(start, color, strokeWidth);
        points.add(start);
    }

    @Override
    public void setEnd(Point end) {
        points.add(end);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth));
        if (points.size() < 2) return;
        
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    @Override
    public boolean contains(Point p) {
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            if (java.awt.geom.Line2D.ptSegDist(p1.x, p1.y, p2.x, p2.y, p.x, p.y) < strokeWidth + 2) {
                return true;
            }
        }
        return false;
    }
}
