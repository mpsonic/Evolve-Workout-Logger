import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.ExerciseSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementData;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing for the ExerciseSession class
 *
 * Created by Mitchell on 5/25/2016.
 */

@RunWith(JUnit4.class)
public class ExerciseSessionTest {

    private Exercise exercise;
    private ExerciseSession session;


    @Before
    public void setUp() throws Exception {
        exercise = new Exercise();
        exercise.setName("testExercise");
        exercise.trackNewMeasurementCategory(MeasurementCategory.REPS);
        exercise.trackNewMeasurementCategory(MeasurementCategory.WEIGHT);
        session = new ExerciseSession(exercise, true);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructor() throws Exception {
        assertEquals(session.getNumSets(), 1);

        ExerciseSession secondSession = new ExerciseSession(exercise, false);
        assertEquals(secondSession.getNumSets(), 0);
    }

    @Test
    public void testGenerateNewSet() throws Exception {

        Set compareSet = new Set();
        compareSet.addMeasurement(new MeasurementData(
                MeasurementCategory.REPS,
                MeasurementCategory.REPS.getDefaultMeasurement()
        ));
        compareSet.addMeasurement(new MeasurementData(
                MeasurementCategory.WEIGHT,
                MeasurementCategory.WEIGHT.getDefaultMeasurement()
        ));

        session.generateNewSet();
        Set sessionSet = session.getSet(session.getNumSets()-1);
        assertTrue(compareSet.equals(sessionSet));

        sessionSet.incrementMeasurement(MeasurementCategory.WEIGHT, 5);
        Set newCompareSet = new Set();
        newCompareSet.copyMeasurementInfo(sessionSet);

        session.generateNewSet();
        Set newSessionSet = session.getSet(session.getNumSets()-1);

        assertTrue(newCompareSet.equals(newSessionSet));
    }

    @Test
    public void testEquals() throws Exception {
        ExerciseSession secondSession = new ExerciseSession(exercise, true);
        assertTrue(secondSession.equals(session));

        session.generateNewSet();
        session.generateNewSet();
        session.generateNewSet();

        secondSession.generateNewSet();
        secondSession.generateNewSet();
        secondSession.generateNewSet();

        assertTrue(secondSession.equals(session));
    }

    @Test
    public void testGetSetProgress() throws Exception {
        assertEquals(session.getSetProgress(), 0);

        session.generateNewSet();
        session.generateNewSet();

        assertEquals(session.getSetProgress(), 0);
        assertFalse(session.isCompleted());
        session.completeCurrentSetAndMoveToNext();
        assertEquals(session.getSetProgress(), 33);
        assertFalse(session.isCompleted());
        session.completeCurrentSetAndMoveToNext();
        assertEquals(session.getSetProgress(), 66);
        assertFalse(session.isCompleted());
        session.completeCurrentSetAndMoveToNext();
        assertEquals(session.getSetProgress(), 100);
        assertTrue(session.isCompleted());
    }

    @Test
    public void testCompleteCurrentSetAndMoveToNext() throws Exception {
        session = new ExerciseSession(exercise, false);
        session.completeCurrentSetAndMoveToNext();
        assertEquals(session.getSetProgress(), 0);
        assertFalse(session.isCompleted());

        session.generateNewSet();
        session.completeCurrentSetAndMoveToNext();
        assertEquals(session.getSetProgress(), 100);
        assertTrue(session.isCompleted());

        session.completeCurrentSetAndMoveToNext();
        assertTrue(session.getCurrentSetIndex() != 0);
        assertEquals(session.getSetProgress(), 100);
        assertTrue(session.isCompleted());

        session.generateNewSet();
        assertEquals(session.getSetProgress(), 50);
        assertFalse(session.isCompleted());

        session.completeCurrentSetAndMoveToNext();
        assertEquals(session.getSetProgress(), 100);
        assertTrue(session.isCompleted());
    }

    @Test
    public void testCopySetInfo() throws Exception {
        Exercise newExercise = new Exercise();
        ExerciseSession newSession = new ExerciseSession(newExercise, false);
        session.generateNewSet();
        session.generateNewSet();
        session.generateNewSet();

        newSession.copySetInfo(session);
        assertTrue(newSession.equals(session));
    }

    @Test
    public void testCopySetInfoAndIncrement() throws Exception {
        Exercise newExercise = new Exercise();
        ExerciseSession newSession = new ExerciseSession(newExercise, false);
        session.generateNewSet();
        session.generateNewSet();

        newSession.copySetInfoAndIncrement(session, MeasurementCategory.REPS, 1);
        session.getSet(0).incrementMeasurement(MeasurementCategory.REPS, 1);
        session.getSet(1).incrementMeasurement(MeasurementCategory.REPS, 1);
        session.getSet(2).incrementMeasurement(MeasurementCategory.REPS, 1);

        assertTrue(session.equals(newSession));
    }
}
