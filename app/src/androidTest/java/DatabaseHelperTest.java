import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.ServiceTestCase;

import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.ExerciseSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.RoutineSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

/**
 * Testing for the DatabaseHelper class
 *
 * Created by Mitchell on 5/25/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest extends AndroidTestCase {

    private DatabaseHelper db;
    private Routine routine1;
    private Exercise exercise1;
    private RoutineSession rSession1;
    private ExerciseSession eSession1;

    /**
     * @return The {@link Context} of the test project.
     */
    private Context getTestContext()
    {
        try
        {
            Method getTestContext = ServiceTestCase.class.getMethod("getTestContext");
            return (Context) getTestContext.invoke(this);
        }
        catch (final Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Context context = InstrumentationRegistry.getTargetContext();
        RenamingDelegatingContext renamingDelegatingContext = new RenamingDelegatingContext(context, "test_");
        setContext(renamingDelegatingContext);

        db = DatabaseHelper.getInstance(renamingDelegatingContext);

        routine1 = new Routine();
        routine1.setName("routine1");
        exercise1 = new Exercise();
        exercise1.setName("exercise1");
        exercise1.trackNewMeasurementCategory(MeasurementCategory.REPS);
        exercise1.trackNewMeasurementCategory(MeasurementCategory.TIME);
        routine1.addExercise(exercise1);

        rSession1 = routine1.createNewRoutineSession();
        eSession1 = rSession1.getExerciseSession(0);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        getContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }

    @Test
    public void testGetLastRoutineSession() throws Exception {
        String exerciseName = db.insertExercise(exercise1, true);
        String routineName = db.insertRoutine(routine1, true);
        int rSessionId = db.insertRoutineSession(rSession1);
        RoutineSession rSession2 = db.getLastRoutineSession(routine1);
        assertTrue(rSession1.equals(rSession2));
    }

    @Test
    public void testInsertExercise() throws Exception {
        String exerciseName = db.insertExercise(exercise1, true);
    }

    @Test
    public void testInsertRoutine() throws Exception {
        String routineName = db.insertRoutine(routine1, true);
    }

    @Test
    public void testInsertRoutineSession() throws Exception {
        int rSessionId = db.insertRoutineSession(rSession1);
    }
}
