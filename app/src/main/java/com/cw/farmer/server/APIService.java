package com.cw.farmer.server;

import com.cw.farmer.model.AdhocResponse;
import com.cw.farmer.model.AllCentreResponse;
import com.cw.farmer.model.AllResponse;
import com.cw.farmer.model.BankNameResponse;
import com.cw.farmer.model.BlacklistPostResponse;
import com.cw.farmer.model.BlacklistResponse;
import com.cw.farmer.model.CropDateResponse;
import com.cw.farmer.model.CropWalkTB;
import com.cw.farmer.model.DashboardResponse;
import com.cw.farmer.model.DestructionReasonResponse;
import com.cw.farmer.model.GeneralSpinnerResponse;
import com.cw.farmer.model.FarmRegistrationRequestBody;
import com.cw.farmer.model.FarmerAccountsResponse;
import com.cw.farmer.model.FarmerDocResponse;
import com.cw.farmer.model.FarmerErrorResponse;
import com.cw.farmer.model.FarmerHarvestResponse;
import com.cw.farmer.model.FarmerModel;
import com.cw.farmer.model.SearchAreaResponse;
import com.cw.farmer.model.PlantBlockResponse;
import com.cw.farmer.model.PlantVerifyResponse;
import com.cw.farmer.model.PlantingVerificationResponse;
import com.cw.farmer.model.RegisterResponse;
import com.cw.farmer.model.RequisitionResponse;
import com.cw.farmer.model.Result;
import com.cw.farmer.model.SearchContractResponse;
import com.cw.farmer.model.SearchCropWalkResponse;
import com.cw.farmer.model.SearchDestructionResponse;
import com.cw.farmer.model.SprayFarmerResponse;
import com.cw.farmer.model.SprayNumbersResponse;
import com.cw.farmer.model.TasksResponse;
import com.cw.farmer.spinner_models.CropStageResponse;
import com.cw.farmer.spinner_models.GrowthParameterResponse;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
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

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/farmerscapture")
    Call<RegisterResponse> getRegister(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/farmerscapture")
    Call<FarmerErrorResponse> createFarmer(@Header("Authorization") String authorization, @Body FarmerModel farmerModel);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/banks")
    Call<List<BankNameResponse>> getbankname(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/reasonsforchange")
    Call<List<BlacklistResponse>> getblacklistreasons(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/farmerscapture/{id}")
    Call<BlacklistPostResponse> createblacklist( @Header("Authorization") String authorization,@Path("id") int groupId, @Query("command") String command,@Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/recruitment")
    Call<AllResponse> recruit(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/cropdates/activecropdates")
    Call<List<CropDateResponse>> getcropdate(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/cropdates/activerecruitmentcropdates")
    Call<List<CropDateResponse>> getRecruitCropDates(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/contractsigning/contractfarmers")
    Call<SearchContractResponse> getContractfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/contractsigning")
    Call<AllResponse> postcontract(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/contractsigning")
    Call<SearchDestructionResponse> getDestructionfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/cropdestructionreason")
    Call<List<DestructionReasonResponse>> getdestructionreasons(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/capturecropdestruction")
    Call<AllResponse> postcropdestruction(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default",})
    @GET("/fineract-provider/api/v1/{id}/farmerdocs")
    Call<List<FarmerDocResponse>> getfamerdocs(@Path("id") int groupId, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/{id}/farmeraccounts")
    Call<List<FarmerAccountsResponse>> getfameraccount(@Path("id") int groupId, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/contractsigning")
    Call<FarmerHarvestResponse> getHarvestfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/harvesting")
    Call<AllResponse> postharvesting(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @PUT("/fineract-provider/api/v1/farmerscapture/{id}")
    Call<AllResponse> posteditfarmer(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl,@Path("id") int groupId);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/plantingverification/contractedFarmers")
    Call<PlantVerifyResponse> getplantingfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/plantingverification")
    Call<AllResponse> postplantverify(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/tasks/")
    Call<TasksResponse> gettask(@Header("Authorization") String authorization, @Query("centerId") String groupId);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/plantrequisition/requistionitems/{id}")
    Call<List<RequisitionResponse>> getrequsition(@Path("id") String groupId, @Header("Authorization") String authorization);

    //RETURN PLANT VERIFICATION
    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/plantingverification/{entityId}")
    Call<PlantingVerificationResponse> getPlantingVerfication(@Path("entityId") String groupId, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/tasks/{taskID}?command=returnstock")
    Call<ResponseBody> postReturnStock(@Path("taskID") String taskID, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/centrestore")
    Call<JsonObject> postreceiveinventory(@Header("Authorization") String authorization, @Body JsonObject registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/hierarchy/template")
    Call<List<AllCentreResponse>> getcentre(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/farmercentres")
    Call<AllResponse> postchangecentre(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @PUT("/fineract-provider/api/v1/{id}/farmeraccounts/{entry}")
    Call<AllResponse> changebank(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl, @Path("id") String groupId, @Path("entry") String entry);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @PUT("/fineract-provider/api/v1/{id}/farmerdocs/{entry}")
    Call<AllResponse> changedocs(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl, @Path("id") String groupId, @Path("entry") String entry);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/plantingverification/verifiedFarmers")
    Call<SprayFarmerResponse> getSprayfarmer(@Query("limit") int limit, @Query("offset") int offset, @Query("sqlSearch") String search, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/sprayprogram/regionprograms")
    Call<SprayNumbersResponse> getspraynumber(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/sprayconfirmation")
    Call<AllResponse> spraypost(@Header("Authorization") String authorization, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/statistics/{id}")
    Call<DashboardResponse> getdashboard(@Path("id") String groupId, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/tasks/adhoctasks")
    Call<AdhocResponse> getAdhoc(@Query("centerId") String centerId, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/tasks/{taskid}")
    Call<AllResponse> approvetasks(@Path("taskid") String taskid, @Header("Authorization") String authorization, @Query("command") String command, @Body HashMap registerApiPayloadl);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/farmregistration")
    Call<String>  farmRegistration(@Header("Authorization") String authorization,@Body FarmRegistrationRequestBody requestBody);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/farmnames")
    Call<List<GeneralSpinnerResponse>>  getFarmNames(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/plantblocks")//?offset=0&limit=15
    Call<PlantBlockResponse> getPlantBlockNames(@Query("limit") int limit, @Query("offset") int offset, @Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/farmregistrationarea/searchAreas")//?offset=0&limit=15
    Call<SearchAreaResponse> getSearchArea(@Query("limit") int limit, @Query("offset") int offset, @Header("Authorization") String authorization);


    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/plantvariety")
    Call<List<GeneralSpinnerResponse>>  getVariety(@Header("Authorization") String authorization);


    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/harvestblocks")
    Call<ResponseBody> postHarvestBlocks(@Header("Authorization") String authorization, @Body HashMap requestBody);


    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/plantblocks")
    Call<ResponseBody> postPlantBlock(@Header("Authorization") String authorization, @Body HashMap requestBody);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/farmregistration")
    Call<ResponseBody> postRegisterBlock(@Header("Authorization") String authorization, @Body HashMap requestBody);


    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/farmregistrationarea/areas/{registrationid}")
    Call<ResponseBody> getPlantingBlockCropDate(@Query("registrationid")String registrationid,@Header("Authorization") String authorization);


    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/irrigateblocks")
    Call<ResponseBody> postIrrigateBlock(@Header("Authorization") String authorization, @Body HashMap requestBody);


    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/fertilizer")
    Call<List<GeneralSpinnerResponse>> getFertilizer(@Header("Authorization") String authorization);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/applyfertilizer")
    Call<ResponseBody> postApplyFertilizer(@Header("Authorization") String authorization, @Body HashMap requestBody);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/scoutmonitor")
    Call<ResponseBody> postScoutMonitor(@Header("Authorization") String authorization, @Body HashMap requestBody);

    //https://howtodoinjava.com/retrofit2/query-path-parameters/
    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/cropwalkparameter/parameters/{id}")
    Call<List<GrowthParameterResponse>> getGrowthParameters(@Header("Authorization") String authorization, @Path("id") Long id);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @GET("/fineract-provider/api/v1/cropwalkparameter/cropstages")
    Call<List<CropStageResponse>> getCropStages(@Header("Authorization") String authorization);


    //NOTE: WITH IDS AS STRINGS
    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/cropwalk")
    Call<ResponseBody> postCropWalk(@Header("Authorization") String authorization, @Body HashMap request);

    //NOTE: WITH IDS AS INTEGER
    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    @POST("/fineract-provider/api/v1/cropwalk")
    Call<ResponseBody> postCropWalk(@Header("Authorization") String authorization, @Body CropWalkTB request);

    @Headers({"Accept: application/json", "Fineract-Platform-TenantId:default"})
    //@GET("/fineract-provider/api/v1/cropwalk")
    @GET("fineract-provider/api/v1/contractsigning/cropwalkfarmers")
    Call<SearchCropWalkResponse> getCropWalkSearch(@Header("Authorization") String authorization, @Query("offset") int offset, @Query("limit") int limit,@Query("sqlSearch") String search);


}



