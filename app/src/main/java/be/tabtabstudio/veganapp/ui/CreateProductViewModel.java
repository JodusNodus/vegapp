package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import be.tabtabstudio.veganapp.data.VegRepository;

public class CreateProductViewModel extends ViewModel {
    private VegRepository repo;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        repo = VegRepository.getInstance(context);
    }

    public void startNewForm() {
        repo.createProductFormContainer();
    }

    public void setEan(String ean) {
        repo.getProductFormContainer().ean.setValue(ean);
    }

    public MutableLiveData<String> getEanObservable() {
        return repo.getProductFormContainer().ean;
    }
}
