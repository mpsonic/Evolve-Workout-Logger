import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementData;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Set;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Unit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Testing the Set class
 *
 * Created by Mitchell on 1/4/2016.
 */
public class SetTest {

    @Test
    public void testSetfunctions(){
        Set test = new Set();
        test.add_measurement(new MeasurementData(MeasurementCategory.TIME, Unit.HOURS, 10));
        test.add_measurement(new MeasurementData(MeasurementCategory.DISTANCE, Unit.MILES, 1));
        test.add_measurement(new MeasurementData(MeasurementCategory.WEIGHT, Unit.POUNDS, 100));
        test.add_measurement(new MeasurementData(MeasurementCategory.REPS, Unit.REPS, 8));

        double values[] = {10, 1, 100, 8};

        // Testing increment_measurement
        for(int i = 0; i<4; i++){
            test.increment_measurement(i, 2.5);
            values[i] += 2.5;
            assertEquals(test.getMeasurementData(i).getMeasurement(), values[i]);
        }

        // Testing decrement_measurement
        for(int i = 0; i<4; i++){
            test.decrement_measurement(i, 2.5);
            values[i] -= 2.5;
            assertEquals(test.getMeasurementData(i).getMeasurement(), values[i]);
        }

        // Testing copyMeasurementInfo function
        Set Set1 = new Set();
        Set1.copyMeasurementInfo(test);
        assertTrue(Set1.equals(test));

        // Testing copyMeasurementInfoAndIncrement function
        Set Set2 = new Set();
        Set2.copyMeasurementInfoAndIncrement(test, MeasurementCategory.REPS, 1);
        test.increment_measurement(3, 1);
        assertTrue(Set2.equals(test));

    }
}
