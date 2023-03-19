package com.example.movieapp.Data.Repositroy

class NetworkState(status: Status, message: String) {

    companion object {
        val loading : NetworkState = NetworkState(Status.RUNNING,"Running on it")
        val loaded : NetworkState = NetworkState(Status.SUCCESS,"Loaded Successfully")
        val errorLoading : NetworkState = NetworkState(Status.FAILED,"Error in loading")
    }
}