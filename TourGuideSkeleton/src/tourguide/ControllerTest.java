/**
 * 
 */
package tourguide;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author pbj
 *
 */
public class ControllerTest {

    private Controller controller;
    private static final double WAYPOINT_RADIUS = 10.0;
    private static final double WAYPOINT_SEPARATION = 25.0;
   
    // Utility methods to help shorten test text.
    private static Annotation ann(String s) { return new Annotation(s); }
    private static void checkStatus(Status status) { 
        Assert.assertEquals(Status.OK, status);
    }
    private static void checkStatusNotOK(Status status) { 
        Assert.assertNotEquals(Status.OK, status);
    }
    private void checkOutput(int numChunksExpected, int chunkNum, Chunk expected) {
        List<Chunk> output = controller.getOutput();
        Assert.assertEquals("Number of chunks", numChunksExpected, output.size());
        Chunk actual = output.get(chunkNum);
        Assert.assertEquals(expected, actual);  
    }
    
    
    /*
     * Logging functionality
     */
    
    // Convenience field.  Saves on getLogger() calls when logger object needed.
    private static Logger logger;
    
    // Update this field to limit logging.
    public static Level loggingLevel = Level.ALL;
    
    private static final String LS = System.lineSeparator();

    @BeforeClass
    public static void setupLogger() {
         
        logger = Logger.getLogger("tourguide"); 
        logger.setLevel(loggingLevel);
        
        // Ensure the root handler passes on all messages at loggingLevel and above (i.e. more severe)
        Logger rootLogger = Logger.getLogger("");
        Handler handler = rootLogger.getHandlers()[0];
        handler.setLevel(loggingLevel);
    }

    private String makeBanner(String testCaseName) {
        return  LS 
          + "#############################################################" + LS
          + "TESTCASE: " + testCaseName + LS
          + "#############################################################";
    }


    
    @Before
    public void setup() {
        controller = new ControllerImp(WAYPOINT_RADIUS, WAYPOINT_SEPARATION);
    }
    
    @Test
    public void noTours() {  
        logger.info(makeBanner("noTours"));
        
        checkOutput(1, 0, new Chunk.BrowseOverview() );
     }
    
    // Locations roughly based on St Giles Cathedral reference.
    
    private void addOnePointTour() {
        
        checkStatus( controller.startNewTour(
                "T1", 
                "Informatics at UoE", 
                ann("The Informatics Forum and Appleton Tower\n"))
                );
        
        checkOutput(1, 0, new Chunk.CreateHeader("Informatics at UoE", 0,  0));
      
        controller.setLocation(300, -500);
  
        checkStatus( controller.addLeg(ann("Start at NE corner of George Square\n")) );
       
        checkOutput(1, 0, new Chunk.CreateHeader("Informatics at UoE", 1,  0));
        
        checkStatus( controller.addWaypoint(ann("Informatics Forum")) );     
        
        checkOutput(1, 0, new Chunk.CreateHeader("Informatics at UoE", 1,  1));
  
        checkStatus( controller.endNewTour() );
        
    }
    
    @Test
    public void testAddOnePointTour() { 
        logger.info(makeBanner("testAddOnePointTour"));
        
        addOnePointTour(); 
    }
    

    private void addTwoPointTour() {
         checkStatus(
                controller.startNewTour("T2", "Old Town", ann("From Edinburgh Castle to Holyrood\n"))
                );
        
        checkOutput(1, 0, new Chunk.CreateHeader("Old Town", 0,  0));
      
        controller.setLocation(-500, 0);
        
        // Leg before this waypoint with default annotation added at same time
        checkStatus( controller.addWaypoint(ann("Edinburgh Castle\n")) );     
        
        checkOutput(1, 0, new Chunk.CreateHeader("Old Town", 1,  1));
  
        checkStatus( controller.addLeg(ann("Royal Mile\n")) );
  
        checkOutput(1, 0, new Chunk.CreateHeader("Old Town", 2,  1) );
      
        checkStatusNotOK( 
                controller.endNewTour()
                );
  
        controller.setLocation(1000, 300);
               
        checkStatus( controller.addWaypoint(ann("Holyrood Palace\n")) );

        checkOutput(1, 0, new Chunk.CreateHeader("Old Town", 2,  2) );
  
        checkStatus( controller.endNewTour() );
        
    }
    
    @Test
    public void testAddTwoPointTour() { 
        logger.info(makeBanner("testAddTwoPointTour"));
       
        addTwoPointTour(); 
    }
    
    @Test
    public void testAddOfTwoTours() {
        logger.info(makeBanner("testAddOfTwoTour"));
        
        addOnePointTour();
        addTwoPointTour();
    }
    
    @Test
    public void browsingTwoTours() {
        logger.info(makeBanner("browsingTwoTours"));
        
        addOnePointTour();
        addTwoPointTour();
 
        Chunk.BrowseOverview overview = new Chunk.BrowseOverview(); 
        overview.addIdAndTitle("T1", "Informatics at UoE");
        overview.addIdAndTitle("T2", "Old Town");
        checkOutput(1, 0, overview);
        
        checkStatusNotOK( controller.showTourDetails("T3") );
        checkStatus( controller.showTourDetails("T1") );
            
        checkOutput(1, 0, new Chunk.BrowseDetails(
                "T1", 
                "Informatics at UoE", 
                ann("The Informatics Forum and Appleton Tower\n")
                ));
    }
    
