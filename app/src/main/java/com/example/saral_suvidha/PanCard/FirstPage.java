package com.example.saral_suvidha.PanCard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class FirstPage extends AppCompatActivity {

    Button btnNext;
    Spinner spinnerTitle;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    EditText userName;
    EditText fatherName;
    EditText userPhoneNo, userEMail, birthDate;

    DatePickerDialog.OnDateSetListener dateSetListener;

    public static final String INSERT_API = Url.API_URL.url +"insert_userdata.php";
    public static final String SELECT_API = Url.API_URL.url+"select_userdata.php?uid=";
    public static final String CHECK_API = Url.API_URL.url+"check_user.php?uid=";
    public static final String UPDATE_API = Url.API_URL.url+"update_userdata.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        btnNext = findViewById(R.id.btnNext);
        spinnerTitle = findViewById(R.id.spinnerTitle);

        userName = findViewById(R.id.edtTxt_UserName);
        fatherName = findViewById(R.id.edtTxt_fatherName);

        userPhoneNo = findViewById(R.id.edtTxt_mobileNo);
        userEMail = findViewById(R.id.edtTxt_emailId);
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        birthDate = findViewById(R.id.edtTxt_birthdate);

        userEMail.setText(firebaseUser.getEmail());
        userEMail.setEnabled(false);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "/" + month + "/" + day;
                birthDate.setText(date);
            }
        };

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(FirstPage.this, dateSetListener, calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        List<String> listTitle = new ArrayList<String>();
        listTitle.add("Select Title");
        listTitle.add("Mr");
        listTitle.add("Mrs");
        listTitle.add("Miss");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listTitle);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTitle.setAdapter(arrayAdapter);
        spinnerTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    spinnerTitle.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText[] editTexts = {userName, fatherName, userPhoneNo, userEMail, birthDate};

                if (new Validator(FirstPage.this).validateEditText(editTexts)) {
                    getJSONCheck(CHECK_API);
                    Intent intent = new Intent(FirstPage.this, SecondPage.class);
                    startActivity(intent);
                }

            }
        });

        getJSON(SELECT_API);
    }

    private void getJSON(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url + FirebaseAuth.getInstance().getCurrentUser().getUid(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response==null) {
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
                String uName, fName, uEmail, uTitle, uPhone, uDob;
                uTitle = JO.getString("title");
                uName = JO.getString("name");
                fName = JO.getString("father_name");
                uEmail = JO.getString("email");
                uPhone = JO.getString("phone");
                uDob = JO.getString("dob");

                if (uTitle.equals("Mr")) {
                    spinnerTitle.setSelection(1);
                } else if (uTitle.equals("Mrs")) {
                    spinnerTitle.setSelection(2);
                } else if(uTitle.equals("Miss")){
                    spinnerTitle.setSelection(3);
                }
                userName.setText(uName);
                fatherName.setText(fName);
                userEMail.setText(uEmail);
                userPhoneNo.setText(uPhone);
                birthDate.setText(uDob);

                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSONCheck(String apiUrl2) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                apiUrl2 + FirebaseAuth.getInstance().getCurrentUser().getUid() + "&table=userdata"+"&type=2",
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

                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void checkJSON(String url, final int key) {

        final int title = spinnerTitle.getSelectedItemPosition();
        final String uUserName = userName.getText().toString().trim();
        final String fFatherName = fatherName.getText().toString().trim();
        final String uPhoneNo = userPhoneNo.getText().toString().trim();
        //final String uEmail = userEMail.getText().toString().trim();
        final String uBirthdate = birthDate.getText().toString().trim();

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
                                Toast.makeText(FirstPage.this, "Error", Toast.LENGTH_SHORT).show();
                            else if(res.equals("1")){
                                if (key == 1)
                                    Toast.makeText(FirstPage.this, "Insert Success", Toast.LENGTH_SHORT).show();
                                else if(key == 0)
                                    Toast.makeText(FirstPage.this, "Update Success", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(FirstPage.this, ""+response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                    }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FirstPage.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("table","userdata");
                hashMap.put("type", "2");
                if (title == 1) {
                    hashMap.put("title", "Mr");
                }
                if (title == 2) {
                    hashMap.put("title", "Mrs");
                }
                if (title == 3) {
                    hashMap.put("title", "Miss");
                }

                hashMap.put("name", uUserName);
                hashMap.put("father_name", fFatherName);
                hashMap.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                hashMap.put("phone", uPhoneNo);
                hashMap.put("dob", uBirthdate);

                return hashMap;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}
