package com.koenschouten.notificationcreator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.koenschouten.notificationcreator.database.AppDatabase;
import com.koenschouten.notificationcreator.database.tables.NotificationChannelDB;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.database.tables.NotificationDBDao;
import com.koenschouten.notificationcreator.viewModels.NotificationChannelViewModel;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_FIRST_RUN = "first_run";
    private static final String KEY_PREFERENCES = "preferences";

    private NotificationChannelViewModel notificationChannelViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setOpenableLayout(findViewById(R.id.drawer_layout))
                        .build();

        NavigationView navView = findViewById(R.id.nav_view);
        setupDrawerContent(navView);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        setupViewModels();


        if(isFirstRun()){
            createDefaultNotificationChannel();
            setFirstRunFalse();
        } else{
            Toast.makeText(this, "Not first start", Toast.LENGTH_LONG).show();
        }
    }

    protected void setupViewModels(){
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        notificationChannelViewModel = viewModelProvider.get(NotificationChannelViewModel.class);
    }


    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                }
        );
    }

    /**
     * When selecting an item in the menu, this sets the fragment in the nav_host_fragment to the
     * selected fragment.
     *
     * @param menuItem the selected menuItem
     */
    private void selectDrawerItem(MenuItem menuItem){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        Intent settingsIntent;

        switch (menuItem.getItemId()){
            case R.id.menu_notification_list:
                navController.navigate(R.id.notificationListFragment);
                break;
            case R.id.menu_create_notification:
                navController.navigate(R.id.createNotificationFragment);
                break;
            case R.id.menu_notification_channel_list:
                navController.navigate(R.id.notificationChannelListFragment);
                break;
            case R.id.menu_create_notification_channel:
                navController.navigate(R.id.createNotificationChannelFragment);
                break;
            case R.id.menu_app_settings:
                settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                startActivity(settingsIntent);
                break;
            case R.id.menu_app_notification_settings:
                settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(settingsIntent);
                break;
            default:
                break;
        }

        if(menuItem.getGroupId() != R.id.menu_group_settings){
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();
    }

    /**
     * Checks whether this Activity is running for the first time
     * @return true when the activity is running for the first time
     */
    private boolean isFirstRun(){
        boolean firstRun = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE).getBoolean(KEY_FIRST_RUN, true);
        if(!firstRun){
            getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE).edit().putBoolean(KEY_FIRST_RUN, false).commit();
        }
        return firstRun;
    }

    /**
     * Sets the first_run value in preferences to true
     */
    private void setFirstRunFalse(){
        getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE).edit().putBoolean(KEY_FIRST_RUN, false).commit();
    }

    private void createDefaultNotificationChannel(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String title = "Default Channel";
        String description = "Default Channel for NotificationCreator";
        NotificationChannelDB.ChannelImportance importance = NotificationChannelDB.ChannelImportance.IMPORTANCE_DEFAULT;
        Date date = new Date(System.currentTimeMillis());

        NotificationChannelDB notificationChannelDB = new NotificationChannelDB(title, description,importance,date);

        //Start an asynchronous function to insert the noficationChannel into the database.
        //Database insertions can't be done on the Main thread because it could block the UI.
        executor.execute(() -> {
            //Insert notification into dataBase
            notificationChannelViewModel.insert(notificationChannelDB);
        });
    }
}