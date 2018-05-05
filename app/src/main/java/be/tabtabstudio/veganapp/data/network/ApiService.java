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

import static be.tabtabstudio.veganapp.data.network.ApiServiceFactory.ORIGIN_HEADER;

public interface ApiService {
    @Headers(ORIGIN_HEADER)
    @POST("signup")
    Call<LoginResult> signup(@Body UserSignupBody userSignupBody);

    @Headers(ORIGIN_HEADER)
    @POST("login")
    Call<LoginResult> login(@Body UserLoginBody userLoginBody);

    @Headers(ORIGIN_HEADER)
    @POST("logout")
    Call<Void> logout();

    @Headers(ORIGIN_HEADER)
    @POST("api/location")
    Call<Void> setLocation(@Body Location location);

    @Headers(ORIGIN_HEADER)
    @GET("api/products")
    Call<GetProductsResult> getProducts(@Query("searchquery") String searchquery, @Query("orderby") String orderby, @Query("size") int size, @Query("page") int page, @Query("labels") String labels);

    @Headers(ORIGIN_HEADER)
    @GET("api/products/{ean}")
    Call<GetProductResult> getProduct(@Path("ean") long ean);

    @Headers(ORIGIN_HEADER)
    @POST("api/products/{ean}/rate")
    Call<Void> rateProduct(@Path("ean") long ean, @Body RateProductBody rateProductBody);

    @Headers(ORIGIN_HEADER)
    @DELETE("api/products/{ean}")
    Call<Void> markProductInvalid(@Path("ean") long ean);

    @Headers(ORIGIN_HEADER)
    @GET("api/supermarkets")
    Call<GetSupermarketsResult> getSupermarktes();

    @Headers(ORIGIN_HEADER)
    @Multipart
    @POST("api/products/picture")
    Call<UploadProductImageResult> uploadProductImage(@Part MultipartBody.Part ean, @Part MultipartBody.Part picture);

    @Headers(ORIGIN_HEADER)
    @POST("api/products")
    Call<Void> createProduct(@Body CreateProductBody createProductBody);

    @Headers(ORIGIN_HEADER)
    @GET("api/brands")
    Call<GetBrandsResult> getBrands();

    @Headers(ORIGIN_HEADER)
    @GET("api/labels")
    Call<GetLabelsResult> getLabels();
}
