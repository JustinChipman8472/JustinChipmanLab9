// justin chipman n01598472
package justin.chipman.n01598472;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private final String themePrefKey = "ThemePref";
    private final String darkModeKey = "darkmode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.justoolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.jusdrawer_layout);
        NavigationView navigationView = findViewById(R.id.jusnav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            onNavigationItemSelected(item);
            return true;
        });

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.jusnav_jus1tin);
            getSupportFragmentManager().beginTransaction().replace(R.id.jusfragment_container,
                    new Jus1tin()).commit();
        }

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    showExitConfirmationDialog();
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(themePrefKey, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(darkModeKey, false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.jusaction_jus_toggle) {
            // Handle settings action
            toggleTheme();
            return true;
        } else if(item.getItemId() == R.id.jusaction_chi_search){
            showSearchDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.jusnav_jus1tin) {
            launchSplash();
            getSupportFragmentManager().beginTransaction().replace(R.id.jusfragment_container, new Jus1tin()).commit();
        } else if (itemId == R.id.jusnav_chip2man) {
            getSupportFragmentManager().beginTransaction().replace(R.id.jusfragment_container, new Chip2man()).commit();
        } else if (itemId == R.id.jusnav_logout) {
            showExitConfirmationDialog();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ichigo)
                .setTitle(getString(R.string.justin_chipman))
                .setMessage(R.string.exit_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //clearSharedPreferences();
                        MainActivity.this.finishAffinity();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void toggleTheme() {
        // Check the current theme mode and toggle it
        SharedPreferences sharedPreferences = getSharedPreferences(themePrefKey, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(darkModeKey, false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            sharedPreferences.edit().putBoolean(darkModeKey, false).apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            sharedPreferences.edit().putBoolean(darkModeKey, true).apply();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem toggleItem = menu.findItem(R.id.jusaction_jus_toggle);
        MenuItem searchItem = menu.findItem(R.id.jusaction_chi_search);

        int textColor = getResources().getColor(R.color.menu_text_color);
        SpannableString sToggle = new SpannableString(toggleItem.getTitle());
        sToggle.setSpan(new ForegroundColorSpan(textColor), 0, sToggle.length(), 0);
        toggleItem.setTitle(sToggle);

        SpannableString sSearch = new SpannableString(searchItem.getTitle());
        sSearch.setSpan(new ForegroundColorSpan(textColor), 0, sSearch.length(), 0);
        searchItem.setTitle(sSearch);

        return super.onPrepareOptionsMenu(menu);
    }


    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.search));

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_search, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText input = viewInflated.findViewById(R.id.jussearch_edit_text);

        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.search_go, (dialog, which) -> {
            String searchQuery = input.getText().toString();
            performSearch(searchQuery);
            dialog.dismiss();
            // Hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void performSearch(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);

    }

    private void launchSplash() {
        // Start the splash screen activity
        Intent splashIntent = new Intent(this, JusChiSplash.class);
        splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(splashIntent);
        finish(); // Close the current activity
    }




}
