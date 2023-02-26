package com.sorongos.vocabook

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**데이터베이스라는 것을 알려주는 어노테이션
 * 싱글톤
 * */
@Database(entities = [Word::class], version = 1) // 여러개 가능
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase?{
            if(INSTANCE == null){
                //하나만 만들어짐
                synchronized(AppDatabase::class.java){
                    //초기화 해줘야함, 데이터베이스 빌더를 통해서
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app-database.db" //database name
                    ).build() //만들어짐
                }

            }
            return INSTANCE
        }
    }
}