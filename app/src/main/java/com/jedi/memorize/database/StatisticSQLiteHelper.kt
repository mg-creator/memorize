package com.jedi.memorize.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class StatisticSQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        private const val DATABASE_NAME = "FeedReader.db"
        private const val DATABASE_VERSION = 1

        object StatisticColumns : BaseColumns {
            const val TABLE_NAME = "statistics"
            const val COLUMN_NAME = "name"
            const val COLUMN_TIME = "time"
            const val COLUMN_DATE = "date"
        }

        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${StatisticColumns.TABLE_NAME} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "${StatisticColumns.COLUMN_NAME} TEXT," +
                        "${StatisticColumns.COLUMN_TIME} TEXT," +
                        "${StatisticColumns.COLUMN_DATE} TEXT)"

        private const val SQL_DELETE_ENTRIES =
                "DROP TABLE ${StatisticColumns.TABLE_NAME}"
    }
}