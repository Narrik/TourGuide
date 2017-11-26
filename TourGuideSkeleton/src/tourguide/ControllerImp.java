/**
 * 
 */
package tourguide;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import tourguide.Chunk.CreateHeader;

/**
 * @author pbj
 */
public class ControllerImp implements Controller {
    private static Logger logger = Logger.getLogger("tourguide");
    private static final String LS = System.lineSeparator();

    private String startBanner(String messageName) {
        return  LS 
                + "-------------------------------------------------------------" + LS
                + "MESSAGE: " + messageName + LS
                + "-------------------------------------------------------------";
    }

    public Mode mode;
    public Tour tour;
    public Location loc;
    public Displacement disp;
    public int stage;
    public double waypointRadius;
    public double waypointSeparation;
    public Library lib;
    public boolean browseDetails;
    public String browseDetailsTourId;
    
    public ControllerImp(double waypointRadius, double waypointSeparation) {
        this.mode = Mode.BrowseTours;
        this.stage = 0;
        this.browseDetails = false;
        this.waypointRadius = waypointRadius;
        this.waypointSeparation = waypointSeparation;     
    }

    //--------------------------
    // Create tour mode
    //--------------------------

    // Some examples are shown below of use of logger calls.  The rest of the methods below that correspond 
    // to input messages could do with similar calls.
    
    @Override
    public Status startNewTour(String id, String title, Annotation annotation) {
        logger.fine(startBanner("startNewTour"));
        if (mode == Mode.BrowseTours && !browseDetails){
            mode = Mode.CreateTour;
            tour = new Tour(id,title,annotation);
            return Status.OK;
        }
        return new Status.Error("Cannot start a new tour while not in tour overview");
    }

    @Override
    public Status addWaypoint(Annotation ann) {
        logger.fine(startBanner("addWaypoint"));
        if (mode == Mode.CreateTour) {
            if (tour.getNumberOfWaypoints() == tour.getNumberOfLegs()){
                tour.addLeg(Annotation.getDefault());
            }
            tour.addWaypoint(ann,waypointRadius,loc);
            return Status.OK;
        }
        return new Status.Error("Cannot add a waypoint while not in create tour mode");
    }

    @Override
    public Status addLeg(Annotation annotation) {
        logger.fine(startBanner("addLeg"));
        if (mode == Mode.CreateTour){
            if (tour.getNumberOfWaypoints() == tour.getNumberOfLegs()) {
                tour.addLeg(annotation);
                return Status.OK;
            } else {
                return new Status.Error("Cannot add more than one leg to a waypoint");
            }

        }
        return new Status.Error("Cannot add a leg while not in create tour mode");
    }

    @Override
    public Status endNewTour() {
        logger.fine(startBanner("endNewTour"));
        if (mode == Mode.CreateTour) {
            if (tour.getNumberOfWaypoints() == 0) {
                return new Status.Error("Must add at least one waypoint to create a tour");
            }
            if (tour.getNumberOfWaypoints() == tour.getNumberOfLegs()) {
                lib.addTour(tour);
                mode = Mode.BrowseTours;
                return Status.OK;
            } else {
                return new Status.Error("A tour must the same amount of waypoints than legs");
            }
        }
        return new Status.Error("Cannot end a tour while not in create tour mode");
    }

    //--------------------------
    // Browse tours mode
    //--------------------------

    @Override
    public Status showTourDetails(String tourID) {
        logger.fine(startBanner("showTourDetails"));
        if (mode == Mode.BrowseTours) {
            if (!browseDetails) {
                if (!lib.get_tour_lib().containsKey(tourID)){
                    return new Status.Error("Tour not found");
                }
                browseDetails = true;
                browseDetailsTourId = tourID;
                return Status.OK;
            } else {
                return new Status.Error("Already browsing details of a tour");
            }
        }
        return new Status.Error("Cannot view details of a tour while not in browse tours mode");
    }
  
    @Override
    public Status showToursOverview() {
        logger.fine(startBanner("showToursOverview"));
        if (mode == Mode.BrowseTours){
                if (browseDetails){
                    browseDetails = false;
                    browseDetailsTourId = "";
                    return Status.OK;
                } else {
                    return new Status.Error("Already in tour overview");
                }
            }
        return new Status.Error("Cannot view tour overview while not in browse tours mode");
    }

    //--------------------------
    // Follow tour mode
    //--------------------------
    
    @Override
    public Status followTour(String id) {
        if (mode == Mode.BrowseTours) {
            if (!(id.equals(browseDetailsTourId))) {
                return new Status.Error("Cannot start following a tour without viewing its details first");
            }
            mode = Mode.FollowTour;
            tour = lib.get_tour_lib().get(id);
            while (mode == Mode.FollowTour) {
                if ((tour.getWaypoint(stage).near(loc))) {
                    // if we are near the next waypoint
                    if ((tour.getNumberOfWaypoints() >= stage)) {
                        // and not already at the last waypoint, increase ou
                        stage++;
                    }
                }
            }
            return Status.OK;
        }
        return new Status.Error("Cannot start following a tour while not in browse tours mode");
    }
    @Override
    public Status endSelectedTour() {
        if (mode == Mode.FollowTour){
            mode = Mode.BrowseTours;
            browseDetails = false;
            return Status.OK;
        }
        return new Status.Error("Cannot stop following a tour while not in follow tour mode");
    }

    //--------------------------
    // Multi-mode methods
    //--------------------------
    @Override
    public void setLocation(double easting, double northing) {
    	loc = new Location(easting, northing);
    }

    @Override
    public List<Chunk> getOutput() {
    	List<Chunk> chunk_list = new ArrayList<Chunk>();
    	if (mode == Mode.CreateTour) {
    		chunk_list.add(new Chunk.CreateHeader(tour.title, tour.getNumberOfLegs(), tour.getNumberOfWaypoints()));
    	}
    	if (mode == Mode.FollowTour) {
    		chunk_list.add(new Chunk.FollowHeader(tour.title, stage, tour.getNumberOfWaypoints()));
    		chunk_list.add(new Chunk.FollowWaypoint(tour.getWaypoint(stage).note));
    		chunk_list.add(new Chunk.FollowLeg(tour.getLeg(stage).note));
    		chunk_list.add(new Chunk.FollowBearing(disp.bearing(), disp.distance()));
    	}
    	if (mode == Mode.BrowseTours) {
    		LinkedHashMap<String,Tour> tour_lib;
    		tour_lib = lib.get_tour_lib();
    		Chunk.BrowseOverview browse_tours = new Chunk.BrowseOverview();
    		for(Map.Entry<String, Tour> entry: tour_lib.entrySet()) {
    			browse_tours.addIdAndTitle(entry.getKey(), entry.getValue().title);
    		}
    		chunk_list.add(browse_tours);
    		chunk_list.add(new Chunk.BrowseDetails(tour.id, tour.title, tour.annotation));
    	}
        return chunk_list;
    }


}
