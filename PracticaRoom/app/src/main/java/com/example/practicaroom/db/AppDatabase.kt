package com.example.practicaroom.db

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.practicaroom.db.dao.ContactDao
import com.example.practicaroom.db.entities.Contact

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        fun getDatabase(context: Context): AppDatabase {
            val db =
                Room.databaseBuilder<AppDatabase>(context, "app-db")
                    .build()
            return db
        }
    }
}