package io.astefanich.shinro.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val vmProvider: Provider<GameViewModel>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return vmProvider.get() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
//@Singleton
//class ViewModelFactory @Inject constructor(val viewModelMap: Map<Class<out ViewModel>, ViewModel>) :
//    ViewModelProvider.Factory {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return viewModelMap[modelClass] as T
//    }
//}

//@Singleton
//class ViewModelFactory: ViewModelProvider.Factory {
//
//    @Inject
//    lateinit var gameProvider: Provider<GameViewModel>
//
//    override fun <T: ViewModel> create(modelClass: Class<T>): T {
//       if(modelClass.isAssignableFrom(GameViewModel::class.java)) {
//           return gameProvider.get() as T
//       }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}