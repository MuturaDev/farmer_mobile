package com.cw.farmer.server;

import com.cw.farmer.activity.SearchContractFarmerActivity;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.BankNameResponse;
import com.cw.farmer.model.BlacklistPostResponse;
import com.cw.farmer.model.BlacklistResponse;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.DestructionReasonResponse;
import com.cw.farmer.model.FarmerAccountsResponse;
import com.cw.farmer.model.FarmerDocResponse;
import com.cw.farmer.model.FarmerErrorResponse;
import com.cw.farmer.model.FarmerHarvestResponse;
import com.cw.farmer.model.FarmerModel;
import com.cw.farmer.model.PlantVerifyResponse;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.model.RequisitionResponse;
import com.cw.farmer.model.Result;
import com.cw.farmer.model.SearchContractResponse;
import com.cw.farmer.model.SearchDestructionResponse;
import com.cw.farmer.model.TasksResponse;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/authentication")
    Call<Result> userLogin(
            @Query("username") String username,
            @Query("password") String password
    );

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/farmerscapture")
    Call<RegisterResponse> getRegister(@Query("limit") int limit,@Query("offset") int offset,@Query("sqlSearch") String search);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/farmerscapture")
    Call<FarmerErrorResponse> createFarmer(@Header("Authorization") String authorization, @Body FarmerModel farmerModel);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/banks")
    Call<List<BankNameResponse>> getbankname();

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/reasonsforchange")
    Call<List<BlacklistResponse>> getblacklistreasons();

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/farmerscapture/{id}")
    Call<BlacklistPostResponse> createblacklist( @Header("Authorization") String authorization,@Path("id") int groupId, @Query("command") String command,@Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/recruitment")
    Call<AllResponse> recruit(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/cropdates/activecropdates")
    Call<List<CropDateResponse>> getcropdate();

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/contractsigning/contractfarmers")
    Call<SearchContractResponse> getContractfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/contractsigning")
    Call<AllResponse> postcontract(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/contractsigning")
    Call<SearchDestructionResponse> getDestructionfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/cropdestructionreason")
    Call<List<DestructionReasonResponse>> getdestructionreasons();

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/capturecropdestruction")
    Call<AllResponse> postcropdestruction(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/{id}/farmerdocs")
    Call<List<FarmerDocResponse>> getfamerdocs( @Path("id") int groupId);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/{id}/farmeraccounts")
    Call<List<FarmerAccountsResponse>> getfameraccount(@Path("id") int groupId);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/contractsigning/contractedfarmers")
    Call<FarmerHarvestResponse> getHarvestfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/harvesting")
    Call<AllResponse> postharvesting(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @PUT("/fineract-provider/api/v1/farmerscapture/{id}")
    Call<AllResponse> posteditfarmer(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl,@Path("id") int groupId);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/plantingverification/contractedFarmers")
    Call<PlantVerifyResponse> getplantingfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/plantingverification")
    Call<AllResponse> postplantverify(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/tasks/{id}")
    Call<TasksResponse> gettask(@Header("Authorization") String authorization,@Path("id") int groupId);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default", "Authorization:Basic YWRtaW46bWFudW5pdGVk"})
    @GET("/fineract-provider/api/v1/plantrequisition/requistionitems/{id}")
    Call<List<RequisitionResponse>> getrequsition(@Path("id") String groupId);






}



