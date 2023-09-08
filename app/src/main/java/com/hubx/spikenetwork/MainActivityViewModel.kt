package com.hubx.spikenetwork

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
//    var count = 0
    var count = MutableLiveData<Int>()
    init {
        count.value = -1
    }

    fun updateCount() {
//        ++count
        count.value = (count.value)?.plus(1)
    }

    fun resetsCount() {
        count.value = 0
    }
}