package com.example.movieapp.Data.Repositroy


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.Data.API.MovieDBInterface
import com.example.movieapp.Data.VO.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(
    private val apiService:MovieDBInterface, private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()

    //Getters to get LiveData of Both (NetworkState & MovieDetails)
    val networkState : LiveData<NetworkState> get() = _networkState
    val downloadedMovieDetailsResponse : LiveData<MovieDetails> get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId:Int) {
        _networkState.postValue(NetworkState.loading)
        try {
            compositeDisposable.add(
                //return Single Movie depends on movieId
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.loaded)
                        },
                        {
                            _networkState.postValue(NetworkState.errorLoading)
                            Log.e("MovieDetailsDataSource","ERROR LOADING BECAUSE OF --------> ${it.message!!}")
                        }
                    )
            )
        } catch (e:Exception) {
            _networkState.postValue(NetworkState.errorLoading)
            Log.e("EXCEPTION CATCHING","CATCHING THE EXCEPTION ----> ${e.message!!}")
        }
    }
}