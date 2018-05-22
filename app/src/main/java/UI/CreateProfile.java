package UI;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.evillari.sip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import helper.StoreProfile;
import helper.UserProfile;

import static java.lang.Integer.parseInt;

/**
 * Created by etv on 2/1/2018.
 */

public class CreateProfile extends Fragment {

    private EditText sellersname, email, location, storenum, storename, comment;
    private Button submit;
    private RadioGroup userrole;
    private String role;
    private Integer sn;
    private DatabaseReference mDatabase,ref;

    private DatabaseReference storeref = FirebaseDatabase.getInstance().getReference("StoreProfile");
    private DatabaseReference userref = FirebaseDatabase.getInstance().getReference("UserProfile");

    private boolean storeexist = false;
    private boolean userexist = false;

    public CreateProfile(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_profile, container, false);

        sellersname = (EditText) rootView.findViewById(R.id.et_name);
        email = (EditText) rootView.findViewById(R.id.et_email);
        location = (EditText) rootView.findViewById(R.id.et_location);
        storenum = (EditText) rootView.findViewById(R.id.et_store_num);

        storename = (EditText) rootView.findViewById(R.id.et_store_name);
        comment = (EditText)rootView.findViewById(R.id.et_profile_comment);
        submit = (Button)rootView.findViewById(R.id.btn_create_profile);

        userrole = (RadioGroup) rootView.findViewById(R.id.rg_role);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        userrole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_isuser:
                        role = "user";
                        break;
                    case R.id.rb_isadmin:
                        role = "admin";
                        break;
                    default:
                        role = "";
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sellersname.getText().toString().matches("") && !email.getText().toString().matches("") && !location.getText().toString().matches("") && !storenum.getText().toString().matches("") && !storename.getText().toString().matches("") && !comment.getText().toString().matches("") && (userrole.getCheckedRadioButtonId() != -1)){
                    final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    final Date date = new Date();

                    storeref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                if(dataSnapshot.hasChild(storenum.getText().toString())){
                                    storeexist = true;
                                } else {
                                    storeexist = false;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    userref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot!=null){
                                char[] chars = email.getText().toString().toCharArray();
                                final StringBuilder hex = new StringBuilder();
                                for (char ch : chars) {
                                    hex.append(Integer.toHexString((int) ch));
                                }
                                if(dataSnapshot.hasChild(hex.toString())){
                                    userexist = true;
                                } else {
                                    userexist = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    if(userexist == false ) {
                        UserProfile userProfile = new UserProfile(sellersname.getText().toString(), email.getText().toString(), location.getText().toString(), parseInt(storenum.getText().toString()), storename.getText().toString(), role, dateFormat.format(date), dateFormat.format(date), comment.getText().toString());
                        char[] chars = email.getText().toString().toCharArray();
                        StringBuilder hex = new StringBuilder();
                        for (char ch : chars) {
                            hex.append(Integer.toHexString((int) ch));
                        }

                        mDatabase.getDatabase().getReference("UserProfile").child(hex.toString()).setValue(userProfile);

                        if(storeexist == false){
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            ref = db.getReference("StoreProfile");

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(storenum.getText().toString())){

                                    } else {

                                        StoreProfile storeProfile = new StoreProfile(storename.getText().toString(), parseInt(storenum.getText().toString()),location.getText().toString(), 2.00, 0,0.00,0,dateFormat.format(date));
                                        ref.child(storenum.getText().toString()).setValue(storeProfile);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    } else {

                        //TODO
                    }

                } else {

                }

            }
        });

        return rootView;

    }
}
