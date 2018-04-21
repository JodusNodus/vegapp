package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pchmn.materialchips.ChipsInput;
import com.squareup.picasso.Picasso;
import com.wonderkiln.camerakit.CameraKitImage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Label;

public class CreateProductActivity extends AppCompatActivity {

    private CreateProductViewModel mViewModel;
    private ImageView newProductImage;
    private TextView newProductBarcode;
    private ChipsInput labelChipsInput;
    private AutoCompleteTextView brandNameInput;
    private EditText productNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        newProductBarcode = findViewById(R.id.new_product_barcode);
        labelChipsInput = findViewById(R.id.label_chips_input);
        brandNameInput = findViewById(R.id.brand_name_input);
        productNameInput = findViewById(R.id.product_name_input);

        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);
        mViewModel.startNewForm();

        mViewModel.getEanObservable().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long ean) {
                mViewModel.doesProductAlreadyExist(ean);
                setBarcode(ean);
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

        mViewModel.getLabelSuggestionsObservable().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> labelSuggestions) {
                setLabelList(labelSuggestions);
            }
        });

        mViewModel.getBrandSuggestionsObservable().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> brandSuggestions) {
                setBrandList(brandSuggestions);
            }
        });

        setLabelList(new ArrayList<>());
        scanBarcode();
    }

    private void setLabelList(List<String> labelList) {
        List<LabelChip> labelChipList = new ArrayList<>();
        for (String l : labelList) {
            labelChipList.add(new LabelChip(l));
        }
        labelChipsInput.setFilterableList(labelChipList);
    }

    private void setBrandList(List<String> brandList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, brandList);
        brandNameInput.setThreshold(2);
        brandNameInput.setAdapter(adapter);
    }

    private void setBarcode(Long ean) {
        newProductBarcode.setText(ean.toString());
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13, IntentIntegrator.EAN_8);
        integrator.setPrompt(getString(R.string.scan_barcode));
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void takePicture() {
        Intent k = new Intent(this, ProductCameraActivity.class);
        startActivity(k);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // Barcode result
        if(result != null && result.getContents() != null) {
            mViewModel.setEan(Long.valueOf(result.getContents()));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
