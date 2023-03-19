package com.example.movieapp.Data.API

import com.example.movieapp.Data.VO.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDBInterface {

    /**
     * Get Movie Details using movie_id
     * @Path is for telling that id parameter is the same movie_id
     * Single is an Observable JavaRx Component that emmit data to Observers, it send single types of data.
     */
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}