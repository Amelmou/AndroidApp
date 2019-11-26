package deep.com.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*



val Context.dbstudent: DBStudent
    get() = DBStudent.getInstance(applicationContext)

class DBStudent(ctx: Context) : ManagedSQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "Students.db"
        const val DATABASE_VERSION = 11

        const val TABLE_NAME1 = "Students"
        const val COLUMN_PERSONNES_ID = "id"
        const val COLUMN_ISSUE_ID = "issue_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SURNAME = "surname"
        const val COLUMN_DOB = "dob"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_REG_DATE = "date"

        const val TABLE_NAME2 = "Books"
        const val COLUMN_BOOK_NAME = "NAME"
        const val COLUMN_BOOK_AUTHOR = "AUTHOR"
        const val COLUMN_ISSUE = "ISSUE"
        const val COLUMN_RETURN = "RETURN"

        private var instance: DBStudent? = null

        @Synchronized
        fun getInstance(ctx: Context): DBStudent {
            if (instance == null) {
                instance = DBStudent(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            TABLE_NAME1, true,
                COLUMN_PERSONNES_ID to INTEGER + PRIMARY_KEY,
                COLUMN_NAME to TEXT + NOT_NULL,
                COLUMN_SURNAME to TEXT + NOT_NULL,
                COLUMN_DOB to TEXT + NOT_NULL,
                COLUMN_GENDER to TEXT + NOT_NULL,
                COLUMN_EMAIL to TEXT + NOT_NULL,
                COLUMN_REG_DATE to INTEGER + NOT_NULL)
        db.createTable(
            TABLE_NAME2, true,
            COLUMN_ISSUE_ID to INTEGER+ PRIMARY_KEY,
            COLUMN_PERSONNES_ID to INTEGER,
            COLUMN_BOOK_NAME to TEXT + NOT_NULL,
            COLUMN_BOOK_AUTHOR to TEXT + NOT_NULL,
            COLUMN_ISSUE to TEXT + NOT_NULL,
            COLUMN_RETURN to TEXT + NOT_NULL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(oldVersion < 11) {

            db.execSQL("Delete "+ TABLE_NAME1+" if exist")
            db.execSQL("Delete "+ TABLE_NAME2+" if exist")
            db.createTable(
                TABLE_NAME1, true,
                COLUMN_PERSONNES_ID to INTEGER + PRIMARY_KEY,
                COLUMN_NAME to TEXT + NOT_NULL,
                COLUMN_SURNAME to TEXT + NOT_NULL,
                COLUMN_DOB to TEXT + NOT_NULL,
                COLUMN_REG_DATE to INTEGER + NOT_NULL)
            db.createTable(
                TABLE_NAME2, true,
                COLUMN_ISSUE_ID to INTEGER+ PRIMARY_KEY,
                COLUMN_PERSONNES_ID to INTEGER,
                COLUMN_BOOK_NAME to TEXT + NOT_NULL,
                COLUMN_BOOK_AUTHOR to TEXT + NOT_NULL,
                COLUMN_ISSUE to TEXT + NOT_NULL,
                COLUMN_RETURN to TEXT + NOT_NULL)
        }
    }
}