    @Test 
    public void followOldTownTour() {
        logger.info(makeBanner("followOldTownTour"));
       
        addOnePointTour();
        addTwoPointTour();

        checkStatus( controller.followTour("T2") );
        
        controller.setLocation(0.0, 0.0);
  
        checkOutput(3,0, new Chunk.FollowHeader("Old Town", 0, 2) );      
        checkOutput(3,1, new Chunk.FollowLeg(Annotation.DEFAULT));
        checkOutput(3,2, new Chunk.FollowBearing(270.0, 500.0));
         
        controller.setLocation(-490.0, 0.0);
      
        checkOutput(4,0, new Chunk.FollowHeader("Old Town", 1, 2) );  
        checkOutput(4,1, new Chunk.FollowWaypoint(ann("Edinburgh Castle\n")));
        checkOutput(4,2, new Chunk.FollowLeg(ann("Royal Mile\n")));
        checkOutput(4,3, new Chunk.FollowBearing(79.0, 1520.0));
 
        controller.setLocation(900.0, 300.0);
        
        checkOutput(3,0, new Chunk.FollowHeader("Old Town", 1, 2) );  
        checkOutput(3,1, new Chunk.FollowLeg(ann("Royal Mile\n")));
        checkOutput(3,2, new Chunk.FollowBearing(90.0, 100.0));
        
        controller.setLocation(1000.0, 300.0);
  
        checkOutput(2,0, new Chunk.FollowHeader("Old Town", 2, 2) );  
        checkOutput(2,1, new Chunk.FollowWaypoint(ann("Holyrood Palace\n")));
                      
        controller.endSelectedTour();
        
        Chunk.BrowseOverview overview = new Chunk.BrowseOverview(); 
        overview.addIdAndTitle("T1", "Informatics at UoE");
        overview.addIdAndTitle("T2", "Old Town");
        checkOutput(1, 0, overview);
    
    }
    
    @Test
    public void testTwoWaypointsClose() {
    	//JUnit test to check if Status raises an error when two waypoints are too close
    	logger.info(makeBanner("TwoWaypointsClose"));
    	
    	 checkStatus(
                 controller.startNewTour("T3", "Old Town", ann("From Edinburgh Castle to Holyrood\n"))
                 );
         
         checkOutput(1, 0, new Chunk.CreateHeader("Old Town", 0,  0));
       
         controller.setLocation(0, 1);
         
         // Leg before this waypoint with default annotation added at same time
         checkStatus( controller.addWaypoint(ann("Edinburgh Castle\n")) );     
         
         checkOutput(1, 0, new Chunk.CreateHeader("Old Town", 1,  1));
   
         controller.setLocation(0, 2);
                
         checkStatusNotOK( controller.addWaypoint(ann("Holyrood Palace\n")) );

         checkStatus( controller.endNewTour() );
    	
    }
    

    @Test
    public void testendTourTwice() {
    	//JUnit test to check if Status is not OK when two a tour is ended twice.
    	
    	 addOnePointTour();
         
         checkStatusNotOK( controller.endNewTour() );
    	
    }
    
    @Test
    public void teststartTourTwice() {
    	//JUnit test to check if Status is not OK when two a tour is created twice.
    	
    	 checkStatus( controller.startNewTour(
                 "T4", 
                 "Botanical Gardens", 
                 ann("The Botanical Gardems of Edinburgh\n"))
                 );
    	 checkStatusNotOK( controller.startNewTour(
                 "T5", 
                 "Princes Street", 
                 ann("The high street of the New Town\n"))
                 );
    	
    }
    
    @Test
    public void testaddLegTwice() {
    	//JUnit test to check if Status is not OK when two two legs are added consecutively.
    	
    	 checkStatus( controller.startNewTour(
                 "T4", 
                 "Botanical Gardens", 
                 ann("The Botanical gardens of Edinburgh\n"))
                 );
    	 checkStatus( controller.addWaypoint(ann("Princes Street\n")) );     
           
         checkOutput(1, 0, new Chunk.CreateHeader("Botanical Gardens", 1,  1));
     
         checkStatus( controller.addLeg(ann("St Andrews square\n")) );
     
         checkStatusNotOK( controller.addLeg(ann("National Gallery\n")) );
         
    }
    
  
    @Test
    public void testnoWaypoint() {
    	//JUit test to check if Status is not OK if user tries to follw tour without adding waypoints 
    	logger.info(makeBanner("TwoWaypointsClose"));
    	
    	 checkStatus(
                 controller.startNewTour("T2", "Old Town", ann("From Edinburgh Castle to Holyrood\n"))
                 );
         
         checkStatusNotOK(controller.followTour("T2"));
         
         checkStatusNotOK( controller.endNewTour() );
         
    }
    
    
    
    @Test
    public void testendsinLeg() {
    	//JUnit test to check if Status raises an error if the end point is a leg and not a waypoint
    	 checkStatus( controller.startNewTour(
                 "T1", 
                 "Informatics at UoE", 
                 ann("The Informatics Forum and Appleton Tower\n"))
                 );
         
         checkOutput(1, 0, new Chunk.CreateHeader("Informatics at UoE", 0,  0));
       
         controller.setLocation(300, -500);
   
         checkStatus( controller.addLeg(ann("Start at NE corner of George Square\n")) );
        
         checkOutput(1, 0, new Chunk.CreateHeader("Informatics at UoE", 1,  0));
         
         checkStatusNotOK(controller.followTour("T1"));
    	
    }
    
    @Test
    public void testfollowWhenCreate() {
    	//JUnit test to check if Status is not OK if user decides to follow a tour when in create mode
    	 checkStatus( controller.startNewTour(
                 "T4", 
                 "Botanical Gardens", 
                 ann("The Botanical gardens of Edinburgh\n"))
                 );
    	 checkStatus( controller.addWaypoint(ann("Princes Street\n")) );  
    	 
    	 checkStatusNotOK( controller.followTour("T4"));
    	
    }
   
    
  
    
}
