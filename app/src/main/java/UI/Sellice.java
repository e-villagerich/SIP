package UI;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.evillari.sip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Utilities.AddMoreItemDialog;
import Utilities.CacheUtil;
import Utilities.JsonUtils;
import Utilities.NetworkUtils;
import Utilities.REQUEST;
import helper.StoreInventory;

import static java.lang.Integer.parseInt;

/**
 * Created by etv on 11/26/2017.
 */

public class Sellice extends Fragment {

    private String date="01/01/1980";

    private TextView tv_username;
    private TextView tv_area;
    private TextView tv_name;
    private TextView tv_title_line;
    private TextView tv_soldprice;
    private TextView tv_solditem;
    private TextView tv_instock;
    private ImageView profilepic;
    private String token ="";
    private String username = "";
    private Button btn_minus;
    private Button btn_plus;
    private Button submit_button;

    private DatabaseReference storeref = FirebaseDatabase.getInstance().getReference("StoreProfile");
    private DatabaseReference userref = FirebaseDatabase.getInstance().getReference("UserProfile");
    private DatabaseReference activityfeedsdbref = FirebaseDatabase.getInstance().getReference().child("ActivityFeeds");
    private DatabaseReference storeinventorydbref = FirebaseDatabase.getInstance().getReference().child("StoreInventory");

    public String storenum;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    EditText et_quantity;
    public Sellice() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.submit_layout, container,false);

        CacheUtil cu = new CacheUtil();

        submit_button = (Button)rootView.findViewById(R.id.submit_btn);
        btn_minus =(Button)rootView.findViewById(R.id.plus_button);
        btn_plus=(Button)rootView.findViewById(R.id.minus_button);
        et_quantity =(EditText)rootView.findViewById(R.id.input_q);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        et_quantity.setFilters(new InputFilter[]{new InputFilterMinMax(1, 12)});
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.format(c.getTime());

                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            char[] chars = currentUser.getEmail().toCharArray();
                            final StringBuilder hex = new StringBuilder();
                            for (char ch : chars) {
                                hex.append(Integer.toHexString((int) ch));
                            }
                            if(dataSnapshot.hasChild(hex.toString())){
                                storenum = dataSnapshot.child(hex.toString()).child("storenum").getValue().toString();
                                updatestoreprofile(storenum, et_quantity.getText().toString(), date);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });



        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_quantity.getText().toString().matches(""))  {

                    if((parseInt(et_quantity.getText().toString()) >= 1) &&  (parseInt(et_quantity.getText().toString()) < 12)) {
                        et_quantity.setText(String.valueOf(parseInt(et_quantity.getText().toString()) + 1));
                    }

                } else {
                    et_quantity.setText("1");
                }
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_quantity.getText().toString().matches(""))  {

                    if((parseInt(et_quantity.getText().toString()) > 1) &&  (parseInt(et_quantity.getText().toString()) <= 12)) {
                        et_quantity.setText(String.valueOf(parseInt(et_quantity.getText().toString()) - 1));
                    }

                } else {
                    et_quantity.setText("1");
                }

            }
        });

        return rootView;
    }

    private void updatestoreprofile(final String storenum, final String input, final String adate){
        storeref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if(dataSnapshot.hasChild(storenum)){

                        //update 'icestock', 'numsoldice', 'soldamount',
                        //get 'iceprice'

                        Integer icestock = parseInt(dataSnapshot.child(storenum).child("icestock").getValue().toString());
                        Integer numsoldice = parseInt(dataSnapshot.child(storenum).child("numsoldice").getValue().toString());
                        Double soldamount = Double.parseDouble(dataSnapshot.child(storenum).child("soldamount").getValue().toString());
                        Double iceprice = Double.parseDouble(dataSnapshot.child(storenum).child("iceprice").getValue().toString());

                        Integer newstock = icestock - parseInt(input);
                        Integer newnumsoldice = numsoldice + parseInt(input);
                        Double newsoldamount = Double.parseDouble(input) * iceprice + soldamount;

                        storeref.child(storenum).child("icestock").setValue(newstock.toString());
                        storeref.child(storenum).child("numsoldice").setValue(newnumsoldice.toString());
                        storeref.child(storenum).child("soldamount").setValue(newsoldamount.toString());

                        StoreInventory storeInventory = new StoreInventory(currentUser.getEmail().toString(), parseInt(storenum),"SellIce", icestock, 0,iceprice, parseInt(input),Double.parseDouble(input)*iceprice,newstock,adate );
                        storeinventorydbref.push().setValue(storeInventory);

                        String details = "In Stock: " + newstock.toString() + " | No. Items Sold: " + newnumsoldice.toString() + " | Amount Sold: " + newsoldamount.toString();
                        //"In Stock: " + result[2] + " | No. Items Sold: " + result[3] + " | Amount Sold: " + result[4]
                        if(currentUser.getPhotoUrl()!=null){
                            ActivityFeeds activityFeeds = new ActivityFeeds(currentUser.getEmail().toString(),currentUser.getPhotoUrl().toString(),"Ice(s) Sold from Store: " + storenum , details, adate);
                            activityfeedsdbref.push().setValue(activityFeeds);
                        } else {
                            ActivityFeeds activityFeeds = new ActivityFeeds(currentUser.getEmail().toString(),"PhotoURL","Ice(s) Sold from Store: " + storenum ,details , adate);
                            activityfeedsdbref.push().setValue(activityFeeds);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = parseInt(min);
            this.max = parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
