package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// 楕円のクラス
public class Ellipse extends BaseObject {
    protected double width = 1.0, height = 1.0;
    protected int nVertex = 80;

    // (0, 0, 0)に位置される1x1の楕円を作成
    public Ellipse() {
        super();
        this.updateMesh();
    }

    // p に位置される1x1の楕円を作成
    public Ellipse(Vector3d p) {
        super(p);
        this.updateMesh();
    }

    // p に位置されるwidth x heightの楕円を作成
    public Ellipse(Vector3d p, double width, double height) {
        super(p);
        this.width = width;
        this.height = height;

        this.updateMesh();
    }

    // 楕円の解像度を設定する
    public Ellipse setSmoothness(int smoothness) {
        this.nVertex = 4 * smoothness;

        this.updateMesh();
        return this;
    }

    // このオブジェクトを描くのに必要な頂点数を返す
    protected int getVertexCount() {
        return this.nVertex;
    }

    // サイズまたは解像度が変更されたときに頂点の座標を再計算
    // この座標はオブジェクトのXYZ軸に対して計算されたもの
    protected void updateMesh() {
        this.initVertices();

        for(int i = 0; i < this.nVertex; i++) {
            double phi = 2 * Math.PI * i / this.nVertex;
            this.vertex[i].set(this.width * Math.sin(phi), this.height * Math.cos(phi), 0);
        }
    }

    // 楕円を描く
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
