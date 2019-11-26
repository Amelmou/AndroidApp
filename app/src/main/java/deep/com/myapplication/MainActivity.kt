package deep.com.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.*
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : AppCompatActivity() {

    var student_list: MutableList<String> = java.util.ArrayList()
    lateinit var dialog: AlertDialog
    lateinit var adapter: ArrayAdapter<String>
    var book_name: MutableList<String> = java.util.ArrayList()
    var auhtor: MutableList<String> = java.util.ArrayList()
    var id: MutableList<Long> = java.util.ArrayList()
    fun addbook(){
        book_name.add("Book Ranking On Amazon")
        auhtor.add("Brian Graves")
        book_name.add("Bhendi Bazaar")
        auhtor.add("Vish Dhamija")
        book_name.add("Front Page News")
        auhtor.add("Katie Rowney")
        book_name.add("Strange Winds")
        auhtor.add("Lastname")
        book_name.add("Bone Coulee")
        auhtor.add("Larry Warmaruk")
        book_name.add("Enchantress")
        auhtor.add("James Maxwell")
        book_name.add("Hell Of A War In Sri Lanka")
        auhtor.add("COL D.D.P Thorat(Retd)")
        book_name.add("Code Reading")
        auhtor.add("Diomidis Spinellis")
        book_name.add("Balanced Leadership")
        auhtor.add("Robert Pasick")
        try {
            book_name.forEachWithIndex { i, s ->
                dbbook.use {
                    insert(
                        DBBook.TABLE_NAME2,
                        DBBook.COLUMN_BOOK_NAME to s,
                        DBBook.COLUMN_BOOK_AUTHOR to auhtor.get(i)
                    )
                }
            }
        }catch(e:Exception){

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addbook()
        getStudents()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, student_list)

        students.adapter = adapter as ArrayAdapter<String>

        fab.setOnClickListener { addStudent_Popup() }

        students.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val intent = Intent(this@MainActivity, Student::class.java)
                intent.putExtra("id", this@MainActivity.id.get(position))
                startActivity(intent)
            }
        }
    }

    fun addStudent_Popup() {
        var view = layoutInflater.inflate(R.layout.form, null)
        var btn = view.findViewById<Button>(R.id.btn_signup)
        var fname = view.findViewById<EditText>(R.id.signup_input_name)
        var lname = view.findViewById<EditText>(R.id.signup_input_last_name)
        var email = view.findViewById<EditText>(R.id.signup_input_email)
        var dob = view.findViewById<EditText>(R.id.signup_input_dob)
        btn.setOnClickListener {
            var gender :String
            if (view.findViewById<RadioButton>(R.id.male_radio_btn).isChecked)
                gender = "Male"
            else
                gender = "Female"
            addStudent(
                view.findViewById<EditText>(R.id.signup_input_id).text.toString(),
                fname.text.toString(),
                lname.text.toString(),
                dob.text.toString(),
                email.text.toString(),
                gender
            )
            dialog.dismiss()
        }
        var builder = AlertDialog.Builder(this)
            .setView(view)
        dialog = builder.create()
        dialog.show()
    }

    fun addStudent(id :String,name: String, surname: String, dob: String, email: String, gender: String) {
        val c = Calendar.getInstance()
        try{
            dbstudent.use {
                insert(
                    DBStudent.TABLE_NAME1,
                    DBStudent.COLUMN_PERSONNES_ID to id,
                    DBStudent.COLUMN_NAME to name,
                    DBStudent.COLUMN_SURNAME to surname,
                    DBStudent.COLUMN_DOB to dob,
                    DBStudent.COLUMN_GENDER to gender,
                    DBStudent.COLUMN_EMAIL to email,
                    DBStudent.COLUMN_REG_DATE to (c.timeInMillis)
                )
                getStudents()
                adapter.notifyDataSetChanged()
            }
        }catch (e:Exception){
            toast("ID already Exist")
        }
    }

    fun getStudents() {
        student_list.removeAll { true }
        dbstudent.use {
            select(
                DBStudent.TABLE_NAME1, // Table Name
                DBStudent.COLUMN_PERSONNES_ID,
                DBStudent.COLUMN_NAME,
                DBStudent.COLUMN_SURNAME
            ).exec {
                for (row in asMapSequence()) {
                    id.add(row[DBStudent.COLUMN_PERSONNES_ID] as Long)
                    var name = row[DBStudent.COLUMN_NAME] as String + " "
                    name += row[DBStudent.COLUMN_SURNAME] as String
                    student_list.add(name)
                }
            }
        }
    }

}
