package tourguide;

public class Waypoint {
	Annotation note;
	Location currWayp;
	double radius; 
	Waypoint(Annotation n, double r, Location loc){
		currWayp = loc;
		radius = r;
		note = n;
	}
	public boolean near(Location location) {
		return radius <= location.deltaFrom(location, currWayp).distance(); 
	}
	
}
