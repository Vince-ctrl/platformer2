import javax.swing.*;
import java.io.File;
import java.util.Arrays;

public class PlatformerGame extends JFrame {
    private GamePanel gamePanel;
    private SoundManager soundManager;

    public PlatformerGame(String levelFile) {
        setTitle("Platformer Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("src/icon.png");
        setIconImage(icon.getImage());

        // Initialize game panel
        gamePanel = new GamePanel(levelFile);
        add(gamePanel);

        // Play background music
        soundManager = new SoundManager();
        soundManager.playBackgroundMusic("src/background_music.wav"); // Ensure file exists

        setVisible(true);
    }

    public static void main(String[] args) {
        String[] options = {"Play Game", "Open Level Editor"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Platformer",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            String selectedLevel = selectLevelFile();
            if (selectedLevel != null) {
                SwingUtilities.invokeLater(() -> new PlatformerGame(selectedLevel));
            }
        } else if (choice == 1) {
            SwingUtilities.invokeLater(LevelEditor::new);
        }
    }

    private static String selectLevelFile() {
        File levelFolder = new File("./"); // Location of CSV levels
        File[] levelFiles = levelFolder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (levelFiles == null || levelFiles.length == 0) {
            JOptionPane.showMessageDialog(null, "No levels found!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String[] levelNames = Arrays.stream(levelFiles).map(File::getName).toArray(String[]::new);
        return (String) JOptionPane.showInputDialog(null, "Select a level:", "Level Selection",
                JOptionPane.QUESTION_MESSAGE, null, levelNames, levelNames[0]);
    }
}