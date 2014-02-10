package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// A rectangle class
public class Rectangle extends BaseObject {
    protected double width = 1.0, height = 1.0;
    protected int nVertex = 4;

    // Default constructor to create a 1x1 rectangle on position (0, 0, 0)
    public Rectangle() {
        super();
        this.updateMesh();
    }

    // Constructor to create a 1x1 rectangle on position (0, 0, 0)
    public Rectangle(Vector3d p) {
        super(p);
        this.updateMesh();
    }

    // Constructor to create a width x height rectangle on positon (0, 0, 0)
    public Rectangle(Vector3d p, double width, double height) {
        super(p);
        this.width = width;
        this.height = height;

        this.updateMesh();
    }

    // Return the vertex count for this object
    protected int getVertexCount() {
        return this.nVertex;
    }

    // When either size changes, this function needs to be called
    // to recalculate the vertex coordinates based on this object's
    // XYZ axis system
    protected void updateMesh() {
        this.initVertices();

        this.vertex[0].set(-this.width/2.0, this.height/2.0, 0);
        this.vertex[1].set(this.width/2.0, this.height/2.0, 0);
        this.vertex[2].set(this.width/2.0, -this.height/2.0, 0);
        this.vertex[3].set(-this.width/2.0, -this.height/2.0, 0);
    }

    // Draw the rectangle
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

