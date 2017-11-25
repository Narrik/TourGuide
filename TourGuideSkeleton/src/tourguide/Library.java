package tourguide;
import java.util.*;
public class Library {
	LinkedHashMap<String,Tour> tour_lib = new LinkedHashMap<String,Tour>();
	public void addTour(Tour tour){
		tour_lib.put(tour.id,tour);
	}
}
