package tourguide;

import java.util.*;

public class Library {
    LinkedHashMap<String, Tour> tour_lib = new LinkedHashMap<String, Tour>();

    public void addTour(Tour tour) {
        tour_lib.put(tour.id, tour);
    }

    public LinkedHashMap<String, Tour> get_tour_lib() {
        return tour_lib;
    }
}
