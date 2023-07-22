package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface CurrencyTransactionDAO {

    @Insert
    public long insertTransaction(CurrencyTransaction t);

    @Query("Select * from CurrencyTransaction")
    public List<CurrencyTransaction> getAllTransactions();

    @Delete
    public void deleteTransaction(CurrencyTransaction t);
}
