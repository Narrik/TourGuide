/**
 * 
 */
package tourguide;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    public Location location;
    
    public ControllerImp(double waypointRadius, double waypointSeparation) {
    }

    //--------------------------
    // Create tour mode
    //--------------------------

    // Some examples are shown below of use of logger calls.  The rest of the methods below that correspond 
    // to input messages could do with similar calls.
    
    @Override
    public Status startNewTour(String id, String title, Annotation annotation) {
        logger.fine(startBanner("startNewTour"));
        if (mode == Mode.BrowseTours){
            tour = new Tour(id,title,annotation);
            mode = Mode.CreateTour;
            return Status.OK;
        }
        return new Status.Error("Cannot start a new tour while not in browse tours mode");
    }

    @Override
    public Status addWaypoint(Annotation annotation) {
        logger.fine(startBanner("addWaypoint"));
        if (mode == Mode.CreateTour) {
            if (tour.getNumberOfWaypoints() > tour.getNumberOfLegs()){
                tour.addLeg(Annotation.getDefault());
            }
            tour.addWaypoint(location,annotation);
            return Status.OK;
        }
        return new Status.Error("Cannot add a waypoint while not in create tour mode");
    }

    @Override
    public Status addLeg(Annotation annotation) {
        logger.fine(startBanner("addLeg"));
        if (mode == Mode.CreateTour){
            if (tour.getNumberOfWaypoints() > tour.getNumberOfLegs()) {
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
        return new Status.Error("unimplemented");
    }

    //--------------------------
    // Browse tours mode
    //--------------------------

    @Override
    public Status showTourDetails(String tourID) {
        return new Status.Error("unimplemented");
    }
  
    @Override
    public Status showToursOverview() {
        return new Status.Error("unimplemented");
    }

    //--------------------------
    // Follow tour mode
    //--------------------------
    
    @Override
    public Status followTour(String id) {
        return new Status.Error("unimplemented");
    }

    @Override
    public Status endSelectedTour() {
        return new Status.Error("unimplemented");
    }

    //--------------------------
    // Multi-mode methods
    //--------------------------
    @Override
    public void setLocation(double easting, double northing) {
    }

    @Override
    public List<Chunk> getOutput() {
        return new ArrayList<Chunk>();
    }


}
