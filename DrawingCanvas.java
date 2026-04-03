import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DrawingCanvas extends JPanel {
    private List<MyShape> shapes = new ArrayList<>();
    private Stack<MyShape> redoStack = new Stack<>();
    private MyShape currentShape = null;
    private String tool = "Pencil"; 
    private Color color = Color.BLACK;
    private float strokeWidth = 2.0f;
    private float eraserSize = 20.0f;
    private BufferedImage backgroundImage = null;

    public DrawingCanvas() {
        setBackground(Color.WHITE);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                
                if (tool.equals("ObjectEraser")) {
                    for (int i = shapes.size() - 1; i >= 0; i--) {
                        if (shapes.get(i).contains(p)) {
                            redoStack.push(shapes.remove(i));
                            repaint();
                            break;
                        }
                    }
                    return;
                }

                if (tool.equals("Select")) return;

                switch (tool) {
                    case "Line":
                        currentShape = new Line(p, color, strokeWidth);
                        break;
                    case "Rect":
                        currentShape = new Rect(p, color, strokeWidth);
                        break;
                    case "Oval":
                        currentShape = new Oval(p, color, strokeWidth);
                        break;
                    case "Pencil":
                        currentShape = new Freehand(p, color, strokeWidth);
                        break;
                    case "Eraser":
                        currentShape = new Freehand(p, getBackground(), eraserSize);
                        break;
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentShape != null) {
                    currentShape.setEnd(e.getPoint());
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentShape != null) {
                    shapes.add(currentShape);
                    redoStack.clear(); 
                    currentShape = null;
                    repaint();
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
    }

    public void setEraserSize(float size) {
        this.eraserSize = size;
    }

    public void setBackgroundImage(BufferedImage img) {
        this.backgroundImage = img;
        repaint();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundImage = null;
        setBackground(color);
        repaint();
    }

    public void undo() {
        if (!shapes.isEmpty()) {
            redoStack.push(shapes.remove(shapes.size() - 1));
            repaint();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            shapes.add(redoStack.pop());
            repaint();
        }
    }

    public void clear() {
        shapes.clear();
        redoStack.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        
        for (MyShape shape : shapes) {
            shape.draw(g2d);
        }
        
        if (currentShape != null) {
            currentShape.draw(g2d);
        }
    }

    public List<MyShape> getShapes() {
        return shapes;
    }

    public void setShapes(List<MyShape> shapes) {
        this.shapes = shapes;
        this.redoStack.clear();
        repaint();
    }
}
