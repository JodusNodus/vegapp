package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.List;

import be.tabtabstudio.veganapp.R;
import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class ProductDetailsViewModel extends ViewModel {

    private VegRepository repo;

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

    public void fetchProduct(long ean) {
        repo.fetchProduct(ean);
    }

    public void handleFavoriteProduct() {
        Product p = getProductObservable().getValue();
        if (p != null) {
            boolean isFavorite = repo.getFavoritesObservable().getValue().contains(p);
            if (isFavorite) {
                repo.removeFavorite(p);
                Toast.makeText(getContext(), R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
            } else {
                repo.addFavorite(p);
                Toast.makeText(getContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void handleRateProduct(int rating) {
        Product p = getProductObservable().getValue();
        repo.rateProduct(p, rating);
    }

    public void handleMarkInvalid() {
        Product p = repo.getProductObservable().getValue();
        if (p != null) {
            repo.markProductInvalid(p);
        }
    }

    public void handleSupermarketClick(int index) {
        Product product = getProductObservable().getValue();
        if (product == null) { return; }
        Supermarket sm = product.supermarkets.get(index);
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
