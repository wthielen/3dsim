package sim3d;

import java.util.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.vecmath.Vector3d;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel implements Runnable {
    public final int width, height;
    public final int focus;
    public final int fps = 60;

    // A dynamic collection of objects placed in this World
    private HashMap<String, BaseObject> objects;

    public static void main(String[] args) {
        World w = new World(600, 600, 300);

        Block b = new Block(new Vector3d(-100, 0, 450), 200, 100, 50);
        Ellipsoid e = new Ellipsoid(new Vector3d(100, 0, 450), 100, 80, 50);
        //e.setSmoothness(6);

        w.addObject(b, "block");
        w.addObject(e, "ellipsoid");

        new Thread(w).start();
    }

    // The World constructor
    public World(int width, int height, int focus) {
        this.width = width;
        this.height = height;
        this.focus = focus;

        this.objects = new HashMap<String, BaseObject>();
    }

    // Execute the thread
    @Override
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

        // This thread's loop
        while(true) {
            repaint();

            objects.get("block")
                .rotate(new Vector3d(Math.PI/4.0, Math.PI/4.0, 0));

            objects.get("ellipsoid")
                .rotate(new Vector3d(Math.PI/4.0, Math.PI/2.0, 0));

            try {
                Thread.sleep(1000 / this.fps);
            } catch(InterruptedException e) {
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw all the registered objects
        for(BaseObject o: this.getDrawables()) {
            o.draw(g);
        }
    }

    // Add the object to this World
    public void addObject(BaseObject o, String id) {
        o.setWorld(this);
        this.objects.put(id, o);
    }

    // Get an object
    public BaseObject getObject(String id) {
        return this.objects.get(id);
    }

    // Get drawable objects in decreasing order of their Z coordinates
    public List<BaseObject> getDrawables() {
        List<BaseObject> drawables = new ArrayList<BaseObject>(objects.values());
        Comparator<BaseObject> zIndex = new Comparator<BaseObject>() {
            public int compare(BaseObject u, BaseObject v) {
                Vector3d up, vp;
                up = u.getPosition();
                vp = v.getPosition();

                if (up.z == vp.z) return 0;
                return up.z > vp.z ? -1 : 1;
            }
        };
        Collections.sort(drawables, zIndex);

        return drawables;
    }
}
