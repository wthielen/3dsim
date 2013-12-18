package 3dsim;

import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.vecmath.Vector3d;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel implements Runnable {
    public final int width, height;
    public final int focus;

    private ArrayList<BaseObject> objects;

    public static void main(String[] args) {
        World w = new World(600, 600, 200);

        Block b = new Block(new Vector3d(-100, 0, 300), 300, 200, 100);
        w.addObject(b);

        new Thread(w).start();
    }

    public World(int width, int height, int focus) {
        this.width = width;
        this.height = height;
        this.focus = focus;

        this.objects = new ArrayList<BaseObject>();
    }

    public void run() {
        JFrame app = new JFrame("3D world");
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        app.setSize(this.width, this.height);
        app.setResizable(false);
        app.getContentPane().add(this);
        app.setVisible(true);

        while(true) {
            repaint();

            for(BaseObject o: objects) {
                o.translate(new Vector3d(10, 0, 0));
            }

            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(BaseObject o: objects) {
            o.draw(this, g);
        }
    }

    public void addObject(BaseObject o) {
        this.objects.add(o);
    }
}
