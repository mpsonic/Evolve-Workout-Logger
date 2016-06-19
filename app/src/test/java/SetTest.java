import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementData;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * Testing the Set class
 *
 * Created by Mitchell on 1/4/2016.
 */
public class SetTest {

    private Set set;
    private MeasurementData repsData;
    private MeasurementData weightData;
    private MeasurementData distanceData;
    private MeasurementData timeData;

    @Before
    public void setUp() {
        set = new Set();
        repsData = new MeasurementData(MeasurementCategory.REPS, 8);
        weightData = new MeasurementData(MeasurementCategory.WEIGHT, 50);
        distanceData = new MeasurementData(MeasurementCategory.DISTANCE, (float)5.5);
        timeData = new MeasurementData(MeasurementCategory.TIME, 300);
        set.addMeasurement(repsData);
        set.addMeasurement(weightData);
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testGetNumMeasurements() {
        assertEquals(2, set.getNumMeasurements());
    }

    @Test
    public void testAddMeasurement() {
        set.addMeasurement(distanceData);
        assertEquals(3, set.getNumMeasurements());
    }

    @Test
    public void testDeleteMeasurement() throws Exception {
        set.deleteMeasurement(MeasurementCategory.REPS);
        assertEquals(1, set.getNumMeasurements());
    }

    @Test
    public void testGetMeasurementData() throws Exception {
        MeasurementData getRepsData = set.getMeasurementData(MeasurementCategory.REPS);
        MeasurementData getWeightData = set.getMeasurementData(MeasurementCategory.WEIGHT);
        MeasurementData getTimeData = set.getMeasurementData(MeasurementCategory.TIME);
        assertTrue(repsData.equals(getRepsData));
        assertTrue(weightData.equals(getWeightData));
        assertTrue(getTimeData == null);
    }

    @Test
    public void testEquals() throws Exception {
        Set equalSet = new Set();
        equalSet.addMeasurement(repsData);
        equalSet.addMeasurement(weightData);

        Set differentSet = new Set();

        assertTrue(equalSet.equals(set));
        assertFalse(differentSet.equals(set));
    }

    @Test
    public void testCopyMeasurementInfo() throws Exception {
        Set equalSet = new Set();
        equalSet.copyMeasurementInfo(set);
        assertTrue(equalSet.equals(set));
    }

    @Test
    public void testCopyMeasurementInfoAndIncrement() throws Exception {
        Set incrementSet = new Set();
        incrementSet.copyMeasurementInfoAndIncrement(set, MeasurementCategory.REPS, 2);
        set.incrementMeasurement(MeasurementCategory.REPS, 2);
        assertTrue(incrementSet.equals(set));
    }
}
