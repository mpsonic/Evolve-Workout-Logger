package edu.umn.paull011.evolveworkoutlogger.activities;

/**
 * An enum of currently defined activity classes
 *
 * Created by Mitchell on 5/9/2016.
 */
public enum ActivityEnum {

    START_PAGE("StartPage", StartPage.class),
    START_ROUTINE("StartRoutine", StartRoutine.class),
    CREATE_ROUTINE("CreateRoutine", CreateRoutine.class),
    ADD_EXERCISE("AddExercise", AddExercise.class),
    CREATE_EXERCISE("CreateExercise", CreateExercise.class),
    ACTIVE_ROUTINE_SESSION("ActiveRoutineSession", ActiveRoutineSession.class),
    //ACTIVE_EXERCISE_SESSION("ActiveExerciseSession", ActiveExerciseSession.class),
    NONE("None", null);

    ActivityEnum(String name, Class c) {
        m_name = name;
        m_class = c;
    }

    public String getName() {
        return m_name;
    }

    public Class getActivityClass() {
        return m_class;
    }

    private final String m_name;
    private final Class m_class;

    public final static String SENDER = "Sender";
    public final static int REQUEST_NEW_ROUTINE = 1;
    public final static int REQUEST_NEW_EXERCISE = 2;
}
