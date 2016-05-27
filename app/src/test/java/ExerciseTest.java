import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.ExerciseSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementData;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Set;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Unit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Testing the Exercise class
 *
 * Created by Mitchell on 1/4/2016.
 */
public class ExerciseTest {

    private Exercise exercise;

    @Before
    public void setUp() throws Exception {
        exercise = new Exercise();
        exercise.setIncrement(1);
        exercise.setMeasurementCategoryToIncrement(MeasurementCategory.WEIGHT);
        exercise.trackNewMeasurementCategory(MeasurementCategory.DISTANCE);
        exercise.trackNewMeasurementCategory(MeasurementCategory.WEIGHT);
        exercise.setInitialMeasurementValue(MeasurementCategory.WEIGHT, 25);
        exercise.setInitialMeasurementValue(MeasurementCategory.DISTANCE, 100);
        exercise.setUnit(MeasurementCategory.WEIGHT, Unit.POUNDS);
        exercise.setUnit(MeasurementCategory.DISTANCE, Unit.METERS);
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testGenerateInitialSet() throws Exception {
        Set compareSet = new Set();
        compareSet.addMeasurement( new MeasurementData(
                MeasurementCategory.WEIGHT,
                25
        ));
        compareSet.addMeasurement( new MeasurementData(
                MeasurementCategory.DISTANCE,
                100
        ));

        Set generatedSet = exercise.generateInitialSet();
        assertTrue(generatedSet.equals(compareSet));
    }

    @Test
    public void testCreateNewExerciseSession() throws Exception {
        exercise.setMeasurementCategoryToIncrement(null);
        ExerciseSession firstSession = exercise.createNewExerciseSession();
        assertEquals(firstSession.getNumSets(), 1);

        ExerciseSession secondSession = exercise.createNewExerciseSession();
        assertTrue(firstSession.equals(secondSession));

        exercise.setMeasurementCategoryToIncrement(MeasurementCategory.WEIGHT);
        secondSession = exercise.createNewExerciseSession();
        firstSession.getSet(0).incrementMeasurement(MeasurementCategory.WEIGHT,1);

        assertTrue(firstSession.equals(secondSession));

        secondSession.generateNewSet();
        secondSession.generateNewSet();

        ExerciseSession thirdSession = exercise.createNewExerciseSession();
        secondSession.getSet(0).incrementMeasurement(MeasurementCategory.WEIGHT,1);
        secondSession.getSet(1).incrementMeasurement(MeasurementCategory.WEIGHT,1);
        secondSession.getSet(2).incrementMeasurement(MeasurementCategory.WEIGHT,1);

        assertEquals(thirdSession.getNumSets(), 3);
        assertTrue(secondSession.equals(thirdSession));
    }
}
