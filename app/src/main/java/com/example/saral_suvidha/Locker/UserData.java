package com.example.saral_suvidha.Locker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
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

public class UserData extends AppCompatActivity {

    TextView moreInfo;
    TextView name, email, phone;

    LinearLayout ll1, ll2, ll3;

    public static final String API_URL = Url.API_URL.url+"select.php?uid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        name = findViewById(R.id.userName1);
        email = findViewById(R.id.userEmail_userData);
        phone = findViewById(R.id.userPhone_userData);

        ll1 = findViewById(R.id.ll_name);
        ll2 = findViewById(R.id.ll_email);
        ll3 = findViewById(R.id.ll_phone);

        moreInfo = findViewById(R.id.moreInfo);

        getJSON(API_URL);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserData.this, UserDetailData.class);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(ll1, "trans_name");
                pairs[1] = new Pair<View, String>(ll2, "trans_email");
                pairs[2] = new Pair<View, String>(ll3, "trans_phone");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserData.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });
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

    private void parseDishes(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            while(count < jsonArray.length()){
                JSONObject JO = jsonArray.getJSONObject(count);
                String uName, fName, uEmail, uUid, uPhone, uDob;

                uName = JO.getString("name");
                fName = JO.getString("father_name");
                uEmail = JO.getString("email");
                uUid = JO.getString("uid");
                uPhone = JO.getString("phone");
                uDob = JO.getString("dob");

                name.setText(uName);
                email.setText(uEmail);
                phone.setText(uPhone);

                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
