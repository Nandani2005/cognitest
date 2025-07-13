package com.example.cognigent

import QuestionModel
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "OnlineExam.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_QUESTIONS = "questn"
        const val COLUMN_ID = "id"
        const val COLUMN_EXAM_TYPE = "examType"
        const val COLUMN_QUESTION = "ques"
        const val COLUMN_OPT1 = "opt1"
        const val COLUMN_OPT2 = "opt2"
        const val COLUMN_OPT3 = "opt3"
        const val COLUMN_OPT4 = "opt4"
        const val COLUMN_CORRECT_ANS = "correctAns"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_QUESTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EXAM_TYPE TEXT,
                $COLUMN_QUESTION TEXT,
                $COLUMN_OPT1 TEXT,
                $COLUMN_OPT2 TEXT,
                $COLUMN_OPT3 TEXT,
                $COLUMN_OPT4 TEXT,
                $COLUMN_CORRECT_ANS TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        onCreate(db)
    }

    // INSERT DATA
    fun insertQuestion(
        examType: String,
        question: String,
        opt1: String,
        opt2: String,
        opt3: String,
        opt4: String,
        correctAns: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EXAM_TYPE, examType)
            put(COLUMN_QUESTION, question)
            put(COLUMN_OPT1, opt1)
            put(COLUMN_OPT2, opt2)
            put(COLUMN_OPT3, opt3)
            put(COLUMN_OPT4, opt4)
            put(COLUMN_CORRECT_ANS, correctAns)
        }
        return db.insert(TABLE_QUESTIONS, null, values)
    }

    // RETRIEVE DATA
    fun getAllQuestions(): List<QuestionModel> {
        val list = mutableListOf<QuestionModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM questn", null)

        if (cursor.moveToFirst()) {
            do {
                val opt1 = cursor.getString(cursor.getColumnIndexOrThrow("opt1"))
                val opt2 = cursor.getString(cursor.getColumnIndexOrThrow("opt2"))
                val opt3 = cursor.getString(cursor.getColumnIndexOrThrow("opt3"))
                val opt4 = cursor.getString(cursor.getColumnIndexOrThrow("opt4"))
                val correctAns = cursor.getString(cursor.getColumnIndexOrThrow("correctAns"))

                val question = QuestionModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    questionText = cursor.getString(cursor.getColumnIndexOrThrow("ques")),
                    optionA = opt1,
                    optionB = opt2,
                    optionC = opt3,
                    optionD = opt4,
                    correctIndex = getCorrectIndex(correctAns, listOf(opt1, opt2, opt3, opt4))
                )
                list.add(question)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }
    fun getCorrectIndex(correctAns: String, options: List<String>): Int {
        return options.indexOfFirst { it.trim().equals(correctAns.trim(), ignoreCase = true) }
            .takeIf { it >= 0 } ?: 0
    }


}
