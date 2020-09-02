package com.ersiver.filmflop.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ersiver.filmflop.util.Resource
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * ResultType - data type used locally.
 * RequestType - data type returned from the API
 */
abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result = MutableLiveData<Resource<ResultType>>()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    suspend fun build(): NetworkBoundResource<ResultType, RequestType> {

        withContext(Dispatchers.Main) {
            result.value =
                Resource.loading(null)
        }

        coroutineScope.launch {
            val dbResult = loadFromDb()

            if (shouldFetch(dbResult)) {
                try {
                    fetchFromNetwork(dbResult)
                } catch (e: Exception) {
                    Timber.i("Failed do load from network. ${e.message}")
                    setValue(Resource.error(e.toString(), dbResult))
                }
            } else
                setValue(Resource.success(dbResult))
        }
        return this
    }

    private suspend fun fetchFromNetwork(dbResult: ResultType) {
        setValue(Resource.loading(dbResult))
        val apiResponse = createCallAsync()
        val result = processResponse(apiResponse)
        saveCallResults(result)
        val newData = loadFromDb()
        setValue(Resource.success(newData))
    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue)
            result.postValue(newValue)
    }

    //Decide whether to fetch potentially updated data from the network
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean


    //Get the cached data from the database.
    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType


    //Create the API call
    @MainThread
    protected abstract suspend fun createCallAsync(): RequestType


    //Maps response into Database objects
    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType


    // Save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveCallResults(items: ResultType)


    //Called when the fetch fails.
    protected abstract fun onFetchFailed()
}