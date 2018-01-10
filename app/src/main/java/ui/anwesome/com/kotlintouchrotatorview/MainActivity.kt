package ui.anwesome.com.kotlintouchrotatorview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.touchrotatorview.TouchRotatorView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TouchRotatorView.create(this)
    }
}
