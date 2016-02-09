import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.ExerciseSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Unit;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * Testing Google's gson library for converting objects into json code
 *
 * Created by Mitchell on 1/6/2016.
 */
public class JsonObjectTest {
    @Test
    public void testJsonObject(){
        Exercise ex = new Exercise();
        ex.setName("test");

        ex.trackNewMeasurementCategory(MeasurementCategory.REPS);
        ex.trackNewMeasurementCategory(MeasurementCategory.WEIGHT);

        try{
            ex.setTrackedMeasurementUnit(MeasurementCategory.REPS, Unit.REPS);
            ex.setTrackedMeasurementUnit(MeasurementCategory.WEIGHT, Unit.POUNDS);

            ex.setTrackedMeasurementValue(MeasurementCategory.REPS, 10);
            ex.setTrackedMeasurementValue(MeasurementCategory.WEIGHT, 100);
        }catch(Exception exception){
        }

        ex.setGoalIncrease(MeasurementCategory.REPS, 1);
        ex.setGoalIncrease(MeasurementCategory.WEIGHT, 5);

        ex.createNewExerciseSession(false);
        ExerciseSession session = ex.getCurrentExerciseSession();
        session.generateNewSet();
        session.generateNewSet();
        session.generateNewSet();

        ex.createNewExerciseSession(false);
        ExerciseSession nextSession = ex.getCurrentExerciseSession();
        Assert.assertTrue(nextSession.equals(session));

        ex.setCategoryToIncrement(MeasurementCategory.WEIGHT);
        ex.createNewExerciseSession(true);

        Gson gson = new Gson();
        String json = gson.toJson(ex);
        Exercise newExercise = gson.fromJson(json, Exercise.class);
    }

}
