package be.tabtabstudio.veganapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;
import be.tabtabstudio.veganapp.data.entities.User;
import be.tabtabstudio.veganapp.data.network.ApiResponse;
import be.tabtabstudio.veganapp.data.network.ApiService;
import be.tabtabstudio.veganapp.data.network.ApiServiceFactory;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserLoginBody;
import be.tabtabstudio.veganapp.data.network.results.GetProductResult;
import be.tabtabstudio.veganapp.data.network.results.LoginResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VegRepository {
    private static final String LOG_TAG = VegRepository.class.getSimpleName();

    private static final VegRepository ourInstance = new VegRepository();

    private final MutableLiveData<Product> product;
    private final MutableLiveData<List<Supermarket>> supermarkets;
    private final MutableLiveData<User> user;

    private final MutableLiveData<Location> location;

    public static VegRepository getInstance() {
        return ourInstance;
    }

    private final ApiService api;

    private VegRepository() {
        api = ApiServiceFactory.create();
        product = new MutableLiveData<>();
        user = new MutableLiveData<>();
        supermarkets = new MutableLiveData<>();
        location = new MutableLiveData<>();
    }

    public LiveData<Location> getLocation() {
        return location;
    }

    public LiveData<List<Supermarket>> getSupermarkets() {
        return supermarkets;
    }

    public LiveData<Product> getProduct() {
        return product;
    }

    public LiveData<User> getUser() {
        return user;
    }


    public void login(String email, String password) {
        UserLoginBody body = new UserLoginBody(email, password);
        api.login(body).enqueue(new Callback<ApiResponse<LoginResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResult>> call, Response<ApiResponse<LoginResult>> response) {
                if (response.code() == 200) {
                    user.setValue(response.body().result.user);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResult>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setLocation(Location loc) {
        api.setLocation(loc).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    location.setValue(loc);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchProduct(long ean) {
        api.getProduct(ean).enqueue(new Callback<ApiResponse<GetProductResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<GetProductResult>> call, Response<ApiResponse<GetProductResult>> response) {
                if (response.code() == 200) {
                    supermarkets.setValue(response.body().result.supermarkets);
                    product.setValue(response.body().result.product);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GetProductResult>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void markProductInvalid(long ean) {
        api.markProductInvalid(ean).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    Product p = product.getValue();
                    if (p.ean == ean) {
                        p.userHasCorrected = true;
                        product.setValue(p);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
