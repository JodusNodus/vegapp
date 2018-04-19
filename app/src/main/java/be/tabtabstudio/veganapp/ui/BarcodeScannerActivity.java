package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import be.tabtabstudio.veganapp.R;
import info.androidhive.barcode.BarcodeReader;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private CreateProductViewModel mViewModel;
    private BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
    }


    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        finish();
        mViewModel.setEan(Long.valueOf(barcode.displayValue));
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {
        onScanned(list.get(0));
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}
