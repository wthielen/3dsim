package sim3d;

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

    // この世界に置いてあるオブジェクトのダイナミック収集
    private ArrayList<BaseObject> objects;

    public static void main(String[] args) {
        World w = new World(600, 600, 300);

        Block b = new Block(new Vector3d(-400, 0, 800), 300, 200, 100);
        w.addObject(b);

        new Thread(w).start();
    }

    // 世界のコンストラクタ
    public World(int width, int height, int focus) {
        this.width = width;
        this.height = height;
        this.focus = focus;

        this.objects = new ArrayList<BaseObject>();
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

            for(BaseObject o: objects) {
                o.rotate(new Vector3d(0, 0, 400), new Vector3d(0, 0, 0.1), true);
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

        // 登録された全てのオブジェクトを描く
        for(BaseObject o: objects) {
            o.draw(this, g);
        }
    }

    // この世界にオブジェクトを追加
    public void addObject(BaseObject o) {
        this.objects.add(o);
    }
}
