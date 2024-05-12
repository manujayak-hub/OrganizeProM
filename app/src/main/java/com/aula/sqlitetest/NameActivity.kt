package com.aula.sqlitetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_name.*

class NameActivity : AppCompatActivity() {

    // Database
    val databaseHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        val edit = intent.getBooleanExtra("edit", false)
        val position = intent.getIntExtra("position", 0)
        if(edit){
            val task = databaseHandler.getTask(position)
            etNome.setText(task.nome)
            btnInsertNome.setText("Edit")
        }
        btnInsertNome.setOnClickListener {
            if(etNome.text.toString() == ""){
                Toast.makeText(this,"Name is empty.",Toast.LENGTH_SHORT).show()
            }
            else {
                if(edit){
                    val task = Task(position, etNome.text.toString())
                    databaseHandler.updateTask(task)
                    finish()
                }
                else {
                    val task = Task(0, etNome.text.toString())
                    databaseHandler.addTask(task)
                    finish()
                }
            }
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }
}