package com.example.saral_suvidha.PanCard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saral_suvidha.R;
import com.example.saral_suvidha.helper.Url;
import com.example.saral_suvidha.helper.Validator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondPage extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Button btnNext, btnPrevious;

    TextView txtResidentAddress;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;

    int gender;

    public static final String INSERT_API = Url.API_URL.url +"insert_address.php";
    public static final String SELECT_API = Url.API_URL.url+"select_address.php?uid=";
    public static final String CHECK_API = Url.API_URL.url+"check_user.php?uid=";
    public static final String UPDATE_API = Url.API_URL.url+"update_address.php";

    EditText houseNo, streetArea, city, pinCode, state, nationality;
    RadioButton genderMale, genderFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        txtResidentAddress = findViewById(R.id.txt_Resident_address);

        houseNo = findViewById(R.id.edtTxt_houseNo);
        streetArea = findViewById(R.id.edtTxt_street_Area);
        city = findViewById(R.id.edtTxt_city);
        state = findViewById(R.id.edtTxt_state);
        pinCode = findViewById(R.id.edtTxt_pinCode);
        nationality = findViewById(R.id.edtTxt_nationality);

        genderMale = findViewById(R.id.radio_gender_male);
        genderFemale = findViewById(R.id.radio_gender_female);

        btnNext =findViewById(R.id.btn_next_second_page);
        btnPrevious = findViewById(R.id.btn_pevious_second_page);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText[] editTexts = {houseNo, streetArea, city, state, pinCode, nationality};

                if(new Validator(SecondPage.this).validateEditText(editTexts)){
                    getJSONCheck(CHECK_API);

                    Intent intent = new Intent(SecondPage.this,ThirdPage.class);
                    startActivity(intent);
                }

            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        genderMale.setOnCheckedChangeListener(this);
        genderFemale.setOnCheckedChangeListener(this);


        getJSON(SELECT_API);

    }

    private void getJSON(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url + FirebaseAuth.getInstance().getCurrentUser().getUid(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            return;
                        } else {
                            parseJSON(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void parseJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);

                String uGender, uAddress, uNationality;
                uGender = JO.getString("gender");
                uAddress = JO.getString("user_address");
                uNationality = JO.getString("nationality");

                String[] add = uAddress.split(",");

                houseNo.setText(add[0]);
                streetArea.setText(add[1]);
                city.setText(add[2]);
                state.setText(add[3]);
                pinCode.setText(add[4]);

                if(uGender.equals("0")){
                    genderFemale.setChecked(true);
                }else if (uGender.equals("1")){
                    genderMale.setChecked(true);
                }

                nationality.setText(uNationality);

                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSONCheck(String apiUrl2) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                apiUrl2 + FirebaseAuth.getInstance().getCurrentUser().getUid() + "&table=address"+"&type=2",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject JO = jsonObject.getJSONArray("response").getJSONObject(0);
                            String res = JO.getString("response");
                            if (res.equals("1")) {
                                checkJSON(INSERT_API, 1);
                            } else {
                                checkJSON(UPDATE_API, 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SecondPage.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void checkJSON(String url, final int key) {

        final int uGender = gender;
        final String uHouseNo = houseNo.getText().toString().trim();
        final String uStreetArea = streetArea.getText().toString().trim();
        final String uCity = city.getText().toString().trim();
        final String uState = state.getText().toString().trim();
        final String uPincode = pinCode.getText().toString().trim();
        final String uNationality = nationality.getText().toString().trim();

        final String address = uHouseNo+","+uStreetArea+","+uCity+","+uState+","+uPincode;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject JO = jsonObject.getJSONArray("response").getJSONObject(0);
                            String res = JO.getString("response");
                            if (res.equals("0"))
                                Toast.makeText(SecondPage.this, "Error", Toast.LENGTH_SHORT).show();
                            else if(res.equals("1")){
                                if (key == 1)
                                    Toast.makeText(SecondPage.this, "Insert Success", Toast.LENGTH_SHORT).show();
                                else if(key == 0)
                                    Toast.makeText(SecondPage.this, "Update Success", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(SecondPage.this, ""+response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SecondPage.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("uid", uid);
                hashMap.put("type", "2");
                hashMap.put("table", "address");
                hashMap.put("gender", ""+ uGender);
                hashMap.put("user_address", address);
                hashMap.put("nationality", uNationality);
                return hashMap;
            }
        };
        Volley.newRequestQueue(SecondPage.this).add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id){
            case R.id.radio_gender_male:
                if(genderMale.isChecked()){
                    gender = 1;
                }

                break;
            case R.id.radio_gender_female:
                if(genderFemale.isChecked()){
                    gender = 0;
                }

                break;

        }
    }
}