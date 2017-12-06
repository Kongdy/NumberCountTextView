package com.kongdy.numbercounttextview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(p0: View?) {

        nctv_test.setAnimationText((2000f*Math.random()).toFloat())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nctv_test.setAnimationText(500f)

        btn_change.setOnClickListener(this)
    }
}
