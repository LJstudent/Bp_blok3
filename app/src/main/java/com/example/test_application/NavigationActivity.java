package com.example.test_application;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

// navigation activity hier worden de fragments aangeroepen als de gebruiker op de balk klikt
public class NavigationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Bundle LoginUser = getIntent().getExtras();
        String username = LoginUser.getString("user");
        Fragment fragment = new TableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", username);
        fragment.setArguments(bundle);
        loadFragment(fragment);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle LoginUser = getIntent().getExtras();
            String username = LoginUser.getString("user");
            Bundle bundle = new Bundle();
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_table:
                    fragment = new TableFragment();;
                    bundle.putString("user", username);
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_chart:
                    fragment = new ChartFragment();
                    bundle.putString("user", username);
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_troubleshoot:
                    fragment = new TroubleFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    // fragments worden geladen
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    // simpel regel code die ervoor zorgt dat als gebruiker op de back knop van zijn mobiel dat die naar het home scherm gaat
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
