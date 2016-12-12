package oaklabs.supportal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static final String BASE_URL = "hurst.pythonanywhere.com/supportal/rest-auth/";
    private static Retrofit retro = null;

    public static Retrofit getClient(){
        if (retro == null){
            retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retro;
    }
}
