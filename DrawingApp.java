import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class DrawingApp extends JFrame {
    private DrawingCanvas canvas;

    public DrawingApp() {
        setTitle("Enhanced Java Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new DrawingCanvas();
        add(canvas, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());
        add(createTopPanel(), BorderLayout.NORTH);

        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> canvas.clear());
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveDrawing());
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> loadDrawing());
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(e -> canvas.undo());
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(e -> canvas.redo());
        editMenu.add(undoItem);
        editMenu.add(redoItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        return menuBar;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] tools = {"Pencil", "Line", "Rect", "Oval", "Eraser", "ObjectEraser"};
        for (String tool : tools) {
            JButton toolBtn = new JButton(tool);
            toolBtn.addActionListener(e -> canvas.setTool(tool));
            toolBar.add(toolBtn);
        }

        toolBar.add(new JSeparator(JSeparator.VERTICAL));

        // Line Width Spinner
        toolBar.add(new JLabel("Width: "));
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));
        widthSpinner.addChangeListener(e -> canvas.setStrokeWidth(((Integer)widthSpinner.getValue()).floatValue()));
        toolBar.add(widthSpinner);

        // Eraser Size Spinner
        toolBar.add(new JLabel(" Eraser Size: "));
        JSpinner eraserSpinner = new JSpinner(new SpinnerNumberModel(20, 1, 200, 5));
        eraserSpinner.addChangeListener(e -> canvas.setEraserSize(((Integer)eraserSpinner.getValue()).floatValue()));
        toolBar.add(eraserSpinner);

        toolBar.add(new JSeparator(JSeparator.VERTICAL));

        // Color Picker Button
        JButton colorPickerBtn = new JButton("Pick Color");
        colorPickerBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select Drawing Color", Color.BLACK);
            if (c != null) canvas.setColor(c);
        });
        toolBar.add(colorPickerBtn);

        // Background Image Button
        JButton bgBtn = new JButton("Set BG Image");
        bgBtn.addActionListener(e -> loadBackground());
        toolBar.add(bgBtn);

        // Background Color Button
        JButton bgColorBtn = new JButton("Set BG Color");
        bgColorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select Background Color", Color.WHITE);
            if (c != null) canvas.setBackgroundColor(c);
        });
        toolBar.add(bgColorBtn);

        JButton clearBtn = new JButton("Clear Canvas");
        clearBtn.addActionListener(e -> canvas.clear());
        toolBar.add(clearBtn);

        topPanel.add(toolBar);
        return topPanel;
    }

    private void loadBackground() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage img = ImageIO.read(fileChooser.getSelectedFile());
                if (img != null) {
                    canvas.setBackgroundImage(img);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage());
            }
        }
    }

    private void saveDrawing() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Drawing Files (*.draw)", "draw"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".draw")) {
                file = new File(file.getAbsolutePath() + ".draw");
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(canvas.getShapes());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadDrawing() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Drawing Files (*.draw)", "draw"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<MyShape> shapes = (List<MyShape>) ois.readObject();
                canvas.setShapes(shapes);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error loading: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default if system L&F fails
        }
        SwingUtilities.invokeLater(() -> new DrawingApp().setVisible(true));
    }
}
