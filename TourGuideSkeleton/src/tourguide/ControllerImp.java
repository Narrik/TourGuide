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
        return LS
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
    public Waypoint currWaypoint;

    public ControllerImp(double waypointRadius, double waypointSeparation) {
        this.lib = new Library();
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
        if (mode == Mode.BrowseTours) {
            mode = Mode.CreateTour;
            logger.finer("Entering Create Tour mode");
            tour = new Tour(id, title, annotation);
            return Status.OK;
        }
        logger.warning("Cannot start a new tour while not browse tours mode");
        return new Status.Error("Cannot start a new tour while not in browse tours mode");
    }

    @Override
    public Status addWaypoint(Annotation ann) {
        logger.fine(startBanner("addWaypoint"));
        if (mode == Mode.CreateTour) {
            if (tour.getNumberOfWaypoints() > 0) {
                logger.finer("Checking if adjacent waypoints aren't too close together");
                if ((loc.deltaFrom(currWaypoint.location).distance()) <= waypointSeparation) {
                    logger.warning("Cannot add adjacent waypoints so close together");
                    return new Status.Error("Cannot add adjacent waypoints so close together");
                } else {
                    logger.finer("Distance between adjacent waypoints is sufficient");
                }
            }
            if (tour.getNumberOfWaypoints() == tour.getNumberOfLegs()) {
                logger.finer("Adding a default leg annotation");
                tour.addLeg(Annotation.getDefault());
            }
            tour.addWaypoint(ann, waypointRadius, loc);
            currWaypoint = tour.getWaypoint(tour.getNumberOfWaypoints() - 1);
            return Status.OK;
        }
        logger.warning("Cannot add a waypoint while not in create tour mode");
        return new Status.Error("Cannot add a waypoint while not in create tour mode");
    }

    @Override
    public Status addLeg(Annotation annotation) {
        logger.fine(startBanner("addLeg"));
        if (mode == Mode.CreateTour) {
            if (tour.getNumberOfWaypoints() == tour.getNumberOfLegs()) {
                tour.addLeg(annotation);
                return Status.OK;
            } else {
                logger.warning("Cannot add more than one leg to a waypoint");
                return new Status.Error("Cannot add more than one leg to a waypoint");
            }

        }
        logger.warning("Cannot add a leg while not in create tour mode");
        return new Status.Error("Cannot add a leg while not in create tour mode");
    }

    @Override
    public Status endNewTour() {
        logger.fine(startBanner("endNewTour"));
        if (mode == Mode.CreateTour) {
            if (tour.getNumberOfWaypoints() == 0) {
                logger.warning("Must add at least one waypoint to create a tour");
                return new Status.Error("Must add at least one waypoint to create a tour");
            }
            if (tour.getNumberOfWaypoints() == tour.getNumberOfLegs()) {
                lib.addTour(tour);
                mode = Mode.BrowseTours;
                browseDetails = false;
                logger.finer("Entering Browse Tours mode, tour overview submode");
                return Status.OK;
            } else {
                logger.warning("A tour must have the same amount of waypoints and legs");
                return new Status.Error("A tour must have the same amount of waypoints and legs");
            }
        }
        logger.warning("Cannot end a tour while not in create a tour mode");
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
                if (!lib.get_tour_lib().containsKey(tourID)) {
                    logger.warning("Unrecognized tour " + tourID);
                    return new Status.Error("Unrecognized tour " + tourID);
                }
                browseDetails = true;
                tour = lib.get_tour_lib().get(tourID);
                logger.finer("Viewing details of tour " + tourID);
                return Status.OK;
            } else {
                logger.warning("Already viewing details of a tour");
                return new Status.Error("Already viewing details of a tour");
            }
        }
        logger.warning("Cannot view details of a tour while not in browse tours mode");
        return new Status.Error("Cannot view details of a tour while not in browse tours mode");
    }

    @Override
    public Status showToursOverview() {
        logger.fine(startBanner("showToursOverview"));
        if (mode == Mode.BrowseTours) {
            if (browseDetails) {
                browseDetails = false;
                return Status.OK;
            } else {
                logger.warning("Already in tour overview");
                return new Status.Error("Already in tour overview");
            }
        }
        logger.warning("Cannot view tour overview while not in browse tours mode");
        return new Status.Error("Cannot view tour overview while not in browse tours mode");
    }

    //--------------------------
    // Follow tour mode
    //--------------------------

    @Override
    public Status followTour(String id) {
        logger.fine(startBanner("followTour"));
        if (mode == Mode.BrowseTours) {
            if (!(lib.get_tour_lib().containsKey(id))) {
                logger.warning("Unrecognized tour " + id);
                return new Status.Error("Unrecognized tour " + id);
            }
            mode = Mode.FollowTour;
            logger.finer("Entering Follow Tour mode");
            tour = lib.get_tour_lib().get(id);
            return Status.OK;
        }
        logger.warning("Cannot start following a tour while not in browse tours mode");
        return new Status.Error("Cannot start following a tour while not in browse tours mode");
    }

    @Override
    public Status endSelectedTour() {
        if (mode == Mode.FollowTour) {
            mode = Mode.BrowseTours;
            logger.finer("Entering Browse Tours mode, tour overview submode");
            browseDetails = false;
            return Status.OK;
        }
        logger.warning("Cannot stop following a tour while not in follow tour mode");
        return new Status.Error("Cannot stop following a tour while not in follow tour mode");
    }

    //--------------------------
    // Multi-mode methods
    //--------------------------
    @Override
    public void setLocation(double easting, double northing) {
        logger.fine(startBanner("setLocation"));
        loc = new Location(easting, northing);
        if (mode == Mode.FollowTour) {
            if (stage < tour.getNumberOfWaypoints()) {
                // if we are not at the last stage
                if (tour.getWaypoint(stage).near(loc)) {
                    // if we are near the next waypoint, increase stage
                    stage++;
                }
            }
            if (stage < tour.getNumberOfWaypoints()) {
                disp = loc.deltaFrom(tour.getWaypoint(stage).location);
            } else {
                disp = loc.deltaFrom(tour.getWaypoint(stage - 1).location);
            }
        }
    }

    @Override
    public List<Chunk> getOutput() {
        List<Chunk> chunk_list = new ArrayList<Chunk>();
        if (mode == Mode.CreateTour) {
            chunk_list.add(new Chunk.CreateHeader(tour.title, tour.getNumberOfLegs(), tour.getNumberOfWaypoints()));
        }
        if (mode == Mode.FollowTour) {
            chunk_list.add(new Chunk.FollowHeader(tour.title, stage, tour.getNumberOfWaypoints()));
            if (stage != 0) {
                if (tour.getWaypoint(stage - 1).near(loc)) {
                    // if we are near a waypoint, get information for waypoint
                    chunk_list.add(new Chunk.FollowWaypoint(tour.getWaypoint(stage - 1).note));
                }
            }
            if (stage < tour.getNumberOfWaypoints()) {
                // if we are not at the last stage
                chunk_list.add(new Chunk.FollowLeg(tour.getLeg(stage).note));
                chunk_list.add(new Chunk.FollowBearing(disp.bearing(), disp.distance()));
            }

        }
        if (mode == Mode.BrowseTours) {
            LinkedHashMap<String, Tour> tour_lib;
            if (!browseDetails) {
                Chunk.BrowseOverview browse_tours = new Chunk.BrowseOverview();
                tour_lib = lib.get_tour_lib();
                for (Map.Entry<String, Tour> entry : tour_lib.entrySet()) {
                    browse_tours.addIdAndTitle(entry.getKey(), entry.getValue().title);
                }
                chunk_list.add(browse_tours);
            } else {
                chunk_list.add(new Chunk.BrowseDetails(tour.id, tour.title, tour.annotation));
            }
        }
        return chunk_list;
    }


}
