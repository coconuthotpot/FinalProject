package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class to establish variables for Currency Converter Module to be stored in database
 * @author Ka Yan Ieong
 * @version 1.0
 */
@Entity
public class CurrencyTransaction {

    /**
     * an id variable to indicate individual currency conversion
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    /**
     * a String variable to store original currency info
     */
    @ColumnInfo(name="Currency_From")
    protected String currencyFrom;

    /**
     * a String variable to store currency to be converted to
     */
    @ColumnInfo(name="Currency_To")
    protected String currencyTo;

    /**
     * a String variable to store the amount of money for conversion
     */
    @ColumnInfo(name="Amount_Input")
    protected String amountInput;

    /**
     * a String variable to store the amount of money converted in target currency after conversion
     */
    @ColumnInfo(name="Amount_Converted")
    protected String showAmount;

    /**
     * no-arg constructor to create CurrencyTransaction object
     */
    public CurrencyTransaction() {}

    /**
     * constructor to create CurrencyTransaction object with essential elements
     * @param currencyFrom original currency info
     * @param currencyTo currency to be converted to
     * @param amountInput the amount of money for conversion
     * @param showAmount the amount of money converted in target currency after conversion
     */
    public CurrencyTransaction(String currencyFrom, String currencyTo, String amountInput, String showAmount) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.amountInput = amountInput;
        this.showAmount = showAmount;
    }

    /**
     * method to store id auto-increment in database
     * @return Long id number for individual record
     */
    public long getId() {return id;}

    /**
     * method to store original currency info for database storage or retrieval
     * @return String original currency info
     */
    public String getCurrencyFrom() {return currencyFrom;}

    /**
     * method to store target currency info for database storage or retrieval
     * @return String target currency info
     */
    public String getCurrencyTo() {return currencyTo;}

    /**
     * method to store the amount of money for conversion for database storage or retrieval
     * @return String the amount of money for conversion
     */
    public String getAmountInput() {return amountInput;}

    /**
     * method to store the amount of money converted in target currency after conversion
     * for database storage or retrieval
     * @return String the amount of money converted in target currency after conversion
     */
    public String getShowAmount() {return showAmount;}

}
