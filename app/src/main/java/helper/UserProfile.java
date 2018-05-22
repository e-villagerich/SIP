package helper;

public class UserProfile {

    public String sellersname;
    public String email;
    public String location;
    public Integer storenum;
    public String storename;
    public String userrole;
    public String datecreated;
    public String  datemodified;
    public String comment;


    public UserProfile(){

    }

    public UserProfile(String sellersname, String email, String location, Integer storenum, String storename, String userrole, String datecreated, String datemodified, String comment){

        this.sellersname = sellersname;
        this.email = email;
        this.location = location;
        this.storenum = storenum;
        this.storename = storename;
        this.userrole = userrole;
        this.datecreated = datecreated;
        this.datemodified = datemodified;
        this.comment = comment;

    }


}
