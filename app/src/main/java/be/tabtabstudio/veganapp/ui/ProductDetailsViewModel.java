package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class ProductDetailsViewModel extends ViewModel {
    private VegRepository repo;

    public ProductDetailsViewModel() {
        repo = VegRepository.getInstance();
    }

    public LiveData<Product> getProductObservable() {
        return repo.getProduct();
    }

    public LiveData<List<Supermarket>> getSupermarketsObservable() {
        return repo.getSupermarkets();
    }

    public void fetchProduct(long ean) {
        repo.fetchProduct(ean);
    }

    public void handleAddToBasket() {

    }

    public void handleMarkInvalid() {
        Product p = repo.getProduct().getValue();
        if (p != null) {
            repo.markProductInvalid(p.ean);
        }
    }
}
