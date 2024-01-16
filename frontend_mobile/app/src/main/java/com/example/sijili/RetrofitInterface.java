package com.example.sijili;

import com.example.sijili.requests.BusinessRequest;
import com.example.sijili.requests.CommerceRequest;
import com.example.sijili.requests.EmailRequest;
import com.example.sijili.requests.MessageRequest;
import com.example.sijili.requests.NotificationsRequest;
import com.example.sijili.requests.OTPRequest;
import com.example.sijili.requests.PasswordResetRequest;
import com.example.sijili.requests.SignupRequest;
import com.example.sijili.requests.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface RetrofitInterface {

    //login
    @POST("/login")
    Call<LoginResponse> executeLogin(@Body LoginRequest loginRequest);

    //signUp
    @POST("/register")
    Call<Void> executeSignup(@Body SignupRequest signupRequest);

    //otpVerify
    @POST("/verify")
    Call<Void> verifyOtp(@Body OTPRequest otpRequest);


    @PUT("/admin/users/{id}")
    Call<User> saveUser(@Path("id") String userId, @Body User user);
    /*#################################################
    /-------------forgotAndReseatPassword-----------------
     #################################################*/

    @POST("/forgot-password")
    Call<Void> forgotPassword(@Body EmailRequest emailRequest);

    @POST("/forgot-password/reset-password")
    Call<Void> resetPassword(@Body PasswordResetRequest passwordResetRequest);

    /*#################################################
    /-------------------CLIENT-----------------------
     #################################################*/
    @POST("/client/post")
    Call<Void> executeNewRequest(@Header("Authorization") String authToken, @Body BusinessRequest businessRequest);
    @GET("/client/posts")
    Call<List<BusinessRequest>> getMyBusinessRequests(@Header("Authorization") String authToken);

    @GET("/client/posts/{id}")
    Call<BusinessRequest> getBusinessRequestById(@Header("Authorization")String authToken, @Path("id") String id);
    @DELETE("/client/posts/{id}")
    Call<Void> deleteMyCommerceRequest(@Header("Authorization")String authToken, @Path("id") String requestId);

    @PUT("/client/{id}/validate-payment")
    Call<Void> validatePayment(@Header("Authorization")String authToken, @Path("id") String id);

    @GET("/client/{id}/register-pdf")
    Call<ResponseBody> downloadPDF(@Header("Authorization")String authToken, @Path("id") String id);
    @GET("/notifications")
    Call<List<NotificationsRequest>> getNotifications(@Header("Authorization") String authToken);

    @GET("/notifications/notif")
    Call<Boolean> getNotificationSeenStatus(@Header("Authorization")String authToken);
    @DELETE("/notifications/{timestamp}")
    Call<Void> deleteNotification(
            @Header("Authorization") String authToken,
            @Path("timestamp") String timestamp
    );

    /*#################################################
    /-------------------SERVER-----------------------
     #################################################*/
    @GET("/server/posts") // New endpoint to get commerce registration requests
    Call<List<CommerceRequest>> getCommerceRequests(@Header("Authorization")String authToken);

    @GET("/server/posts/{id}") // New endpoint to get a specific commerce registration request by ID
    Call<CommerceRequest> getCommerceRequestById(@Header("Authorization")String authToken, @Path("id") String id);

    @PUT("/server/posts/{id}")
    Call<CommerceRequest> updateBusinessRequestStatus(@Header("Authorization")String authToken, @Path("id") String id);
    @PUT("/server/posts/{id}/refuse")
    Call<Void> deleteCommerceRequest(@Header("Authorization")String authToken, @Path("id") String requestId);

    @GET("/server/messages")
    Call<List<MessageRequest>> getMessages();

    @PUT("/logout")
    Call<Void> logout(@Header("Authorization")String authToken);



    @PUT("/switch-role")
    Call<Void> switchRole(@Header("Authorization")String authToken);

    @GET("/static/service")
    Call<List<BusinessRequestCount>> getBusinessRequestsCount();


}
