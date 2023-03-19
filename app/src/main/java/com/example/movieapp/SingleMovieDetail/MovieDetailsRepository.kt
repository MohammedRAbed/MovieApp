package com.example.movieapp.SingleMovieDetail

import androidx.lifecycle.LiveData
import com.example.movieapp.Data.API.MovieDBInterface
import com.example.movieapp.Data.Repositroy.MovieDetailsNetworkDataSource
import com.example.movieapp.Data.Repositroy.NetworkState
import com.example.movieapp.Data.VO.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService:MovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource : MovieDetailsNetworkDataSource

    //Get LiveData of Movie Details
    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int) : LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    //Get LiveData of Network State
    fun fetchMovieDetailsNetworkState() : LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}