package sim3d;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.vecmath.Vector3d;

// Triangle class, used by objects for their mesh
public class Triangle {
    // The associated object
    protected BaseObject base;

    // The triangle's vertices. There are only 3 of them.
    // Also the triangle's normal vector. This is important for the visibility.
    protected Vector3d[] vertex;
    protected Vector3d normal;

    // The default constructor
    public Triangle() {
        this.vertex = new Vector3d[3];
        this.normal = new Vector3d();
    }

    // Construct a triangle with points p, q and r
    public Triangle(Vector3d p, Vector3d q, Vector3d r) {
        this.vertex = new Vector3d[3];
        this.normal = new Vector3d();

        this.vertex[0] = p;
        this.vertex[1] = q;
        this.vertex[2] = r;

        this.calculateNormal();
    }

    // Recalculates the normal vector when a triangle's points
    // have been changed. The normal vector is to be the normalized
    // cross-product between the two legs emerging from point p,
    // i.e. the vectors q-p and r-p.
    private Triangle calculateNormal() {
        // Create the legs from q and r
        Vector3d[] legs = new Vector3d[2];
        legs[0] = new Vector3d(this.vertex[1]);
        legs[1] = new Vector3d(this.vertex[2]);

        // Subtract p from both
        legs[0].sub(this.vertex[0]);
        legs[1].sub(this.vertex[0]);

        // Calculate the normal and normalize it
        this.normal.cross(legs[0], legs[1]);
        this.normal.normalize();

        return this;
    }

    // Gets the normal vector in World terms
    // We need to subtract the position of the associated object
    // because of the way getWorldVertex() works
    public Vector3d getNormal() {
        Vector3d normal = this.base.getWorldVertex(this.normal);
        normal.sub(this.base.p);
        normal.normalize();

        return normal;
    }

    // Sets the associated object
    public Triangle setObject(BaseObject base) {
        this.base = base;

        return this;
    }

    // Calculates whether this triangle is visible from the observer
    // on World's (0, 0, 0) position.
    // If the dot-product of the normal vector (in World's terms) and
    // the position vector of the p vertex is negative, it means the
    // angle between these vectors is more than Math.PI/2, i.e. facing
    // away from the observer, i.e. not visible.
    public boolean visible() {
        Vector3d v = this.base.getWorldVertex(this.vertex[0]);

        return v.dot(this.getNormal()) > 0;
    }

    // Draws this triangle
    public void draw(Graphics g) {
        g.setColor(this.base.getColor());

        World w = this.base.getWorld();
        Polygon p = new Polygon();

        Vector3d v;
        for(int i = 0; i < 3; i++) {
            v = this.base.getWorldVertex(this.vertex[i]);

            p.addPoint(
                    (int)(w.width/2.0 + v.x * w.focus / v.z),
                    (int)(w.height/2.0 - v.y * w.focus / v.z)
                );
        }

        g.drawPolygon(p);
    }
}
