package io.astefanich.shinro.di.activities.main.fragments

import android.content.Context
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.di.PerFragment
import java.io.File
import javax.inject.Named
import kotlin.random.Random

@Module
class BoardModule {

    @PerFragment
    @Provides
    fun withBlackList(@Named("appCtx") ctx: Context): (Difficulty) -> File {
        val dir = ctx.filesDir
        val createIfNotExistent = { f: File -> f.createNewFile(); f }
        return { difficulty ->
            when (difficulty) {
                Difficulty.EASY -> createIfNotExistent(File(dir, "easy_blacklist.txt"))
                Difficulty.MEDIUM -> createIfNotExistent(File(dir, "medium_blacklist.txt"))
                else -> createIfNotExistent(File(dir, "hard_blacklist.txt"))
            }
        }
    }

    @PerFragment
    @Provides
    fun providesMaxBlacklistSize(): Int = 16

    @PerFragment
    @Provides
    fun providesRandomBoardId(): () -> Int = { Random.nextInt(1, 46) }
}

