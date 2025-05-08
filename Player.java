import java.awt.*;
import java.util.List;

class Player {
    private int x, y, width, height;
    private int velocityX, velocityY;
    private boolean onGround;
    private final int gravity = 2;

    public Player(int startX, int startY) {
        x = startX;
        y = startY;
        width = 30;
        height = 30;
        velocityX = 0;
        velocityY = 0;
    }

    public void update(List<Platform> platforms, List<Spike> spikes, Rectangle goal) {
        velocityY += gravity;
        x += velocityX;
        y += velocityY;

        for (Platform p : platforms) {
            if (new Rectangle(x, y, width, height).intersects(new Rectangle(p.x, p.y, p.width, p.height))) {
                y = p.y - height;
                velocityY = 0;
                onGround = true;
            }
        }

        for (Spike s : spikes) {
            if (new Rectangle(x, y, width, height).intersects(new Rectangle(s.x, s.y, 20, 20))) {
                respawn();
            }
        }
        if (y > 600) respawn();

        if (new Rectangle(x, y, width, height).intersects(goal)) {
            System.out.println("Victory!");
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public void jump() {
        if (onGround) {
            velocityY = -20;
            onGround = false;
        }
    }

    public void moveLeft() { velocityX = -5; }
    public void moveRight() { velocityX = 5; }
    public void stopMoving() { velocityX = 0; } // Fixes movement issue
    public void respawn() { x = 100; y = 500; velocityX = 0; velocityY = 0; }
}