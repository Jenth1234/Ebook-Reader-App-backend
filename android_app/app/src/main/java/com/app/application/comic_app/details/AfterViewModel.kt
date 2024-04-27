package com.app.application.comic_app.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.application.comic_app.database.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class AfterViewModel: ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    fun handleSaveHistory(idUser: String, idBook: String) =
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            RetrofitInstance.apiService.handleSaveHistory(idUser, idBook)
            RetrofitInstance.apiService.handleRaise1View(idBook)
            isLoading.postValue(false)
        }

}