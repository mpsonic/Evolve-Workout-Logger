import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementData;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Unit;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * Testing the MeasurementData class
 *
 * Created by Mitchell on 1/4/2016.
 */
public class MeasurementDataTest {
    @Test
    public void testConstructor(){
        MeasurementData test = new MeasurementData(MeasurementCategory.REPS, Unit.REPS, 10);
        Assert.assertEquals(MeasurementCategory.REPS, test.getCategory());
        Assert.assertEquals(Unit.REPS, test.getUnit());
        Assert.assertEquals(10.0, test.getMeasurement());
    }
}
