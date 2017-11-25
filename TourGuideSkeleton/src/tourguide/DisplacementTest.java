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
    public void testEastBearing() {
        double bearing = new Displacement(0.0, 1.0).bearing();
        assertEquals(0.0, bearing, EPS);
    }
    
    @Test
    public void testDistance() {
        double distance1 = new Displacement(0.0, 1.0).distance();
        double distance2 = new Displacement(4.0, -3.0).distance();
        double distance3 = new Displacement(-3.0, 4.0).distance();
        assertEquals(1.0, distance1, EPS);
        assertEquals(5.0, distance2, EPS);
        assertEquals(5.0, distance3, EPS);
    }
    
    
    
    
}
