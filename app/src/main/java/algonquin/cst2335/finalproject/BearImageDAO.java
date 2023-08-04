package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * this is the DAO class
 */
@Dao
public interface BearImageDAO {
    /**
     * this is the function to add image
     * @param image the image to be added
     * @return return the id of it
     */
    @Insert
    public long insertImage(BearImage image);

    /**
     * this is the function to get all images
     * @return return a image list
     */
    @Query("Select * from BearImage")
    public List<BearImage> getAllImages();


    /**
     * this is the delete function
     * @param image the image to be deleted
     */
    @Delete
    void deleteImage(BearImage image);
}
