import java.awt.*;

class Platform {
    int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
    }
}

class Spike {
    int x, y;

    public Spike(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillPolygon(new int[]{x, x + 10, x + 20}, new int[]{y + 20, y, y + 20}, 3);
    }
}