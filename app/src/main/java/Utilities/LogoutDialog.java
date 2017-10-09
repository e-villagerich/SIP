package Utilities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.evillari.sip.R;

/**
 * Created by etv on 9/6/2017.
 */

public class LogoutDialog extends DialogFragment implements View.OnClickListener {

    Button btn_yes;
    Button btn_no;

    public LogoutDialog(){

    }

    @Override
    public void onClick(View v) {

        LogoutDialogListener activity = (LogoutDialogListener) getActivity();

        if (v.equals(btn_yes)) {
            activity.onLogout(true);
            this.dismiss();
        }
        if (v.equals(btn_no)){
            activity.onLogout(false);
            this.dismiss();
        }

    }

    public interface LogoutDialogListener {
        void onLogout(Boolean logout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = (inflater.inflate(R.layout.logout,container));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        btn_yes = (Button) view.findViewById(R.id.btn_yes_logout);
        btn_no = (Button) view.findViewById(R.id.btn_no_logout);
        btn_yes.setOnClickListener(this);
        btn_no.setOnClickListener(this);

        return view;

    }
}
