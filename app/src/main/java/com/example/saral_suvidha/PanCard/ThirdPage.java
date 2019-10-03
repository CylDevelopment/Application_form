package com.example.saral_suvidha.PanCard;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saral_suvidha.R;
import com.example.saral_suvidha.adapters.SpinnerAdapter;
import com.example.saral_suvidha.helper.Url;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdPage extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Button btnPrev, btnSubmit;
    String incomeType;
    Spinner spinnerID, spinnerAddress, spinnerIncome;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    CheckBox confirmation;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;

    public static final String INSERT_API = Url.API_URL.url +"insert_proof.php";
    public static final String SELECT_API = Url.API_URL.url+"select_proof.php?uid=";
    public static final String CHECK_API = Url.API_URL.url+"check_user.php?uid=";
    public static final String UPDATE_API = Url.API_URL.url+"update_proof.php";

    List<String> addproof, idProof, annualIncome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);

        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        confirmation = findViewById(R.id.checkbox_declaration_confirmation);

        btnPrev = findViewById(R.id.btn_previous_third_page);
        btnSubmit = findViewById(R.id.btn_submit_third_page);
        spinnerID = findViewById(R.id.spinner_idProof);
        spinnerAddress = findViewById(R.id.spinner_address_proof);
        spinnerIncome = findViewById(R.id.spinner_income);

        radioButton1 = findViewById(R.id.tv_radiobutton1);
        radioButton2 = findViewById(R.id.tv_radiobutton2);
        radioButton3 = findViewById(R.id.tv_radiobutton3);
        radioButton4 = findViewById(R.id.tv_radiobutton4);
        radioButton5 = findViewById(R.id.tv_radiobutton5);

        idProof = new ArrayList<String>();
        idProof.add("Select");
        idProof.add("Voters Identity Crad");
        idProof.add("Driving License");
        idProof.add("Passport");
        idProof.add("Electricity Bill");
        idProof.add("Consumer Gas Connection");
        idProof.add("Ration Card with photograph");
        idProof.add("Post office pass book");
        idProof.add("Property Registration Document");
        idProof.add("Aadhar Card issued by UIDAI");
        SpinnerAdapter dataAdapter = new SpinnerAdapter(ThirdPage.this, idProof);
        spinnerID.setAdapter(dataAdapter);

        spinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    spinnerID.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addproof = new ArrayList<>();
        addproof.add("Select");
        addproof.add("Voters Identity Card");
        addproof.add("Driving License");
        addproof.add("Passport");
        addproof.add("Electricity Bill");
        addproof.add("Consumer Gas Connection");
        addproof.add("Ration Card with photograph");
        addproof.add("Post office pass book");
        addproof.add("Property Registration Document");
        addproof.add("Aadhar Card issued by UIDAI");
        final SpinnerAdapter dataAdapter1 = new SpinnerAdapter(this, addproof);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                spinnerAddress.setAdapter(dataAdapter1);
            }
        });
        spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    spinnerAddress.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerIncome.setEnabled(false);
        radioButton1.setChecked(true);

        annualIncome = new ArrayList<>();
        annualIncome.add("Annual Income");
        annualIncome.add("Less Than 3,00,000");
        annualIncome.add("Between 3,00,000 to 5,00,000");
        annualIncome.add("Between 5,00,000 to 8,00,000");
        annualIncome.add("Greater than 8,00,000");

        SpinnerAdapter dataAdapter3 = new SpinnerAdapter(this, annualIncome);
        spinnerIncome.setAdapter(dataAdapter3);
        spinnerIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                } else {
                    spinnerIncome.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioButton2.setOnCheckedChangeListener(this);
        radioButton1.setOnCheckedChangeListener(this);
        radioButton3.setOnCheckedChangeListener(this);
        radioButton4.setOnCheckedChangeListener(this);
        radioButton5.setOnCheckedChangeListener(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (confirmation.isChecked()) {
                    getJSONCheck(CHECK_API);
                } else {
                    Toast.makeText(ThirdPage.this, "Please Confirm before Submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        if (response.equals("null")) {
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
                String u_Id, uAdd, uIncType, uAnnInc;

                u_Id = JO.getString("id_proof");
                uAdd = JO.getString("address_proof");
                uIncType = JO.getString("income_type");
                uAnnInc = JO.getString("annual_income");


                switch (u_Id) {
                    case "Voters Identity Card":
                        spinnerID.setSelection(1);
                        break;
                    case "Driving License":
                        spinnerID.setSelection(2);
                        break;
                    case "Passport":
                        spinnerID.setSelection(3);
                        break;
                    case "Electricity Bill":
                        spinnerID.setSelection(4);
                        break;
                    case "Consumer Gas Connection":
                        spinnerID.setSelection(5);
                        break;
                    case "Ration Card with photograph":
                        spinnerID.setSelection(6);
                        break;
                    case "Post office pass book":
                        spinnerID.setSelection(7);
                        break;
                    case "Property Registration Document":
                        spinnerID.setSelection(8);
                        break;
                    case "Aadhar Card issued by UIDAI":
                        spinnerID.setSelection(9);
                        break;
                }

                switch (uAdd) {
                    case "Voters Identity Card":
                        spinnerAddress.setSelection(1);
                        break;
                    case "Driving License":
                        spinnerAddress.setSelection(2);
                        break;
                    case "Passport":
                        spinnerAddress.setSelection(3);
                        break;
                    case "Electricity Bill":
                        spinnerAddress.setSelection(4);
                        break;
                    case "Consumer Gas Connection":
                        spinnerAddress.setSelection(5);
                        break;
                    case "Ration Card with photograph":
                        spinnerAddress.setSelection(6);
                        break;
                    case "Post office pass book":
                        spinnerAddress.setSelection(7);
                        break;
                    case "Property Registration Document":
                        spinnerAddress.setSelection(8);
                        break;
                    case "Aadhar Card issued by UIDAI":
                        spinnerAddress.setSelection(9);
                        break;
                }

                switch (uIncType) {
                    case "No Income":
                        radioButton1.setChecked(true);
                        break;
                    case "Business/Profession":
                        radioButton2.setChecked(true);
                        break;
                    case "Salary":
                        radioButton3.setChecked(true);
                        break;
                    case "House Property":
                        radioButton4.setChecked(true);
                        break;
                    case "Other Sources":
                        radioButton5.setChecked(true);
                        break;
                }

                switch (uAnnInc) {
                    case "Annual Income":
                        spinnerIncome.setSelection(0);
                        spinnerIncome.setEnabled(false);
                        break;
                    case "Less Than 3,00,000":
                        spinnerIncome.setSelection(1);
                        break;
                    case "Between 3,00,000 to 5,00,000":
                        spinnerIncome.setSelection(2);
                        break;
                    case "Between 5,00,000 to 8,00,000":
                        spinnerIncome.setSelection(3);
                        break;
                    case "Greater than 8,00,000":
                        spinnerIncome.setSelection(4);
                        break;
                }
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSONCheck(String apiUrl2) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                apiUrl2 + FirebaseAuth.getInstance().getCurrentUser().getUid() + "&table=proof" + "&type=2",
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
        int posId = spinnerID.getSelectedItemPosition();
        int posAdd = spinnerAddress.getSelectedItemPosition();
        int posInc = spinnerIncome.getSelectedItemPosition();
        final String id_proof = idProof.get(posId).trim();
        final String add_proof = addproof.get(posAdd).trim();
        final String inc_Type = incomeType;
        final String inc_proof = annualIncome.get(posInc).trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject JO = jsonObject.getJSONArray("response").getJSONObject(0);
                            String res = JO.getString("response");
                            if (res.equals("0")) {
                                Toast.makeText(ThirdPage.this, "Error", Toast.LENGTH_SHORT).show();
                            } else if(res.equals("1")){
                                if (key == 1)
                                    Toast.makeText(ThirdPage.this, "Insert Success", Toast.LENGTH_SHORT).show();
                                else if(key == 0)
                                    Toast.makeText(ThirdPage.this, "Update Success", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(ThirdPage.this, ""+response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThirdPage.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("type", "2");
                hashMap.put("table","proof");
                hashMap.put("id_proof", id_proof);
                hashMap.put("address_proof", add_proof);
                hashMap.put("income_type", inc_Type);
                hashMap.put("annual_income", inc_proof);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.tv_radiobutton1:
                incomeType = radioButton1.getText().toString().trim();
                if (radioButton1.isChecked())
                    spinnerIncome.setEnabled(false);
                break;
            case R.id.tv_radiobutton2:
                incomeType = radioButton2.getText().toString().trim();
                if (radioButton2.isChecked())
                    spinnerIncome.setEnabled(true);

                break;
            case R.id.tv_radiobutton3:
                incomeType = radioButton3.getText().toString().trim();
                if (radioButton3.isChecked())
                    spinnerIncome.setEnabled(true);

                break;
            case R.id.tv_radiobutton4:
                incomeType = radioButton4.getText().toString().trim();
                if (radioButton4.isChecked())
                    spinnerIncome.setEnabled(true);
                break;
            case R.id.tv_radiobutton5:
                incomeType = radioButton5.getText().toString().trim();
                if (radioButton5.isChecked())
                    spinnerIncome.setEnabled(true);
                break;
        }
    }

}

