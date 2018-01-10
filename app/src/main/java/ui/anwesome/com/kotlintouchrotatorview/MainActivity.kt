package ui.anwesome.com.kotlintouchrotatorview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ui.anwesome.com.touchrotatorview.TouchRotatorView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view = TouchRotatorView.create(this)
        view.addOnRotateStopListener {
            Toast.makeText(this,"Line has stopped",Toast.LENGTH_SHORT).show()
        }
    }
}
