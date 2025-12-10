// Archivo: RecordeDbHelper.kt
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import gz.dam.aprendiendosqlite.RecordeContract

class RecordeDbHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        RecordeContract.DATABASE_NAME,
        null,
        RecordeContract.DATABASE_VERSION
    ) {

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${RecordeContract.RecordeEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," + // Heredado de BaseColumns
                "${RecordeContract.RecordeEntry.COLUMN_PUNTUACION} INTEGER," +
                "${RecordeContract.RecordeEntry.COLUMN_FECHA} TEXT)"

    private val SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS ${RecordeContract.RecordeEntry.TABLE_NAME}"

    // Se llama cuando la base de datos se crea por primera vez (la primera vez que se llama a getWritableDatabase/getReadableDatabase).
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    // Se llama si la versión de la BD (DATABASE_VERSION) cambia.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // La política más simple es descartar los datos existentes y empezar de nuevo.
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    // Opcional: Para el caso en que la nueva versión es menor que la anterior.
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}