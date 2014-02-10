package sim3d;

import java.awt.Graphics;
import javax.vecmath.Vector3d;

// A class for an ellipsoid
public class Ellipsoid extends BaseObject {
    protected double sizeX = 1.0, sizeY = 1.0, sizeZ = 1.0;
    protected int nVertex = 20 * 20 + 2;

    // Default constructor to create a 1x1x1 ellipsoid at (0, 0, 0)
    public Ellipsoid() {
        super();
        this.updateMesh();
    }

    // Constructor to create a 1x1x1 ellipsoid at position p
    public Ellipsoid(Vector3d p) {
        super(p);
        this.updateMesh();
    }

    // Constructor to create a sizeX x sizeY x sizeZ ellipsoid at position p
    public Ellipsoid(Vector3d p, double sizeX, double sizeY, double sizeZ) {
        super(p);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        this.updateMesh();
    }

    // Sets the smoothness of the ellipsoid. The smoothness number is the
    // total number of vertices on each latitude and longitude.
    // 2 is added to the total number for the northpole and southpole.
    public Ellipsoid setSmoothness(int smoothness) {
        this.nVertex = smoothness * smoothness + 2;

        this.updateMesh();
        return this;
    }

    // Returns the total number of vertices
    protected int getVertexCount() {
        return this.nVertex;
    }

    // Recalculates the vertex coordinates, and the mesh triangles
    // when sizes or the smoothness changes.
    protected void updateMesh() {
        this.initVertices();

        // Set the poles
        this.vertex[0].set(0, 0, this.sizeZ);
        this.vertex[this.nVertex - 1].set(0, 0, -this.sizeZ);

        int n = this.nVertex - 2;

        // Calculate each vertex's coordinates
        int steps = (int)(Math.sqrt(n));
        double phi, theta;
        for(int i = 0; i < steps; i++) {
            for (int j = 0; j < steps; j++) {
                phi = (i + 1) * Math.PI / (steps + 2);
                theta = j * 2 * Math.PI / steps;

                this.vertex[i * steps + j + 1].set(
                        this.sizeX * Math.sin(phi) * Math.cos(theta),
                        this.sizeY * Math.sin(phi) * Math.sin(theta),
                        this.sizeZ * Math.cos(phi)
                    );
            }
        }

        // Prepare number of triangles
        if (null == this.triangle || this.triangle.length != n * 2) {
            this.triangle = new Triangle[n * 2];
        }

        // For the first triangle:
        // p is the current vertex
        // q is the eastern neighbouring vertex, i.e. the vertex on the higher longitude
        // r is the northern neighbour, i.e. the vertex on the higher latitude
        //
        // For the second triangle:
        // p is the current vertex
        // q is the western neighbouring vertex, i.e. the vertex on the lower longitude
        // r is the southern neighbouring vertex, i.e. the vertex on the lower latitude
        int p, q, r;
        for(int i = 0; i < n; i++) {
            // First triangle
            p = i;
            q = (i / steps) * steps + (i + 1) % steps;
            r = (i / steps - 1) * steps + i % steps;
            if (r < 0) r = -1;
            this.triangle[2 * i] = new Triangle(
                    this.vertex[p + 1],
                    this.vertex[q + 1],
                    this.vertex[r + 1]
                );
            this.triangle[2 * i].setObject(this);

            // Second triangle
            p = i;
            q = (i / steps) * steps + ((i - 1) % steps + (i - 1) + steps) % steps;
            r = (i / steps + 1) * steps + i % steps;
            if (r > n) r = n;
            this.triangle[2 * i + 1] = new Triangle(
                    this.vertex[p + 1],
                    this.vertex[q + 1],
                    this.vertex[r + 1]
                );
            this.triangle[2 * i + 1].setObject(this);
        }
    }

    // Draws the ellipsoid
    public void draw(Graphics g) {
        if (this.p.z < 0) return;

        g.setColor(this.c);

        int n = this.nVertex - 2;
        for(int i = 0; i < 2 * n; i++) {
            if (this.triangle[i].visible()) {
                this.triangle[i].draw(g);
            }
        }
    }
}
