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

    // この世界に置いてあるオブジェクトのダイナミック収集
    private HashMap<String, BaseObject> objects;

    public static void main(String[] args) {
        World w = new World(600, 600, 300);

        Rectangle red = new Rectangle(new Vector3d(15, 0, 500), 40, 40);
        Rectangle blue = new Rectangle(new Vector3d(15, -150, 400), 20, 20);

        red.setColor(Color.RED);
        blue.setColor(Color.BLUE);

        w.addObject(red, "red");
        w.addObject(blue, "blue");

        new Thread(w).start();
    }

    // 世界のコンストラクタ
    public World(int width, int height, int focus) {
        this.width = width;
        this.height = height;
        this.focus = focus;

        this.objects = new HashMap<String, BaseObject>();
    }

    // スレッドを実行する
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

        // スレッドのループ
        while(true) {
            repaint();

            objects.get("blue")
                .rotate(new Vector3d(15, 0, 350), new Vector3d(-0.2, 0, 0), false);

            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 登録された全てのオブジェクトを描く
        for(BaseObject o: this.getDrawables()) {
            o.draw(this, g);
        }
    }

    // この世界にオブジェクトを追加
    public void addObject(BaseObject o, String id) {
        this.objects.put(id, o);
    }

    // オブジェクトをゲット
    public BaseObject getObject(String id) {
        return this.objects.get(id);
    }

    // Z座標の順番でオブジェクトをソートして返す
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
