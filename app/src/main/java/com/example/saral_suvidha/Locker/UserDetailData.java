package com.example.saral_suvidha.Locker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saral_suvidha.R;
import com.example.saral_suvidha.helper.Url;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailData extends AppCompatActivity {

    TextView name, f_Name, uid, email, phone, dob, uGen, uAdd, uNat, idProof, addProof, incType, annIn;

    public static final String API_URL = Url.API_URL.url+"select.php?uid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_data);

        name = findViewById(R.id.userName_userDetailData);
        f_Name = findViewById(R.id.userFname_userDetailData);
        uid = findViewById(R.id.userUid_userDetailData);
        email = findViewById(R.id.uEmail_userDetailData);
        phone = findViewById(R.id.uPhone_userDetailData);
        dob = findViewById(R.id.uDob_userDetailData);

        uGen = findViewById(R.id.uGen_userDetailData);
        uAdd = findViewById(R.id.uAdd_userDetailData);
        uNat = findViewById(R.id.nationality_userDetailData);
        idProof = findViewById(R.id.idProof_userDetailData);
        addProof = findViewById(R.id.addProof_userDetailData);
        incType = findViewById(R.id.incType_userDetailData);
        annIn = findViewById(R.id.annIncome_userDetailData);

        getJSON(API_URL);

    }

    private void getJSON(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url+ FirebaseAuth.getInstance().getCurrentUser().getUid(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseDishes(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), ""+error.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void parseDishes(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                String uName, fName, uEmail, uUid, uPhone, uDob, u_Gen, u_Add, u_Nat, u_IdProof, u_AddProof, u_IncType, u_AnnInc;

                uName = JO.getString("name");
                fName = JO.getString("father_name");
                uEmail = JO.getString("email");
                uUid = JO.getString("uid");
                uPhone = JO.getString("phone");
                uDob = JO.getString("dob");

                u_Gen = JO.getString("gender");
                u_Add = JO.getString("user_address");
                u_Nat = JO.getString("nationality");
                u_IdProof = JO.getString("id_proof");
                u_AddProof = JO.getString("address_proof");
                u_IncType = JO.getString("income_type");
                u_AnnInc = JO.getString("annual_income");

                name.setText(uName);
                f_Name.setText(fName);
                uid.setText(uUid);
                email.setText(uEmail);
                phone.setText(uPhone);
                dob.setText(uDob);

                if(u_Gen.equals("0")){
                    uGen.setText("female");
                }else{
                    uGen.setText("Male");
                }
                uAdd.setText(u_Add);
                uNat.setText(u_Nat);
                idProof.setText(u_IdProof);
                addProof.setText(u_AddProof);
                incType.setText(u_IncType);

                if(u_AnnInc.equals("annual_income")){
                    annIn.setText("No Income");
                }else{
                    annIn.setText(u_AnnInc);
                }

                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
