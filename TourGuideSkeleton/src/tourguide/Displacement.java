package tourguide;

import java.util.logging.Logger;
/**
 * A class representing displacement and functionalities to find 
 * distance and bearing.
 * For example:
 * <pre>
 *    Displacement dist = new Displacement();
 *    dist.bearing();
 * </pre>
 *
 * @author  Kristian Kalinak
 * @author 	Likhitha Sai Modalavalasa 
 * @see     java.awt.BaseWindow
 * @see     java.awt.Button
 */
public class Displacement {
    /**
     * The value of this field logger of the type Logger is "tourguide".
     * 
     * @see java.util.logging.Logger 
     */
    private static Logger logger = Logger.getLogger("tourguide");
    /**
     * The east distance indicates the location's position in 
     * metres east of some reference position.
     * 
     * @see #distance()
     * @see #bearing()
     */  
    public double east;
    /**
     * The north distance indicates the location's position in 
     * metres north of some reference position.
     * 
     * @see #distance()
     * @see #bearing()
     */  
    public double north;
    /**
     * Constructor of the Displacement class which receives east and north 
     * distance as arguments and assigns them to the respective fields.
     *
     * @param e   the distance in metres to be recorded as east distance.
     * @param n   the distance in metres to be recorded as north distance.
     */
    public Displacement(double e, double n) {
        logger.finer("East: " + e + "  North: "  + n);
        
        east = e;
        north = n;
    }
    /**
     * Returns the character at the specified index. 
     *
     * @return    the distance to the location.
     */
    public double distance() {
        logger.finer("Entering");
        
        return Math.sqrt(east * east + north * north);
    }
    /**
     * The function first computes the angle from x-axis towards y-axis
     * then makes the angle positive if inRadians is negative and 
     * at the end return the bearing angle in radians. 
     *
     * @return    the bearings measured clockwise from the north direction in degrees.
     */
    // Bearings measured clockwise from north direction.
    public double bearing() {
        logger.finer("Entering");
              
        // atan2(y,x) computes angle from x-axis towards y-axis, returning a negative result
        // when y is negative.
        
        double inRadians = Math.atan2(east, north);
        
        if (inRadians < 0) {
            inRadians = inRadians + 2 * Math.PI;
        }
        
        return Math.toDegrees(inRadians);
    }
        
    
    
}
