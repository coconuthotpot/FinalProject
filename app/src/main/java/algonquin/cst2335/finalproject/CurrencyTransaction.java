package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrencyTransaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="Currency_From")
    protected String currencyFrom;

    @ColumnInfo(name="Currency_To")
    protected String currencyTo;

    @ColumnInfo(name="Amount_Input")
    protected String amountInput;

    @ColumnInfo(name="Amount_Converted")
    protected String showAmount;

    public CurrencyTransaction() {}

    public CurrencyTransaction(String currencyFrom, String currencyTo, String amountInput, String showAmount) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.amountInput = amountInput;
        this.showAmount = showAmount;
    }

    public String getCurrencyFrom() {return currencyFrom;}

    public String getCurrencyTo() {return currencyTo;}

    public String getAmountInput() {return amountInput;}

    public String getShowAmount() {return showAmount;}

}
