import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Player player;
    private List<Platform> platforms;
    private List<Spike> spikes;
    private Rectangle goal;
    private Timer timer;
    private String levelFile;

    public GamePanel(String levelFile) {
        this.levelFile = levelFile; // Store selected level file
        player = new Player(100, 500);
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        goal = new Rectangle(700, 500, 50, 50);

        loadLevel(); // Load the selected level

        timer = new Timer(16, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    public void loadLevel() {
        platforms.clear();
        spikes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(levelFile))) { // Load the selected file
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                switch (parts[0]) {
                    case "platform":
                        platforms.add(new Platform(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
                        break;
                    case "spike":
                        spikes.add(new Spike(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                        break;
                    case "goal":
                        goal = new Rectangle(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 50, 50);
                        break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading level file: " + levelFile, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (Platform p : platforms) p.draw(g);
        for (Spike s : spikes) s.draw(g);
        g.setColor(Color.GREEN);
        g.fillRect(goal.x, goal.y, goal.width, goal.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update(platforms, spikes, goal);
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.jump();
            case KeyEvent.VK_A -> player.moveLeft();
            case KeyEvent.VK_D -> player.moveRight();
            case KeyEvent.VK_R -> player.respawn();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
        case KeyEvent.VK_A, KeyEvent.VK_D -> player.stopMoving();
    }
}
    @Override public void keyTyped(KeyEvent e) {}
}