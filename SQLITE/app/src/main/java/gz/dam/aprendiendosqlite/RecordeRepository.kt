// Archivo: RecordeRepository.kt
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import gz.dam.aprendiendosqlite.RecordeContract

class RecordeRepository(context: Context) {
    // Inicializa el asistente de base de datos
    private val dbHelper = RecordeDbHelper(context)

    // Obtenemos las constantes de nuestro contrato para facilitar el código
    private val TABLE_NAME = RecordeContract.RecordeEntry.TABLE_NAME
    private val COL_PUNTUACION = RecordeContract.RecordeEntry.COLUMN_PUNTUACION
    private val COL_FECHA = RecordeContract.RecordeEntry.COLUMN_FECHA

    // --- 1. Método para INSERTAR un nuevo récord ---
    fun insertarRecorde(puntuacion: Int, fecha: String): Long {
        // Necesitas obtener la base de datos en modo escritura
        val db = dbHelper.writableDatabase

        // 1. Prepara los datos a insertar usando ContentValues
        val values = ContentValues().apply {
            put(COL_PUNTUACION, puntuacion)
            put(COL_FECHA, fecha)
        }

        // 2. Ejecuta la inserción.
        // El método insert devuelve el ID de la nueva fila o -1 si hubo un error.
        val newRowId = db.insert(TABLE_NAME, null, values)

        // Cierra la conexión (MUY IMPORTANTE)
        db.close()

        return newRowId
    }

    // --- 2. Método para LEER el mejor récord ---
    fun obtenerMejorRecorde(): Pair<Int, String>? {
        // Necesitas obtener la base de datos en modo lectura
        val db = dbHelper.readableDatabase

        // 1. Define qué columnas quieres recuperar
        val projection = arrayOf(COL_PUNTUACION, COL_FECHA)

        // 2. Define el orden (puntuación descendente) y el límite (solo 1 fila)
        val sortOrder = "$COL_PUNTUACION DESC"
        val limit = "1"

        // 3. Ejecuta la consulta y obtén el Cursor
        val cursor = db.query(
            TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder,
            limit
        )

        var mejorRecord: Pair<Int, String>? = null

        // 4. Navega por el Cursor para extraer los datos
        with(cursor) {
            if (moveToFirst()) { // Intenta moverse al primer resultado (el mejor)
                // Obtiene los índices de las columnas por su nombre
                val puntuacionIndex = getColumnIndexOrThrow(COL_PUNTUACION)
                val fechaIndex = getColumnIndexOrThrow(COL_FECHA)

                // Extrae los valores
                val puntuacion = getInt(puntuacionIndex)
                val fecha = getString(fechaIndex)

                mejorRecord = Pair(puntuacion, fecha)
            }
        }

        // 5. Cierra el Cursor y la base de datos (MUY IMPORTANTE)
        cursor.close()
        db.close()

        return mejorRecord
    }
}