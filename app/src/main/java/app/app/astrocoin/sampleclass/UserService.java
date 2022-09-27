package app.app.astrocoin.sampleclass;

import app.app.astrocoin.models.CheckWallet;
import app.app.astrocoin.models.ImgUpload;
import app.app.astrocoin.models.LoginRequest;
import app.app.astrocoin.models.SendTransferRequest;
import app.app.astrocoin.models.SetPassword;
import app.app.astrocoin.models.TokenRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {
    @POST("login")
    Call<LoginRequest> uerLogin(@Body LoginRequest loginRequest);

    @GET("user")
    Call<TokenRequest> userTokenRequest(@Header("Authorization") String token);

    @GET("orders")
    Call<Object> userOrderRequest(@Query("page") int param, @Header("Authorization") String orders);

    @GET("users")
    Call<Object> userSearchRequest(@Header("Authorization") String users);

    @POST("wallet/transfer")
    Call<Object> sendTransfers(@Header("Authorization") String token, @Body SendTransferRequest transferRequest);

    @GET("transfers")
    Call<Object> userGetTransfers(@Query("page") int param, @Header("Authorization") String token);

    @Multipart
    @POST("/api/user/photo")
    Call<ImgUpload> userSetPhoto(@Header("Authorization") String token, @Part MultipartBody.Part photo);

    @POST("user/password")
    Call<Object> userChangePassword(@Header("Authorization") String token, @Body SetPassword setPassword);

    @POST("wallet")
    Call<Object> userWalletName(@Header("Authorization") String token, @Body CheckWallet checkWallet);

    @POST("logout")
    Call<Object> userLogOut(@Header("Authorization") String token);
}
