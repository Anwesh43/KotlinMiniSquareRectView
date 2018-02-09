package ui.anwesome.com.kotlinminisquarerectview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.minisquarerectview.MiniSquareRectView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MiniSquareRectView.create(this)
    }
}
