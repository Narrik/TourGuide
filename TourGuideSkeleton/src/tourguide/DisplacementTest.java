/**
 *
 */
package tourguide;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author pbj
 */
public class DisplacementTest {
    /**
     * EPS = Epsilon, the difference to allow in floating point numbers when
     * comparing them for equality.
     */
    private static final double EPS = 0.01;

    @Test
    public void testNorthBearing() {
        double bearing = new Displacement(0.0, 1.0).bearing();
        assertEquals(0.0, bearing, EPS);
    }

    @Test
    public void testNorthEastBearing() {
        double bearing = new Displacement(7.8, 7.8).bearing();
        assertEquals(45.0, bearing, EPS);
    }

    @Test
    public void testEastBearing() {
        double bearing = new Displacement(6.0, 0.0).bearing();
        assertEquals(90.0, bearing, EPS);
    }

    @Test
    public void testSouthEastBearing() {
        double bearing = new Displacement(0.1, -0.1).bearing();
        assertEquals(135.0, bearing, EPS);

    }

    @Test
    public void testSouthBearing() {
        double bearing = new Displacement(0.0, -10.0).bearing();
        assertEquals(180.0, bearing, EPS);
    }

    @Test
    public void testSouthWestBearing() {
        double bearing = new Displacement(-13.0, -13.0).bearing();
        assertEquals(225.0, bearing, EPS);

    }

    @Test
    public void testWestBearing() {
        double bearing = new Displacement(-5.0, 0.0).bearing();
        assertEquals(270.0, bearing, EPS);
    }

    @Test
    public void testNorthWestBearing() {
        double bearing = new Displacement(-6.23, 6.23).bearing();
        assertEquals(315.0, bearing, EPS);
    }

    @Test
    public void testUnorthodoxAngle() {
        double bearing = new Displacement(6.0, 3.463).bearing();
        assertEquals(60.0, bearing, EPS);
    }


    @Test
    public void testDistance() {
        double distance1 = new Displacement(0.0, 1.0).distance();
        double distance2 = new Displacement(4.0, -3.0).distance();
        double distance3 = new Displacement(-3.0, 4.0).distance();
        double distance4 = new Displacement(1.0, 0.0).distance();
        double distance5 = new Displacement(-7.8, 6.92).distance();
        double distance6 = new Displacement(6.92001, 7.7999).distance();
        assertEquals(distance4, distance1, EPS);
        assertEquals(distance3, distance2, EPS);
        assertEquals(distance5, distance6, EPS);


    }


}
