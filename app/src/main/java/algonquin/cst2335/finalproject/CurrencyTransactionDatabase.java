package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * an abstract class to establish database to store transactions from currency conversion
 */
@Database(entities = {CurrencyTransaction.class}, version=1)
public abstract class CurrencyTransactionDatabase extends RoomDatabase {

    /**
     * an abstract method to create a database access object under CurrencyTransactionDAO interface
     * to perform database related actions
     * @return a CurrencyTransactionDAO object
     */
    public abstract CurrencyTransactionDAO ctDAO();
}
