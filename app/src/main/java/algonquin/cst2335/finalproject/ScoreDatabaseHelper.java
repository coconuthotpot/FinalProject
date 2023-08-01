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

public class ScoreDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "score.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_score = "score";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    private static final String CREATE_TABLE_score =
            "CREATE TABLE " + TABLE_score + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_SCORE + " INTEGER" +
                    ")";

    public ScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_score);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // Method to add a new score to the database
    public void addScore(Score score) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, score.getName());
        values.put(COLUMN_SCORE, score.getScore());
        database.insert(TABLE_score, null, values);
        database.close();
    }

    public void deleteScore(String name) {
        SQLiteDatabase database = getWritableDatabase();
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {String.valueOf(name)};
        database.delete(TABLE_score, selection, selectionArgs);
        database.close();

    }


    // Method to fetch the top 10 high score from the database
    public List<Score> getTop10score() {
        List<Score> score = new ArrayList<>();
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

                Log.d("ScoreDatabaseHelper", "Id: " + id + ", Name: " + name + ", Score: " + scoreValue);
                score.add(new Score(name, scoreValue));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return score;
    }
}
