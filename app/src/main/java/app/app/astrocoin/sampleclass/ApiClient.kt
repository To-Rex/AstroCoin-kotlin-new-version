package app.app.astrocoin.sampleclass
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private fun getretrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl("https://api.astrocoin.uz/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val userService: UserService get() = getretrofit().create(UserService::class.java)
}