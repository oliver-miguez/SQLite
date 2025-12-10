package gz.dam.aprendiendosqlite

import android.provider.BaseColumns

// Archivo: RecordeContract.kt

object RecordeContract {
    // Definiciones del esquema a nivel global de la BD
    const val DATABASE_NAME = "RecordsBD.db"
    const val DATABASE_VERSION = 1

    // Clase interna para la tabla "Recorde"
    object RecordeEntry : BaseColumns {
        const val TABLE_NAME = "recordes"
        const val COLUMN_PUNTUACION = "puntuacion"
        const val COLUMN_FECHA = "fecha" // Usaremos TEXT para almacenar la fecha
    }
}