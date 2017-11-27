package tourguide;

import java.util.logging.Logger;

public class Leg {
    Annotation note;
    private static Logger logger = Logger.getLogger("tourguide");

    Leg(Annotation n) {
        logger.finer("Annotation: " + n);
        note = n;
    }
}
