package Utilities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evillari.sip.R;

/**
 * Created by etv on 9/5/2017.
 */

public class AddMoreStock extends android.support.v4.app.DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText inputText;
    private Button btn_stock;
    private Button btn_cancel;

    public AddMoreStock(){

    }

    @Override
    public void onClick(View v) {

        if (v.equals(btn_stock)) {
            AddMoreStockListener activity = (AddMoreStockListener) getActivity();
            activity.onFinishInput(inputText.getText().toString());
            this.dismiss();
        }

        if (v.equals(btn_cancel)) {

            this.dismiss();
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId){
            AddMoreStockListener activity = (AddMoreStockListener)getActivity();
            activity.onFinishInput(inputText.getText().toString());
            this.dismiss();
            return true;
        }

        return false;
    }


    public interface AddMoreStockListener{
        void onFinishInput(String input);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addmorestock,container);
        inputText = (EditText) view.findViewById(R.id.et_stock_input);
        btn_stock=(Button)view.findViewById(R.id.btn_addstock);
        btn_cancel=(Button)view.findViewById(R.id.btn_cancel_addstock);
        inputText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});
        inputText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        inputText.setOnEditorActionListener(this);
        btn_stock.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        return view;

    }

    private class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
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
