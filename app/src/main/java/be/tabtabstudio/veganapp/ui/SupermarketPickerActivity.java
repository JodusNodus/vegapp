package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class SupermarketPickerActivity extends AppCompatActivity {

    private CreateProductViewModel mViewModel;
    private LinearLayout supermarketListView;
    private View.OnClickListener supermarketClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_picker);

        supermarketListView = findViewById(R.id.supermarkets_list);

        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);

        supermarketClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer index = (Integer) view.getTag();
                finish();
                mViewModel.handleSupermarketClick(index);
            }
        };

        mViewModel.getSupermarketsObservable().observe(this, new Observer<List<Supermarket>>() {
            @Override
            public void onChanged(@Nullable List<Supermarket> supermarkets) {
                setSupermarkets(supermarkets);
            }
        });

    }

    private void setSupermarkets(List<Supermarket> supermarkets) {
        // Clear items in view
        supermarketListView.removeAllViews();

        for (int i = 0; i < supermarkets.size(); i++) {
            Supermarket sm = supermarkets.get(i);

            View rowView=getLayoutInflater().inflate(R.layout.supermarket_list_item, null,true);
            TextView nameView = (TextView) rowView.findViewById(R.id.supermarket_name);
            TextView addresView = (TextView) rowView.findViewById(R.id.supermarket_address);
            nameView.setText(sm.name);
            addresView.setText(sm.address);

            rowView.setTag(new Integer(i));
            rowView.setOnClickListener(supermarketClickListener);
            supermarketListView.addView(rowView);
        }
    }

}
