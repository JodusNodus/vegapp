package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
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
        repo.createProductRepository();
    }

    public void setEan(long ean) {
        repo.getCreateProductRepository().ean.postValue(ean);
    }

    public LiveData<Long> getEanObservable() {
        return repo.getCreateProductRepository().ean;
    }

    public void doesProductAlreadyExist(long ean) {
        repo.getCreateProductRepository().doesProductAlreadyExist(ean);
    }

    public LiveData<Boolean> getAlreadyExistsObservable() {
        return repo.getCreateProductRepository().alreadyExists;
    }
}
