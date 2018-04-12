package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.ProductListItem;

public class MainActivity extends AppCompatActivity implements TabPageFragment.OnListFragmentInteractionListener, MaterialSearchBar.OnSearchActionListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private MainViewModel mViewModel;
    private MaterialSearchBar searchBar;
    private AbstractList<String> lastSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.setContext(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);
        lastSearches = new ArrayList<>();
        searchBar.setLastSuggestions(lastSearches);

        setupSearchFragment();
    }

    private void setupSearchFragment() {
        Fragment tabPageFragment = new TabPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TabPageFragment.ARG_DATA, TabPageFragment.SEARCH_PAGE);
        bundle.putInt(TabPageFragment.ARG_COLUMN_COUNT, 2);
        tabPageFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.search_placeholder, tabPageFragment);
        ft.commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        List<String> resArray = Arrays.asList(TabPageFragment.NEW_PAGE, TabPageFragment.POPULAR_PAGE, TabPageFragment.RATING_PAGE, "snack", "koek", "speculoos", "vleesvervanger");
        for (int i = 0; i < resArray.size(); i++) {
            adapter.addFrag(new TabPageFragment(), resArray.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(ProductListItem item) {
        Intent k = new Intent(this, ProductDetailsActivity.class);
        k.putExtra(ProductDetailsActivity.EXTRA_EAN, item.ean);
        startActivity(k);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (enabled) {
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        } else {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        mViewModel.searchProducts(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode){
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList< >();
        private final List < String > mFragmentTitleList = new ArrayList < >();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            Bundle bundle = new Bundle();
            bundle.putInt(TabPageFragment.ARG_COLUMN_COUNT, 2);
            bundle.putString(TabPageFragment.ARG_DATA, title);
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
