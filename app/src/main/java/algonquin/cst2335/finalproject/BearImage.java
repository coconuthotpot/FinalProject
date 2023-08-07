package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * this class present the object of bear image
 */
@Entity
public class BearImage {
    /**
     * the width of the image
     */
    @ColumnInfo(name="Width")
    String width;

    /**
     * the height of the image
     */
    @ColumnInfo(name="Height")
    String height;

    /**
     * the time that image generated
     */
    @ColumnInfo(name = "timeGenerated")
    protected String timeGenerated;


    /**
     *the id of the image
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")
    public long id;

    /**
     *the no arg constructor
     */
    public BearImage(){}

    /**
     * this is the constructor
     * @param timeGenerated  the time image generater
     * @param width  the width of image
     * @param height the height of image
     */
    public BearImage(String timeGenerated,String width, String height) {
        this.timeGenerated= timeGenerated;
        this.width = width;
        this.height = height;

    }

    /**
     * get the width of the image
     * @return return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * set the width of image
     * @param width the width to be setted
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * get the height of the image
     * @return return the height of the image
     */
    public String getHeight() {
        return height;
    }

    /**
     * set the height of the image
     * @param height return the height of image
     */
    public void setHeight(String height) {
        this.height = height;
    }


    /**
     * get the time of image generated
     * @return return the time of image generated
     */
    public String getTimeGenerated() {
        return timeGenerated;
    }

}

