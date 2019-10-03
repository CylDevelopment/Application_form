package com.example.saral_suvidha.helper;

import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Validator {

    public Validator(Context context) {
        this.context = context;
    }

    Context context;

    public boolean validateEditText(EditText[] editTexts){
        boolean b = false;
        for(EditText editText : editTexts){
            if(editText.getText().toString().trim().equals("")) {
                Toast.makeText(context, "Please fill all fields...", Toast.LENGTH_SHORT).show();
                b = false;
            }
            else
                b = true;
        }
        return b;
    }
    /*public boolean validateSpinner(Spinner[] spinners){
        for (Spinner spinner: spinners) {
            if(spinner.getSelectedItemPosition() == 0) {
                Toast.makeText(context, "Please Select all fields...", Toast.LENGTH_SHORT).show();
            }else
                return true;
        }
        return false;
    }*/
}
