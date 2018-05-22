package helper;

public class StoreProfile {

    public String storename;
    public Integer storenum;
    public String storelocation;
    public Double iceprice;
    public Integer numsoldice;
    public Double soldamount;
    public Integer icestock;
    public String updatedate;

    public StoreProfile(){

    }

    public StoreProfile(String storename, Integer storenum, String storelocation, Double iceprice, Integer numsoldice, Double soldamount, Integer icestock, String updatedate) {
        this.storename = storename;
        this.storenum = storenum;
        this.storelocation = storelocation;
        this.iceprice = iceprice;
        this.numsoldice = numsoldice;
        this.soldamount = soldamount;
        this.icestock = icestock;
        this.updatedate = updatedate;
    }
}
