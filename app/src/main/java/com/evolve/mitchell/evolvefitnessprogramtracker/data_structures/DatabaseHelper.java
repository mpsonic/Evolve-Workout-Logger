package com.evolve.mitchell.evolvefitnessprogramtracker.data_structures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class talks to the SQLite database and helps in storing
 * Routine and Exercise data for the user
 *
 * Created by Mitchell on 1/6/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Public
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_JSON + " TEXT)";
        String CREATE_ROUTINES_TABLE = "CREATE TABLE " + TABLE_ROUTINES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_JSON + " TEXT)";
        db.execSQL(CREATE_EXERCISES_TABLE);
        db.execSQL(CREATE_ROUTINES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINES);
        onCreate(db);
    }

    // Exercise Table Methods
    public long addExercise(Exercise exercise){

        SQLiteDatabase db = this.getWritableDatabase();

        // Check to see whether exercise is already in the database?

        // Enter values into database
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, exercise.getName());
        values.put(KEY_JSON, toJson(exercise));
        long rowId = db.insert(TABLE_EXERCISES, null, values);
        db.close();
        return rowId;
    }

    public long updateExercise(Exercise exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, exercise.getName());
        values.put(KEY_JSON, toJson(exercise));
        long result = db.update(TABLE_EXERCISES, values, KEY_ID + "=?", new String[]{String.valueOf(exercise.getId())});
        db.close();
        return result;
    }

    public Exercise getExercise(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXERCISES, new String[]{KEY_JSON}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Exercise exercise = null;
        if(cursor.moveToFirst()){
            String json = cursor.getString(0);
            exercise = (Exercise) parseJson(json, Exercise.class);
            if (exercise.getId() != id)
                exercise.setId(id);
        }

        db.close();
        return exercise;
    }

    public List<String> getExerciseNames(){
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISES, new String[]{KEY_NAME}, null, null, null, null, null);
        if(cursor.moveToFirst()){
            names.add(cursor.getString(0));
            do{
                cursor.moveToNext();
                names.add(cursor.getString(0));
            }while(!cursor.isLast());
        }
        db.close();
        return names;
    }

    public void deleteExercise(Exercise exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISES, KEY_ID + "=?", new String[]{String.valueOf(exercise.getId())});
        db.close();
    }

    public Cursor getExercisesCursor(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXERCISES;
        return db.rawQuery(query, null);
    }

    public int getExerciseCount(){
        Cursor cursor = getExercisesCursor();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Routine Table Methods
    public long addRoutine(Routine routine){
        SQLiteDatabase db = this.getWritableDatabase();

        // Check to see whether exercise is already in the database?

        // Enter values into database
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, routine.getName());
        values.put(KEY_JSON, toJson(routine));
        long rowId = db.insert(TABLE_ROUTINES, null, values);
        db.close();
        return rowId;
    }

    public long updateRoutine(Routine routine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, routine.getName());
        values.put(KEY_JSON, toJson(routine));
        long result = db.update(TABLE_ROUTINES, values, KEY_ID + "=?", new String[]{String.valueOf(routine.getId())});
        db.close();
        return result;
    }

    // Can return null if id is not in the database
    public Routine getRoutine(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROUTINES, new String[]{KEY_JSON}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Routine routine = null;
        if(cursor.moveToFirst()) {
            String json = cursor.getString(0);
            routine = (Routine) parseJson(json, Routine.class);
            if (routine.getId() != id)
                routine.setId(id);
        }

        cursor.close();
        return routine;
    }

    public List<Pair<String, Long>> getRoutineNamesAndIds(){
        List<Pair<String, Long>> pairs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ROUTINES, new String[]{KEY_NAME, KEY_ID}, null, null, null, null, null);
        String name;
        Long id;
        Pair<String, Long> newEntry;
        if(cursor.moveToFirst()){
            name = cursor.getString(0);
            id = cursor.getLong(1);
            newEntry = new Pair<>(name, id);
            pairs.add(newEntry);
            do{
                cursor.moveToNext();
                name = cursor.getString(0);
                id = cursor.getLong(1);
                newEntry = new Pair<>(name, id);
                pairs.add(newEntry);
            }while(!cursor.isLast());
        }
        cursor.close();
        return pairs;
    }

    public void deleteRoutine(Routine routine){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ROUTINES, KEY_ID + "=?", new String[]{String.valueOf(routine.getId())});
        db.close();
    }

    public Cursor getRoutinesCursor(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ROUTINES;
        return db.rawQuery(query, null);
    }

    public int getRoutineCount(){
        Cursor cursor = getRoutinesCursor();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    // Private

    // Convert an object to json code
    private String toJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj, obj.getClass());
    }

    // Convert json back into an object of class c
    private Object parseJson(String json, Class c){
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

    // Static Variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "fitnessDatabase";

    // Table Names
    private static final String TABLE_EXERCISES = "Exercises";
    private static final String TABLE_ROUTINES = "Routines";

    // Exercises table column names
    private static final String KEY_ID = BaseColumns._ID;
    private static final String KEY_NAME = "Name";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_JSON = "Json";

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_DESCRIPTION = 2;
    public static final int COLUMN_JSON = 3;
}
