package be.tabtabstudio.veganapp.data.network;

import be.tabtabstudio.veganapp.data.network.requestBodies.CreateProductBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.RateProductBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserLoginBody;
import be.tabtabstudio.veganapp.data.network.requestBodies.UserSignupBody;
import be.tabtabstudio.veganapp.data.network.results.GetBrandsResult;
import be.tabtabstudio.veganapp.data.network.results.GetLabelsResult;
import be.tabtabstudio.veganapp.data.network.results.GetProductResult;
import be.tabtabstudio.veganapp.data.network.results.GetProductsResult;
import be.tabtabstudio.veganapp.data.network.results.LoginResult;
import be.tabtabstudio.veganapp.data.network.results.UploadProductImageResult;
import be.tabtabstudio.veganapp.data.entities.Location;
import be.tabtabstudio.veganapp.data.entities.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("signup")
    Call<ApiResponse<LoginResult>> signup(@Body UserSignupBody userSignupBody);

    @POST("login")
    Call<ApiResponse<LoginResult>> login(@Body UserLoginBody userLoginBody);

    @POST("logout")
    Call<ApiResponse> logout();

    @POST("api/location")
    Call<ApiResponse> setLocation(@Body Location location);

    @GET("api/products")
    Call<ApiResponse<GetProductsResult>> getProducts(@Query("searchquery") String searchquery, @Query("page") int page);

    @GET("api/products/{ean}")
    Call<ApiResponse<GetProductResult>> getProduct(@Path("ean") long ean);

    @POST("api/products/{ean}/rate")
    Call<ApiResponse> rateProduct(@Path("ean") long ean, @Body RateProductBody rateProductBody);

    @DELETE("api/products/{ean}")
    Call<ApiResponse> markProductInvalid(@Path("ean") long ean);

    @Multipart
    @POST("api/products/image")
    Call<ApiResponse<UploadProductImageResult>> uploadProductImage(@Part long ean, @Part("photo") RequestBody photo);

    @POST("api/products")
    Call<ApiResponse> createProduct(@Body CreateProductBody createProductBody);

    @GET("api/brands")
    Call<ApiResponse<GetBrandsResult>> getBrands();

    @GET("api/labels")
    Call<ApiResponse<GetLabelsResult>> getLabels();
}
