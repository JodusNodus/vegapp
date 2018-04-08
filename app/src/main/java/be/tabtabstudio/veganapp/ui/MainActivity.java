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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.ProductListItem;
import be.tabtabstudio.veganapp.ui.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements TabPageFragment.OnListFragmentInteractionListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private TabPageViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(TabPageViewModel.class);
        mViewModel.setContext(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mViewModel.fetchProducts();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        List<String> resArray = Arrays.asList("page1", "page2", "page3", "page4", "page5", "page6");
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
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
