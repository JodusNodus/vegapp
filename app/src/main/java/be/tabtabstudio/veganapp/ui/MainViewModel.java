package be.tabtabstudio.veganapp.ui;

import android.content.Context;

import be.tabtabstudio.veganapp.data.VegRepository;

public class MainViewModel extends ViewModel {
    private VegRepository repo;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        this.repo = VegRepository.getInstance(context);
    }

    public void searchProducts(String searchquery) {
        repo.searchProducts(searchquery, 20, 1);
    }
}
