package com.flypika.pack.presentation.ext

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

inline fun <reified A : AppCompatActivity> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, A::class.java).apply(block)
    startActivity(intent)
}
