package UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.net.URI;

import Utilities.CacheUtil;

/**
 * Created by etv on 11/26/2017.
 */

public class Dashboard extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView tv_username;
    private TextView tv_area;
    private TextView tv_name;
    private TextView tv_soldprice;
    private TextView tv_solditem;
    private TextView tv_instock;
    private ImageView profilepic;
    private TextView tv_storename;
    private TextView tv_storelocation;

    private String sn = "test";

    private DatabaseReference storeref = FirebaseDatabase.getInstance().getReference("StoreProfile");
    private DatabaseReference userref = FirebaseDatabase.getInstance().getReference("UserProfile");
    private ValueEventListener storeeventlistener;
    private ValueEventListener usereventlistener;


    public Dashboard() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.home_content,container,false);

        tv_username = (TextView)rootView.findViewById(R.id.tv_username);
        tv_area = (TextView)rootView.findViewById(R.id.tv_area);
        tv_name = (TextView)rootView.findViewById(R.id.tv_name);
        tv_soldprice = (TextView)rootView.findViewById(R.id.sales_hit);
        tv_solditem =(TextView)rootView.findViewById(R.id.tv_solditem);
        tv_instock = (TextView)rootView.findViewById(R.id.total_counter);
        tv_storename = (TextView)rootView.findViewById(R.id.tv_storename);
        tv_storelocation = (TextView) rootView.findViewById(R.id.tv_storelocation);

        profilepic = (ImageView)rootView.findViewById(R.id.iv_profile_pic);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        usereventlistener = userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                char[] chars = currentUser.getEmail().toCharArray();
                final StringBuilder hex = new StringBuilder();
                for (char ch : chars) {
                    hex.append(Integer.toHexString((int) ch));
                }

                if (dataSnapshot != null){

                    tv_username.setText(dataSnapshot.child(hex.toString()).child("email").getValue().toString());
                    tv_area.setText(dataSnapshot.child(hex.toString()).child("storenum").getValue().toString());
                    tv_name.setText(dataSnapshot.child(hex.toString()).child("sellersname").getValue().toString());
                    sn = dataSnapshot.child(hex.toString()).child("storenum").getValue().toString();
                    storeprofile(sn);

                } else {
                    tv_username.setText("");
                    tv_area.setText("");
                    tv_name.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        displayProfile(currentUser);

        return rootView;

    }

    public void storeprofile(final String storenum){
        storeeventlistener=storeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    if(dataSnapshot.hasChild(storenum)){
                        tv_instock.setText(dataSnapshot.child(storenum).child("icestock").getValue().toString());
                        tv_soldprice.setText(dataSnapshot.child(storenum).child("soldamount").getValue().toString());
                        tv_solditem.setText(dataSnapshot.child(storenum).child("numsoldice").getValue().toString());
                        tv_storename.setText(dataSnapshot.child(storenum).child("storename").getValue().toString());
                        tv_storelocation.setText(dataSnapshot.child(storenum).child("storelocation").getValue().toString());
                    }
                } else {
                    tv_instock.setText("fetching...");
                    tv_soldprice.setText("fetching...");
                    tv_solditem.setText("fetching...");
                    tv_storename.setText("");
                    tv_storelocation.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayProfile(FirebaseUser currentUser) {

        tv_name.setText(currentUser.getEmail());
        tv_username.setText(currentUser.getDisplayName());
        Uri uri = currentUser.getPhotoUrl();
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(10)
                .oval(true)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        Picasso.with(getContext())
                .load(uri)
                .transform(transformation)
                .into(profilepic);
    }

}
