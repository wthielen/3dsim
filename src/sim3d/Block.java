package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// ブロッククラス
// このブロックは高・幅・奥の３つのサイズを持っている
public class Block extends BaseObject {
    protected double sizeX, sizeY, sizeZ;
    protected Vector3d[] vertex;

    // (0, 0, 0)に位置される1x1x1のブロックを作成
    public Block() {
        super();
        this.sizeX = this.sizeY = this.sizeZ = 1.0;

        this.vertex = new Vector3d[8];
        for(int i = 0; i < 8; i++) {
            this.vertex[i] = new Vector3d();
        }

        this.updateMesh();
    }

    // p に位置される1x1x1のブロックを作成
    public Block(Vector3d p) {
        super(p);
        this.sizeX = this.sizeY = this.sizeZ = 1.0;

        this.vertex = new Vector3d[8];
        for(int i = 0; i < 8; i++) {
            this.vertex[i] = new Vector3d();
        }

        this.updateMesh();
    }

    // p に位置されるsizeX x sizeY x sizeZのブロックを作成
    public Block(Vector3d p, double sizeX, double sizeY, double sizeZ) {
        super(p);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        this.vertex = new Vector3d[8];
        for(int i = 0; i < 8; i++) {
            this.vertex[i] = new Vector3d();
        }

        this.updateMesh();
    }

    // サイズが変更されたときに頂点の座標を再計算
    // この座標はオブジェクトのXYZ軸に対して計算されたもの
    protected void updateMesh() {
        this.vertex[0].set(-this.sizeX/2.0, this.sizeY/2.0, this.sizeZ/2.0);
        this.vertex[1].set(this.sizeX/2.0, this.sizeY/2.0, this.sizeZ/2.0);
        this.vertex[2].set(this.sizeX/2.0, -this.sizeY/2.0, this.sizeZ/2.0);
        this.vertex[3].set(-this.sizeX/2.0, -this.sizeY/2.0, this.sizeZ/2.0);

        this.vertex[4].set(-this.sizeX/2.0, this.sizeY/2.0, -this.sizeZ/2.0);
        this.vertex[5].set(this.sizeX/2.0, this.sizeY/2.0, -this.sizeZ/2.0);
        this.vertex[6].set(this.sizeX/2.0, -this.sizeY/2.0, -this.sizeZ/2.0);
        this.vertex[7].set(-this.sizeX/2.0, -this.sizeY/2.0, -this.sizeZ/2.0);
    }

    public void draw(World w, Graphics g) {
        g.setColor(this.c);

        int x[] = new int[8];
        int y[] = new int[8];

        Vector3d v = new Vector3d();
        for(int i = 0; i < 8; i++) {
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

        Polygon p[] = new Polygon[6];
        for(int i = 0; i < 6; i++) {
            p[i] = new Polygon();
        }

        // 各多角形（4角形）に必要な頂点を集める
        // updateMeshでつかったインデックスと同じ識別子
        int[][] points = {
            { 0, 1, 2, 3 },
            { 1, 5, 6, 2 },
            { 5, 4, 7, 6 },
            { 4, 0, 3, 7 },
            { 0, 1, 5, 4 },
            { 3, 2, 6, 7 }
        };

        for(int i = 0; i < 6; i++) {
            for(int j: points[i]) {
                p[i].addPoint(x[j], y[j]);
            }

            g.drawPolygon(p[i]);
        }
    }
}
