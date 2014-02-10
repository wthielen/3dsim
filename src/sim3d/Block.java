package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// Block class
// A block has sizes in 3 dimensions: width,  height and depth
public class Block extends BaseObject {
    protected double sizeX = 1.0, sizeY = 1.0, sizeZ = 1.0;
    protected int nVertex = 8;

    // Default constructor to create a 1x1x1 block at (0, 0, 0)
    public Block() {
        super();
        this.updateMesh();
    }

    // A constructor to create a 1x1x1 block at position p
    public Block(Vector3d p) {
        super(p);
        this.updateMesh();
    }

    // A constructor to create a sizeX x sizeY x sizeZ block at position p
    public Block(Vector3d p, double sizeX, double sizeY, double sizeZ) {
        super(p);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        this.updateMesh();
    }

    // Returns the number of vertices for this object
    protected int getVertexCount() {
        return this.nVertex;
    }

    // When the block's size has been updated, the mesh's vertices
    // need to be recalculated. This function does that.
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

    // Draw the block
    public void draw(Graphics g) {
        g.setColor(this.c);

        Vector3d xy[] = this.mapVertices();

        Polygon p[] = new Polygon[6];
        for(int i = 0; i < 6; i++) {
            p[i] = new Polygon();
        }

        // These are six rectangular polygons and the numbers
        // refer to the indexes of those points according to
        // the order as used by the updateMesh function
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
