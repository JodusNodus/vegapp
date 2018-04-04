package be.tabtabstudio.veganapp.ui;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public void handleSupermarketClick(Context context, int index) {
        List<Supermarket> supermarkets = getSupermarketsObservable().getValue();
        if (supermarkets == null) { return; }
        Supermarket sm = supermarkets.get(index);
        if (sm == null) { return; }

        try {
            String query = URLEncoder.encode(sm.name, "utf-8");
            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + query + "&query_place_id=" + sm.placeid);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
