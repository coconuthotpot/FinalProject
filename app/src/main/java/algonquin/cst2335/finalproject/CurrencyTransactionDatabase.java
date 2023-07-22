package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CurrencyTransaction.class}, version=1)
public abstract class CurrencyTransactionDatabase extends RoomDatabase {
    public abstract CurrencyTransactionDAO ctDAO();
}
