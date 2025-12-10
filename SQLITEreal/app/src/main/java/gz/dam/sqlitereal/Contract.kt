package gz.dam.sqlitereal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object FeedReaderContract {

    // Contenido de la tabla de la base de datos
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "RecordTable"
        const val COLUMN_NAME_TITLE = "Record"
        const val COLUMN_NAME_SUBTITLE = "subtitle"
    }

    // "Script" que se lanza para crear las tablas, centrandonos principalmente en los valores que creamos antes
    // Ademas utiliza _Id que proviene de BaseColumns
    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                "${FeedEntry.COLUMN_NAME_SUBTITLE} TEXT)"

    // Elimina la tabla
    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"

    /**
     * Clase interna que crea la base de datos y la estructura de la tabla
     * @param context Es necesario para poder crear la base de datos
     * @constructor Crea la base de datos
     *
     */
    class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        // Crea la base de datos
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        // Actualiza la base de datos
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        // Recoge una versión antigua con datos anteriores de bases de datos anteriores
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        // Constantes de la base de datos
        // Companion sirve como un objeto statico dentro de una clase object
        companion object {
            const val DATABASE_VERSION = 1 // Version de la base de datos
            const val DATABASE_NAME = "FeedReader.db" // Nombre de la base de datos
        }
    }
}

