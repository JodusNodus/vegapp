package be.tabtabstudio.veganapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import be.tabtabstudio.veganapp.model.entities.Product;
import be.tabtabstudio.veganapp.model.entities.Supermarket;

import static java.util.Calendar.*;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productTitleView;
    private TextView productShortDetails;
    private RatingBar productRating;
    private ImageView coverImageView;
    private LinearLayout supermarketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productTitleView = findViewById(R.id.product_title);
        productShortDetails  = findViewById(R.id.product_short_details);
        productRating  = findViewById(R.id.product_rating);
        coverImageView = findViewById(R.id.product_cover_image_view);
        supermarketList = findViewById(R.id.supermarkets_list);

        setProduct(Product.getMock());

        List<Supermarket> supermarkets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            supermarkets.add(Supermarket.getMock());
        }
        showSupermarkets(supermarkets);
    }

    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        return dateFormat.format(date);
    }

     public void setProduct(Product p) {
         productTitleView.setText(p.brand.name + " " + p.name);

         productShortDetails.setText(String.format("Geplaatst door %s op %s", p.user.getName(), formatDate(p.creationdate)));

         productRating.setRating(p.rating);

         Picasso.get().load(p.coverPicture).into(coverImageView);
     }

    public void showSupermarkets(List<Supermarket> supermarkets) {
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
