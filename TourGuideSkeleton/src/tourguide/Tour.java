package tourguide;

import java.util.*;

public class Tour {
	public String id;
	public String title;
	public Annotation annotation;
	public LinkedList<Waypoint> waypoints;
    public LinkedList<Leg> legs;

	public Tour (String id, String title, Annotation annotation){
		this.id = id;
		this.title = title;
		this.annotation = annotation;
		this.waypoints = new LinkedList<Waypoint>();
        this.legs = new LinkedList<Leg>();

	}
	public void addWaypoint(Location location, Annotation annotation) {
		waypoints.add(new Waypoint(location,annotation));
	}
	
	public void addLeg(Annotation annotation) {
		legs.add(new Leg(annotation));
	}
	
	public Waypoint getWaypoint(int step) {
        return waypoints.get(step);
	}

	public Leg getLeg(int step) {
        return legs.get(step);

	}

	public int getNumberOfWaypoints(){
	    return waypoints.size();
    }

    public int getNumberOfLegs(){
        return legs.size();
    }
	
}
