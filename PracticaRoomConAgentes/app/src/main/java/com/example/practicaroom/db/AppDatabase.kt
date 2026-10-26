package com.example.practicaroom.db

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.practicaroom.db.dao.ContactDao
import com.example.practicaroom.db.entities.Contact
import com.example.practicaroom.db.entities.ContactPhone
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

@Database(entities = [Contact::class, ContactPhone::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        fun getDatabase(context: Context): AppDatabase {
            val db =
                Room.databaseBuilder<AppDatabase>(context, "app-db")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            return db
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override suspend fun migrate(connection: SQLiteConnection) {
                connection.execSQL("CREATE TABLE IF NOT EXISTS contact_phone (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, contact_id INTEGER NOT NULL, number TEXT NOT NULL, FOREIGN KEY(contact_id) REFERENCES Contact(id) ON UPDATE NO ACTION ON DELETE CASCADE)")
                connection.execSQL("CREATE INDEX IF NOT EXISTS index_contact_phone_contact_id ON contact_phone(contact_id)")
                connection.execSQL("INSERT INTO contact_phone (contact_id, number) SELECT id, phone_number FROM contact WHERE phone_number != ''")
                connection.execSQL("CREATE TABLE contact_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, email TEXT NOT NULL, address TEXT NOT NULL)")
                connection.execSQL("INSERT INTO contact_new (id, name, email, address) SELECT id, name, email, address FROM contact")
                connection.execSQL("DROP TABLE contact")
                connection.execSQL("ALTER TABLE contact_new RENAME TO contact")
            }
        }
    }
}
