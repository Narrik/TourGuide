package tourguide;
import java.util.*;
public class Library {
	HashMap<String,Tour> tour_lib = new HashMap<String,Tour>();
	public void addTour(String id, String title, Annotation note, Tour tour){
		tour_lib.put(id,tour);
	}
}
