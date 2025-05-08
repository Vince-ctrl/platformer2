import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LevelEditor extends JFrame implements MouseListener, MouseMotionListener, ActionListener {
    private List<GameObject> objects = new ArrayList<>();
    private JTextField fileField;
    private JButton saveButton, loadButton, platformButton, spikeButton, goalButton;
    private GameObject selectedObject;

    public LevelEditor() {
        setTitle("Level Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        fileField = new JTextField("level1.csv", 10);
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        platformButton = new JButton("Add Platform");
        spikeButton = new JButton("Add Spike");
        goalButton = new JButton("Set Victory Condition");

        controlPanel.add(new JLabel("Filename:"));
        controlPanel.add(fileField);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(platformButton);
        controlPanel.add(spikeButton);
        controlPanel.add(goalButton);

        add(controlPanel, BorderLayout.NORTH);

        saveButton.addActionListener(this);
        loadButton.addActionListener(e -> loadLevel());
        platformButton.addActionListener(e -> addObject("platform"));
        spikeButton.addActionListener(e -> addObject("spike"));
        goalButton.addActionListener(e -> addObject("goal"));

        addMouseListener(this);
        addMouseMotionListener(this);

        setVisible(true);
    }

    private void addObject(String type) {
        objects.add(new GameObject(type, 100, 100, type.equals("platform") ? 100 : 20, 20));
        repaint();
    }

    private void loadLevel() {
        objects.clear();
        String filename = fileField.getText().trim();
        if (!filename.endsWith(".csv")) {
            JOptionPane.showMessageDialog(this, "Invalid file format. Must be .csv", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                objects.add(new GameObject(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 
                                           parts[0].equals("platform") ? Integer.parseInt(parts[3]) : 20, 
                                           20));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading file", "Error", JOptionPane.ERROR_MESSAGE);
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String filename = fileField.getText().trim();
        if (!filename.endsWith(".csv")) {
            JOptionPane.showMessageDialog(this, "Invalid file format. Must be .csv", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (GameObject obj : objects) {
                writer.println(obj.toCSV());
            }
            JOptionPane.showMessageDialog(this, "Level saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (GameObject obj : objects) {
            obj.draw(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (GameObject obj : objects) {
            if (obj.contains(e.getPoint())) {
                selectedObject = obj;
                return;
            }
        }
        selectedObject = null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        selectedObject = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedObject != null) {
            selectedObject.x = e.getX();
            selectedObject.y = e.getY();
            repaint();
        }
    }
    
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

class GameObject {
    String type;
    int x, y, width, height;

    public GameObject(String type, int x, int y, int width, int height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        switch (type) {
            case "platform" -> g.setColor(Color.GRAY);
            case "spike" -> g.setColor(Color.RED);
            case "goal" -> g.setColor(Color.GREEN);
        }
        g.fillRect(x, y, width, height);
    }

    public boolean contains(Point p) {
        return new Rectangle(x, y, width, height).contains(p);
    }

    public String toCSV() {
        return type + "," + x + "," + y + (type.equals("platform") ? "," + width + "," + height : "");
    }
}