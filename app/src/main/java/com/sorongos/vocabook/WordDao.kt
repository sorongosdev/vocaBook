package com.sorongos.vocabook

import androidx.room.*

/**
 * 쿼리로 구성
 * Data Access Object라는 뜻
 * 즉, 저장된 데이터와 상호작용하겠다
 * */
@Dao
interface WordDao {
    @Query("SELECT * from word ORDER BY id DESC") //최신순
    fun getAll(): List<Word>

    @Query("SELECT * from word ORDER BY id DESC LIMIT 1")
    fun getLatestWord() : Word

    @Insert
    fun insert(word: Word)

    @Delete
    fun delete(word: Word)

    @Update
    fun update(word: Word)
}