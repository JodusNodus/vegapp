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

    public LiveData<List<Product>> getProductsObservable() {
        return repo.getProductsObservable();
    }

    public void fetchProducts() {
        repo.fetchProducts("sap", 1);
    }
}
