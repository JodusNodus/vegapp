package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.net.URLEncoder;
import java.util.List;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class ProductDetailsViewModel extends ViewModel {

    private VegRepository repo;
    private LiveData<Boolean> productIsFavorite;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        this.repo = VegRepository.getInstance(context);
    }

    public LiveData<Product> getProductObservable() {
        return repo.getProductObservable();
    }
    public LiveData<Boolean> getProductIsFavoriteObservable(long ean) {
        return Transformations.map(repo.getFavoritesObservable(), (favorites) ->
                favorites.findById(ean) != null);
    }

    public LiveData<List<Supermarket>> getSupermarketsObservable() {
        return repo.getSupermarketsObservable();
    }

    public void fetchProduct(long ean) {
        repo.fetchProduct(ean);
    }

    public void handleFavoriteProduct() {
        Product p = getProductObservable().getValue();
        if (p != null) {
            boolean isFavorite = repo.getFavoritesObservable().getValue().contains(p);
            if (isFavorite) {
                repo.removeFavorite(p);
            } else {
                repo.addFavorite(p);
            }
        }
    }

    public void handleRateProduct() {

    }

    public void handleMarkInvalid() {
        Product p = repo.getProductObservable().getValue();
        if (p != null) {
            repo.markProductInvalid(p);
        }
    }

    public void handleSupermarketClick(int index) {
        List<Supermarket> supermarkets = getSupermarketsObservable().getValue();
        if (supermarkets == null) { return; }
        Supermarket sm = supermarkets.get(index);
        if (sm == null) { return; }

        try {
            String query = URLEncoder.encode(sm.name, "utf-8");
            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + query + "&query_place_id=" + sm.placeid);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(mapIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
