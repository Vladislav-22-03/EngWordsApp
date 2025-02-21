package ru.ievetrov.engwordsapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomePage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        var start:Button= findViewById(R.id.btnStart)
        start.setOnClickListener {
            val intent:Intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        var exit:Button=findViewById(R.id.btnExit)
        exit.setOnClickListener {
            showExitConfirmationDialog()
        }
    }
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выход")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Да") { _, _ -> finish() }
            .setNegativeButton("Отмена", null)
            .show()
    }

}