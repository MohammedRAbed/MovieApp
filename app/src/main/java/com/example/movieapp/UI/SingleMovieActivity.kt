package com.example.movieapp.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.telephony.data.ApnSetting
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.movieapp.Data.API.MovieDBClient
import com.example.movieapp.Data.API.POSTER_PATH
import com.example.movieapp.Data.Repositroy.NetworkState
import com.example.movieapp.Data.VO.MovieDetails
import com.example.movieapp.R
import com.example.movieapp.SingleMovieDetail.MovieDetailsRepository
import com.example.movieapp.SingleMovieDetail.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*



@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class SingleMovieActivity : AppCompatActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId = 76600
        val apiService = MovieDBClient.getClient()

        movieDetailsRepository = MovieDetailsRepository(apiService)
        movieDetailsViewModel = getMovieDetails(movieId)

        movieDetailsViewModel.movieDetails.observe(this) {
            bindUI(it)
        }

        movieDetailsViewModel.networkState.observe(this) {

            Toast.makeText(this,
                when (it) {
                    NetworkState.errorLoading -> "ERROR LOADING"
                    NetworkState.loaded -> "LOADED SUCCESSFULLY"
                    else -> null
                },
                Toast.LENGTH_SHORT
            ).show()


            if (it==NetworkState.errorLoading || it==NetworkState.loaded) {
                progressBar.setMyVisibility(View.GONE)
                if (it==NetworkState.errorLoading)
                    showAlertDialog()
            }

        }


    }



    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("LOADING MOVIE ERROR!")
        builder.setMessage("Can't load movie details, check you're internet and try again.")
        builder.setCancelable(false)
        builder.setPositiveButton("Try Again") { _, _ ->
            startActivity(Intent(this,SingleMovieActivity::class.java))
            finish()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
            finish()
        }
        builder.create().show()
    }


    // MotionLayout Instruction, so that I can hide and show children elements
    private fun View.setMyVisibility(visibility: Int) {
        val motionLayout = parent as MotionLayout
        motionLayout.constraintSetIds.forEach {
            val constraintSet = motionLayout.getConstraintSet(it) ?: return@forEach
            constraintSet.setVisibility(this.id, visibility)
            constraintSet.applyTo(motionLayout)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindUI(it: MovieDetails) {

        /** Movie Title + Release Date + Overview */
        movie_title.text = it.title
        movie_released_value.text = it.releaseDate
        movie_overview_value.text = it.overview

        /** Movie Runtime + Rating */
        movie_duration_value.text = "${it.runtime / 60}h ${it.runtime % 60}m"
        movie_user_score_value.text = "%${(it.rating * 10).toInt()}"

        /** Movie Cover */
        Glide.with(this)
            .load(POSTER_PATH + it.posterPath)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("IMAGE LOADING","Failed in image loading")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Palette.from(resource!!.toBitmap()).generate(){platte ->
                        platte.let {
                            val intColor = it?.darkMutedSwatch?.rgb?:0
                            //DrawableCompat.set
                            movie_container.setBackgroundColor(intColor)
                        }
                    }
                    return false
                }

            })
            .into(movie_img)
    }

    private fun getMovieDetails(movieId: Int): MovieDetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieDetailsViewModel(movieDetailsRepository, movieId) as T
            }
        })[MovieDetailsViewModel::class.java]
    }



}