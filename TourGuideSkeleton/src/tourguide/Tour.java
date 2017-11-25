package tourguide;

import java.util.*;

public class Tour {
	public String id;
	public String title;
	public Annotation annotation;
	public Integer currStage;
	public Waypoint currWaypoint;
	public Leg currLeg;
	public LinkedList<Waypoint> waypoints;
    public LinkedList<Leg> legs;

	public Tour (String id, String title, Annotation annotation){
		this.id = id;
		this.title = title;
		this.annotation = annotation;
		this.waypoints = new LinkedList<Waypoint>();
        this.legs = new LinkedList<Leg>();

	}
	public void addWaypoint (Annotation ann, double waypointRadius, Location loc) {
		waypoints.add(new Waypoint(ann, waypointRadius, loc));
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
