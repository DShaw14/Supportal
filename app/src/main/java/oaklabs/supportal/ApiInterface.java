package oaklabs.supportal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by David Shaw on 12/12/2016.
 */

public interface ApiInterface {
    @POST("login")
    Call<User> loginUser(@Body User user);
}
