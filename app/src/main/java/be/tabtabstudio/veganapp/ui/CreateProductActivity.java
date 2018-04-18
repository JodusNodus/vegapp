package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import be.tabtabstudio.veganapp.R;

public class CreateProductActivity extends AppCompatActivity {

    private CreateProductViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);
        mViewModel.startNewForm();

        mViewModel.getEanObservable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String ean) {
                Toast.makeText(CreateProductActivity.this, ean, Toast.LENGTH_LONG).show();
            }
        });

        scanBarcode();
    }

    private void scanBarcode() {
        Intent k = new Intent(this, BarcodeScannerActivity.class);
        startActivity(k);
    }
}
