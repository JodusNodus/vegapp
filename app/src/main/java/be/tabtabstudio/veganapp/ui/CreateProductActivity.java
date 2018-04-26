package be.tabtabstudio.veganapp.ui;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.squareup.picasso.Picasso;
import com.tylersuehr.chips.ChipsInputLayout;
import com.wonderkiln.camerakit.CameraKitImage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Label;
import be.tabtabstudio.veganapp.data.entities.Supermarket;
import be.tabtabstudio.veganapp.utilities.StringUtils;

public class CreateProductActivity extends AppCompatActivity {

    private CreateProductViewModel mViewModel;
    private ImageView newProductImage;
    private NachoTextView labelsInput;
    private AutoCompleteTextView brandNameInput;
    private EditText productNameInput;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView coverImageView;
    private FloatingActionButton doneBtn;
    private ProgressDialog signupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        coverImageView = findViewById(R.id.product_cover_image_view);
        doneBtn = findViewById(R.id.done_btn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        labelsInput = findViewById(R.id.nacho_text_view);
        brandNameInput = findViewById(R.id.brand_name_input);
        productNameInput = findViewById(R.id.product_name_input);

        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);
        mViewModel.startNewForm();

        doneBtn.setOnClickListener(view -> {
            saveProduct();
        });

        TextWatcher titleWatcher = new InputTitleWatcher();
        productNameInput.addTextChangedListener(titleWatcher);
        brandNameInput.addTextChangedListener(titleWatcher);

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
                    pickSupermarket();
                    mViewModel.fetchNeededFormData();
                }
            }
        });

        mViewModel.getProductSupermarketObservable().observe(this, new Observer<Supermarket>() {
            @Override
            public void onChanged(@Nullable Supermarket supermarket) {
                takePicture();
            }
        });

        mViewModel.getAllLabelsObservable().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> allLabels) {
                setLabelList(allLabels);
            }
        });

        mViewModel.getAllBrandsObservable().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> allBrands) {
                setBrandList(allBrands);
            }
        });

        mViewModel.getCoverImageObservable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String url) {
                if (url == null) {
                    finish();
                    return;
                }
                setCoverImage(url);
                enableForm();
            }
        });

        mViewModel.getLabelSuggestionsObservable().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> labelList) {
                setSuggestedLabelList(labelList);
            }
        });

        mViewModel.getCreateSuccessObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSuccess) {
                if (isSuccess == true) {
                    signupDialog.hide();
                    openProductAndClose();
                    Toast.makeText(getApplicationContext(), R.string.creating_product_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.creating_product_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });

        disableForm();

        scanBarcode();
    }

    private void openProductAndClose() {
        Intent k = new Intent(this, ProductDetailsActivity.class);
        k.putExtra(ProductDetailsActivity.EXTRA_EAN, mViewModel.getEanObservable().getValue());
        finish();
        startActivity(k);
    }

    private void saveProduct() {
        String productName = productNameInput.getText().toString();
        if (productName.length() < 1) {
            productNameInput.setError(getString(R.string.error_field_required));
            return;
        }

        String brandName = brandNameInput.getText().toString();
        if (brandName.length() < 1) {
            brandNameInput.setError(getString(R.string.error_field_required));
            return;
        }

        List<String> labelList = labelsInput.getChipValues();
        if (labelList.size() < 1) {
            labelsInput.setError(getString(R.string.error_min_one_label));
            return;
        }

        signupDialog = new ProgressDialog(this);
        signupDialog.setIndeterminate(true);
        signupDialog.setMessage(getString(R.string.creating_product));
        signupDialog.show();

        mViewModel.createProduct(productName, brandName, labelList);
    }

    private void pickSupermarket() {
        Intent k = new Intent(this, SupermarketPickerActivity.class);
        startActivity(k);
    }

    private void disableForm() {
        disableView(productNameInput);
        disableView(brandNameInput);
        disableView(labelsInput);
        disableView(doneBtn);
    }

    private void enableForm() {
        enableView(productNameInput);
        enableView(brandNameInput);
        enableView(labelsInput);
        enableView(doneBtn);
    }

    private void setProductTitle() {
        String brandName = brandNameInput.getText().toString();
        String productName = productNameInput.getText().toString();

        String str = "";
        if (brandName != null) {
            str += StringUtils.capitize(brandName) + " ";
        }
        str += StringUtils.capitize(productName);
        collapsingToolbar.setTitle(str);
    }

    private void setCoverImage(String url) {
        Picasso.get().load(url).into(coverImageView);
    }

    private List<LabelChip> getLabelChipList(List<String> labelList) {
        List<LabelChip> labelChipList = new ArrayList<>();
        for (String l : labelList) {
            labelChipList.add(new LabelChip(l));
        }
        return labelChipList;
    }

    private void setLabelList(List<String> labelList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, labelList);
        labelsInput.setThreshold(1);
        labelsInput.setAdapter(adapter);
        labelsInput.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        labelsInput.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        labelsInput.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
    }


    private void setSuggestedLabelList(List<String> labelList) {
        //labelChipsInput.setSelectedChipList(getLabelChipList(labelList));
    }

    private void setBrandList(List<String> brandList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, brandList);
        brandNameInput.setThreshold(1);
        brandNameInput.setAdapter(adapter);
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
        if(result != null) {
            if (result.getContents() != null) {
                mViewModel.setEan(Long.valueOf(result.getContents()));
            } else {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void disableView(View view) {
        view.setEnabled(false);
        view.setAlpha(0.5f);
    }

    private void enableView(View view) {
        view.setEnabled(true);
        view.setAlpha(1.0f);
    }

    private class InputTitleWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            setProductTitle();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
