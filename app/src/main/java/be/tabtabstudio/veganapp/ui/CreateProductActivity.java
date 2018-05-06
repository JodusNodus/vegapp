package be.tabtabstudio.veganapp.ui;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.entities.Brand;
import be.tabtabstudio.veganapp.utilities.StringUtils;

public class CreateProductActivity extends AppCompatActivity {

    private CreateProductViewModel mViewModel;
    private NachoTextView labelsInput;
    private AutoCompleteTextView brandNameInput;
    private EditText productNameInput;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView coverImageView;
    private FloatingActionButton doneBtn;
    private ProgressDialog signupDialog;
    private CoordinatorLayout addProductInfoLayout;
    private LinearLayout addProductLoadingLayout;
    private FlexboxLayout labelSuggestions;
    private FlexboxLayout brandSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        addProductInfoLayout = findViewById(R.id.add_product_info);
        addProductLoadingLayout = findViewById(R.id.add_product_loading);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        coverImageView = findViewById(R.id.product_cover_image_view);
        doneBtn = findViewById(R.id.done_btn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        labelSuggestions = findViewById(R.id.label_suggestions);
        brandSuggestions = findViewById(R.id.brand_suggestions);
        labelsInput = findViewById(R.id.nacho_text_view);
        brandNameInput = findViewById(R.id.brand_name_input);
        productNameInput = findViewById(R.id.product_name_input);

        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);
        mViewModel.startNewForm();

        addProductInfoLayout.setVisibility(View.GONE);
        addProductLoadingLayout.setVisibility(View.VISIBLE);
        disableForm();

        // start step 1
        addEanObservable();
        scanBarcode();
    }

    // step 0
    private void disableForm() {
        disableView(productNameInput);
        disableView(brandNameInput);
        disableView(labelsInput);
        disableView(doneBtn);
    }

    // step 1: Take a pictue of the BAR CODE
    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13, IntentIntegrator.EAN_8);
        integrator.setPrompt(getString(R.string.scan_barcode));
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void addEanObservable(){
        mViewModel.getEanObservable().observe(this, ean -> {
            addAlreadyExistsObservable();
            mViewModel.doesProductAlreadyExist(ean);
        });
    }

    private void addAlreadyExistsObservable(){
        mViewModel.getAlreadyExistsObservable().observe(this, alreadyExists -> {
            addProductSupermarketObservable();
            pickSupermarket();
            mViewModel.fetchNeededFormData();
        });
    }


    // Step 2: Select SUPERMARKET
    private void pickSupermarket() {
        Intent k = new Intent(this, SupermarketPickerActivity.class);
        startActivity(k);
    }

    private void addProductSupermarketObservable(){
        mViewModel.getProductSupermarketObservable().observe(this, supermarket -> {

            if (mViewModel.getAlreadyExistsObservable().getValue() == true) {
                mViewModel.createProduct();
            } else {
                addCoverImageObservable();
                takePicture();
            }
        });
    }

    // Step 3: Take a PICTURE of the product
    private void takePicture() {
        Intent k = new Intent(this, ProductCameraActivity.class);
        startActivity(k);
    }

    private void addCoverImageObservable(){
        mViewModel.getCoverImageObservable().observe(this, url -> {
            if (url == null) {
                finish();
                return;
            }
            setCoverImage(url);
            addInfoObservables();
            enableForm();
            addProductLoadingLayout.setVisibility(View.GONE);
            addProductInfoLayout.setVisibility(View.VISIBLE);
        });
    }

    private void setCoverImage(String url) {
        Picasso.get().load(url).into(coverImageView);
    }

    private void enableForm() {
        enableView(productNameInput);
        enableView(brandNameInput);
        enableView(labelsInput);
        enableView(doneBtn);
        addSaveObservables();
    }


    // Step 4: INFO (title, band ...)
    private void addInfoObservables(){
        TextWatcher titleWatcher = new InputTitleWatcher();
        productNameInput.addTextChangedListener(titleWatcher);
        brandNameInput.addTextChangedListener(titleWatcher);

        mViewModel.getAllLabelsObservable().observe(this, allLabels -> {
            setLabelList(allLabels);
        });

        mViewModel.getAllBrandsObservable().observe(this, allBrands -> {
            setBrandList(allBrands);
        });

        mViewModel.getLabelSuggestionsObservable().observe(this, labelList -> {
            setSuggestedLabelList(labelList);
        });

        mViewModel.getBrandSuggestionsObservable().observe(this, brandList ->{
            setSuggestedBrandList(brandList);
        });
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
        for (String label : labelList){
            Button button = new Button(this);
            button.setBackgroundResource(R.drawable.chip_drawable);
            button.setTextColor(Color.WHITE);
            button.setText(label);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setAllCaps(false);
            button.setTextSize(11);
            button.setMinHeight(5);
            button.setMinimumHeight(5);
            button.setMinWidth(5);
            button.setMinimumWidth(5);

            button.setOnClickListener(v -> {
                String newLabel = ((Button)v).getText().toString();

                List<Chip> chips = labelsInput.getAllChips();
                List<String> labels = new ArrayList<>();
                for (Chip c : chips){
                    labels.add(c.getText().toString());
                }

                labels.add(newLabel);
                labelsInput.setText(labels);
            });

            labelSuggestions.addView(button);
        }
    }

    private void setSuggestedBrandList(List<Brand> brandList) {
        Log.i("test", "hier komen de logs ...");
        for (Brand brand : brandList){
            Log.i("label", brand.name);
            Button button = new Button(this);
            button.setBackgroundResource(R.drawable.chip_drawable);
            button.setTextColor(Color.WHITE);
            button.setText(brand.name);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setAllCaps(false);
            button.setTextSize(11);
            button.setMinHeight(5);
            button.setMinimumHeight(5);
            button.setMinWidth(5);
            button.setMinimumWidth(5);

            button.setOnClickListener(v -> {
                String text = ((Button)v).getText().toString();
                brandNameInput.setText(text);
            });

            brandSuggestions.addView(button);
        }
    }

    private void setBrandList(List<String> brandList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, brandList);
        brandNameInput.setThreshold(1);
        brandNameInput.setAdapter(adapter);
    }


    // Step 5: save
    private void addSaveObservables(){
        doneBtn.setOnClickListener(view -> {
            saveProduct();
        });

        mViewModel.getCreateSuccessObservable().observe(this, isSuccess -> {
            if (isSuccess) {
                if (signupDialog != null) {
                    signupDialog.hide();
                }
                openProductAndClose();
                Toast.makeText(getApplicationContext(), R.string.creating_product_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.creating_product_failure, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openProductAndClose() {
        Log.i("test", "open product and close");
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

    // other
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
