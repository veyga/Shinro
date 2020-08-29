package io.astefanich.shinro.repository

import arrow.core.extensions.list.foldable.foldLeft
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.model.Board
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


class BoardRepository
@Inject
constructor(
    val boardDao: BoardDao,
    val maxBlacklistSize: Int,
    val blacklist: @JvmSuppressWildcards (Difficulty) -> File,
    val randomId: @JvmSuppressWildcards () -> Int
) {

    suspend fun getRandomBoardByDifficulty(difficulty: Difficulty): Board {
        return withContext(Dispatchers.IO) {
            try {
                val queue = ArrayDeque<Int>(maxBlacklistSize) //keep track of ordering
                val reserved = mutableSetOf<Int>()
                val file = blacklist(difficulty)
                file.forEachLine {
                    val current = Integer.parseInt(it)
                    queue.addLast(current)
                    reserved.add(current)
                }
                var targetId = randomId()
                while (reserved.contains(targetId))
                    targetId = randomId()

                if (reserved.size >= maxBlacklistSize)
                    queue.removeFirst()
                queue.addLast(targetId)
                file.writeText(queue.foldLeft("", { s, i -> s + "${i}\n" }))
                boardDao.getBoardByNumAndDifficulty(targetId, difficulty)
            } catch (e: Exception) {
                boardDao.getBoardByNumAndDifficulty(randomId(), difficulty)
            }
        }
    }
}


