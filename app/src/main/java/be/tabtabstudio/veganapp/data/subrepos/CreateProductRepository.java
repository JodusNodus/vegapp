package be.tabtabstudio.veganapp.data.subrepos;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Brand;
import be.tabtabstudio.veganapp.data.entities.Label;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;
import be.tabtabstudio.veganapp.data.local.AppDatabase;
import be.tabtabstudio.veganapp.data.network.ApiService;
import be.tabtabstudio.veganapp.data.network.requestBodies.CreateProductBody;
import be.tabtabstudio.veganapp.data.network.results.GetBrandsResult;
import be.tabtabstudio.veganapp.data.network.results.GetLabelsResult;
import be.tabtabstudio.veganapp.data.network.results.GetProductResult;
import be.tabtabstudio.veganapp.data.network.results.GetSupermarketsResult;
import be.tabtabstudio.veganapp.data.network.results.UploadProductImageResult;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductRepository {
    private ApiService api;
    private AppDatabase db;

    public final MutableLiveData<Long> ean;
    public final MutableLiveData<Supermarket> productSupermarket;

    public final MutableLiveData<Boolean> alreadyExists;
    public final MutableLiveData<List<String>> allLabels;
    public final MutableLiveData<List<String>> allBrands;
    public final MutableLiveData<String> coverImage;
    public final MutableLiveData<List<String>> labelSuggestions;
    public final MutableLiveData<List<String>> brandSuggestions;
    public final MutableLiveData<List<Supermarket>> supermarkets;
    public final MutableLiveData<Boolean> isSuccess;

    public CreateProductRepository(ApiService api, AppDatabase db) {
        this.api = api;
        this.db = db;
        ean = new MutableLiveData<>();
        productSupermarket = new MutableLiveData<>();

        alreadyExists = new MutableLiveData<>();
        coverImage = new MutableLiveData<>();
        labelSuggestions = new MutableLiveData<>();
        brandSuggestions = new MutableLiveData<>();
        allLabels = new MutableLiveData<>();
        allBrands = new MutableLiveData<>();
        supermarkets = new MutableLiveData<>();

        isSuccess = new MutableLiveData<>();
    }

    public void doesProductAlreadyExist(long ean) {
        api.getProduct(ean).enqueue(new Callback<GetProductResult>() {
            @Override
            public void onResponse(Call<GetProductResult> call, Response<GetProductResult> response) {
                if (response.code() == 200) {
                    alreadyExists.setValue(true);
                } else {
                    alreadyExists.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<GetProductResult> call, Throwable t) {
                t.printStackTrace();
                alreadyExists.setValue(false);
            }
        });
    }

    private File jpgToFile(Long ean, byte[] jpgPicture) throws IOException {
        File f = File.createTempFile(ean.toString(), "new-vegan-product");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(jpgPicture);
        fos.flush();
        fos.close();
        return f;
    }

    public void uploadProductImage(long ean, CameraKitImage image) {

        File f;
        try {
            f = jpgToFile(ean, image.getJpeg());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        MultipartBody.Part eanPart = MultipartBody.Part.createFormData("ean", String.valueOf(ean));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part picturePart = MultipartBody.Part.createFormData("picture", f.getName(), reqFile);

        api.uploadProductImage(eanPart, picturePart).enqueue(new Callback<UploadProductImageResult>() {
            @Override
            public void onResponse(Call<UploadProductImageResult> call, Response<UploadProductImageResult> response) {
                if (response.code() == 200) {
                    labelSuggestions.setValue(response.body().labelSuggestions);
                    brandSuggestions.setValue(response.body().brandSuggestions);
                    coverImage.setValue(Product.getCoverImage(ean));
                } else {
                    labelSuggestions.setValue(null);
                    brandSuggestions.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UploadProductImageResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchAllLabels() {
        api.getLabels().enqueue(new Callback<GetLabelsResult>() {
            @Override
            public void onResponse(Call<GetLabelsResult> call, Response<GetLabelsResult> response) {
                if (response.code() == 200) {
                    List<String> l = new ArrayList<>();
                    for (Label label : response.body().labels) {
                        l.add(label.name);
                    }
                    allLabels.setValue(l);
                } else {
                    allLabels.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetLabelsResult> call, Throwable t) {
                t.printStackTrace();
                allLabels.setValue(null);
            }
        });
    }

    public void fetchAllBrands() {
        api.getBrands().enqueue(new Callback<GetBrandsResult>() {
            @Override
            public void onResponse(Call<GetBrandsResult> call, Response<GetBrandsResult> response) {
                if (response.code() == 200) {
                    List<String> l = new ArrayList<>();
                    for (Brand brand : response.body().brands) {
                        l.add(brand.name);
                    }
                    allBrands.setValue(l);
                } else {
                    allBrands.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetBrandsResult> call, Throwable t) {
                t.printStackTrace();
                allBrands.setValue(null);
            }
        });
    }

    public void fetchSupermarkets() {
        api.getSupermarktes().enqueue(new Callback<GetSupermarketsResult>() {
            @Override
            public void onResponse(Call<GetSupermarketsResult> call, Response<GetSupermarketsResult> response) {
                if (response.code() == 200) {
                    supermarkets.setValue(response.body().supermarkets);
                } else {
                    supermarkets.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetSupermarketsResult> call, Throwable t) {
                t.printStackTrace();
                supermarkets.setValue(null);
            }
        });
    }

    public void createProduct(CreateProductBody body) {
        api.createProduct(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    isSuccess.setValue(true);
                } else {
                    isSuccess.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isSuccess.setValue(null);
                t.printStackTrace();
            }
        });
    }
}
