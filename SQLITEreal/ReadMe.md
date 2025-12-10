# Proyecto SQLite


## Componentes Principales

### 1. Definición del Esquema (`FeedReaderContract`)

El archivo `Contract.kt` establece la estructura de la base de datos:

*   **Base de Datos**: `FeedReader.db`
*   **Versión**: 1
*   **Tabla**: `RecordTable`
*   **Columnas**:
    *   `_ID`: Clave primaria (heredada de `BaseColumns`).
    *   `Record`: Título de la entrada.
    *   `subtitle`: Subtítulo de la entrada.

Incluye la clase `FeedReaderDbHelper` que implementa `SQLiteOpenHelper` para manejar la creación (`onCreate`) y el borrado/recreación en caso de actualización (`onUpgrade`).

### 2. Lógica (`MainActivity`)

En la actividad principal, al iniciarse la aplicación, se ejecutan secuencialmente varias operaciones de base de datos para probar su funcionamiento. 

#### Operaciones Realizadas:

1.  **Inserción (INSERT)**:
    *   Se crea un mapa de valores (`ContentValues`).
    *   Se inserta un registro con título "MyTitle".
    *   Se registra el ID de la nueva fila.

2.  **Consulta (QUERY)**:
    *   Se configuran proyecciones para leer solo columnas específicas.
    *   Se aplica una selección (`WHERE title = ?`) y argumentos (`"MyTitle"`).
    *   Se ordena el resultado de forma descendente.
    *   Se itera sobre el cursor resultante para obtener los IDs.

3.  **Borrado (DELETE)**:
    *   Se eliminan filas que coinciden con el criterio de búsqueda (Título similar a "MyTitle").

4.  **Actualización (UPDATE)**:
    *   Se intenta actualizar el título de registros existentes a "MyNewTitle" basándose en una condición (aunque en el código de ejemplo busca "MyOldTitle").

## Cómo comprobar el funcionamiento

La aplicación no muestra los datos en la interfaz gráfica. Para verificar que la base de datos funciona correctamente, debes observar los logs del sistema.

Deberías ver una salida similar a esta:
```text
2025-12-10 11:44:45.934 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  Base de datos 1 SQLiteDatabase: /data/user/0/gz.dam.sqlitereal/databases/FeedReader.db
2025-12-10 11:44:45.944 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  Nueva fila introducida 6
2025-12-10 11:44:45.944 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  Base de datos 2 SQLiteDatabase: /data/user/0/gz.dam.sqlitereal/databases/FeedReader.db
2025-12-10 11:44:45.945 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  ID: 6, Title: MyTitle
2025-12-10 11:44:45.945 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  Valores recibidos [6]
2025-12-10 11:44:45.948 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  Filas borradas 1
2025-12-10 11:44:45.948 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  Base de datos 3 SQLiteDatabase: /data/user/0/gz.dam.sqlitereal/databases/FeedReader.db
2025-12-10 11:44:45.948 16457-16457 Sqlite                  gz.dam.sqlitereal                    D  Nuevo valor para columna Record=MyNewTitle
```
