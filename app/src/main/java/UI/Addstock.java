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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evillari.sip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static java.lang.Integer.parseInt;

/**
 * Created by etv on 11/26/2017.
 */

public class Addstock extends Fragment {


    private EditText inputText;
    private Button btn_stock;
    private Button btn_cancel;
    private String date="01/01/1980";




    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    private DatabaseReference storeref = FirebaseDatabase.getInstance().getReference("StoreProfile");
    private DatabaseReference userref = FirebaseDatabase.getInstance().getReference("UserProfile");


    private DatabaseReference storeinventorydbref = FirebaseDatabase.getInstance().getReference().child("StoreInventory");

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String sn;
    private Integer previousstock=0;
    public Addstock(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.addmorestock,container,false);




        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ActivityFeeds");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        inputText = (EditText) rootView.findViewById(R.id.et_stock_input);
        btn_stock=(Button)rootView.findViewById(R.id.btn_addstock);
        btn_cancel=(Button)rootView.findViewById(R.id.btn_cancel_addstock);

        inputText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});
        inputText.requestFocus();

        btn_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e){

                }

                onFinishInput(inputText.getText().toString());
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e){

                }

            }
        });


        return rootView;
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

    public void onFinishInput (final String input) {

        if (!input.equals("")) {

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(c.getTime());


            Log.d("ADDING STOCK DATE: => ", date);

            Log.d("calling uri @addstock", "");

            userref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){
                        char[] chars = currentUser.getEmail().toCharArray();
                        final StringBuilder hex = new StringBuilder();
                        for (char ch : chars) {
                            hex.append(Integer.toHexString((int) ch));
                        }
                        sn = dataSnapshot.child(hex.toString()).child("storenum").getValue().toString();
                        updatestoreprofile(sn, input, date);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        } else {


            AddMoreItemDialog amd = new AddMoreItemDialog();
            amd.message = "Stocks: Invalid amount.";
            FragmentManager fm = getChildFragmentManager();
            amd.show(fm, "Solar-ICE Progam");

        }

    }



    public void updatestoreprofile(final String storenum, final String input, final String adate){
        storeref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if(dataSnapshot.hasChild(storenum)){

                        // update 'icestock';
                        Integer currenstock = parseInt(dataSnapshot.child(storenum).child("icestock").getValue().toString());
                        Integer icestock = parseInt(input) + currenstock;

                        String solditems = dataSnapshot.child(storenum).child("numsoldice").getValue().toString();
                        String amountsold = dataSnapshot.child(storenum).child("soldamount").getValue().toString();

                        storeref.child(storenum).child("icestock").setValue(icestock);
                        StoreInventory storeInventory = new StoreInventory(currentUser.getEmail().toString(), parseInt(storenum), "AddStock",currenstock, parseInt(input), Double.parseDouble(dataSnapshot.child(storenum).child("iceprice").getValue().toString()),parseInt(dataSnapshot.child(storenum).child("numsoldice").getValue().toString()),Double.parseDouble(dataSnapshot.child(storenum).child("soldamount").getValue().toString()),icestock, adate);
                        storeinventorydbref.push().setValue(storeInventory);
                        if (currentUser.getPhotoUrl()!=null){

                            ActivityFeeds activityFeeds = new ActivityFeeds(currentUser.getDisplayName(), currentUser.getPhotoUrl().toString(),"Has added cool Ice @from store " + storenum, "In Stock: " + icestock.toString() + " | Sold Items: " + solditems + " | Amount Sold: " + amountsold, adate);
                            databaseReference.push().setValue(activityFeeds);
                        } else {

                            ActivityFeeds activityFeeds = new ActivityFeeds(currentUser.getDisplayName(), "PhotoURL","Has added cool Ice @from store " + storenum, "In Stock: " + icestock.toString() + " | Sold Items: " + solditems + " | Amount Sold: " + amountsold, adate);
                            databaseReference.push().setValue(activityFeeds);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




}
