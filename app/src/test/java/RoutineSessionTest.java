import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Routine;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing for the RoutineSession class
 *
 * Created by Mitchell on 5/25/2016.
 */
public class RoutineSessionTest {

    private Routine routine;
    private RoutineSession session;

    @Before
    public void setUp() throws Exception {
        routine = new Routine();
        routine.setName("routine");
        Exercise exercise1 = new Exercise();
        exercise1.trackNewMeasurementCategory(MeasurementCategory.REPS);
        Exercise exercise2 = new Exercise();
        exercise2.trackNewMeasurementCategory(MeasurementCategory.WEIGHT);
        routine.addExercise(exercise1);
        routine.addExercise(exercise2);
        session = routine.createNewRoutineSession();
    }

    @After
    public void tearDown() throws Exception {


    }

    @Test
    public void testEquals() throws Exception {
        RoutineSession secondSession = routine.createNewRoutineSession();
        assertTrue(session.equals(secondSession));

        secondSession.removeExerciseSession(1);
        assertFalse(session.equals(secondSession));


    }
}
