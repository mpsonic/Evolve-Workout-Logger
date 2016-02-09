import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;

import org.junit.Test;

/**
 *
 * Testing the DatabaseHelper class
 *
 * Created by Mitchell on 1/7/2016.
 */
public class DatabaseHelperTest {
    @Test
    public void testDatabaseHelper(){
        Exercise e1 = new Exercise();
        Exercise e2 = new Exercise();
        Exercise e3 = new Exercise();
        e1.setName("exercise 1");
        e2.setName("exercise 2");
        e3.setName("exercise 3");
        e1.trackNewMeasurementCategory(MeasurementCategory.REPS);
        e2.trackNewMeasurementCategory(MeasurementCategory.WEIGHT);
        e3.trackNewMeasurementCategory(MeasurementCategory.DISTANCE);
        e1.setGoalIncrease(MeasurementCategory.REPS, 1);
        e2.setGoalIncrease(MeasurementCategory.WEIGHT, 5);
        e3.setGoalIncrease(MeasurementCategory.DISTANCE, 0.5);
        e2.setMetricUnits();
    }
}
