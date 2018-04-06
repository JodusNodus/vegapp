package be.tabtabstudio.veganapp.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Favorites;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EAN = "EXTRA_EAN";

    private FloatingActionButton favoriteBtn;
    private Button markInvalidBtn;
    private Button rateBtn;
    private TextView productTitleView;
    private TextView productShortDetails;
    private RatingBar productRating;
    private ImageView coverImageView;
    private LinearLayout supermarketListView;
    private ProductDetailsViewModel mViewModel;
    private View.OnClickListener supermarketClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        favoriteBtn = findViewById(R.id.favorite_product_btn);
        markInvalidBtn = findViewById(R.id.mark_invalid_btn);
        rateBtn = findViewById(R.id.rate_product_btn);

        productTitleView = findViewById(R.id.product_title);
        productShortDetails  = findViewById(R.id.product_short_details);
        productRating  = findViewById(R.id.product_rating);
        coverImageView = findViewById(R.id.product_cover_image_view);
        supermarketListView = findViewById(R.id.supermarkets_list);

        mViewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);
        mViewModel.setContext(this);

        favoriteBtn.setOnClickListener((View v) -> {
            mViewModel.handleFavoriteProduct();
        });

        markInvalidBtn.setOnClickListener((View v) -> {
            mViewModel.handleMarkInvalid();
        });

        rateBtn.setOnClickListener((View v) -> {
        });

        long ean = getIntent().getLongExtra(EXTRA_EAN, -1);
        if (ean == -1) {
            finish();
            return;
        }

        supermarketClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer index = (Integer) view.getTag();
                mViewModel.handleSupermarketClick(index);
            }
        };

        mViewModel.getProductObservable().observe(this, new Observer<Product>() {
            @Override
            public void onChanged(@Nullable Product product) {
                if (product != null) {
                    setProduct(product);
                }
            }
        });
        mViewModel.getSupermarketsObservable().observe(this, new Observer<List<Supermarket>>() {
            @Override
            public void onChanged(@Nullable List<Supermarket> supermarkets) {
                if (supermarkets != null) {
                    setSupermarkets(supermarkets);
                }
            }
        });

        mViewModel.getProductIsFavoriteObservable(ean).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean productIsFavorite) {
                Log.i("test", String.valueOf(productIsFavorite));
                setFavoriteButtonState(productIsFavorite);
            }
        });

        mViewModel.fetchProduct(ean);
    }

    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        return dateFormat.format(date);
    }

    private void setProduct(Product p) {
        productTitleView.setText(p.getProductName());

        productShortDetails.setText(String.format("Geplaatst door %s op %s", p.user.getName(), formatDate(p.creationdate)));

        productRating.setRating(p.rating);

        Picasso.get().load(p.coverPicture).into(coverImageView);

        if (p.userHasCorrected) {
            disableButton(markInvalidBtn);
        } else {
            enableButton(markInvalidBtn);
        }
    }

    private void setFavoriteButtonState(boolean productisFavorite) {
        if (productisFavorite) {
            favoriteBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            favoriteBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }

    private void setSupermarkets(List<Supermarket> supermarkets) {
        // Clear items in view
        supermarketListView.removeAllViews();

        Supermarket[] smArr = new Supermarket[supermarkets.size()];
        smArr = supermarkets.toArray(smArr);

        SupermarketListAdapter adapter = new SupermarketListAdapter(this, smArr);
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, supermarketListView);
            view.setTag(new Integer(i));
            view.setOnClickListener(supermarketClickListener);
            supermarketListView.addView(view);
        }
    }

    private class SupermarketListAdapter extends ArrayAdapter {

        private Activity context;
        private Supermarket[] supermarketsArr;

        public SupermarketListAdapter(Activity context, Supermarket[] supermarketsArr) {
            super(context,R.layout.supermarket_list_item , supermarketsArr);
            this.supermarketsArr = supermarketsArr;
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.supermarket_list_item, null,true);

            //this code gets references to objects in the listview_row.xml file
            TextView nameView = (TextView) rowView.findViewById(R.id.supermarket_name);
            TextView addresView = (TextView) rowView.findViewById(R.id.supermarket_address);

            //this code sets the values of the objects to values from the arrays
            Supermarket sm = supermarketsArr[position];
            nameView.setText(sm.name);
            addresView.setText(sm.address);

            return rowView;
        }
    }

    private void enableButton(View btn) {
        btn.setEnabled(true);
        btn.setAlpha(1f);
    }

    private void disableButton(View btn) {
        btn.setEnabled(false);
        btn.setAlpha(.5f);
    }

}
