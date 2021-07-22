package info.androidhive.habadProject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.az.igrot.hakodesh.R;
import info.androidhive.habadProject.fragments.OneFragment;
import info.androidhive.habadProject.fragments.ThreeFragment;
import info.androidhive.habadProject.fragments.TwoFragment;

public class MainClass extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    static TextView dateTextview;
    static String currentDate;
    private int[] tabIcons = {
            R.drawable.ic_tab_favourite,
            R.drawable.all,
            R.drawable.random
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_text_tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        dateTextview = (TextView) findViewById(R.id.jewishDate);
        dateTextview.setText(currentDate);


    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).setIcon(tabIcons[0]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "כל האגרות");
        adapter.addFrag(new TwoFragment(), "האגרות שלי");
        adapter.addFrag(new ThreeFragment(), "אגרת אקראית");
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.setAdapter(adapter);

    }

    public void onBackPressed() {
        //intent to run the app in the background
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);

    }


}

