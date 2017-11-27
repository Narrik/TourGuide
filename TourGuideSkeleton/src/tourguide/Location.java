package tourguide;

import java.util.logging.Logger;

public class Location {
	private static Logger logger = Logger.getLogger("tourguide");
	public double easting;
	public double northing;
	Location(double e, double n){
		logger.finer("East: " + e + "  North: "  + n);
		easting = e;
		northing = n;
	}
	public Displacement deltaFrom(Location location) {
		double east = location.easting - this.easting;
		double north = location.northing - this.northing;
		Displacement disp = new Displacement(east,north);
		return disp;
		
	}
}
