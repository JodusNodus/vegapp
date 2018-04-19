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

        mViewModel.getEanObservable().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long ean) {
                mViewModel.doesProductAlreadyExist(ean);
            }
        });

        mViewModel.getAlreadyExistsObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean alreadyExists) {
                if (alreadyExists) {
                    finish();
                    Toast.makeText(getApplicationContext(), R.string.product_already_exists, Toast.LENGTH_SHORT);
                } else {
                    takePicture();
                }
            }
        });

        scanBarcode();
    }

    private void scanBarcode() {
        Intent k = new Intent(this, BarcodeScannerActivity.class);
        startActivity(k);
    }

    private void takePicture() {
        Intent k = new Intent(this, ProductCameraActivity.class);
        startActivity(k);
    }
}
