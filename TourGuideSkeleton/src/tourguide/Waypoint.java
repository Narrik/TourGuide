package tourguide;

import java.util.logging.Logger;

public class Waypoint {
	Annotation note;
	Location location;
	double radius;
	private static Logger logger = Logger.getLogger("tourguide");
	Waypoint(Annotation n, double r, Location loc){
        logger.finer("Annotation: "  + n);
		location = loc;
		radius = r;
		note = n;
	}
	public boolean near(Location loc) {
		return (this.location.deltaFrom(loc).distance() <= radius);
	}
	
}
