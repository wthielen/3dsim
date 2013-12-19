package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// 四角形のクラス 
public class Rectangle extends BaseObject {
    protected double width, height;
    protected Vector3d[] vertex;

    // (0, 0, 0)に位置される1x1の四角形を作成
    public Rectangle() {
        super();
        this.width = this.height = 1.0;

        this.updateMesh();
    }

    // p に位置される1x1の四角形を作成
    public Rectangle(Vector3d p) {
        super(p);
        this.width = this.height = 1.0;

        this.updateMesh();
    }

    // p に位置されるwidth x heightの四角形を作成
    public Rectangle(Vector3d p, double width, double height) {
        super(p);
        this.width = width;
        this.height = height;

        this.updateMesh();
    }

    // サイズが変更されたときに頂点の座標を再計算
    // この座標はオブジェクトのXYZ軸に対して計算されたもの
    protected void updateMesh() {
        if (null == this.vertex) {
            this.vertex = new Vector3d[4];
            for(int i = 0; i < 4; i++) {
                this.vertex[i] = new Vector3d();
            }
        }

        this.vertex[0].set(-this.width/2.0, this.height/2.0, 0);
        this.vertex[1].set(this.width/2.0, this.height/2.0, 0);
        this.vertex[2].set(this.width/2.0, -this.height/2.0, 0);
        this.vertex[3].set(-this.width/2.0, -this.height/2.0, 0);
    }

    // 四角形を描く
    public void draw(World w, Graphics g) {
        if (this.p.z < 0) return;

        g.setColor(this.c);

        int x[] = new int[4];
        int y[] = new int[4];

        Vector3d v = new Vector3d();
        for(int i = 0; i < 4; i++) {
            // 0, 0, 0 から始まる
            v.set(0, 0, 0);

            // オブジェクトの位置へ移動
            v.add(this.p);

            // 各頂点の座標はオブジェクトのXYZ軸に対して計算されたので
            // ドット積を利用して各頂点の座標を計算して加算
            v.add(new Vector3d(
                    this.axis[0].dot(this.vertex[i]),
                    this.axis[1].dot(this.vertex[i]),
                    this.axis[2].dot(this.vertex[i])
                ));

            x[i] = (int)(w.width/2.0 + v.x * w.focus / v.z);
            y[i] = (int)(w.height/2.0 - v.y * w.focus / v.z);
        }

        Polygon p = new Polygon();
        for(int i = 0; i < 4; i++) {
            p.addPoint(x[i], y[i]);
        }

        g.fillPolygon(p);
    }
}

