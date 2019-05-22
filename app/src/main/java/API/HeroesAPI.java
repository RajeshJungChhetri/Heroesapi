package API;

import java.util.List;

import Model.Heroes;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HeroesAPI {
    //using field
    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addHero(@Field("name") String name, @Field("desc")String desc);

    @GET("heroes")
    Call<List<Heroes>> getHeroes();

}
