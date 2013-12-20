package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

// ブロッククラス
// このブロックは高・幅・奥の３つのサイズを持っている
public class Block extends BaseObject {
    protected double sizeX = 1.0, sizeY = 1.0, sizeZ = 1.0;
    protected int nVertex = 8;

    // (0, 0, 0)に位置される1x1x1のブロックを作成
    public Block() {
        super();
        this.updateMesh();
    }

    // p に位置される1x1x1のブロックを作成
    public Block(Vector3d p) {
        super(p);
        this.updateMesh();
    }

    // p に位置されるsizeX x sizeY x sizeZのブロックを作成
    public Block(Vector3d p, double sizeX, double sizeY, double sizeZ) {
        super(p);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        this.updateMesh();
    }

    protected int getVertexCount() {
        return this.nVertex;
    }

    // サイズが変更されたときに頂点の座標を再計算
    // この座標はオブジェクトのXYZ軸に対して計算されたもの
    protected void updateMesh() {
        this.initVertices();

        this.vertex[0].set(-this.sizeX/2.0, this.sizeY/2.0, this.sizeZ/2.0);
        this.vertex[1].set(this.sizeX/2.0, this.sizeY/2.0, this.sizeZ/2.0);
        this.vertex[2].set(this.sizeX/2.0, -this.sizeY/2.0, this.sizeZ/2.0);
        this.vertex[3].set(-this.sizeX/2.0, -this.sizeY/2.0, this.sizeZ/2.0);

        this.vertex[4].set(-this.sizeX/2.0, this.sizeY/2.0, -this.sizeZ/2.0);
        this.vertex[5].set(this.sizeX/2.0, this.sizeY/2.0, -this.sizeZ/2.0);
        this.vertex[6].set(this.sizeX/2.0, -this.sizeY/2.0, -this.sizeZ/2.0);
        this.vertex[7].set(-this.sizeX/2.0, -this.sizeY/2.0, -this.sizeZ/2.0);
    }

    // ブロックを描く
    public void draw(Graphics g) {
        g.setColor(this.c);

        Vector2d xy[] = this.mapVertices();

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
                p[i].addPoint((int)(xy[j].x), (int)(xy[j].y));
            }

            g.drawPolygon(p[i]);
        }
    }
}
