package UI;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.evillari.sip.R;
import com.evillari.sip.loginmain;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by etv on 11/26/2017.
 */

public class Logout extends Fragment {

    TextView textView;
    Button button;

    public Logout() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.logout,container,false);




        textView = (TextView)rootView.findViewById(R.id.tv_confirm_logout);
        button = (Button)rootView.findViewById(R.id.btn_yes_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), loginmain.class);
                intent.putExtra("logout", "logout");
                startActivity(intent);
                getActivity().finish();

            }
        });

        return rootView;

    }

}
