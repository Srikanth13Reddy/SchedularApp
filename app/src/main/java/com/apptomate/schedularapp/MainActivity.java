package com.apptomate.schedularapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.apptomate.schedularapp.adapter.NavMenuAdapter;
import com.apptomate.schedularapp.fragments.BusinesshoursFragment;
import com.apptomate.schedularapp.fragments.DoNotDisturbFragment;
import com.apptomate.schedularapp.fragments.HolidayScheduleFragment;
import com.apptomate.schedularapp.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{

    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Menu menu;
    LinearLayout tl_ll;
    BottomNavigationView navigation;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        tl_ll = findViewById(R.id.tl_ll);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
       // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

         drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.colorPrimaryDark));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();
         navigation = findViewById(R.id.bottom_view);
        navigation.setOnNavigationItemSelectedListener(this);
        navHeader();
//        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
//            {
//                Fragment fragment = null;
//                switch (menuItem.getItemId())
//                {
//
//                    case R.id.navigation_home:
//                       // Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
//                        fragment = new HomeFragment();
//                        break;
//                    case R.id.navigation_bhours:
//                        //Toast.makeText(MainActivity.this, "BHours", Toast.LENGTH_SHORT).show();
//                        fragment = new BusinesshoursFragment();
//                        break;
//
//                    case R.id.navigation_hschedule:
//                       // Toast.makeText(MainActivity.this, "BSCHEDULE", Toast.LENGTH_SHORT).show();
//                        fragment = new HolidayScheduleFragment();
//                        break;
//                    case R.id.navigation_donotdisturb:
//                        //Toast.makeText(MainActivity.this, "DONOT DISTURB", Toast.LENGTH_SHORT).show();
//                        fragment = new DoNotDisturbFragment();
//                        break;
//
//                }
//
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                if (fragment != null) {
//                    transaction.replace(R.id.container, fragment);
//                }
//                transaction.commit();
//                return false;
//            }
//        });
        //navHeader();


    }

    private void navHeader()
    {
        View headerLayout = navigationView.getHeaderView(0);
      ///  ListView lv=headerLayout.findViewById(R.id.lv_nav);
       // lv.setAdapter(new NavMenuAdapter(this));
        LinearLayout ll_ll_sms=headerLayout.findViewById(R.id.ll_sms);
        LinearLayout ll_donot_disturb=headerLayout.findViewById(R.id.ll_donot_disturb);
        LinearLayout ll_businesshours=headerLayout.findViewById(R.id.ll_businesshours);
        LinearLayout ll_holiday_schedule=headerLayout.findViewById(R.id.ll_holiday_schedule);
        LinearLayout ll_alert=headerLayout.findViewById(R.id.ll_alert);
        LinearLayout ll_contacts=headerLayout.findViewById(R.id.ll_contacts);
        ll_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,ContactsActivity.class);
                startActivity(i);

                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        ll_ll_sms.setOnClickListener(v ->
        {

//            drawer.closeDrawers();
//            Fragment fragment = new SMSFragment();
//            navActions(fragment);

            Intent i=new Intent(MainActivity.this,SmsActivity.class);
            startActivity(i);

            overridePendingTransition(R.anim.right_in, R.anim.left_out);


        });
        ll_alert.setOnClickListener(v -> {
            Intent i=new Intent(MainActivity.this,AlertActivity.class);
            startActivity(i);

            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            drawer.closeDrawers();
//            Fragment fragment = new AlertFragment();
//            navActions(fragment);
        });
        ll_donot_disturb.setOnClickListener(v -> {
            drawer.closeDrawers();
            Fragment fragment = new DoNotDisturbFragment();
            loadFragment(fragment);
            navigation.getMenu().getItem(3).setChecked(true);
            menu.findItem(R.id.action_settings).setVisible(false);
            tl_ll.setVisibility(View.GONE);
            toolbar.setTitle("Do Not Disturb ");


        });
        ll_businesshours.setOnClickListener(v -> {
            drawer.closeDrawers();
            Fragment fragment = new BusinesshoursFragment();
            loadFragment(fragment);
            navigation.getMenu().getItem(1).setChecked(true);
            menu.findItem(R.id.action_settings).setVisible(false);
            tl_ll.setVisibility(View.GONE);
            toolbar.setTitle("Business hours");

        });

        ll_holiday_schedule.setOnClickListener(v -> {
            drawer.closeDrawers();
            Fragment fragment = new HolidayScheduleFragment();
            loadFragment(fragment);
            navigation.getMenu().getItem(2).setChecked(true);
            menu.findItem(R.id.action_settings).setVisible(false);
            tl_ll.setVisibility(View.GONE);
            toolbar.setTitle("Holiday schedule");

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu=menu;
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        MenuItem item_= menu.findItem(R.id.action_settings);

        switch (item.getItemId()) {
            case R.id.navigation_home:
                tl_ll.setVisibility(View.VISIBLE);
                toolbar.setTitle("");
                item_.setVisible(true);
                fragment = new HomeFragment();
                break;

            case R.id.navigation_bhours:
                tl_ll.setVisibility(View.GONE);
                toolbar.setTitle("Business hours");
               item_.setVisible(false);
                fragment = new BusinesshoursFragment();
                break;


            case R.id.navigation_hschedule:
                item_.setVisible(false);
                tl_ll.setVisibility(View.GONE);
                toolbar.setTitle("Holiday schedule");
                fragment = new HolidayScheduleFragment();
                break;
            case R.id.navigation_donotdisturb:
                item_.setVisible(false);
                tl_ll.setVisibility(View.GONE);
                toolbar.setTitle("Do Not Disturb");
                fragment = new DoNotDisturbFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment)
    {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}
