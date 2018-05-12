package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;

import java.util.List;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Favorites;
import be.tabtabstudio.veganapp.data.entities.Label;
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

    public LiveData<List<Product>> getHighestRatedProductsObservable() {
        return repo.getHighestRatedProductsObservable();
    }

    public LiveData<List<Product>> getMostPopularProductsObservable() {
        return repo.getMostPopularProductsObservable();
    }

    public LiveData<List<Product>> getProductsObservableWithLabel(String label) {
        return repo.getProductsObservableWithLabel(label);
    }

    public LiveData<List<Product>> getFavoriteProductsObservable() {
        LiveData<Favorites> listOfFavorites = repo.getFavoritesObservable();
        LiveData<List<Product>> products = Transformations.map(listOfFavorites, favorites
                -> favorites.getAll());
        return products;
    }

    public void fetchNewProducts() {
        repo.fetchNewProducts(20, 1);
    }
    public void fetchHighestRatedProducts() {
        repo.fetchHighestRatedProducts(20, 1);
    }
    public void fetchMostPopularProducts() {
        repo.fetchMostPopularProducts(20, 1);
    }
    public void fetchProductsWithLabel(String label) {
        repo.fetchProductsWithLabel(label, 20, 1);
    }
}
