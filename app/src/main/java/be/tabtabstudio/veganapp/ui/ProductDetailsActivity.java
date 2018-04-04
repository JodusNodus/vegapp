package be.tabtabstudio.veganapp.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToBasketBtn;
    private Button markInvalidBtn;
    private TextView productTitleView;
    private TextView productShortDetails;
    private RatingBar productRating;
    private ImageView coverImageView;
    private LinearLayout supermarketList;
    private ProductDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        addToBasketBtn = findViewById(R.id.add_to_cart_btn);
        markInvalidBtn = findViewById(R.id.mark_invalid_btn);

        productTitleView = findViewById(R.id.product_title);
        productShortDetails  = findViewById(R.id.product_short_details);
        productRating  = findViewById(R.id.product_rating);
        coverImageView = findViewById(R.id.product_cover_image_view);
        supermarketList = findViewById(R.id.supermarkets_list);

        mViewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);

        addToBasketBtn.setOnClickListener((View v) -> {
            mViewModel.handleAddToBasket();
        });
        markInvalidBtn.setOnClickListener((View v) -> {
            mViewModel.handleMarkInvalid();
        });

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
                    showSupermarkets(supermarkets);
                }
            }
        });

        mViewModel.fetchProduct(555555555555L);

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
             markInvalidBtn.setEnabled(false);
             markInvalidBtn.setAlpha(.5f);
         }
     }

    private void showSupermarkets(List<Supermarket> supermarkets) {
        Supermarket[] smArr = new Supermarket[supermarkets.size()];
        smArr = supermarkets.toArray(smArr);

        SupermarketListAdapter adapter = new SupermarketListAdapter(this, smArr);
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, supermarketList);
            supermarketList.addView(view);
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
}
