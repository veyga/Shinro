package io.astefanich.shinro.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton



//abstracting so we dont have to manually add viewmodel types to factory
@Singleton
class ViewModelFactory @Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators.get(modelClass)
        if (creator == null) {
            for (entry in creators.entries) {
                if (modelClass.isAssignableFrom(entry.key)) {
                    creator = entry.value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}


//from the article
//@Singleton
//class ViewModelFactory(): ViewModelProvider.Factory{
//    @Inject
//    lateinit var titleProvider: Provider<TitleViewModel>
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T{
//        if(modelClass.isAssignableFrom(TitleViewModel::class.java)){
//            return titleProvider.get() as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

// MINE
//class ViewModelFactory(private val boardId: Int) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
//            return GameViewModel(boardId) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}