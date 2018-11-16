package com.app.spyapp.helper;



import com.app.spyapp.helper.apiModel.MessageResp;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIService {

    @POST(Config.URL_GET_PRIMARY_DATA)
    @FormUrlEncoded
    Call<MessageResp> getPrimaryData(@Field("email") String email, @Field("device token") String device_token, @Field("model_number") String model_number);


//    @POST(Config.LOGIN_URL)
//    Call<TokenResp> getLoginData(@Body Map<String, String> body);

//    @POST(Config.SIGNUP_URL)
//    Call<Object> getSignUpData(@QueryMap("authorities") String authorities, @Body Map<String,String> body);

//    @POST(Config.SIGNUP_URL)
//    Call<Void> getSignUpData(@Body Map<String, Object> body);
//
//    @GET(Config.MENU_ITEM_URL)
//    Call<List<MenuItemResp>> getAllMenuItem(@Header("Authorization") String authorization);
//
    @POST(Config.NEW_SMS)
    Call<MessageResp> newSMS(@Header("token") String token, @Body Map<String, Object> body);

    @POST(Config.CONTACT_LIST)
    Call<MessageResp> getContactList(@Header("token") String token, @Body Map<String,Object> body);

    @POST(Config.CALL_LOG)
    Call<MessageResp> getCallLogList(@Header("token") String token, @Body Map<String,Object> body);

//    @Multipart
//    @POST(Config.CALL_RECORD)
//    Call<MessageResp> getCallRecord(@Header("token") String token, @Body Map<String,Object> body);

//    @Multipart
//    @POST(Config.CALL_RECORD)
//    Call<MessageResp> getCallRecord(@Header("token") String token, @Body Map<String,Object> body);

    @Multipart
    @POST(Config.CALL_RECORD)
    Call<MessageResp> getCallRecord(@Header("token") String token, @Part MultipartBody.Part file,@Part("incoming_no") RequestBody user_name, @Part("outgoing_no") RequestBody user_pass, @Part("date") RequestBody user_id, @Part("duration") RequestBody gender);



//    public RequestBody toRequestBody(String value) {
//        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
//        return body;
//    }

//    private MultipartBody.Part getImage(String path) {
//        File newFile = new File(path);
//        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), newFile);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", newFile.getName(), reqFile);
//        return body;
//    }





//    @POST(Config.CALL_RECORD)
//    Call<MessageResp> getCall(@Header("token") String token, @Body Map<String,Object> body);




//    @PUT(Config.MENU_ITEM_URL)
//    Call<MenuItemResp> UpdateMenuItem(@Header("Authorization") String authorization, @Body Map<String, String> body);
//
//    @DELETE(Config.MENU_ITEM_DELETE_URL)
//    Call<MenuItemResp> DeleteMenuItem(@Header("Authorization") String authorization, @Path("id") int id);
//
//    @GET(Config.ACCOUNT)
//    Call<UserDetail> getUserDetail(@Header("Authorization") String authorization);
//
//    @PUT(Config.USER)
//    Call<UserDetail> getUpdateUser(@Header("Authorization") String authorization, @Body Map<String, Object> body);
//
//
//
//    @POST(Config.CHANGE_PASSWORD)
//    Call<Void> changePassword(@Header("Authorization") String authorization, @Body Map<String, String> body);
//
//    @POST(Config.LOCATIONS)
//    Call<LocationResp> createLocations(@Header("Authorization") String authorization, @Body Map<String, Object> body);
//
//    @GET(Config.LOCATIONID)
//    Call<LocationResp> getLocation(@Header("Authorization") String authorization, @Path("resturentid") int id);
//
//    @PUT(Config.LOCATIONS)
//    Call<LocationResp> updateLocations(@Header("Authorization") String authorization, @Body Map<String, Object> body);
//
//    @DELETE(Config.LOCATIONID)
//    Call<Void> deleteLocation(@Header("Authorization") String authorization, @Path("resturentid") int id);

//    @POST(Config.SIGNUP_URL)
//    @FormUrlEncoded
//    Call<SignupResponse> getSignupData(@Field(Config.EMAIL) String email, @Field(Config.PASSWORD) String password, @Field(Config.USERNAME) String username, @Field(Config.USER_ROLE_ID) String user_role_id);
//
//    @POST(Config.FORGET_PWD_URL)
//    @FormUrlEncoded
//    Call<LoginResponse> getData(@Field(Config.EMAIL) String email);
//
//    @POST(Config.CHANGE_PWD_URL)
//    @FormUrlEncoded
//    Call<ChangePassword> getChngPwdData(@Field(Config.OLDPASSOWRD) String oldpass, @Field(Config.NEWPASSOWRD) String newpass, @Field(Config.USER_ID) String user_id);
//
//    @GET(Config.CATEGORY_LIST_URL)
//    Call<CaregoryList> getData();
//
//    @POST(Config.SUBCATEGORY_LIST)
//    @FormUrlEncoded
//    Call<SubcatList> getSubCatData(@Field(Config.CATEGOTY_ID) String cat_id, @Field(Config.SEARCH_BY) String search_by, @Field(Config.SORT_BY) String sort_by, @Field(Config.PRICE) String price);
//
//    @POST(Config.SUBCATEGORY)
//    @FormUrlEncoded
//    Call<SubCategories> getSubCategories(@Field(Config.CATEGOTY_ID) String cat_id);
//
//    @GET(Config.COURSE_LIST_URL)
//    Call<CourseList> getCourseData();
//
//    @POST(Config.HOME)
//    @FormUrlEncoded
//    Call<Categories> getHomeData(@Field(Config.USER_ID) String user_id);
//
//    @POST(Config.FACEBOOK_SIGNIN)
//    @FormUrlEncoded
//    Call<FbResponse> getFbData(@Field(Config.EMAIL) String email, @Field(Config.FB_ID) String fb_id, @Field(Config.USERNAME) String username, @Field(Config.AVATAR) String avatar, @Field(Config.USER_ROLE_ID) String user_role_id);
//
//    @POST(Config.GOOGLE_SIGNIN)
//    @FormUrlEncoded
//    Call<GmailResponse> getGpData(@Field(Config.EMAIL) String email, @Field(Config.GP_ID) String gplus_id, @Field(Config.USERNAME) String username, @Field(Config.AVATAR) String avatar, @Field(Config.USER_ROLE_ID) String user_role_id);
//
//    @Multipart
//    @POST(Config.UPDATE_PROFILE)
//    Call<UpdateProfile> uploadImage(@Part(Config.USER_NAME) RequestBody user_name, @Part(Config.USER_PHONE) RequestBody user_pass, @Part(Config.USER_ID) RequestBody user_id, @Part MultipartBody.Part file, @Part(Config.USER_GENDER) RequestBody gender, @Part(Config.USER_ADDRESS) RequestBody address);
//
//    @POST(Config.COURSEDETAIL)
//    @FormUrlEncoded
//    Call<CourseDetail> getCourseDetail(@Field(Config.USER_ID) String user_id,@Field(Config.COURSEID) String course_id);
//
//    @Multipart
//    @POST(Config.CREATE_COURSE)
//    Call<CreateCourse> createCourse(@Part(Config.CATEGOTY_ID) RequestBody category_id,
//                                    @Part(Config.SUB_CATEGORY_ID) RequestBody sub_category_id,
//                                    @Part(Config.COURSE_TITLE) RequestBody course_title,
//                                    @Part(Config.COURSE_INTRO) RequestBody course_intro,
//                                    @Part(Config.COURSE_DESCRIPTION) RequestBody course_description,
//                                    @Part(Config.COURSE_REQUIREMENT) RequestBody requirement,
//                                    @Part(Config.COURSE_WHAT_I_LEARN) RequestBody what_i_get,
//                                    @Part MultipartBody.Part file,
//                                    @Part(Config.COURSE_PRICE) RequestBody price,
//                                    @Part(Config.CREATED_BY) RequestBody created_by,
//                                    @Part(Config.COURSE_DISCOUNT) RequestBody discount,
//                                    @Part(Config.COURSE_LEVEL) RequestBody level,
//                                    @Part(Config.TIER_ID) RequestBody tier_id);
//
//    @Multipart
//    @POST(Config.CREATE_QUIZ)
//    Call<CreateQuizResp> createQuiz(@Part(Config.TITLE_NAME) RequestBody quiz_title,
//                                    @Part(Config.CATEGOTY_ID) RequestBody category_id,
//                                    @Part(Config.TITLE_ID) RequestBody sub_category_id,
//                                    @Part(Config.COURSE_ID) RequestBody course_id,
//                                    @Part(Config.SYLLABUS) RequestBody syllabus,
//                                    @Part(Config.EXAM_PRICE) RequestBody exam_price,
//                                    @Part(Config.PUBLIC) RequestBody public_,
//                                    @Part(Config.TIME_DURATION) RequestBody time_duration,
//                                    @Part(Config.RANDOM_QUESTION_NO) RequestBody random_que_no,
//                                    @Part(Config.RETAKE_ALLOW) RequestBody retake_allowed,
//                                    @Part(Config.STUDENT_CAN_SEE_RESULT) RequestBody see_result,
//                                    @Part(Config.STUDENT_CAN_SEE_ANS) RequestBody see_ans_key,
//                                    @Part(Config.STATUS) RequestBody status,
//                                    @Part(Config.PASS_MARK) RequestBody pass_mark,
//                                    @Part MultipartBody.Part file,
//                                    @Part(Config.USER_ID) RequestBody user_id);
//
//
//    @POST(Config.SEARCH_BY_COURSE)
//    @FormUrlEncoded
//    Call<SearchBy> searchCourse(@Field(Config.SEARCH_BY) String search_by);
//
//    @POST(Config.ADD_SECTIONS)
//    @FormUrlEncoded
//    Call<AddSection> addSections(@Field(Config.SECTION_NAME) String section_name, @Field(Config.SECTION_TITLE) String section_title, @Field(Config.COURSE_ID) String course_id);
//
//    @POST(Config.SECTION_LIST)
//    @FormUrlEncoded
//    Call<SectionList> sectionList(@Field(Config.COURSE_ID) String course_id);
//
//    @Multipart
//    @POST(Config.ADD_CONTENT)
//    Call<AddContent> addContent(@Part(Config.COURSE_ID) RequestBody course_id, @Part(Config.SECTION_ID) RequestBody section_id, @Part(Config.CONTENT_TYPE) RequestBody content_type, @Part(Config.VIDEO_TITLE) RequestBody video_title, @Part MultipartBody.Part file);
//
//    @POST(Config.CONTACT_US)
//    @FormUrlEncoded
//    Call<SectionList> contactus(@Field(Config.EMAIL) String email, @Field(Config.USERNAME) String username, @Field(Config.MESSAGE) String message);
//
//    @POST(Config.UPDATE_SECTION)
//    @FormUrlEncoded
//    Call<UpdateSection> updateSection(@Field(Config.SECTION_ID) String section_id, @Field(Config.SECTION_TITLE) String section_title, @Field(Config.SECTION_NAME) String section_name, @Field(Config.COURSE_ID) String course_id);
//
//    @POST(Config.DELETE_SECTION)
//    @FormUrlEncoded
//    Call<DeleteSectionResp> deleteSection(@Field(Config.SECTION_ID) String section_id);
//
//
//    @POST(Config.CREATE_CATEGORIES)
//    @FormUrlEncoded
//    Call<CreateCategory> createCategories(@Field(Config.CATEGORY_NAME) String category_name, @Field(Config.STATUS) String status, @Field(Config.CATAGORY_IMAGE) String cat_img, @Field(Config.CREATED_BY) String created_by);
//
//    @POST(Config.CREATE_SUBCATEGORIES)
//    @FormUrlEncoded
//    Call<CreateSubcategories> createSubCategories(@Field(Config.CAT_ID) String category_id, @Field(Config.SUBCATEGORY_NAME) String sub_cat_name, @Field(Config.STATUS) String status, @Field(Config.CREATED_BY) String created_by);
//
//    @GET(Config.CATEGORY_LIST)
//    Call<TutorCategoryList> getCategoryData();
//
//    @GET(Config.VIEW_SUB_CATEGORY_LIST)
//    Call<TutorSubCategoryList> getSubCategoryData();
//
//    @POST(Config.DELETE_SUBCATEGORY)
//    @FormUrlEncoded
//    Call<DeleteSubcategory> deleteSubCategory(@Field(Config.ID) String cat_id);
//
//    @POST(Config.UPDATE_SUBCATEGORY)
//    @FormUrlEncoded
//    Call<UpdateSubcategories> updateSubCategory(@Field(Config.ID) String id, @Field(Config.CAT_ID) String category_id, @Field(Config.SUBCATEGORY_NAME) String sub_cat_name, @Field(Config.STATUS) String status, @Field(Config.CREATED_BY) String created_by);
//
//    @POST(Config.DELETE_CATEGORY)
//    @FormUrlEncoded
//    Call<DeleteCategory> deleteCategory(@Field(Config.CATEGOTY_ID) String cat_id);
//
//    @POST(Config.UPDATE_CATEGORY)
//    @FormUrlEncoded
//    Call<UpdateCategoryResp> updateCategory(@Field(Config.CATEGOTY_ID) String cat_id,
//                                            @Field(Config.CATEGORY_NAME) String cat_name,
//                                            @Field(Config.STATUS) String status,
//                                            @Field(Config.CATAGORY_IMAGE) String cat_imag,
//                                            @Field(Config.CREATED_BY) String created_by);
//
//    @POST(Config.VIEW_COURSE)
//    @FormUrlEncoded
//    Call<ViewCourseList> viewCourse(@Field(Config.CREATED_BY) String created_by);
//
//    @POST(Config.VIEW_QUIZE)
//    @FormUrlEncoded
//    Call<ViewQuizResp> viewQuiz(@Field(Config.USER_ID) String user_id);
//
//    @POST(Config.LIVE_CLASS)
//    @FormUrlEncoded
//    Call<LiveClassResp> liveClass(@Field(Config.USER_ID) String user_id,
//                                  @Query(Config.OFFSET) int offset);
//
//    @POST(Config.DELETE_QUIZ)
//    @FormUrlEncoded
//    Call<DeleteSectionResp> deleteQuiz(@Field(Config.TITLE_ID) String title_id);
//
//    @POST(Config.DASHBOURD)
//    @FormUrlEncoded
//    Call<DashbourdResp> getDashbourd(@Field(Config.USER_ID) String user_id);
//
//    @GET(Config.SLIDER)
//    Call<SliderResp> getSliderData();
//
//    @POST(Config.MY_COURSE)
//    @FormUrlEncoded
//    Call<MyCourseResp> getMyCourse(@Field(Config.USER_ID) String user_id);
//
//    @POST(Config.GET_SECTION_DETAIL)
//    @FormUrlEncoded
//    Call<LectureResp> getMyLecture(@Field(Config.USER_ID) String user_id,@Field(Config.COURSE_ID) String course_id);
//
//    @POST(Config.WISHLIST)
//    @FormUrlEncoded
//    Call<WishListResp> getMyWishlist(@Field(Config.USER_ID) String user_id,
//                                     @Query(Config.OFFSET) int offset);
//
//    @POST(Config.ADD_WISHLIST)
//    @FormUrlEncoded
//    Call<AddWishListResp> getAddWishlist(@Field(Config.USER_ID) String user_id,
//                                         @Field(Config.COURSE_ID) String add_wishlist,
//                                         @Field(Config.IS_WISH) String is_wish);
//
//    //lecture_id = video_id
//    @POST(Config.ADD_MARKASCOMPLETE)
//    @FormUrlEncoded
//    Call<AddMarkAsCompleteResp> getAddMarkAsComplete(@Field(Config.USER_ID) String user_id,
//                                                     @Field(Config.LECTURE_ID) String lecture_id,
//                                                     @Field(Config.SECTION_ID) String section_id,
//                                                     @Field(Config.IS_COMPLETE) String is_complete);
//
//    @GET(Config.TIER)
//    Call<TierResp> getTierData();
//
//    @GET(Config.CELEBRITY)
//    Call<CelebrityResp> getCelebrity(@Query(Config.OFFSET) int offset);
//
//    @POST(Config.BRAINCERT_BASE_URL)
//    @FormUrlEncoded
//    Call<BraincertResp> getBraincert(@Query(Config.APIKEY) String apikey,
//                                     @Field(Config.TIMEZONE) int timezon,
//                                     @Field(Config.TITLE) String title,
//                                     @Field(Config.DATE) String date,
//                                     @Field(Config.START_TIME) String start_time,
//                                     @Field(Config.END_TIME) String end_time,
//                                     @Field(Config.CURRENCY) String currency,
//                                     @Field(Config.IS_PAID) String isPaid,
//                                     @Field(Config.IS_RECURRING) String isRecurring,
//                                     @Field(Config.REPEAT) String repeat,
//                                     @Field(Config.WEEKDAYS) String weekdays,
//                                     @Field(Config.END_CLASS_COUNT) String end_class_count,
//                                     @Field(Config.SEAT_ATTENDENCE) String seat_attendence,
//                                     @Field(Config.RECORD) String record);
//
//    @POST(Config.ADD_CLASS)
//    @FormUrlEncoded
//    Call<AddScheduleClassResp> getAddScheduleClass( @Field(Config.TIME_ZONE) String timezon,
//                                                    @Field(Config.KEYWORDS) String keywords,
//                                                    @Field(Config.PRICE) String price,
//                                                    @Field(Config.STATUS) String status,
//                                                    @Field(Config.CLASS_TITLE) String class_title,
//                                                    @Field(Config.TUTOR_ID) String tutor_id,
//                                                    @Field(Config.DATE) String date,
//                                                    @Field(Config.START_TIME) String start_time,
//                                                    @Field(Config.END_TIME) String end_time,
//                                                    @Field(Config.CLASS_ID) String class_id);
//
//    @POST(Config.BRAINCERT_LAUNCHCLASS_URL)
//    @FormUrlEncoded
//    Call<BrainCertLaunchUrlResp> getBraincertLaunchURl(@Query(Config.APIKEY) String apikey,
//                                                       @Field(Config.CLASS_ID) String class_id,
//                                                       @Field(Config.BRAINCERT_USERID) String user_id,
//                                                       @Field(Config.BRAINCERT_USERNAME) String userId,
//                                                       @Field(Config.BRAINCERT_ISTEACHER) String isTeacher,
//                                                       @Field(Config.BRAINCERT_LESSIONNAME) String lessionName,
//                                                       @Field(Config.BRAINCERT_COURSE_NAME) String corse_name);
//
//
//    @POST(Config.SUBSCRIBE)
//    @FormUrlEncoded
//    Call<SubscrbeResp> getSubscription(@Field(Config.USER_ID) String user_id,
//                                       @Field(Config.COURSE_ID) String course_id);

}
