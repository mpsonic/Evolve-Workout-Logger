package edu.umn.paull011.evolveworkoutlogger.data_structures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.umn.paull011.evolveworkoutlogger.BuildConfig;

/**
 *
 * This class talks to the SQLite database and helps in storing
 * Routine and Exercise data for the user
 *
 * Created by Mitchell on 1/6/2016.
 */
public class DatabaseHelper extends SQLiteAssetHelper {

    // TODO: Implement way to store and sort exercises by category

    public static synchronized DatabaseHelper getInstance(Context context) {
        Log.d(TAG,"getInstance");
        if (mInstance == null) {
            if (context != null) {
                Context appContext = null;
                try {
                    if (context instanceof android.test.RenamingDelegatingContext) {
                        appContext = context;
                    }
                } catch (NoClassDefFoundError error) {
                    if (context.getApplicationContext() != null) {
                        appContext = context.getApplicationContext();
                    }
                    else {
                        appContext = context;
                    }
                }

                mInstance = new DatabaseHelper(appContext);
            }
        }
        else {
            mInstance.refreshDatabases();
        }
        return mInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG,"DatabaseHelper");
        writableDB = getWritableDatabase();
        readableDB = getReadableDatabase();
    }

    private void refreshDatabases() {
        Log.d(TAG,"refreshDatabases");
        writableDB.close();
        readableDB.close();
        writableDB = getWritableDatabase();
        readableDB = getReadableDatabase();
    }

    @Override
    public synchronized void close() {
        Log.d(TAG,"close");
        super.close();
        writableDB.close();
        readableDB.close();
    }

    /*@Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate");
        String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "(" +
                KEY_EXERCISE_NAME + " varchar(128) NOT NULL," +
                KEY_EXERCISE_DESCRIPTION + " TEXT," +
                KEY_EXERCISE_IS_REPS_TRACKED + " INT," +
                KEY_EXERCISE_IS_WEIGHT_TRACKED + " INT," +
                KEY_EXERCISE_IS_DISTANCE_TRACKED + " INT," +
                KEY_EXERCISE_IS_TIME_TRACKED + " INT," +
                KEY_EXERCISE_WEIGHT_UNIT + " varchar(16)," +
                KEY_EXERCISE_DISTANCE_UNIT + " varchar(16)," +
                KEY_EXERCISE_INCREMENT_CATEGORY + " varchar(128)," +
                KEY_EXERCISE_INCREMENT_PERIOD + " INT," +
                KEY_EXERCISE_INCREMENT + " FLOAT," +
                KEY_EXERCISE_CATEGORY + " varchar(128)," +
                KEY_PERMANENT + " INT NOT NULL DEFAULT 0," +
                "PRIMARY KEY (" + KEY_EXERCISE_NAME + "))";

        String CREATE_ROUTINES_TABLE = "CREATE TABLE " + TABLE_ROUTINES + "(" +
                KEY_ROUTINE_NAME + " VARCHAR(128) NOT NULL," +
                KEY_ROUTINE_DESCRIPTION + " TEXT," +
                KEY_PERMANENT + " INT NOT NULL DEFAULT 0," +
                "PRIMARY KEY (" + KEY_ROUTINE_NAME + "))";

        String CREATE_EXERCISE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_EXERCISE_SESSIONS + "(" +
                KEY_EXERCISE_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ROUTINE_SESSION_ID + " INT," +
                KEY_POSITION + " INT," +
                KEY_EXERCISE_NAME + " VARCHAR(128)," +
                KEY_COMPLETED + " INT," +
                KEY_DATE + " DATE)";

        String CREATE_ROUTINE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_ROUTINE_SESSIONS + "(" +
                KEY_ROUTINE_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ROUTINE_NAME + " VARCHAR(128)," +
                KEY_NOTES + " TEXT," +
                KEY_COMPLETED + " INT," +
                KEY_DATE + " DATE)";

        String CREATE_ROUTINE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_ROUTINE_EXERCISES + "(" +
                KEY_ROUTINE_NAME + " VARCHAR(128) NOT NULL," +
                KEY_EXERCISE_NAME + " VARCHAR(128) NOT NULL," +
                KEY_POSITION + " INT," +
                "PRIMARY KEY ("+ KEY_ROUTINE_NAME + "," + KEY_EXERCISE_NAME +"))";


        String CREATE_SETS_TABLE = "CREATE TABLE " + TABLE_SETS + "(" +
                KEY_EXERCISE_SESSION_ID + " INT NOT NULL," +
                KEY_POSITION + " INT NOT NULL," +
                KEY_EXERCISE_NAME + " VARCHAR(128)," +
                KEY_SETS_REPS_AMOUNT + " INT," +
                KEY_SETS_WEIGHT_AMOUNT + " FLOAT," +
                KEY_SETS_DISTANCE_AMOUNT + " FLOAT," +
                KEY_SETS_TIME_AMOUNT + " INT," +
                KEY_COMPLETED + " INT," +
                KEY_DATE + " DATE," +
                "PRIMARY KEY ("+ KEY_EXERCISE_SESSION_ID + "," + KEY_POSITION +"))";

        db.execSQL(CREATE_EXERCISES_TABLE);
        db.execSQL(CREATE_ROUTINES_TABLE);
        db.execSQL(CREATE_EXERCISE_SESSIONS_TABLE);
        db.execSQL(CREATE_ROUTINE_SESSIONS_TABLE);
        db.execSQL(CREATE_ROUTINE_EXERCISES_TABLE);
        db.execSQL(CREATE_SETS_TABLE);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"onUpgrade");
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);*/
        onCreate(db);
    }

    // Exercise Table Methods

    /**
     * Persist a new exercise to the database
     * @param exercise the exercise to persist to the database
     * @param permanent whether the exercise will be saved permanently (true) or temporarily (false)
     * @return the Name (primary key) of the successfully inserted exercise, or null if there is an error
     */
    public String insertExercise(Exercise exercise, boolean permanent){
        Log.d(TAG,"insertExercise");
        ContentValues values = extractExerciseData(exercise);
        values.put(KEY_PERMANENT, permanent);
        long rowId = writableDB.insertWithOnConflict(
                TABLE_EXERCISES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
        if (rowId == NO_ID) {
            return null;
        }
        ContentValues primaryKeyValues = this.getPrimaryKeyValuesFromRowId(TABLE_EXERCISES, rowId);
        assert primaryKeyValues != null;
        String exerciseName = primaryKeyValues.getAsString(KEY_EXERCISE_NAME);
        if (BuildConfig.DEBUG && exerciseName == null) {throw new AssertionError();}
        if (BuildConfig.DEBUG && !exerciseName.equals(exercise.getName())) {
            throw new AssertionError("This exerciseName = " + exerciseName +", DB exerciseName = " + exercise.getName());
        }
        return exerciseName;
    }

    /***
     * Replaces the old exercise with the new one while retaining all old records associated with
     * the exercise.
     * @param oldExerciseName The name of the exercise to be replaced
     * @param newExercise Replaces the old exercise
     * @return name of the new exercise
     */
    public String replaceExercise(String oldExerciseName, Exercise newExercise) {
        Log.d(TAG,"replaceExercise");
        insertExercise(newExercise, true);
        String newExerciseName = newExercise.getName();
        ContentValues exerciseNameUpdate = new ContentValues();
        exerciseNameUpdate.put(KEY_EXERCISE_NAME, newExerciseName);
        // Exercise Sessions
        writableDB.update(
                TABLE_EXERCISE_SESSIONS,
                exerciseNameUpdate,
                KEY_EXERCISE_NAME + "=?",
                new String[] {oldExerciseName}
        );
        // Routine Exercises
        writableDB.update(
                TABLE_ROUTINE_EXERCISES,
                exerciseNameUpdate,
                KEY_EXERCISE_NAME + "=?",
                new String[] {oldExerciseName}
        );
        // Sets
        writableDB.update(
                TABLE_SETS,
                exerciseNameUpdate,
                KEY_EXERCISE_NAME + "=?",
                new String[] {oldExerciseName}
        );
        return newExerciseName;
    }

    public Exercise getExercise(String exerciseName){
        Log.d(TAG,"getExercise");
        Cursor cursor = readableDB.query(
                TABLE_EXERCISES,
                null,
                KEY_EXERCISE_NAME + "=?",
                new String[]{exerciseName},
                null,
                null,
                null,
                null);

        Exercise exercise = null;
        if(cursor.moveToFirst()){
            exercise = makeExerciseFromCursor(cursor);
        }

        cursor.close();
        return exercise;
    }

    /**
     * Update an existing exercise in the database
     * @param exercise the exercise to be updated (exercise name must already exist in the database)
     * @return the rowId of the successfully updated exercise, or -1 if there is an error
     */
    public String updateExercise(Exercise exercise){
        Log.d(TAG,"updateExercise");
        ContentValues values = extractExerciseData(exercise);
        long rowId = writableDB.insertWithOnConflict(
                TABLE_EXERCISES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
        if (rowId == NO_ID) {
            return null;
        }
        return exercise.getName();
    }

    /***
     * Deletes an exercise and all of the data related to the exercise, including
     * ExerciseSessions, Sets, and RoutineExercises.
     * @param exerciseName Name of the exercise to be deleted from the database
     */
    public void deleteExercise(String exerciseName) {
        Log.d(TAG,"deleteExercise");
        writableDB.delete(
                TABLE_EXERCISES,
                KEY_EXERCISE_NAME + "=?",
                new String[]{exerciseName}
        );
        writableDB.delete(
                TABLE_EXERCISE_SESSIONS,
                KEY_EXERCISE_NAME + "=?",
                new String[]{exerciseName}
        );
        writableDB.delete(
                TABLE_ROUTINE_EXERCISES,
                KEY_EXERCISE_NAME + "=?",
                new String[]{exerciseName}
        );
        writableDB.delete(
                TABLE_SETS,
                KEY_EXERCISE_NAME + "=?",
                new String[]{exerciseName}
        );
    }

    private ContentValues extractExerciseData(Exercise exercise) {
        Log.d(TAG,"extractExerciseData");
        ContentValues values = new ContentValues();
        values.put(KEY_EXERCISE_NAME, exercise.getName());
        values.put(KEY_EXERCISE_DESCRIPTION, exercise.getDescription());
        values.put(KEY_EXERCISE_CATEGORY, exercise.getExerciseCategory());
        values.put(KEY_EXERCISE_INCREMENT, exercise.getIncrement());
        if (exercise.isTracked(MeasurementCategory.DISTANCE))
            values.put(KEY_EXERCISE_DISTANCE_UNIT, exercise.getUnit(MeasurementCategory.DISTANCE).name());
        if (exercise.isTracked(MeasurementCategory.WEIGHT))
            values.put(KEY_EXERCISE_WEIGHT_UNIT, exercise.getUnit(MeasurementCategory.WEIGHT).name());
        if (exercise.getCategoryToIncrement() !=  null)
            values.put(KEY_EXERCISE_INCREMENT_CATEGORY, exercise.getCategoryToIncrement().name());
        values.put(KEY_EXERCISE_INCREMENT_PERIOD, exercise.getIncrementPeriod());
        values.put(KEY_EXERCISE_IS_REPS_TRACKED, exercise.isTracked(MeasurementCategory.REPS));
        values.put(KEY_EXERCISE_IS_WEIGHT_TRACKED, exercise.isTracked(MeasurementCategory.WEIGHT));
        values.put(KEY_EXERCISE_IS_DISTANCE_TRACKED, exercise.isTracked(MeasurementCategory.DISTANCE));
        values.put(KEY_EXERCISE_IS_TIME_TRACKED, exercise.isTracked(MeasurementCategory.TIME));
        if (exercise.isTracked(MeasurementCategory.REPS)) {
            int lastReps = (int) exercise.getInitialMeasurementValue(MeasurementCategory.REPS);
            values.put(KEY_EXERCISE_REPS_LAST_VALUE, lastReps);
        }
        if (exercise.isTracked(MeasurementCategory.WEIGHT)) {
            float lastWeight = exercise.getInitialMeasurementValue(MeasurementCategory.WEIGHT);
            values.put(KEY_EXERCISE_WEIGHT_LAST_VALUE, lastWeight);
        }
        if (exercise.isTracked(MeasurementCategory.DISTANCE)) {
            float lastDistance = exercise.getInitialMeasurementValue(MeasurementCategory.DISTANCE);
            values.put(KEY_EXERCISE_DISTANCE_LAST_VALUE, lastDistance);
        }
        if (exercise.isTracked(MeasurementCategory.TIME)) {
            int lastTime = (int) exercise.getInitialMeasurementValue(MeasurementCategory.TIME);
            values.put(KEY_EXERCISE_TIME_LAST_VALUE, lastTime);
        }
        return values;
    }

    private Exercise makeExerciseFromCursor(Cursor cursor) {
        Log.d(TAG,"makeExerciseFromCursor");
        Exercise exercise = new Exercise();

        String name = cursor.getString(COLUMN_EXERCISE_NAME);
        String description = cursor.getString(COLUMN_EXERCISE_DESCRIPTION);
        int isRepsTracked = cursor.getInt(COLUMN_EXERCISE_IS_REPS_TRACKED);
        int isWeightTracked = cursor.getInt(COLUMN_EXERCISE_IS_WEIGHT_TRACKED);
        int isDistanceTracked = cursor.getInt(COLUMN_EXERCISE_IS_DISTANCE_TRACKED);
        int isTimeTracked = cursor.getInt(COLUMN_EXERCISE_IS_TIME_TRACKED);
        float increment = cursor.getFloat(COLUMN_EXERCISE_INCREMENT);
        String incrementCategory = cursor.getString(COLUMN_EXERCISE_INCREMENT_CATEGORY);
        int incrementPeriod = cursor.getInt(COLUMN_EXERCISE_INCREMENT_PERIOD);
        String exerciseCategory = cursor.getString(COLUMN_EXERCISE_CATEGORY);
        String weightUnit = cursor.getString(COLUMN_EXERCISE_WEIGHT_UNIT);
        String distanceUnit = cursor.getString(COLUMN_EXERCISE_DISTANCE_UNIT);

        exercise.setName(name);
        exercise.setDescription(description);
        if (isRepsTracked != 0) {
            exercise.trackNewMeasurementCategory(MeasurementCategory.REPS);
            exercise.addInitialMeasurementData(
                    new MeasurementData(
                            MeasurementCategory.REPS,
                            cursor.getFloat(COLUMN_EXERCISE_REPS_LAST_VALUE)
                    )
            );
        }
        if (isWeightTracked != 0) {
            exercise.trackNewMeasurementCategory(MeasurementCategory.WEIGHT);
            exercise.addInitialMeasurementData(
                    new MeasurementData(
                            MeasurementCategory.WEIGHT,
                            cursor.getFloat(COLUMN_EXERCISE_WEIGHT_LAST_VALUE)
                    )
            );
        }
        if (isDistanceTracked != 0) {
            exercise.trackNewMeasurementCategory(MeasurementCategory.DISTANCE);
            exercise.addInitialMeasurementData(
                    new MeasurementData(
                            MeasurementCategory.DISTANCE,
                            cursor.getFloat(COLUMN_EXERCISE_DISTANCE_LAST_VALUE)
                    )
            );
        }
        if (isTimeTracked != 0) {
            exercise.trackNewMeasurementCategory(MeasurementCategory.TIME);
            exercise.addInitialMeasurementData(
                    new MeasurementData(
                            MeasurementCategory.TIME,
                            cursor.getFloat(COLUMN_EXERCISE_TIME_LAST_VALUE)
                    )
            );
        }
        exercise.setIncrement(increment);
        if (incrementCategory != null)
            exercise.setMeasurementCategoryToIncrement(MeasurementCategory.getFromName(incrementCategory));
        exercise.setIncrementPeriod(incrementPeriod);
        exercise.setExerciseCategory(exerciseCategory);
        if (weightUnit != null)
            exercise.setUnit(MeasurementCategory.WEIGHT, Unit.getFromName(weightUnit));
        if (distanceUnit != null)
            exercise.setUnit(MeasurementCategory.DISTANCE, Unit.getFromName(distanceUnit));
        exercise.setMostRecentExerciseSession(this.getLastExerciseSession(exercise));

        return exercise;
    }

    public Cursor getExercisesCursor(String category) {
        Log.d(TAG,"getExercisesCursor");
        String query =
                "SELECT " + KEY_EXERCISE_NAME +
                " FROM " + TABLE_EXERCISES +
                " Where " + KEY_PERMANENT + "<>'TRUE'" +
                " AND " + KEY_EXERCISE_NAME + "<>''";
        if (category != null) {
            query += " AND " + KEY_EXERCISE_CATEGORY + "=" + category;
        }
        query += " ORDER BY " + KEY_EXERCISE_NAME;
        return readableDB.rawQuery(query, null);
    }

    public int getExerciseCount(){
        Log.d(TAG,"getExerciseCount");
        Cursor cursor = getExercisesCursor(null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Exercise Session Table Methods
    public int insertExerciseSession(long routineSessionId, ExerciseSession session, int position) {
        Log.d(TAG,"insertExerciseSession");
        ContentValues values = extractExerciseSessionData(session);
        values.put(KEY_ROUTINE_SESSION_ID, routineSessionId);
        values.put(KEY_POSITION, position);
        long rowId = writableDB.insertWithOnConflict(
                TABLE_EXERCISE_SESSIONS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues primaryKeyValues = this.getPrimaryKeyValuesFromRowId(TABLE_EXERCISE_SESSIONS, rowId);
        assert primaryKeyValues != null;
        String temp = primaryKeyValues.getAsString(KEY_EXERCISE_SESSION_ID);
        int exerciseSessionId = primaryKeyValues.getAsInteger(KEY_EXERCISE_SESSION_ID);
        session.setId(exerciseSessionId);

        this.saveExerciseSessionSets(session);
        return exerciseSessionId;
    }

    private void saveExerciseSessionSets(ExerciseSession session) {
        Log.d(TAG,"saveExerciseSessionSets");
        int numSets = session.getNumSets();
        int exerciseSessionId = session.getId();
        String exerciseName = session.getExerciseName();
        ContentValues values;
        Set set;
        for (int i = 0; i < numSets; i++) {
            insertSet(session, i, session.getSet(i));
        }
    }

    private ContentValues getPrimaryKeyValuesFromRowId(String tableName, long rowId) {
        Log.d(TAG,"getPrimaryKeyValuesFromRowId");
        ContentValues primaryKeyValues = new ContentValues();
        String[] primaryKeyNames;
        switch (tableName) {
            case TABLE_ROUTINES:
                primaryKeyNames = new String[] {KEY_ROUTINE_NAME};
                break;
            case TABLE_EXERCISES:
                primaryKeyNames = new String[] {KEY_EXERCISE_NAME};
                break;
            case TABLE_EXERCISE_SESSIONS:
                primaryKeyNames = new String[] {KEY_EXERCISE_SESSION_ID};
                break;
            case TABLE_ROUTINE_SESSIONS:
                primaryKeyNames = new String[] {KEY_ROUTINE_SESSION_ID};
                break;
            case TABLE_ROUTINE_EXERCISES:
                primaryKeyNames = new String[] {KEY_ROUTINE_NAME, KEY_EXERCISE_NAME};
                break;
            case TABLE_SETS:
                primaryKeyNames = new String[] {KEY_EXERCISE_SESSION_ID, KEY_POSITION};
                break;
            default:
                return null;
        }

        Cursor resultCursor = readableDB.query(
                tableName,
                primaryKeyNames,
                KEY_ROWID + "=?",
                new String[]{String.valueOf(rowId)},
                null,
                null,
                null,
                null
        );

        if (resultCursor.moveToFirst()) {
            for (int i = 0; i < primaryKeyNames.length; i++){
                String value = resultCursor.getString(i);
                String keyName = primaryKeyNames[i];
                primaryKeyValues.put(keyName, value);
            }
        } else { return null; }

        resultCursor.close();
        return primaryKeyValues;
    }


    public int updateExerciseSession(ExerciseSession session) {
        Log.d(TAG,"updateExerciseSession");
        ContentValues values = extractExerciseSessionData(session);
        if (session.getId() != NO_ID) {
            long rowId = writableDB.insertWithOnConflict(
                    TABLE_EXERCISE_SESSIONS,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE
            );
            ContentValues primaryKeyValues = this.getPrimaryKeyValuesFromRowId(TABLE_EXERCISE_SESSIONS, rowId);
            assert primaryKeyValues != null;
            int exerciseSessionId = primaryKeyValues.getAsInteger(KEY_EXERCISE_SESSION_ID);
            session.setId(exerciseSessionId);
            return exerciseSessionId;
        }
        return NO_ID;
    }

    public void deleteExerciseSession(ExerciseSession session) {
        Log.d(TAG,"deleteExerciseSession");
        if (session.getId() != NO_ID) {
            writableDB.delete(
                    TABLE_EXERCISE_SESSIONS,
                    KEY_EXERCISE_SESSION_ID + "=?",
                    new String[]{String.valueOf(session.getId())}
            );
        }
    }

    public ExerciseSession getExerciseSession(RoutineSession routineSession, int exerciseSessionId) {
        Log.d(TAG,"getExerciseSession");
        Cursor exerciseSessionCursor = readableDB.query(
                TABLE_EXERCISE_SESSIONS,
                new String[] {KEY_EXERCISE_NAME},
                KEY_EXERCISE_SESSION_ID + "=? AND " + KEY_ROUTINE_SESSION_ID + "=?",
                new String[] {String.valueOf(exerciseSessionId), String.valueOf(routineSession.getId())},
                null,
                null,
                null,
                null
        );
        if (exerciseSessionCursor.moveToFirst()) {
            String exerciseName = exerciseSessionCursor.getString(0);
            Exercise exercise = this.getExercise(exerciseName);
            ExerciseSession exerciseSession = new ExerciseSession(exercise, false);
            exerciseSession.setId(exerciseSessionId);
            List<Set> sets = getExerciseSessionSets(exerciseSession);
            exerciseSessionCursor.close();
            Boolean completed = true;
            for (Set set: sets) {
                exerciseSession.addSet(set);
            }
            exerciseSession.refreshCurrentSetIndex();
            return exerciseSession;
        }
        exerciseSessionCursor.close();
        return null;
    }

    public ExerciseSession getLastExerciseSession(Exercise exercise) {
        Log.d(TAG,"getLastExerciseSession");
        String exerciseName = exercise.getName();
        ExerciseSession exerciseSession = new ExerciseSession(exercise, false);

        Cursor exerciseSessionCursor = readableDB.query(
                TABLE_EXERCISE_SESSIONS,
                new String[]{KEY_EXERCISE_SESSION_ID, KEY_DATE},
                KEY_EXERCISE_NAME + "=?",
                new String[]{exerciseName},
                null,
                null,
                KEY_DATE + " ASC, " + KEY_EXERCISE_SESSION_ID + " DESC",
                "1"
        );

        if (exerciseSessionCursor.moveToFirst()) {
            int exerciseSessionId = exerciseSessionCursor.getInt(0);
            exerciseSession.setId(exerciseSessionId);
            Date exerciseSessionDate = Date.valueOf(exerciseSessionCursor.getString(1));
            exerciseSession.setDate(exerciseSessionDate);
            List<Set> sets = this.getExerciseSessionSets(exerciseSession);
            for (Set set: sets) {
                exerciseSession.addSet(set);
            }
        }
        
        exerciseSessionCursor.close();
        return exerciseSession;
    }

    public int getDaysSinceLastRoutineSession() {
        Cursor routineSessionCursor = readableDB.query(
                TABLE_ROUTINE_SESSIONS,
                new String[] {KEY_DATE},
                null,
                null,
                null,
                null,
                KEY_DATE + " ASC",
                "1"
        );
        long daysSince = -1;
        if (routineSessionCursor.moveToFirst()) {
            Date routineSessionDate = Date.valueOf(routineSessionCursor.getString(0));
            Date now = new Date(Calendar.getInstance().getTimeInMillis());
            long diff = now.getTime() - routineSessionDate.getTime();
            daysSince = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        routineSessionCursor.close();
        return (int) daysSince;
    }

    private ContentValues extractExerciseSessionData(ExerciseSession session) {
        Log.d(TAG,"extractExerciseSessionData");
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        if (session.getId() != NO_ID) {
            values.put(KEY_EXERCISE_SESSION_ID, session.getId());
        }
        values.put(KEY_EXERCISE_NAME, session.getExerciseName());
        values.put(KEY_DATE, session.getDate().toString());
        return values;
    }


    // Sets Table Methods

    private ContentValues extractSetData(Set set) {
        Log.d(TAG,"extractSetData");
        ContentValues values = new ContentValues();
        MeasurementData repsData = set.getMeasurementData(MeasurementCategory.REPS);
        MeasurementData weightData = set.getMeasurementData(MeasurementCategory.WEIGHT);
        MeasurementData distanceData = set.getMeasurementData(MeasurementCategory.DISTANCE);
        MeasurementData timeData = set.getMeasurementData(MeasurementCategory.TIME);
        if (repsData != null) {
            values.put(KEY_SETS_REPS_AMOUNT, repsData.getMeasurement());
        }
        if (weightData != null) {
            values.put(KEY_SETS_WEIGHT_AMOUNT, weightData.getMeasurement());
        }
        if (distanceData != null) {
            values.put(KEY_SETS_DISTANCE_AMOUNT, distanceData.getMeasurement());
        }
        if (timeData != null) {
            values.put(KEY_SETS_TIME_AMOUNT, timeData.getMeasurement());
        }
        values.put(KEY_COMPLETED, set.isCompleted());
        return values;
    }

    /**
     * Persist a set to the database
     *
     * @param session The exercise session associated with the set
     * @param position The position (order) of the set within the exercise session
     * @param set The set to be saved
     * @return true if successful, false if unsuccessful
     */
    public boolean insertSet(ExerciseSession session, int position, Set set) {
        Log.d(TAG,"insertSet");
        if (BuildConfig.DEBUG && session.getId() == NO_ID) {throw new AssertionError();}
        if (BuildConfig.DEBUG && position < 0) {throw new AssertionError();}
        ContentValues values = extractSetData(set);
        values.put(KEY_EXERCISE_SESSION_ID, session.getId());
        values.put(KEY_EXERCISE_NAME, session.getExerciseName());
        String dateString = session.getDate().toString();
        values.put(KEY_DATE, dateString);
        values.put(KEY_POSITION, position);

        long rowId = writableDB.insertWithOnConflict(
                TABLE_SETS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
        return rowId != NO_ID;
    }

    /**
     * Delete one set from the database
     *
     * @param session The parent exercise session of the set
     * @param position The position of the set in its parent exercise session
     * @return true if delete was successful (one row deleted), false if otherwise.
     */
    public boolean deleteSet(ExerciseSession session, int position) {
        Log.d(TAG,"deleteSet");
        boolean deleted = false;
        if (session.getId() != NO_ID) {
            int count = writableDB.delete(
                    TABLE_EXERCISE_SESSIONS,
                    KEY_EXERCISE_SESSION_ID + "=?",
                    new String[]{String.valueOf(session.getId())}
            );
            if (count == 1) {
                deleted = true;
            }
        }
        return deleted;
    }

    public RoutineStats getRoutineStats(Routine routine) {
        Log.d(TAG,"getRoutineStats");
        Cursor setsCursor = readableDB.rawQuery(
                "SELECT s." + KEY_DATE + ", s." + KEY_EXERCISE_NAME +
                        ", Count(s." + KEY_POSITION + ")" + ", s." + KEY_EXERCISE_SESSION_ID +
                        ", es." + KEY_POSITION + " " +
                        "FROM " + TABLE_SETS + " AS s " +
                        "INNER JOIN " + TABLE_EXERCISE_SESSIONS + " AS es " +
                        "ON es." + KEY_EXERCISE_SESSION_ID + " = s." + KEY_EXERCISE_SESSION_ID + " " +
                        "INNER JOIN " + TABLE_ROUTINE_SESSIONS + " AS rs " +
                        "ON es." + KEY_ROUTINE_SESSION_ID + " = rs." + KEY_ROUTINE_SESSION_ID + " " +
                        "WHERE rs." + KEY_ROUTINE_NAME + " = '" + routine.getName() + "' " +
                        "AND s." + KEY_COMPLETED + " = 1 " +
                        "GROUP BY s." + KEY_EXERCISE_NAME + ", s." + KEY_EXERCISE_SESSION_ID + " " +
                        "ORDER BY es." + KEY_POSITION,
                null
        );

        RoutineStats stats = new RoutineStats();
        Date date;
        int setCount;
        String exerciseName;
        if (setsCursor.moveToFirst()) {
            while (!setsCursor.isAfterLast()) {
                date = Date.valueOf(setsCursor.getString(0));
                exerciseName = setsCursor.getString(1);
                setCount = setsCursor.getInt(2);
                stats.add(date, exerciseName, setCount);
                setsCursor.moveToNext();
            }
        }
        setsCursor.close();
        return stats;
    }

    public ExerciseStats getExerciseStats(Exercise exercise) {
        Log.d(TAG,"getExerciseStats");
        Cursor setsCursor = readableDB.query(
                TABLE_SETS,
                new String[] {KEY_SETS_REPS_AMOUNT, KEY_SETS_WEIGHT_AMOUNT,
                KEY_SETS_DISTANCE_AMOUNT, KEY_SETS_TIME_AMOUNT, KEY_DATE},
                KEY_COMPLETED + "=1 AND " + KEY_EXERCISE_NAME + "=?",
                new String[] {exercise.getName()},
                null,
                null,
                null
        );

        ExerciseStats stats = new ExerciseStats();
        if (setsCursor.moveToFirst()) {
            boolean bReps = exercise.isTracked(MeasurementCategory.REPS);
            boolean bWeight = exercise.isTracked(MeasurementCategory.WEIGHT);
            boolean bDistance = exercise.isTracked(MeasurementCategory.DISTANCE);
            boolean bTime = exercise.isTracked(MeasurementCategory.TIME);
            String dateString;
            Date date;
            while (!setsCursor.isAfterLast()) {
                Set set = new Set();
                dateString = setsCursor.getString(4);
                date = Date.valueOf(dateString);
                if (bReps) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.REPS,
                            setsCursor.getInt(0),
                            exercise.getUnit(MeasurementCategory.REPS)
                    );
                    set.addMeasurement(data);
                }
                if (bWeight) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.WEIGHT,
                            setsCursor.getFloat(1),
                            exercise.getUnit(MeasurementCategory.WEIGHT)
                    );
                    set.addMeasurement(data);
                }
                if (bDistance) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.DISTANCE,
                            setsCursor.getFloat(2),
                            exercise.getUnit(MeasurementCategory.DISTANCE)
                    );
                    set.addMeasurement(data);
                }
                if (bTime) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.TIME,
                            setsCursor.getLong(3),
                            exercise.getUnit(MeasurementCategory.TIME)
                    );
                    set.addMeasurement(data);
                }
                stats.addSet(date, set);
                setsCursor.moveToNext();
            }
        }
        setsCursor.close();
        return stats;
    }

    private List<Set> getExerciseSessionSets(ExerciseSession exerciseSession) {
        Log.d(TAG,"getExerciseSessionSets");
        long exerciseSessionId = exerciseSession.getId();
        Cursor setsCursor = readableDB.query(
                TABLE_SETS,
                new String[]{KEY_SETS_REPS_AMOUNT, KEY_SETS_WEIGHT_AMOUNT,
                        KEY_SETS_DISTANCE_AMOUNT, KEY_SETS_TIME_AMOUNT, KEY_DATE, KEY_COMPLETED},
                KEY_EXERCISE_SESSION_ID + "=?",
                new String[]{String.valueOf(exerciseSessionId)},
                null,
                null,
                KEY_POSITION + " ASC",
                null
        );

        LinkedList<Set> sets = new LinkedList<>();
        if (setsCursor.moveToFirst()) {
            boolean completed;
            int repsAmount;
            float weightAmount;
            float distanceAmount;
            long timeAmount;
            boolean reps = exerciseSession.hasCategory(MeasurementCategory.REPS);
            boolean weight = exerciseSession.hasCategory(MeasurementCategory.WEIGHT);
            boolean distance = exerciseSession.hasCategory(MeasurementCategory.DISTANCE);
            boolean time = exerciseSession.hasCategory(MeasurementCategory.TIME);
            while (!setsCursor.isAfterLast()) {
                repsAmount = setsCursor.getInt(0);
                weightAmount = setsCursor.getFloat(1);
                distanceAmount = setsCursor.getFloat(2);
                timeAmount = setsCursor.getInt(3);
                completed = (setsCursor.getInt(5) != 0);
                Set set = new Set();
                if (reps) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.REPS,
                            repsAmount,
                            exerciseSession.getUnit(MeasurementCategory.REPS)
                    );
                    set.addMeasurement(data);
                }
                if (weight) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.WEIGHT,
                            weightAmount,
                            exerciseSession.getUnit(MeasurementCategory.WEIGHT)
                    );
                    set.addMeasurement(data);
                }
                if (distance) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.DISTANCE,
                            distanceAmount,
                            exerciseSession.getUnit(MeasurementCategory.DISTANCE)
                    );
                    set.addMeasurement(data);
                }
                if (time) {
                    MeasurementData data = new MeasurementData(
                            MeasurementCategory.TIME,
                            timeAmount,
                            exerciseSession.getUnit(MeasurementCategory.TIME)
                    );
                    set.addMeasurement(data);
                }
                if (completed) {
                    set.finish();
                }
                sets.add(set);
                setsCursor.moveToNext();
            }
        }
        setsCursor.close();
        return sets;
    }

    // Routine Table Methods

    // Routine Session table methods

    // Routine Exercises table methods

    // Json conversion methods



    /**
     * Persist a routine and all of its routine-exercise information to the database.
     *
     * @param routine   The routine to saved
     * @param permanent Specify whether the routine is temporary or permanent. Temporary
     *                  routines will be cleaned from the database periodically.
     * @return The name of the successfully saved routine
     */
    public String insertRoutine(Routine routine, boolean permanent) {
        Log.d(TAG,"insertRoutine");

        this.deleteRoutine(routine.getName());
        // Save routine in Routine table
        ContentValues values = extractRoutineData(routine);
        values.put(KEY_PERMANENT, permanent);
        long rowId = writableDB.insertWithOnConflict(
                TABLE_ROUTINES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        ContentValues primaryKeyValues = this.getPrimaryKeyValuesFromRowId(TABLE_ROUTINES, rowId);
        assert primaryKeyValues != null;
        String routineName = primaryKeyValues.getAsString(KEY_ROUTINE_NAME);
        if (BuildConfig.DEBUG && !routineName.equals(routine.getName())) {
            throw new AssertionError();
        }

        // Save routine-exercise pairings into Routine Exercises table
        this.saveRoutineExercises(routine);

        return routineName;
    }    // Routine Exercises table column names

    /***
     * Replaces the old routine with the new one while retaining all old records associated with
     * the routine.
     * @param oldRoutineName The name of the routine to be replaced
     * @param newRoutine Replaces the old routine
     * @return name of the new routine
     */
    public String replaceRoutine(String oldRoutineName, Routine newRoutine) {
        Log.d(TAG,"replaceRoutine");
        deleteRoutine(oldRoutineName);
        String newRoutineName = insertRoutine(newRoutine, true);
        ContentValues routineNameUpdate = new ContentValues();
        routineNameUpdate.put(KEY_ROUTINE_NAME, newRoutineName);
        // Routine Sessions
        writableDB.update(
                TABLE_ROUTINE_SESSIONS,
                routineNameUpdate,
                KEY_ROUTINE_NAME + "=?",
                new String[] {oldRoutineName}
        );
        return newRoutineName;
    }

    /**
     * @param routine The routine to update in the database
     * @return the name of the successfully updated routine
     */
    public String updateRoutine(Routine routine) {
        Log.d(TAG,"updateRoutine");
        // Save routine in Routine table
        ContentValues values = extractRoutineData(routine);
        long rowId = writableDB.insertWithOnConflict(
                TABLE_ROUTINES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        ContentValues primaryKeyValues = this.getPrimaryKeyValuesFromRowId(TABLE_ROUTINES, rowId);
        assert primaryKeyValues != null;
        String routineName = primaryKeyValues.getAsString(KEY_ROUTINE_NAME);
        if (BuildConfig.DEBUG && !routineName.equals(routine.getName())) {
            throw new AssertionError();
        }

        // Save routine-exercise pairings into Routine Exercises table
        this.saveRoutineExercises(routine);
        return routineName;
    }    // Exercise Session Table Column Names

    /**
     * Delete a routine and its routine-exercise data from the database
     *
     * @param routineName the name of the routine to be deleted
     * @return true if the routine is sucessfully deleted, false if otherwise;
     */
    public boolean deleteRoutine(String routineName) {
        Log.d(TAG,"deleteRoutine");
        String[] rName = new String[]{routineName};
        int count = writableDB.delete(
                TABLE_ROUTINES,
                KEY_ROUTINE_NAME + "=?",
                rName
        );
        if (count == 1) {
            writableDB.delete(
                    TABLE_ROUTINE_EXERCISES,
                    KEY_ROUTINE_NAME + "=?",
                    rName
            );
            return true;
        }
        return false;
    }    // KEY_EXERCISE_NAME
    // Can return null if routineName is not in the database
    public Routine getRoutine(String routineName) {
        Log.d(TAG,"getRoutine");
        Cursor cursor = readableDB.query(
                TABLE_ROUTINES,
                new String[]{KEY_ROUTINE_NAME, KEY_ROUTINE_DESCRIPTION},
                KEY_ROUTINE_NAME + "=?",
                new String[]{routineName},
                null,
                null,
                null,
                null);

        Routine routine = null;
        if (cursor.moveToFirst()) {
            routine = this.makeRoutineFromCursor(cursor);
        }

        cursor.close();
        return routine;
    }

    // Routine Session Table Column Names

    private ContentValues extractRoutineData(Routine routine) {
        Log.d(TAG,"extractRoutineData");
        ContentValues values = new ContentValues();
        values.put(KEY_ROUTINE_NAME, routine.getName());
        values.put(KEY_ROUTINE_DESCRIPTION, routine.getDescription());
        return values;
    }    // Sets Table Column Names

    private Routine makeRoutineFromCursor(Cursor routineCursor) {
        Log.d(TAG,"makeRoutineFromCursor");
        String name = routineCursor.getString(COLUMN_ROUTINE_NAME);
        String description = routineCursor.getString(COLUMN_ROUTINE_DESCRIPTION);

        Routine routine = new Routine(name);
        routine.setDescription(description);
        List<Exercise> routineExercises = getRoutineExercises(name);
        for (Exercise exercise : routineExercises) {
            routine.addExercise(exercise);
        }

        return routine;
    }

    public Cursor getRoutinesCursor() {
        Log.d(TAG,"getRoutinesCursor");
        String query =
                "SELECT " + KEY_ROUTINE_NAME + "," + KEY_ROUTINE_DESCRIPTION +
                        " FROM " + TABLE_ROUTINES +
                        " WHERE " + KEY_PERMANENT + "='1'" +
                        " AND " + KEY_ROUTINE_NAME + "<>''";
        return readableDB.rawQuery(query, null);
    }

    public int getRoutineCount() {
        Log.d(TAG,"getRoutineCount");
        Cursor cursor = getRoutinesCursor();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * Persist a RoutineSession and all of its ExerciseSession data to the database.
     * If a RoutineSession with the same ID is present in the database, it and all of its associated
     * ExerciseSessions will be overwritten.
     *
     * @param session the RoutineSession to be saved
     * @return The RoutineSessionId of the successfully saved RoutineSession
     */
    public int insertRoutineSessionDeep(RoutineSession session) {
        Log.d(TAG,"insertRoutineSessionDeep");
        this.deleteRoutineSession(session);
        this.insertRoutineSession(session);
        this.saveRoutineSessionExerciseSessions(session);
        return (int) session.getId();
    }

    public int insertRoutineSession(RoutineSession session) {
        Log.d(TAG,"insertRoutineSession");
        ContentValues values = extractRoutineSessionData(session);
        long rowId = writableDB.insertWithOnConflict(
                TABLE_ROUTINE_SESSIONS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        ContentValues primaryKeyValues = this.getPrimaryKeyValuesFromRowId(TABLE_ROUTINE_SESSIONS, rowId);
        assert primaryKeyValues != null;
        Integer sessionId = primaryKeyValues.getAsInteger(KEY_ROUTINE_SESSION_ID);
        if (BuildConfig.DEBUG && sessionId == null) {
            throw new AssertionError();
        }
        session.setId(sessionId);
        return sessionId;
    }

    private void saveRoutineSessionExerciseSessions(RoutineSession routineSession) {
        Log.d(TAG,"saveRoutineSessionExerciseSessions");
        long routineSessionId = routineSession.getId();
        int numExerciseSessions = routineSession.getExerciseSessionCount();
        ExerciseSession exerciseSession;
        for (int i = 0; i < numExerciseSessions; i++) {
            exerciseSession = routineSession.getExerciseSession(i);
            insertExerciseSession(routineSessionId, exerciseSession, i);
        }
    }

    private ContentValues extractRoutineSessionData(RoutineSession session) {
        Log.d(TAG,"extractRoutineSessionData");
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        if (session.getId() != NO_ID) {
            values.put(KEY_ROUTINE_SESSION_ID, session.getId());
        }
        values.put(KEY_ROUTINE_NAME, session.getRoutine().getName());
        values.put(KEY_DATE, session.getDate().toString());
        values.put(KEY_COMPLETED, session.isCompleted());
        values.put(KEY_NOTES, session.getNotes());
        return values;
    }

    public boolean routineSessionExistsForDate(String routineName, Date date) {
        Log.d(TAG,"routineSessionExistsForDate");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String todayDateString = format.format(Calendar.getInstance().getTime());
        Cursor routineSessionCursor = readableDB.query(
                TABLE_ROUTINE_SESSIONS,
                new String[]{KEY_ROUTINE_SESSION_ID},
                KEY_ROUTINE_NAME + " = ? " + " AND " + KEY_DATE + " >= ?",
                new String[]{routineName, todayDateString},
                null,
                null,
                null
        );

        Boolean result = routineSessionCursor.getCount() > 0;
        routineSessionCursor.close();
        return result;
    }

    public RoutineSession getLastRoutineSession(Routine routine) {
        Log.d(TAG,"getLastRoutineSession");
        String routineName = routine.getName();
        RoutineSession routineSession = new RoutineSession(routine, false);

        Cursor routineSessionCursor = readableDB.query(
                TABLE_ROUTINE_SESSIONS,
                new String[]{KEY_ROUTINE_SESSION_ID, KEY_DATE, KEY_NOTES},
                KEY_ROUTINE_NAME + "=?",
                new String[]{routineName},
                null,
                null,
                KEY_DATE + " Desc",
                "1"
        );

        if (routineSessionCursor.moveToFirst()) {
            int routineSessionId = routineSessionCursor.getInt(0);
            routineSession.setId(routineSessionId);
            String dateString = routineSessionCursor.getString(1);
            Date exerciseSessionDate = Date.valueOf(dateString);
            routineSession.setDate(exerciseSessionDate);
            String notes = routineSessionCursor.getString(2);
            routineSession.setNotes(notes);
            List<ExerciseSession> exerciseSessions = this.getRoutineSessionExerciseSessions(routineSession);
            for (ExerciseSession session : exerciseSessions) {
                routineSession.addExerciseSession(session);
            }
        }

        routineSessionCursor.close();
        return routineSession;
    }

    private List<ExerciseSession> getRoutineSessionExerciseSessions(RoutineSession routineSession) {
        Log.d(TAG,"getRoutineSessionExerciseSessions");
        long routineSessionId = routineSession.getId();
        Cursor exerciseSessionCursor = readableDB.query(
                TABLE_EXERCISE_SESSIONS,
                new String[]{KEY_EXERCISE_SESSION_ID, KEY_DATE},
                KEY_ROUTINE_SESSION_ID + "=?",
                new String[]{String.valueOf(routineSessionId)},
                null,
                null,
                KEY_POSITION + " ASC",
                null
        );

        LinkedList<ExerciseSession> exerciseSessions = new LinkedList<>();
        if (exerciseSessionCursor.moveToFirst()) {

            while (!exerciseSessionCursor.isAfterLast()) {
                int exerciseSessionId = exerciseSessionCursor.getInt(0);
                ExerciseSession exerciseSession = this.getExerciseSession(routineSession, exerciseSessionId);
                exerciseSessions.add(exerciseSession);
                exerciseSessionCursor.moveToNext();
            }
        }
        exerciseSessionCursor.close();
        return exerciseSessions;
    }

    /**
     * Delete a RoutineSession and all of the associated ExerciseSession data from the database
     *
     * @param session The RoutineSession to be deleted
     * @return true if successful, false if otherwise
     */
    public boolean deleteRoutineSession(RoutineSession session) {
        Log.d(TAG,"deleteRoutineSession");
        String[] routineSessionId = new String[]{String.valueOf(session.getId())};
        int count = writableDB.delete(
                TABLE_ROUTINE_SESSIONS,
                KEY_ROUTINE_SESSION_ID + "=?",
                routineSessionId
        );

        if (count == 1) {
            Cursor exerciseSessionCursor = readableDB.query(
                    TABLE_EXERCISE_SESSIONS,
                    new String[]{KEY_EXERCISE_SESSION_ID},
                    KEY_ROUTINE_SESSION_ID + "=?",
                    routineSessionId,
                    null,
                    null,
                    null,
                    null
            );

            int exerciseSessionId;
            if (exerciseSessionCursor.moveToFirst()) {
                exerciseSessionId = exerciseSessionCursor.getInt(0);
                writableDB.delete(
                        TABLE_SETS,
                        KEY_EXERCISE_SESSION_ID + "=?",
                        new String[]{String.valueOf(exerciseSessionId)}
                );
            }
            exerciseSessionCursor.close();

            writableDB.delete(
                    TABLE_EXERCISE_SESSIONS,
                    KEY_ROUTINE_SESSION_ID + "=?",
                    routineSessionId
            );

            return true;
        }
        return false;
    }

    private void saveRoutineExercises(Routine routine) {
        Log.d(TAG,"saveRoutineExercises");
        int numExercises = routine.getNumExercises();
        String routineName = routine.getName();
        ContentValues values = new ContentValues();
        Exercise exercise;
        String exerciseName;
        for (int i = 0; i < numExercises; i++) {
            values.clear();
            exercise = routine.getExercise(i);
            exerciseName = exercise.getName();
            insertRoutineExercise(routineName, exerciseName, i);
        }
    }

    public void insertRoutineExercise(String routineName, String exerciseName, int position) {
        Log.d(TAG,"insertRoutineExercises");
        ContentValues values = new ContentValues();
        values.put(KEY_EXERCISE_NAME, exerciseName);
        values.put(KEY_ROUTINE_NAME, routineName);
        values.put(KEY_POSITION, position);
        writableDB.insertWithOnConflict(
                TABLE_ROUTINE_EXERCISES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<Exercise> getRoutineExercises(String routineName) {
        Log.d(TAG,"getRoutineExercises");
        Cursor exercisesCursor = readableDB.rawQuery(
                "SELECT " + TABLE_EXERCISES + ".*" +
                        " FROM " + TABLE_EXERCISES +
                        " INNER JOIN " + TABLE_ROUTINE_EXERCISES +
                        " ON " + TABLE_ROUTINE_EXERCISES + "." + KEY_EXERCISE_NAME +
                        "=" + TABLE_EXERCISES + "." + KEY_EXERCISE_NAME +
                        " WHERE " + TABLE_ROUTINE_EXERCISES + "." + KEY_ROUTINE_NAME +
                        "='" + routineName + "'" +
                        " ORDER BY " + TABLE_ROUTINE_EXERCISES + "." + KEY_POSITION,
                null
        );

        ArrayList<Exercise> routineExercises = new ArrayList<>();
        Exercise exercise;
        if (exercisesCursor.moveToFirst()) {
            while (!exercisesCursor.isAfterLast()) {
                exercise = makeExerciseFromCursor(exercisesCursor);
                routineExercises.add(exercise);
                exercisesCursor.moveToNext();
            }
        }

        return routineExercises;
    }

    public void deleteRoutineExercises(String routineName) {
        Log.d(TAG,"deleteRoutineExercises");
        writableDB.delete(
                TABLE_ROUTINE_EXERCISES,
                KEY_ROUTINE_NAME + "=?",
                new String[]{routineName}
        );
    }

    public void deleteRoutineExercise(String routineName, int position) {
        Log.d(TAG,"deleteRoutineExercises");
        writableDB.delete(
                TABLE_ROUTINE_EXERCISES,
                KEY_ROUTINE_NAME + "=? AND " + KEY_POSITION + "=?",
                new String[]{routineName, String.valueOf(position)}
        );
    }

    public void cleanTemporaryData() {
        Log.d(TAG,"cleanTemporaryData");
        deleteTemporaryExercises();
        deleteTemporaryRoutines();
    }

    public void makeFresh(Context context) {
        Log.d(TAG,"makeFresh");
        /*writableDB.delete(TABLE_EXERCISES, null, null);
        writableDB.delete(TABLE_EXERCISE_SESSIONS, null, null);
        writableDB.delete(TABLE_ROUTINES, null, null);
        writableDB.delete(TABLE_ROUTINE_SESSIONS, null, null);
        writableDB.delete(TABLE_ROUTINE_EXERCISES, null, null);
        writableDB.delete(TABLE_SETS, null, null);*/
        forceDatabaseReload(context);
    }

    public static void forceDatabaseReload(Context context){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.setForcedUpgrade(DATABASE_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.setVersion(-1);
        db.close();
        db = dbHelper.getWritableDatabase();
    }

    private void deleteTemporaryRoutines() {
        Log.d(TAG,"deleteTemporaryRoutines");
        Cursor temporaryRoutines = readableDB.query(
                TABLE_ROUTINES,
                new String[]{KEY_ROUTINE_NAME},
                KEY_PERMANENT + "=?",
                new String[]{"0"},
                null,
                null,
                null,
                null
        );

        if (temporaryRoutines.moveToFirst()) {
            while (!temporaryRoutines.isAfterLast()) {
                String routineName = temporaryRoutines.getString(0);
                this.deleteRoutineExercises(routineName);
                temporaryRoutines.moveToNext();
            }
        }
        temporaryRoutines.close();
        writableDB.delete(
                TABLE_ROUTINES,
                KEY_PERMANENT + "=?",
                new String[]{"0"}
        );
    }

    private void deleteTemporaryExercises() {
        Log.d(TAG,"deleteTemporaryExercises");
        writableDB.delete(TABLE_EXERCISES, KEY_EXERCISE_NAME + "=? OR " + KEY_PERMANENT + "=?", new String[]{"''", "0"});
    }

    // Convert an object to json code
    private String toJson(Object obj) {
        Log.d(TAG,"toJson");
        Gson gson = new Gson();
        return gson.toJson(obj, obj.getClass());
    }

    // Convert json back into an object of class c
    private Object parseJson(String json, Class c) {
        Log.d(TAG,"parseJson");
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

    // Static Variables

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "Evolve.db";

    private static DatabaseHelper mInstance;

    private static SQLiteDatabase writableDB;

    private static SQLiteDatabase readableDB;


    private static final String TAG = DatabaseHelper.class.getSimpleName();


    // Table Names
    private static final String TABLE_EXERCISES = "Exercises";

    private static final String TABLE_ROUTINES = "Routines";
    private static final String TABLE_EXERCISE_SESSIONS = "ExerciseSessions";
    private static final String TABLE_ROUTINE_SESSIONS = "RoutineSessions";
    private static final String TABLE_ROUTINE_EXERCISES = "RoutineExercises";
    private static final String TABLE_SETS = "Sets";

    // Exercises table column names
    public static final String KEY_EXERCISE_NAME = "ExerciseName";
    private static final String KEY_EXERCISE_DESCRIPTION = "Description";
    private static final String KEY_EXERCISE_IS_REPS_TRACKED = "IsRepsTracked";
    private static final String KEY_EXERCISE_IS_WEIGHT_TRACKED = "IsWeightTracked";
    private static final String KEY_EXERCISE_IS_DISTANCE_TRACKED = "IsDistanceTracked";
    private static final String KEY_EXERCISE_IS_TIME_TRACKED = "IsTimeTracked";
    private static final String KEY_EXERCISE_REPS_LAST_VALUE = "RepsLastValue";
    private static final String KEY_EXERCISE_WEIGHT_LAST_VALUE = "WeightLastValue";
    private static final String KEY_EXERCISE_DISTANCE_LAST_VALUE = "DistanceLastValue";
    private static final String KEY_EXERCISE_TIME_LAST_VALUE = "TimeLastValue";
    private static final String KEY_EXERCISE_WEIGHT_UNIT = "WeightUnit";
    private static final String KEY_EXERCISE_DISTANCE_UNIT = "DistanceUnit";
    private static final String KEY_EXERCISE_INCREMENT_CATEGORY = "IncrementCategory";
    private static final String KEY_EXERCISE_INCREMENT_PERIOD = "IncrementPeriod";

    private static final String KEY_EXERCISE_INCREMENT = "Increment";
    private static final String KEY_EXERCISE_CATEGORY = "Category";
    private static final String KEY_PERMANENT = "Permanent";
    public static final String KEY_POSITION = "Position";
    private static final String KEY_DATE = "Date";
    private static final String KEY_NOTES = "Notes";
    private static final String KEY_COMPLETED = "Completed";
    private static final String KEY_ROWID = "ROWID";
    private static final String KEY_SETS_WEIGHT_AMOUNT = "WeightAmount";
    private static final String KEY_SETS_DISTANCE_AMOUNT = "DistanceAmount";
    private static final String KEY_SETS_REPS_AMOUNT = "RepsAmount";
    private static final String KEY_SETS_TIME_AMOUNT = "TimeAmount";

    // Routine table column names
    public static final String KEY_ROUTINE_NAME = "RoutineName";
    private static final String KEY_ROUTINE_DESCRIPTION = "Description";
    private static final String KEY_ROUTINE_SESSION_ID = "RoutineSessionId";
    private static final String KEY_EXERCISE_SESSION_ID = "ExerciseSessionId";


    public static final int NO_ID = -1;
    public static final int COLUMN_ROUTINE_NAME = 0;
    public static final int COLUMN_ROUTINE_DESCRIPTION = 1;
    public static final int COLUMN_ROUTINE_PERMANENT = 2;

    public static final int COLUMN_EXERCISE_NAME = 0;
    public static final int COLUMN_EXERCISE_DESCRIPTION = 1;
    public static final int COLUMN_EXERCISE_IS_REPS_TRACKED = 2;
    public static final int COLUMN_EXERCISE_IS_WEIGHT_TRACKED = 3;
    public static final int COLUMN_EXERCISE_IS_DISTANCE_TRACKED = 4;
    public static final int COLUMN_EXERCISE_IS_TIME_TRACKED = 5;
    public static final int COLUMN_EXERCISE_WEIGHT_UNIT = 6;
    public static final int COLUMN_EXERCISE_DISTANCE_UNIT = 7;
    public static final int COLUMN_EXERCISE_INCREMENT_CATEGORY = 8;
    public static final int COLUMN_EXERCISE_INCREMENT_PERIOD = 9;
    public static final int COLUMN_EXERCISE_INCREMENT = 10;
    public static final int COLUMN_EXERCISE_CATEGORY = 11;
    public static final int COLUMN_EXERCISE_PERMANENT = 12;
    public static final int COLUMN_EXERCISE_REPS_LAST_VALUE = 13;
    public static final int COLUMN_EXERCISE_WEIGHT_LAST_VALUE = 14;
    public static final int COLUMN_EXERCISE_DISTANCE_LAST_VALUE = 15;
    public static final int COLUMN_EXERCISE_TIME_LAST_VALUE = 16;

}