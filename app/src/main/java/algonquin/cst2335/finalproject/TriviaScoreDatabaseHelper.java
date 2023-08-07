package algonquin.cst2335.finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a helper for managing the Trivia scores database using SQLite.
 * It handles creating the database, defining the table structure, and providing methods
 * to add, delete, and retrieve Trivia scores from the database.
 */
public class TriviaScoreDatabaseHelper extends SQLiteOpenHelper {
    /**
     * Database properties
     */
    private static final String DATABASE_NAME = "score.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Table properties
     */
    private static final String TABLE_score = "score";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    /**
     * SQL query to create the score table
     */
    private static final String CREATE_TABLE_score =
            "CREATE TABLE " + TABLE_score + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_SCORE + " INTEGER" +
                    ")";

    /**
     * Constructor for creating a TriviaScoreDatabaseHelper instance.
     * @param context The context of the application.
     */
    public TriviaScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * It executes the SQL query to create the score table.
     * @param db The SQLiteDatabase instance representing the database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_score);
    }

    /**
     * Called when the database needs to be upgraded (e.g., when the DATABASE_VERSION is increased).
     * This method is not implemented in this case.
     * @param db The SQLiteDatabase instance representing the database.
     * @param oldVersion The old version of the database.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not implemented for this database version
    }

    /**
     * Adds a new TriviaScore entry to the database.
     * @param score The TriviaScore instance representing the score to be added.
     */
    public void addScore(TriviaScore score) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, score.getName());
        values.put(COLUMN_SCORE, score.getScore());
        database.insert(TABLE_score, null, values);
        database.close();
    }

    /**
     * Deletes a TriviaScore entry from the database based on the player's name.
     * @param name The name of the player whose score should be deleted.
     */
    public void deleteScore(String name) {
        SQLiteDatabase database = getWritableDatabase();
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {String.valueOf(name)};
        database.delete(TABLE_score, selection, selectionArgs);
        database.close();

    }

    /**
     * Checks if a player name already exists in the database (duplicate entry check).
     * @param name The name to be checked for existence in the database.
     * @return True if the player name already exists in the database, false otherwise.
     */
    public boolean isNameAlreadyExist(String name) {
        SQLiteDatabase database = getReadableDatabase();
        String selection = COLUMN_NAME + "=?";
        String[] selectionArgs = {name};
        Cursor cursor = database.query(
                TABLE_score,
                new String[]{COLUMN_NAME},
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        boolean nameExists = cursor.moveToFirst();
        cursor.close();
        database.close();
        return nameExists;
    }

    /**
     * Retrieves the top 10 Trivia scores from the database.
     * @return A list containing the top 10 TriviaScore instances.
     */
    public List<TriviaScore> getTop10score() {
        List<TriviaScore> scoreList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(
                TABLE_score,
                new String[]{COLUMN_ID,COLUMN_NAME, COLUMN_SCORE},
                null,
                null,
                null,
                null,
                COLUMN_SCORE + " DESC",
                "10"
        );

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") int scoreValue = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
                TriviaScore score = new TriviaScore(name, scoreValue);
                Log.d("ScoreDatabaseHelper", "Id: " + id + ", Name: " + name + ", Score: " + scoreValue);
//                score.add(new Score(name, scoreValue));
                score.setId(id);
                scoreList.add(score);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return scoreList;
    }

}
