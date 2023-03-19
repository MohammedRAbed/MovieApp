package com.example.movieapp.SingleMovieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.Data.Repositroy.NetworkState
import com.example.movieapp.Data.VO.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(movieDetailsRepository: MovieDetailsRepository,movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * @lazy to make sure that we'll get the value when we need it, not when the class is initialized
     */

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieDetailsRepository.fetchMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}