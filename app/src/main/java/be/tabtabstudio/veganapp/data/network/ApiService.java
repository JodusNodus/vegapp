package be.tabtabstudio.veganapp.data.network;

import be.tabtabstudio.veganapp.data.network.requestBodies.CreateProductBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.RateProductBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserLoginBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserSignupBody;
import be.tabtabstudio.veganapp.data.network.results.GetBrandsResult;
import be.tabtabstudio.veganapp.data.network.results.GetLabelsResult;
import be.tabtabstudio.veganapp.data.network.results.GetProductResult;
import be.tabtabstudio.veganapp.data.network.results.GetProductsResult;
import be.tabtabstudio.veganapp.data.network.results.GetSupermarketsResult;
import be.tabtabstudio.veganapp.data.network.results.LoginResult;
import be.tabtabstudio.veganapp.data.network.results.UploadProductImageResult;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("signup")
    Call<LoginResult> signup(@Body UserSignupBody userSignupBody);

    @POST("login")
    Call<LoginResult> login(@Body UserLoginBody userLoginBody);

    @POST("logout")
    Call<Void> logout();

    @POST("api/location")
    Call<Void> setLocation(@Body Location location);

    @GET("api/products")
    Call<GetProductsResult> getProducts(@Query("searchquery") String searchquery, @Query("orderby") String orderby, @Query("size") int size, @Query("page") int page, @Query("labels") String labels);

    @GET("api/products/{ean}")
    Call<GetProductResult> getProduct(@Path("ean") long ean);

    @POST("api/products/{ean}/rate")
    Call<Void> rateProduct(@Path("ean") long ean, @Body RateProductBody rateProductBody);

    @DELETE("api/products/{ean}")
    Call<Void> markProductInvalid(@Path("ean") long ean);

    @GET("api/supermarkets")
    Call<GetSupermarketsResult> getSupermarktes();

    @Multipart
    @POST("api/products/picture")
    Call<UploadProductImageResult> uploadProductImage(@Part MultipartBody.Part ean, @Part MultipartBody.Part picture);

    @POST("api/products")
    Call<Void> createProduct(@Body CreateProductBody createProductBody);

    @GET("api/brands")
    Call<GetBrandsResult> getBrands();

    @GET("api/labels")
    Call<GetLabelsResult> getLabels();
}
