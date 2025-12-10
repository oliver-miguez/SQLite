package gz.dam.sqlitereal

import android.R.attr.subtitle
import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import gz.dam.sqlitereal.ui.theme.SQLITERealTheme

/**
 * Clase principal de la aplicación.
 * Estamos comprobando el funcionamiento de la base de datos en la MainActivity, solo con fines de probar que funcione
 * Lo normal es que se use en otras clases
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dbHelper = FeedReaderContract.FeedReaderDbHelper(application) // Referencia a la base de datos
        setContent {
            SQLITERealTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            // Insertar una nueva fila en la base de datos y obtener su ID de fila
            val db = dbHelper.writableDatabase
            Log.d("Sqlite","Base de datos 1 $db")
            val values = ContentValues().apply {
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "MyTitle")
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, subtitle)
            }

            val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
            Log.d("Sqlite","Nueva fila introducida $newRowId")


            // Leer datos de la base de datos y mostrarlos en la consola
            val db2 = dbHelper.readableDatabase
            Log.d("Sqlite","Base de datos 2 $db2")

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE)

            // Filter results WHERE "title" = 'My Title'
            val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} = ?"
            val selectionArgs = arrayOf("MyTitle")

            // How you want the results sorted in the resulting Cursor
            val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} DESC"

            // Issue the query
            val cursor = db2.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
            )
            // Iterar sobre el cursor para obtener los IDs
            val itemIds = mutableListOf<Long>()
            val itemNomes = mutableListOf<String>()

            with(cursor) {
                while (moveToNext()) {
                    val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val itemTitle = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE))

                    itemIds.add(itemId)
                    itemNomes.add(itemTitle)

                    Log.d("Sqlite","ID: $itemId, Title: $itemTitle")
                }
            }
            cursor.close() // Cerrar el cursor para evitar fugas de memoria

            Log.d("Sqlite","Valores recibidos $itemIds")


            // Borrar una fila de la base de datos
            val selection1 = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
            // Specify arguments in placeholder order.
            val selectionArgs1 = arrayOf("MyTitle")
            // Issue SQL statement.
            val deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection1, selectionArgs1)

            Log.d("Sqlite","Filas borradas $deletedRows")


            // Actualizar una fila en la base de datos y mostrar el número de filas actualizadas
            val db3 = dbHelper.writableDatabase
            Log.d("Sqlite","Base de datos 3 $db3")

            // New value for one column
            val title = "MyNewTitle"
            val values3 = ContentValues().apply {
                put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title)
            }
            Log.d("Sqlite","Nuevo valor para columna $values3")

            // Which row to update, based on the title
            val selection3 = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
            val selectionArgs3 = arrayOf("MyOldTitle")
            val count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection3,
                selectionArgs3)

            Log.d("Sqlite","Count $count")


            dbHelper.close() // Cerrar la base de datos al finalizar
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SQLITERealTheme {
        Greeting("Android")
    }
}