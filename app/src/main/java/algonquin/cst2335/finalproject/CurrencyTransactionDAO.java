package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interface to establish sql statements to be implemented in corresponding java methods or collections
 * establish database access object through this interface
 */
@Dao
public interface CurrencyTransactionDAO {

    /**
     * method to insert a record to database
     * @param t a CurrencyTransaction class object
     * @return Long value of instance id generated in database
     */
    @Insert
    public long insertTransaction(CurrencyTransaction t);

    /**
     * method to retrieve all records in CurrencyTransaction table in database
     * and store to CurrencyTransaction object List Collection
     * @return a List of CurrencyTransaction object
     */
    @Query("Select * from CurrencyTransaction")
    public List<CurrencyTransaction> getAllTransactions();

    /**
     * method to delete a record from database
     * @param t a CurrencyTransaction class object
     */
    @Delete
    public void deleteTransaction(CurrencyTransaction t);
}
