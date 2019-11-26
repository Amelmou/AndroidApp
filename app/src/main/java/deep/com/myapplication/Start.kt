package deep.com.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.start.*
import org.jetbrains.anko.startActivity



class Start : AppCompatActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.start)



    btnliste.setOnClickListener {
        startActivity<MainActivity>()


    }
}

}