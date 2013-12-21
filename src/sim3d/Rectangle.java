package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// 四角形のクラス 
public class Rectangle extends BaseObject {
    protected double width = 1.0, height = 1.0;
    protected int nVertex = 4;

    // (0, 0, 0)に位置される1x1の四角形を作成
    public Rectangle() {
        super();
        this.updateMesh();
    }

    // p に位置される1x1の四角形を作成
    public Rectangle(Vector3d p) {
        super(p);
        this.updateMesh();
    }

    // p に位置されるwidth x heightの四角形を作成
    public Rectangle(Vector3d p, double width, double height) {
        super(p);
        this.width = width;
        this.height = height;

        this.updateMesh();
    }

    // このオブジェクトを描くのに必要な頂点数を返す
    protected int getVertexCount() {
        return this.nVertex;
    }

    // サイズが変更されたときに頂点の座標を再計算
    // この座標はオブジェクトのXYZ軸に対して計算されたもの
    protected void updateMesh() {
        this.initVertices();

        this.vertex[0].set(-this.width/2.0, this.height/2.0, 0);
        this.vertex[1].set(this.width/2.0, this.height/2.0, 0);
        this.vertex[2].set(this.width/2.0, -this.height/2.0, 0);
        this.vertex[3].set(-this.width/2.0, -this.height/2.0, 0);
    }

    // 四角形を描く
    public void draw(Graphics g) {
        if (this.p.z < 0) return;

        g.setColor(this.c);

        Vector3d xy[] = this.mapVertices();

        Polygon p = new Polygon();
        for(int i = 0; i < this.nVertex; i++) {
            p.addPoint((int)(xy[i].x), (int)(xy[i].y));
        }

        g.fillPolygon(p);
    }
}

