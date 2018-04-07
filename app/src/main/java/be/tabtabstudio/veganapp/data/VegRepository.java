package be.tabtabstudio.veganapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import be.tabtabstudio.veganapp.AppExecutors;
import be.tabtabstudio.veganapp.data.entities.Favorites;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.Supermarket;
import be.tabtabstudio.veganapp.data.entities.User;
import be.tabtabstudio.veganapp.data.local.AppDatabase;
import be.tabtabstudio.veganapp.data.network.ApiResponse;
import be.tabtabstudio.veganapp.data.network.ApiService;
import be.tabtabstudio.veganapp.data.network.ApiServiceFactory;
import be.tabtabstudio.veganapp.data.network.requestBodies.RateProductBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserLoginBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserSignupBody;
import be.tabtabstudio.veganapp.data.network.results.GetProductResult;
import be.tabtabstudio.veganapp.data.network.results.LoginResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VegRepository {
    private static final String LOG_TAG = VegRepository.class.getSimpleName();

    private static VegRepository sInstance;

    private final AppExecutors executors;
    private final ApiService api;
    private final AppDatabase db;
    private final SharedPreferences sp;
    private final MutableLiveData<Product> productData;
    private final MutableLiveData<List<Supermarket>> supermarketsData;
    private final MutableLiveData<User> userData;
    private final MutableLiveData<Location> locationData;
    private LiveData<Favorites> favoritesData;

    public static VegRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VegRepository(context);
        }
        return sInstance;
    }

    private VegRepository(Context context) {
        executors = AppExecutors.getInstance();
        db = AppDatabase.getInstance(context);
        api = ApiServiceFactory.create();
        sp = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        productData = new MutableLiveData<>();
        userData = new MutableLiveData<>();
        supermarketsData = new MutableLiveData<>();
        locationData = new MutableLiveData<>();

        setFavoritesObservable();
    }

    public LiveData<Location> getLocationObservable() {
        return locationData;
    }

    public LiveData<List<Supermarket>> getSupermarketsObservable() {
        return supermarketsData;
    }

    public LiveData<Product> getProductObservable() {
        return productData;
    }

    public LiveData<User> getUserObservable() {
        return userData;
    }

    public LiveData<Favorites> getFavoritesObservable() {
        return favoritesData;
    }

    private void setFavoritesObservable() {
        executors.diskIO().execute(() -> {
            LiveData<List<Product>> productListData = db.favoritesDao().getAll();

            executors.mainThread().execute(() -> {
                favoritesData = Transformations.map(productListData, (products) -> {
                    return new Favorites(products);
                });
            });
        });
    }

    public UserLoginBody getPersistedLogin() {
        String email = sp.getString("email", null);
        String password = sp.getString("password", null);
        if (email != null && password != null) {
            return new UserLoginBody(email, password);
        }
        return null;
    }

    private void persistLogin(UserLoginBody userLoginBody) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", userLoginBody.email);
        editor.putString("password", userLoginBody.password);
        editor.commit();
    }

    public Callback<ApiResponse<LoginResult>> getLoginCallback(UserLoginBody userLoginBody) {
        return new Callback<ApiResponse<LoginResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResult>> call, Response<ApiResponse<LoginResult>> response) {
                if (response.code() == 200) {
                    persistLogin(userLoginBody);
                    userData.setValue(response.body().result.user);
                } else {
                    userData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResult>> call, Throwable t) {
                t.printStackTrace();
            }
        };
    }

    public void login(UserLoginBody userLoginBody) {
        api.login(userLoginBody).enqueue(getLoginCallback(userLoginBody));
    }

    public void signup(UserSignupBody userSignupBody) {
        api.signup(userSignupBody).enqueue(getLoginCallback(userSignupBody));
    }

    public void setLocation(Location loc) {
        api.setLocation(loc).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    locationData.setValue(loc);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchProduct(long ean) {
        executors.diskIO().execute(() -> {
            Product product = db.favoritesDao().getById(ean);
            if (product != null) {
                productData.postValue(product);
            }
        });
        api.getProduct(ean).enqueue(new Callback<ApiResponse<GetProductResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<GetProductResult>> call, Response<ApiResponse<GetProductResult>> response) {
                if (response.code() == 200) {
                    supermarketsData.setValue(response.body().result.supermarkets);
                    productData.setValue(response.body().result.product);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GetProductResult>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void markProductInvalid(Product p) {
        api.markProductInvalid(p.ean).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    if (p == productData.getValue()) {
                        p.userHasCorrected = true;
                        productData.setValue(p);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void addFavorite(Product product) {
        executors.diskIO().execute(() -> {
            db.favoritesDao().insert(product);
        });
    }

    public void removeFavorite(Product product) {
        executors.diskIO().execute(() -> {
            db.favoritesDao().delete(product);
        });
    }

    public void rateProduct(Product p, int rating) {
        api.rateProduct(p.ean, new RateProductBody(rating)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    Log.i("repo", "Rate product success");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
