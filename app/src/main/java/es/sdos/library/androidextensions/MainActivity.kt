package es.sdos.library.androidextensions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main__btn__change_text.setOnClickListener {
            main__label__title.text = getSafeString(R.string.main__hello_world)
        }
    }
}
