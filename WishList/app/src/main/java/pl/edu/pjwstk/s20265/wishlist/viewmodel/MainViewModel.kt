package pl.edu.pjwstk.s20265.wishlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class MainViewModel : ViewModel() {
    val counter: MutableLiveData<BigDecimal> = MutableLiveData()
}