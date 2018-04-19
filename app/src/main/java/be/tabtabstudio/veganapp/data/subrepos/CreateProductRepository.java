package be.tabtabstudio.veganapp.data.subrepos;

import android.arch.lifecycle.MutableLiveData;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitImage;

import be.tabtabstudio.veganapp.data.local.AppDatabase;
import be.tabtabstudio.veganapp.data.network.ApiService;
import be.tabtabstudio.veganapp.data.network.results.GetProductResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductRepository {
    private ApiService api;
    private AppDatabase db;

    public final MutableLiveData<Long> ean;
    public final MutableLiveData<Boolean> alreadyExists;
    public final MutableLiveData<CameraKitImage> image;

    public CreateProductRepository(ApiService api, AppDatabase db) {
        this.api = api;
        this.db = db;
        ean = new MutableLiveData<>();
        alreadyExists = new MutableLiveData<>();
        image = new MutableLiveData<>();
    }

    public void setImage(CameraKitImage cameraKitImage) {
        image.setValue(cameraKitImage);
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
}
