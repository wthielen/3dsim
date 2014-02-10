package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// Ellipse class
public class Ellipse extends BaseObject {
    protected double width = 1.0, height = 1.0;
    protected int nVertex = 80;

    // Default constructor to create a 1x1 ellipse at (0, 0, 0)
    public Ellipse() {
        super();
        this.updateMesh();
    }

    // Constructor to create a 1x1 ellipse at position p
    public Ellipse(Vector3d p) {
        super(p);
        this.updateMesh();
    }

    // Constructor to create a width x height ellipse at position p
    public Ellipse(Vector3d p, double width, double height) {
        super(p);
        this.width = width;
        this.height = height;

        this.updateMesh();
    }

    // Sets the smoothness of this ellipse. This number is the number
    // of vertices on a quarter arc of the ellipse.
    public Ellipse setSmoothness(int smoothness) {
        this.nVertex = 4 * smoothness;

        this.updateMesh();
        return this;
    }

    // Returns the vertex count of this object
    protected int getVertexCount() {
        return this.nVertex;
    }

    // When the size or the smoothness changes, it needs to recalculate
    // the vertices. These vertices are calculated based on the ellipse's
    // XYZ axis vectors.
    protected void updateMesh() {
        this.initVertices();

        for(int i = 0; i < this.nVertex; i++) {
            double phi = 2 * Math.PI * i / this.nVertex;
            this.vertex[i].set(this.width * Math.sin(phi), this.height * Math.cos(phi), 0);
        }
    }

    // Draw the ellipse
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
