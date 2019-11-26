package deep.com.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*



val Context.dbbook: DBBook
    get() = DBBook.getInstance(applicationContext)

class DBBook(ctx: Context) : ManagedSQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "Books.db"
        const val DATABASE_VERSION = 11
        const val TABLE_NAME2 = "Books"
        const val COLUMN_PERSONNES_ID="ID"
        const val COLUMN_BOOK_NAME = "NAME"
        const val COLUMN_BOOK_AUTHOR = "AUTHOR"

        private var instance: DBBook? = null

        @Synchronized
        fun getInstance(ctx: Context): DBBook {
            if (instance == null) {
                instance = DBBook(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(
            TABLE_NAME2, true,
                COLUMN_PERSONNES_ID to INTEGER + PRIMARY_KEY,
            COLUMN_BOOK_NAME to TEXT + NOT_NULL + UNIQUE,
            COLUMN_BOOK_AUTHOR to TEXT + NOT_NULL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(oldVersion < 11) {

            db.createTable(
                TABLE_NAME2, true,
                    COLUMN_PERSONNES_ID to INTEGER,
                COLUMN_BOOK_NAME to TEXT + NOT_NULL,
                COLUMN_BOOK_AUTHOR to TEXT + NOT_NULL)
        }
    }
}
