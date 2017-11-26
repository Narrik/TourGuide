package tourguide;

public class Location {
	public double easting;
	public double northing;
	Location(double e, double n){
		easting = e;
		northing = n;
	}
	public Displacement deltaFrom(Location location) {
		easting = location.easting - this.easting;
		northing = location.northing - this.northing;
		Displacement disp = new Displacement(easting,northing);
		return disp;
		
	}
}
