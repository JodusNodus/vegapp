package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Product;

public class TabPageViewModel extends ViewModel {
    private VegRepository repo;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        this.repo = VegRepository.getInstance(context);
    }

    public LiveData<List<Product>> getNewProductsObservable() {
        return repo.getNewProductsObservable();
    }

    public LiveData<List<Product>> getSearchProductsObservable() {
        return repo.getSearchProductsObservable();
    }

    public void fetchNewProducts() {
        repo.fetchNewProducts(20, 1);
    }
}
