import android.util.Log
import retrofit2.Response

/**
 * A generic Abstract class for handling the HTTP [Response] and return the custom [Result] as
 * [Result.Success], [Result.Error] or [Result.NoInternet]. Implement this class in the Repository
 * classes for getting corresponding responses from an API call using Retrofit.
 */
abstract class BaseDataSource {

    /**
     * Generic function to that converts Retrofit [Response] to [Result].
     */
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return  Result.success(body)
            }
            return error(
                " ${response.code()} \n${response.message()} \n${
                    response.errorBody()?.string()
                }"
            )
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }
}
