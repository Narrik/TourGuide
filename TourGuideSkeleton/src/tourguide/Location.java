package tourguide;

public class Location {
	public double easting;
	public double northing;
	Location(double e, double n){
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
