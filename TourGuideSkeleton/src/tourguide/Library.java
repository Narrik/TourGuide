package tourguide;
import java.util.*;
public class Library {
	HashMap<String,Tour> tour_lib = new HashMap<String,Tour>();
	public void addTour(Tour tour){
		tour_lib.put(tour.id,tour);
	}
}
