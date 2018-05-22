package helper;

public class StoreInventory {

    public String sellersname;
    public Integer storenum;
    public String activity;
    public Integer curstocks;
    public Integer stocksadded;
    public double itemprice;
    public Integer itemsold;
    public double soldprice;
    public Integer stocksleft;
    public String activitydate;


    public StoreInventory(){

    }

    public StoreInventory( String sellersname, Integer storenum, String activity, Integer curstocks, Integer stocksAdded, double itemprice, Integer itemsold, double soldprice, Integer stocksleft, String activitydate){

        this.sellersname =sellersname;
        this.storenum = storenum;
        this.activity = activity;
        this.curstocks = curstocks;
        this.stocksadded = stocksAdded;
        this.itemprice = itemprice;
        this.itemsold = itemsold;
        this.soldprice = soldprice;
        this.stocksleft = stocksleft;
        this.activitydate=activitydate;
    }


}
