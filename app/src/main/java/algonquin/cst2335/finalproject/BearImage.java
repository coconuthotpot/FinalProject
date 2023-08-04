package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BearImage {
    @ColumnInfo(name="Width")
    private int width;

    @ColumnInfo(name="Height")
    private int height;
    @ColumnInfo(name="URL")
    private String imageUrl;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")
    public long id;

    public BearImage(int width, int height, String imageUrl) {
        this.width = width;
        this.height = height;
        this.imageUrl = imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

