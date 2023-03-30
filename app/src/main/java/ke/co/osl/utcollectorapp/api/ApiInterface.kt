package ke.co.osl.utcollectorapp.api

import ke.co.osl.utcollectorapp.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {

        companion object {

         var BASE_URL = "http://194.163.165.107:4001/api/"

            fun create() : ApiInterface {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                return retrofit.create(ApiInterface::class.java)
            }
        }


        @POST("auth/login")
        fun loginUser(@Body loginBody: LoginBody) : Call<Message>


        @POST("mobile/forgot")
        fun recoverPassword(@Body recoverPasswordBody: RecoverPasswordBody) : Call<Message>

        @PUT("mobile/{id}")
        fun changePassword(@Path("id") id: String, @Body changePasswordBody: ChangePasswordBody) : Call<Message>


        @POST("agrovets/create")
        fun postAgrovet(@Body agrovetBody: AgrovetBody) : Call<Message>

        @PUT("agrovets/{id}")
        fun putAgrovet(@Path("id") id:String, @Body agrovetBody: AgrovetGetBody) : Call<Message>

        @GET("agrovets/details/{id}")
        fun searchAgrovets(@Path("id") id: String) : Call<List<AgrovetGetBody>>

        @POST("schools/create")
        fun postSchools(@Body schoolsBody: SchoolsBody) : Call<Message>

        @PUT("schools/{id}")
        fun putSchools(@Path("id") id:String, @Body schoolsBody: SchoolsGetBody) : Call<Message>

        @GET("schools/details/{id}")
        fun searchSchools(@Path("id") id: String) : Call<List<SchoolsGetBody>>


        @POST("abattoir/create")
        fun postAbattoir(@Body abattoirBody: AbattoirBody) : Call<Message>

        @PUT("abattoir/{id}")
        fun putAbattoir(@Path("id") id:String, @Body abattoirBody: AbattoirGetBody) : Call<Message>

        @GET("abattoir/details/{id}")
        fun searchAbattoirs(@Path("id") id: String) : Call<List<AbattoirGetBody>>

        @POST("practitioners/create")
        fun postPractitioner(@Body practitionerBody: PractitionerBody) : Call<Message>

        @PUT("practitioners/{id}")
        fun putPractitioner(@Path("id") id:String, @Body practitionerBody: PractitionerGetBody) : Call<Message>

        @GET("practitioners/details/{id}")
        fun searchPractitioners(@Path("id") id: String) : Call<List<PractitionerGetBody>>

        @POST("communitygroups/create")
        fun postCommunityGroups(@Body communitygroupsBody: CommunityGroupsBody) : Call<Message>

        @PUT("communitygroups/{id}")
        fun putCommunityGroups(@Path("id") id:String, @Body communitygroupsBody: CommunityGroupsGetBody) : Call<Message>

        @GET("communitygroups/details/{id}")
        fun searchCommunityGroups(@Path("id") id: String) : Call<List<CommunityGroupsGetBody>>

        @POST("equineowners/create")
        fun postEquineowner(@Body equineownersBody: EquineOwnersBody) : Call<Message>

        @PUT("equineowners/{id}")
        fun putEquineowner(@Path("id") id:String, @Body equineownersBody: EquineOwnersGetBody) : Call<Message>

        @GET("equineowners/details/{id}")
        fun searchEquineowner(@Path("id") id: String) : Call<List<EquineOwnersGetBody>>

        @POST("farriers/create")
        fun postFarrier(@Body farriersBody: FarriersBody) : Call<Message>

        @PUT("farriers/{id}")
        fun putFarrier(@Path("id") id:String, @Body farrierGetBody: FarrierGetBody) : Call<Message>

        @GET("farriers/details/{id}")
        fun searchFarrier(@Path("id") id: String) : Call<List<FarrierGetBody>>

        @POST("agrovets/monitoring/create")
        fun postAgrovetsMonitoring(@Body agrovetMonitoringBody: AgrovetMonitoringBody) : Call<Message>

        }