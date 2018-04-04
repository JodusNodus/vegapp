package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class ProductDetailsViewModel extends ViewModel {

    public LiveData<Product> getProductObservable() {
        return VegRepository.getInstance().getProduct();
    }

    public LiveData<List<Supermarket>> getSupermarketsObservable() {
        return VegRepository.getInstance().getSupermarkets();
    }

    public void handleAddToBasket() {

    }

    public void handleMarkInvalid() {
        VegRepository repo = VegRepository.getInstance();
        Product p = repo.getProduct().getValue();
        if (p != null) {
            repo.markProductInvalid(p.ean);
        }
    }
}
