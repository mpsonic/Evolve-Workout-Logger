import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementData;

/**
 *
 * Testing the MeasurementData class
 *
 * Created by Mitchell on 1/4/2016.
 */
public class MeasurementDataTest {

    private MeasurementData data;

    @Before
    public void setUp() {
        data = new MeasurementData(MeasurementCategory.REPS, 10);
    }

    @Test
    public void testConstructor(){
        Assert.assertEquals(MeasurementCategory.REPS, data.getCategory());
        Assert.assertEquals(10.0, data.getMeasurement(), 10-data.getMeasurement());
    }

    @Test
    public void testEquals() {
        setUp();
        MeasurementData equalData = new MeasurementData(MeasurementCategory.REPS, 10);
        Assert.assertTrue(data.equals(equalData));
        Assert.assertFalse(data.equals(new MeasurementData()));
    }

    @Test
    public void testCopyData() {
        setUp();
        MeasurementData equalData = new MeasurementData();
        equalData.copyData(data);
        Assert.assertTrue(equalData.equals(data));
    }
}