package be.tabtabstudio.veganapp.data.subrepos;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import be.tabtabstudio.veganapp.data.local.AppDatabase;
import be.tabtabstudio.veganapp.data.network.ApiService;
import be.tabtabstudio.veganapp.data.network.results.GetProductResult;
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
    public final MutableLiveData<Boolean> alreadyExists;
    public final MutableLiveData<List<String>> labels;
    public final MutableLiveData<String> brandname;
    public final MutableLiveData<List<String>> labelSuggestions;
    public final MutableLiveData<List<String>> brandSuggestions;

    public CreateProductRepository(ApiService api, AppDatabase db) {
        this.api = api;
        this.db = db;
        ean = new MutableLiveData<>();
        alreadyExists = new MutableLiveData<>();
        labels = new MutableLiveData<>();
        brandname = new MutableLiveData<>();
        labelSuggestions = new MutableLiveData<>();
        brandSuggestions = new MutableLiveData<>();
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
}
