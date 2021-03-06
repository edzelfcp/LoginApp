package com.example.samsung.UniTeach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements Home2Fragment.OnFragmentInteractionListener, TutorFragment.OnFragmentInteractionListener,
BookSellingFragment2.OnFragmentInteractionListener{
    //open field for tab layout purpose

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String current_user_id;

    //private FloatingActionButton addPostBtn;

    /*private BottomNavigationView maintopNav;

    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private AccountFragment bookSellingFragment;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("UniTeach");

        //checks if user exists so log out can work
        if (mAuth.getCurrentUser() != null) {

            //tab layout purpose
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.main_container);
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            //initializationFragment();

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            /*addPostBtn = findViewById(R.id.add_post_btn);
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                    startActivity(newPostIntent);

                }
            });*/
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {

            sendToLogin();

        } else {

            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (!task.getResult().exists()) {

                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }

            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout_btn:
                logOut();
                return true;

            case R.id.action_settings_btn:
                Intent settingIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingIntent);
                return true;

            case R.id.action_apply_btn:
                Intent applyIntent = new Intent(MainActivity.this, TutorApplyActivity.class);
                startActivity(applyIntent);
                return true;

            case R.id.action_book_btn:
                Intent newbookIntent = new Intent(MainActivity.this, NewBookPostActivity.class);
                startActivity(newbookIntent);
                return true;


            default:
                return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }

    /*private void initializationFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, Home2Fragment);
        fragmentTransaction.add(R.id.main_container, TutorFragment);
        fragmentTransaction.add(R.id.main_container, BookSellingFragment2);

        fragmentTransaction.hide(TutorFragment);
        fragmentTransaction.hide(BookSellingFragment2);

        fragmentTransaction.commit();
    }*/

    private void replaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //new codes
        /*if(fragment == Home2Fragment){
            fragmentTransaction.hide(TutorFragment);
            fragmentTransaction.hide(BookSellingFragment2);
        }

        if(fragment == TutorFragment){
            fragmentTransaction.hide(Home2Fragment);
            fragmentTransaction.hide(BookSellingFragment2);
        }

        if(fragment == BookSellingFragment2){
            fragmentTransaction.hide(Home2Fragment);
            fragmentTransaction.hide(TutorFragment);
        }*/

        fragmentTransaction.show(fragment);

        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }
}

