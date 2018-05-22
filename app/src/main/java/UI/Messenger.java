package UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.evillari.sip.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by etv on 11/26/2017.
 */

public class Messenger extends Fragment {

    ListView listView;
    EditText editText;
    Button button;

    private DatabaseReference mDatabase;


    public Messenger() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.messenger_layout,container,false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) rootView.findViewById(R.id.view_messages);
        editText = (EditText)rootView.findViewById(R.id.et_message);
        button = (Button)rootView.findViewById(R.id.btn_message);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabase.child("setter").push().setValue(editText.getText().toString());
                editText.setText("");

            }
        });

        return rootView;
    }





}
