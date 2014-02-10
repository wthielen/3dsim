package sim3d;

import java.awt.Color;
import java.awt.Graphics;
import javax.vecmath.Vector3d;
import javax.vecmath.Quat4d;

public abstract class BaseObject {
    // The world this object is put in
    // Needed to be able to access its focus and fps
    // variables
    protected World w;

    // The position coordinates in 3D
    protected Vector3d p;

    // The XYZ axis vectors for this object
    protected Vector3d[] axis;

    // This object's color
    protected Color c = Color.BLACK;

    // Number of vertices, and its collection
    protected int nVertex;
    protected Vector3d[] vertex;
    protected Triangle[] triangle;

    // Initialize the axis vectors
    private void initAxis() {
        this.axis = new Vector3d[3];
        this.axis[0] = new Vector3d(1, 0, 0);
        this.axis[1] = new Vector3d(0, 1, 0);
        this.axis[2] = new Vector3d(0, 0, 1);
    }

    // Prepare the vertex collection
    protected void initVertices() {
        int nVertex = this.getVertexCount();

        if (null == this.vertex || this.vertex.length != nVertex) {
            this.vertex = new Vector3d[nVertex];
            for(int i = 0; i < nVertex; i++) {
                this.vertex[i] = new Vector3d();
            }
        }
    }

    // Because in Java, class members are not overridden but only
    // class methods, we override the nVertex property by using
    // this class method
    protected int getVertexCount() {
        return this.nVertex;
    }

    // Convert all the vertices to the 2D coordinates
    // for the World's projection
    protected Vector3d[] mapVertices() {
        int nVertex = this.getVertexCount();

        Vector3d xy[] = new Vector3d[nVertex];
        Vector3d v = new Vector3d();
        for(int i = 0; i < nVertex; i++) {
            v = this.getWorldVertex(this.vertex[i]);

            xy[i] = new Vector3d(
                    this.w.width/2.0 + v.x * this.w.focus / v.z,
                    this.w.height/2.0 - v.y * this.w.focus / v.z,
                    0
                );
        }

        return xy;
    }

    // Convert the vertix to the 3D coordinates within the World
    // itself, i.e. with respect to the observer at position 
    // (0, 0, 0) in the World
    public Vector3d getWorldVertex(Vector3d v) {
        Vector3d ret = new Vector3d();
        ret.add(this.p);
        ret.add(new Vector3d(
                    this.axis[0].dot(v),
                    this.axis[1].dot(v),
                    this.axis[2].dot(v)
                ));

        return ret;
    }

    // Default constructor
    public BaseObject() {
        this.p = new Vector3d();
        this.initAxis();
    }

    // Constructor using position vector
    public BaseObject(Vector3d p) {
        this.p = p;
        this.initAxis();
    }

    // Constructor using position coordinates
    public BaseObject(double x, double y, double z) {
        this.p = new Vector3d(x, y, z);
        this.initAxis();
    }

    // Get position
    public Vector3d getPosition() {
        return this.p;
    }

    // Get color
    public Color getColor() {
        return this.c;
    }

    // Set corresponding World
    public BaseObject setWorld(World w) {
        this.w = w;
        return this;
    }

    // Get corresponding World
    public World getWorld() {
        return this.w;
    }

    // Set position using a vector
    public BaseObject setPosition(Vector3d p) {
        this.p.set(p);
        return this;
    }

    // Set position using 3D coordinates
    public BaseObject setPosition(double x, double y, double z) {
        this.p.set(x, y, z);
        return this;
    }

    // Set color
    public BaseObject setColor(Color c) {
        this.c = c;
        return this;
    }

    // Move the object along a translation vector
    public BaseObject translate(Vector3d v) {
        this.p.add(v);
        return this;
    }

    // Move the object along a translation vector of the given coordinates
    public BaseObject translate(double x, double y, double z) {
        this.p.add(new Vector3d(x, y, z));
        return this;
    }

    // Rotate the object itself using the given rotation vector
    public BaseObject rotate(Vector3d omega) {
        Vector3d delta = new Vector3d();

        // Rotate each axis vector
        for (int i = 0; i < 3; i++) {
            this.axis[i] = this._rotate(omega, this.axis[i]);
        }

        return this;
    }

    // Rotate the object around a certain point "base". The includeAxis parameter
    // tells the function whether to also rotate the axis vectors or not. Try both
    // settings to see the effect.
    public BaseObject rotate(Vector3d base, Vector3d omega, boolean includeAxis) {
        Vector3d delta = new Vector3d();
        Vector3d p_omega = new Vector3d();

        // Prepare a vector from base to the object's location
        p_omega.sub(this.p, base);

        // This is the vector we will rotate
        p_omega = this._rotate(omega, p_omega);

        // Now we set the location of the object to whatever the new
        // p_omega points to, by adding base to it
        // p = base + p_omega
        this.p.add(base, p_omega);

        // If we also want to rotate the axis vectors, do this
        if (includeAxis) this.rotate(omega);

        return this;
    }

    // The actual vector rotation function using quaternions
    private Vector3d _rotate(Vector3d omega, Vector3d v) {
        double angle = omega.length() / this.w.fps / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // Convert the rotation vector into a rotation quaternion
        Quat4d q1 = new Quat4d(
                omega.x * sin,
                omega.y * sin,
                omega.z * sin,
                cos
            );
        q1.normalize();

        // Get the quaternion's conjugate
        Quat4d q2 = new Quat4d(q1);
        q2.conjugate();

        // Perform the rotation
        Quat4d rotated = new Quat4d();
        Quat4d qv = new Quat4d(v.x, v.y, v.z, 0);
        rotated.mul(q1, qv);
        rotated.mul(q2);

        // Get the X, Y and Z from the resulting rotated quaternion, and
        // resize the vector to its original length
        Vector3d ret = new Vector3d(rotated.x, rotated.y, rotated.z);
        ret.scale(v.length());

        return ret;
    }

    // Draw the object, but since we don't know its shape
    // it's an abstract function
    abstract void draw(Graphics g);
}
