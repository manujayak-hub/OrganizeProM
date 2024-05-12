package com.aula.sqlitetest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DatabaseHandler (ctx: Context): SQLiteOpenHelper(ctx,DB_NAME,null,DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXTQQAqa);"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addTask(task: Task): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, task.nome)
        val _success = db.insert(TABLE_NAME,null,values)
        return (("$_success").toInt() != -1)
    }

    fun getTask(_id: Int): Task {
        val task = Task()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        task.id = cursor.getInt(cursor.getColumnIndex(ID))
        task.nome = cursor.getString(cursor.getColumnIndex(NAME))
        cursor.close()
        return task
    }

    fun tasks(): ArrayList<Task> {
        val taskList = ArrayList<Task>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val task = Task()
                    task.id = cursor.getInt(cursor.getColumnIndex(ID))
                    task.nome = cursor.getString(cursor.getColumnIndex(NAME))
                    taskList.add(task)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    fun updateTask(task: Task): Boolean{
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(NAME, task.nome)
        }
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(task.id.toString())).toLong()
        db.close()
        return ("$_success").toInt() != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        return ("$_success").toInt() != -1
    }

    fun deleteAllTask(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null,null).toLong()
        db.close()
        return ("$_success").toInt() != -1
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "TaskDb"
        private val TABLE_NAME = "Task"
        private val ID = "Id"
        private val NAME = "Nome"
    }
}