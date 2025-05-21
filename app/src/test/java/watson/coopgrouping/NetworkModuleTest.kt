package watson.coopgrouping

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModuleTest {

    private lateinit var server: MockWebServer
    private lateinit var originalRetrofit: Retrofit

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        // Hold a reference to the original Retrofit instance
        originalRetrofit = NetworkModule.retrofit

        // Create a new Retrofit instance for testing that points to the MockWebServer
        val testRetrofit = Retrofit.Builder()
            .baseUrl(server.url("/")) // Use the MockWebServer's URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Use reflection to replace the NetworkModule.retrofit instance with the test instance
        val retrofitField = NetworkModule::class.java.getDeclaredField("retrofit\$delegate")
        retrofitField.isAccessible = true
        val lazyRetrofit = retrofitField.get(NetworkModule) as Lazy<Retrofit>
        val lazyValueField = lazyRetrofit::class.java.getDeclaredField("_value")
        lazyValueField.isAccessible = true
        lazyValueField.set(lazyRetrofit, testRetrofit)


        // Reset the apiService delegate to use the new testRetrofit instance
        val apiServiceField = NetworkModule::class.java.getDeclaredField("apiService\$delegate")
        apiServiceField.isAccessible = true
        val lazyApiService = apiServiceField.get(NetworkModule) as Lazy<ApiService>
        val lazyApiServiceValueField = lazyApiService::class.java.getDeclaredField("_value")
        lazyApiServiceValueField.isAccessible = true
        lazyApiServiceValueField.set(lazyApiService, null) // Set to null to force re-creation
    }

    @After
    fun tearDown() {
        server.shutdown()

        // Restore the original Retrofit instance using reflection
        val retrofitField = NetworkModule::class.java.getDeclaredField("retrofit\$delegate")
        retrofitField.isAccessible = true
        val lazyRetrofit = retrofitField.get(NetworkModule) as Lazy<Retrofit>
        val lazyValueField = lazyRetrofit::class.java.getDeclaredField("_value")
        lazyValueField.isAccessible = true
        lazyValueField.set(lazyRetrofit, originalRetrofit)


        // Reset the apiService delegate so it uses the original retrofit instance
        val apiServiceField = NetworkModule::class.java.getDeclaredField("apiService\$delegate")
        apiServiceField.isAccessible = true
        val lazyApiService = apiServiceField.get(NetworkModule) as Lazy<ApiService>
        val lazyApiServiceValueField = lazyApiService::class.java.getDeclaredField("_value")
        lazyApiServiceValueField.isAccessible = true
        lazyApiServiceValueField.set(lazyApiService, null) // Set to null to force re-creation
    }

    @Test
    fun testLazyInitializationAndApiCall() {
        // Check that apiService is not initialized before first access
        // We can infer this by checking the internal _value of its Lazy delegate using reflection.
        val apiServiceDelegate = NetworkModule::class.java.getDeclaredField("apiService\$delegate").let {
            it.isAccessible = true
            it.get(NetworkModule) as Lazy<*>
        }
        val apiServiceValueField = apiServiceDelegate::class.java.getDeclaredField("_value")
        apiServiceValueField.isAccessible = true
        assertNull("apiService should be null before first access", apiServiceValueField.get(apiServiceDelegate))


        // Mock the API response
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        // Make the API call
        val call = NetworkModule.apiService.getSampleData()
        val response = call.execute()

        // Verify the request was made to the MockWebServer
        val recordedRequest = server.takeRequest()
        assertEquals("/sample", recordedRequest.path)

        // Assert the call was successful
        assertTrue("Response should be successful", response.isSuccessful)
        assertEquals(200, response.code())

        // Check that apiService is initialized after first access
        assertNotNull("apiService should not be null after first access", apiServiceValueField.get(apiServiceDelegate))

        // Also check that retrofit is initialized (as apiService depends on it)
        val retrofitDelegate = NetworkModule::class.java.getDeclaredField("retrofit\$delegate").let {
            it.isAccessible = true
            it.get(NetworkModule) as Lazy<*>
        }
        val retrofitValueField = retrofitDelegate::class.java.getDeclaredField("_value")
        retrofitValueField.isAccessible = true
        assertNotNull("retrofit should not be null after apiService access", retrofitValueField.get(retrofitDelegate))
    }
}
