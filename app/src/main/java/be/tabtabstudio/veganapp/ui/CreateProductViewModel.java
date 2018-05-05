package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.wonderkiln.camerakit.CameraKitImage;

import java.util.List;

import be.tabtabstudio.veganapp.data.VegRepository;
import be.tabtabstudio.veganapp.data.entities.Supermarket;
import be.tabtabstudio.veganapp.data.network.requestBodies.CreateProductBody;
import be.tabtabstudio.veganapp.data.subrepos.CreateProductRepository;

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
        repo.getCreateProductRepository().ean.setValue(ean);
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

    public void uploadProductImage(CameraKitImage cameraKitImage) {
        CreateProductRepository cpRepo = repo.getCreateProductRepository();
        cpRepo.uploadProductImage(cpRepo.ean.getValue(), cameraKitImage);
    }

    public void fetchNeededFormData() {
        repo.getCreateProductRepository().fetchAllBrands();
        repo.getCreateProductRepository().fetchAllLabels();
        repo.getCreateProductRepository().fetchSupermarkets();
    }

    public LiveData<String> getCoverImageObservable() {
        return repo.getCreateProductRepository().coverImage;
    }

    public LiveData<List<String>> getLabelSuggestionsObservable() {
        return repo.getCreateProductRepository().labelSuggestions;
    }

    public LiveData<List<String>> getBrandSuggestionsObservable() {
        return repo.getCreateProductRepository().brandSuggestions;
    }

    public LiveData<List<String>> getAllBrandsObservable() {
        return repo.getCreateProductRepository().allBrands;
    }

    public LiveData<List<String>> getAllLabelsObservable() {
        return repo.getCreateProductRepository().allLabels;
    }

    public LiveData<List<Supermarket>> getSupermarketsObservable() {
        return repo.getCreateProductRepository().supermarkets;
    }

    public LiveData<Supermarket> getProductSupermarketObservable() {
        return repo.getCreateProductRepository().productSupermarket;
    }

    public void handleSupermarketClick(int index) {
        Supermarket sm = getSupermarketsObservable().getValue().get(index);
        if (sm == null) { return; }
        repo.getCreateProductRepository().productSupermarket.setValue(sm);
    }

    public void createProduct() {
        long ean = getEanObservable().getValue();
        Supermarket supermarket = getProductSupermarketObservable().getValue();

        CreateProductBody body = new CreateProductBody(ean, supermarket);
        repo.getCreateProductRepository().createProduct(body);
    }

    public void createProduct(String name, String brandname, List<String> labels) {
        long ean = getEanObservable().getValue();
        Supermarket supermarket = getProductSupermarketObservable().getValue();

        CreateProductBody body = new CreateProductBody(ean, name, brandname, labels, supermarket);
        repo.getCreateProductRepository().createProduct(body);
    }

    public LiveData<Boolean> getCreateSuccessObservable() {
        return repo.getCreateProductRepository().isSuccess;
    }
}
