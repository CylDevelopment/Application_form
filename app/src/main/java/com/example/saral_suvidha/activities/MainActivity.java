package com.example.saral_suvidha.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.saral_suvidha.Authentication.LoginActivity;
import com.example.saral_suvidha.Locker.PinCreation;
import com.example.saral_suvidha.Locker.EnterPin;
import com.example.saral_suvidha.PanCard.FirstPage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.example.saral_suvidha.R;
import com.example.saral_suvidha.UserProfile.ProfileActivity;
import com.example.saral_suvidha.adapters.RecyclerViewAdapter;
import com.example.saral_suvidha.helper.SessionManagement;
import com.example.saral_suvidha.modal.Aadhar;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Aadhar> lstAadhar;
    CircleImageView imgCurrentUser;
    TextView currentUserName;
    TextView currentUserEmail;

    SharedPreferences sharedPreferences;
    String pinPassword;

    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView ryrv;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("PREFERENCES", 0);
        pinPassword = sharedPreferences.getString("pinPassword", "");

        shimmerFrameLayout = findViewById(R.id.shimmer_view);
        shimmerFrameLayout.startShimmer();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String current_uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(current_uid);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                ryrv.setVisibility(View.VISIBLE);
            }
        },2500);

        //recycler view and drawer implementation
        lstAadhar = new ArrayList<>();
        lstAadhar.add(new Aadhar("Adhar Card"," Aadhar"," Aadhar",R.drawable.aadhar));
        lstAadhar.add(new Aadhar("PAN Card"," Aadhar"," Aadhar",R.drawable. panc));
        lstAadhar.add(new Aadhar("Cast Certificate"," Aadhar"," Aadhar",R.drawable.cast));
        lstAadhar.add(new Aadhar("Income Certificate"," Aadhar"," Aadhar",R.drawable.income));
        lstAadhar.add(new Aadhar("Domicile Certificate"," Aadhar"," Aadhar",R.drawable.house));
        lstAadhar.add(new Aadhar("Passport"," Aadhar"," Aadhar",R.drawable.pass));
        lstAadhar.add(new Aadhar("Driving License"," Aadhar"," Aadhar",R.drawable.driving));
        lstAadhar.add(new Aadhar("Electric Service "," Aadhar"," Aadhar",R.drawable.electric));
        lstAadhar.add(new Aadhar("Property Tax Online Payment "," Aadhar"," Aadhar",R.drawable.tax));

        ryrv = findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstAadhar, new RecyclerViewAdapter.RecyclerOnClickListner() {
            @Override
            public void onItemClick(int i) {

                switch (i){
                    case 0:
                        Toast.makeText(MainActivity.this, "Aadhar Card",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent intent = new Intent(MainActivity.this, FirstPage.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Cast Certificate ",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Income Certificate",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "Domicile Certificate",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, "Passport ",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(MainActivity.this, "Driving License ",Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(MainActivity.this, "Electric Service ",Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(MainActivity.this, " Property Tax Online Payment",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        ryrv.setLayoutManager(new GridLayoutManager(this,2));
        ryrv.setHasFixedSize(true);

        ryrv.setAdapter(myAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navHeaderInfo(firebaseUser);
    }

    public void navHeaderInfo(FirebaseUser user){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imgCurrentUser = headerView.findViewById(R.id.img_current_user);
        currentUserName = headerView.findViewById(R.id.txtName_current_user);
        currentUserEmail = headerView.findViewById(R.id.txtEmail_current_user);

        DatabaseReference re = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String img = dataSnapshot.child("image").getValue().toString();

                if(img.equals("default")){
                    imgCurrentUser.setImageDrawable(getResources().getDrawable(R.drawable.register_user_dummy));
                }
                else {
                    Picasso.get().load(img).into(imgCurrentUser);
                }

                currentUserName.setText(name);
                currentUserEmail.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error Loading User Details", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_service) {

        } else if (id == R.id.nav_locker) {

            SessionManagement sessionManagement = new SessionManagement(this);
            if(sessionManagement.isNotNew()){
                Intent intent = new Intent(MainActivity.this, EnterPin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else{
                Intent intent = new Intent(MainActivity.this, PinCreation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }else if (id == R.id.nav_friend) {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Product Of CYL";
                String shareSubject = "CYL";
                shareIntent.putExtra(Intent.EXTRA_INTENT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(shareIntent, "Share Using..."));
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if (id == R.id.nav_rate) {

            try{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.android.chrome"))); //replace "com.android.chrome" with getPackageName();
            } catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void Search_Layout(View view){
        startActivity(new Intent(MainActivity.this, Search_Layout.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.startShimmer();
    }
}
