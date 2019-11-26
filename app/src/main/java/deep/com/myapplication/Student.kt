package deep.com.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_student.*
import kotlinx.android.synthetic.main.book_form.view.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class Student : AppCompatActivity() {

    var book_name: MutableList<String> = java.util.ArrayList()
    var author_name: MutableList<String> = java.util.ArrayList()
    var issue_date: MutableList<String> = java.util.ArrayList()
    var return_date: MutableList<String> = java.util.ArrayList()
    var id: MutableList<Long> = java.util.ArrayList()
    lateinit var dialog: AlertDialog
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        init()
        adapter = Adapter(this, R.layout.row, book_name, author_name, return_date, issue_date)
        BOOKS.adapter = adapter
        getBookList()

        BOOKS.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                edit_book(position)
            }
        }

        fab_book.setOnClickListener { addBook_Popup() }
    }

    private fun edit_book(i: Int) {
        var view = layoutInflater.inflate(R.layout.book_form, null)
        var btn = view.findViewById<Button>(R.id.add_book)
        btn.text="Update Book"
        var spinner = view.findViewById<Spinner>(R.id.choose_book)
        var sadapter=Spinner_Adapter(this,R.layout.book)
        spinner.adapter=sadapter
        var bookname = view.findViewById<EditText>(R.id.book_name)
        bookname.isEnabled=false
        var author_name = view.findViewById<EditText>(R.id.book_author)
        author_name.isEnabled=false
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                var pos=position+1
                dbbook.use {
                    select(
                        DBBook.TABLE_NAME2,
                        DBBook.COLUMN_BOOK_NAME,
                        DBBook.COLUMN_BOOK_AUTHOR)
                        .whereArgs(
                            "${DBBook.COLUMN_PERSONNES_ID} = {q}",
                            "q" to "$pos"
                        ).exec {
                            for (row in asMapSequence()) {
                                bookname.setText(row[DBBook.COLUMN_BOOK_NAME] as String )
                                author_name.setText(row[DBBook.COLUMN_BOOK_AUTHOR] as String )
                            }
                        }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        var issue_date = view.findViewById<EditText>(R.id.issue_date)
        issue_date.setText(this@Student.issue_date.get(i))
        var return_date = view.findViewById<EditText>(R.id.return_date)
        return_date.setText(this@Student.return_date.get(i))
        btn.setOnClickListener {
            dbstudent.use {
                delete(DBStudent.TABLE_NAME2,
                    "${DBStudent.COLUMN_PERSONNES_ID} = {id} and ${DBStudent.COLUMN_ISSUE_ID} = {issue}",
                    "id" to intent.getLongExtra("id",0),"issue" to this@Student.id.get(i))
            }
            addBook(
                bookname.text.toString(),
                author_name.text.toString(),
                issue_date.text.toString(),
                return_date.text.toString()
            )
            dialog.dismiss()
        }
        var builder = AlertDialog.Builder(this)
            .setView(view)
        dialog = builder.create()
        dialog.show()
    }

    private fun init() {
        var id = intent.getLongExtra("id", 0)
        dbstudent.use {
            select(
                DBStudent.TABLE_NAME1,
                DBStudent.COLUMN_PERSONNES_ID,
                DBStudent.COLUMN_NAME,
                DBStudent.COLUMN_SURNAME,
                DBStudent.COLUMN_DOB,
                DBStudent.COLUMN_EMAIL,
                DBStudent.COLUMN_GENDER
            )
                .whereArgs(
                    "${DBStudent.COLUMN_PERSONNES_ID} = {q}",
                    "q" to "$id"
                ).exec {
                    for (row in asMapSequence()) {
                        Name.text = row[DBStudent.COLUMN_NAME] as String + " " + row[DBStudent.COLUMN_SURNAME] as String
                        EMAIL.text = row[DBStudent.COLUMN_EMAIL] as String
                        DOB.text = row[DBStudent.COLUMN_DOB] as String
                        GENDER.text = row[DBStudent.COLUMN_GENDER] as String
                    }
                }
        }
    }

    fun addBook_Popup() {
        var view = layoutInflater.inflate(R.layout.book_form, null)
        var btn = view.findViewById<Button>(R.id.add_book)
        var spinner = view.findViewById<Spinner>(R.id.choose_book)
        var sadapter=Spinner_Adapter(this,R.layout.book)
        spinner.adapter=sadapter
        var bookname = view.findViewById<EditText>(R.id.book_name)
        bookname.isEnabled=false
        var author_name = view.findViewById<EditText>(R.id.book_author)
        author_name.isEnabled=false
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                var pos=position+1
                dbbook.use {
                    select(
                        DBBook.TABLE_NAME2,
                        DBBook.COLUMN_BOOK_NAME,
                        DBBook.COLUMN_BOOK_AUTHOR)
                        .whereArgs(
                            "${DBBook.COLUMN_PERSONNES_ID} = {q}",
                            "q" to "$pos"
                        ).exec {
                            for (row in asMapSequence()) {
                                bookname.setText(row[DBBook.COLUMN_BOOK_NAME] as String )
                                author_name.setText(row[DBBook.COLUMN_BOOK_AUTHOR] as String )
                            }
                        }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        var issue_date = view.findViewById<EditText>(R.id.issue_date)
        val date = Calendar.getInstance().time.toString()
        var d = date.substring(4, 19) + date.substring(23)
        issue_date.setText(d)
        var return_date = view.findViewById<EditText>(R.id.return_date)
        var c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 7)
        var da = c.time.toString()
        da = da.substring(4, 19) + da.substring(23)
        return_date.setText(da)
        btn.text = "Issue Book"
        btn.setOnClickListener {
            addBook(
                bookname.text.toString(),
                author_name.text.toString(),
                issue_date.text.toString(),
                return_date.text.toString()
            )
            dialog.dismiss()
        }
        var builder = AlertDialog.Builder(this)
            .setView(view)
        dialog = builder.create()
        dialog.show()
    }

    fun addBook(name: String, author: String, issue: String, return_date: String) {
        dbstudent.use {
            insert(
                DBStudent.TABLE_NAME2,
                DBStudent.COLUMN_PERSONNES_ID to intent.getLongExtra("id",0),
                DBStudent.COLUMN_BOOK_NAME to name,
                DBStudent.COLUMN_BOOK_AUTHOR to author,
                DBStudent.COLUMN_ISSUE to issue,
                DBStudent.COLUMN_RETURN to return_date)
        }
        getBookList()
    }

    fun getBookList() {
        book_name.removeAll { true }
        author_name.removeAll{true}
        issue_date.removeAll{true}
        return_date.removeAll{true}
        var id = intent.getLongExtra("id",0)
        dbstudent.use {
            select(
                DBStudent.TABLE_NAME2,
                DBStudent.COLUMN_ISSUE_ID,
                DBStudent.COLUMN_BOOK_NAME,
                DBStudent.COLUMN_BOOK_AUTHOR,
                DBStudent.COLUMN_ISSUE,
                DBStudent.COLUMN_RETURN)
                .whereArgs(
                    "${DBStudent.COLUMN_PERSONNES_ID} = {q}",
                    "q" to "$id")
                .exec {
                    for (row in asMapSequence()) {
                        this@Student.id.add(row[DBStudent.COLUMN_ISSUE_ID] as Long)
                        book_name.add(row[DBStudent.COLUMN_BOOK_NAME] as String )
                        author_name.add(row[DBStudent.COLUMN_BOOK_AUTHOR] as String)
                        issue_date.add(row[DBStudent.COLUMN_ISSUE] as String)
                        return_date.add(row[DBStudent.COLUMN_RETURN] as String)
                    }
                }
        }
        adapter.notifyDataSetChanged()
    }

    class Adapter(
        internal var context: Context,
        resource: Int,
        name: MutableList<String>,
        author: MutableList<String>,
        return_date: MutableList<String>,
        issue: MutableList<String>
    ) : ArrayAdapter<String>(context, resource, R.id.textView, name) {

        var name: MutableList<String>
        var author: MutableList<String>
        var issue: MutableList<String>
        var return_date: MutableList<String>

        init {
            this.name = name
            this.author = author
            this.issue = issue
            this.return_date = return_date
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.row,
                null
            )
            val book_name = view.findViewById<TextView>(R.id.Name)
            book_name.text = name.get(position)
            val author_name = view.findViewById<TextView>(R.id.author)
            author_name.text = author.get(position)
            val issue_date = view.findViewById<TextView>(R.id.issue)
            issue_date.text = issue.get(position).replace("_", " ")
            val return_d = view.findViewById<TextView>(R.id.retrun_date)
            return_d.text = return_date.get(position)
            return view
        }
    }

    class Spinner_Adapter(
        internal var context: Context,
        resource: Int
    ) : ArrayAdapter<String>(context, resource, R.id.textView) {

        var str="book"

        override fun getCount(): Int {
            return 8
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.book, null)
            var img=view.findViewById<ImageView>(R.id.imageView)
            var drawable = context.resources.getDrawable(context.resources.getIdentifier(str+(position+1), "drawable", context.packageName))
            img.setImageDrawable(drawable)
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.book, null)
            try {
                var img = view.findViewById<ImageView>(R.id.imageView)
                var drawable = context.resources.getDrawable(context.resources.getIdentifier(str+(position+1), "drawable", context.packageName))
                img.setImageDrawable(drawable)
            }catch (e:Exception){

            }
            return view
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete ->  {
                dbstudent.use {
                    delete(DBStudent.TABLE_NAME1,
                        "${DBStudent.COLUMN_PERSONNES_ID} = {id}",
                        "id" to intent.getLongExtra("id",0))
                }
                dbstudent.use {
                    delete(DBStudent.TABLE_NAME2,
                        "${DBStudent.COLUMN_PERSONNES_ID} = {id}",
                        "id" to intent.getLongExtra("id",0))
                    val intent= Intent(this@Student,MainActivity::class.java)
                    toast("Deleted")
                    startActivity(intent)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
