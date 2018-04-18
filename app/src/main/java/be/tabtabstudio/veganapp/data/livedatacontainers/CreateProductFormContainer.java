package be.tabtabstudio.veganapp.data.livedatacontainers;

import android.arch.lifecycle.MutableLiveData;

public class CreateProductFormContainer {
    public final MutableLiveData<String> ean;
    public CreateProductFormContainer() {
        ean = new MutableLiveData<>();
    }
}
