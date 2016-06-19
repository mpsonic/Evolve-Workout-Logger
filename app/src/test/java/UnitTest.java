import org.junit.Assert;
import org.junit.Test;

import edu.umn.paull011.evolveworkoutlogger.data_structures.Unit;

/**
 * Testing the Unit enumeration
 *
 * Created by Mitchell on 1/4/2016.
 */
public class UnitTest {
    @Test
    public void testUnits(){
        Unit reps = Unit.REPS;
        Unit kilograms = Unit.KILOGRAMS;
        Unit meters = Unit.METERS;
        Unit kilometers = Unit.KILOMETERS;
        Unit pounds = Unit.POUNDS;
        Unit feet = Unit.FEET;
        Unit miles = Unit.MILES;
        Unit time = Unit.TIME;
        Unit seconds = Unit.SECONDS;
        Unit minutes = Unit.MINUTES;
        Unit hours = Unit.HOURS;

        Assert.assertTrue(reps.isUniversal());
        Assert.assertFalse(kilograms.isUniversal());
        Assert.assertFalse(meters.isUniversal());
        Assert.assertFalse(kilometers.isUniversal());
        Assert.assertFalse(pounds.isUniversal());
        Assert.assertFalse(feet.isUniversal());
        Assert.assertFalse(miles.isUniversal());
        Assert.assertTrue(time.isUniversal());
        Assert.assertTrue(seconds.isUniversal());
        Assert.assertTrue(minutes.isUniversal());
        Assert.assertTrue(hours.isUniversal());

        Assert.assertFalse(kilograms.isImperial());
        Assert.assertFalse(meters.isImperial());
        Assert.assertFalse(kilometers.isImperial());
        Assert.assertTrue(pounds.isImperial());
        Assert.assertTrue(feet.isImperial());
        Assert.assertTrue(miles.isImperial());
    }
}
