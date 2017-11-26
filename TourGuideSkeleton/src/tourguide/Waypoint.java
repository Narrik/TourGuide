package tourguide;

public class Waypoint {
	Annotation note;
	Location location;
	double radius; 
	Waypoint(Annotation n, double r, Location loc){
		location = loc;
		radius = r;
		note = n;
	}
	public boolean near(Location loc) {
		return (this.location.deltaFrom(loc).distance() <= radius);
	}
	
}
