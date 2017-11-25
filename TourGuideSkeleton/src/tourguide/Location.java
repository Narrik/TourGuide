package tourguide;

public class Location {
	public double easting;
	public double northing;
	Location(double e, double n){
		easting = e;
		northing = n;
	}
	public Displacement deltaFrom(Location location, Location currWayp) {
		easting = currWayp.easting - location.easting;
		northing = currWayp.northing - location.northing;
		Displacement disp = new Displacement(easting,northing);
		return disp;
		
	}
}
