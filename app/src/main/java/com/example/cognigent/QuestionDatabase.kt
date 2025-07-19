package com.example.cognigent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class QuestionDatabase(context: Context) : SQLiteOpenHelper(context, "quiz.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                examType TEXT NOT NULL,
                question TEXT NOT NULL,
                optionA TEXT NOT NULL,
                optionB TEXT NOT NULL,
                optionC TEXT NOT NULL,
                optionD TEXT NOT NULL,
                correctAnswer INTEGER NOT NULL,
                userAnswer INTEGER DEFAULT -1
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE results (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                examType TEXT NOT NULL,
                score INTEGER NOT NULL,
                timestamp TEXT NOT NULL
            )
        """.trimIndent())

        insertSampleQuestions(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS questions")
        db.execSQL("DROP TABLE IF EXISTS results")
        onCreate(db)
    }

    private fun insertSampleQuestions(db: SQLiteDatabase) {
        val sampleQuestions = listOf(
            QuestionModel(1, "BCA", "What is the capital of France?", "London", "Berlin", "Paris", "Rome", 2),
            QuestionModel(2, "BCA", "Which number is even?", "3", "7", "10", "5", 2),
            QuestionModel(3, "MBA", "Which one is a fruit?", "Potato", "Carrot", "Banana", "Onion", 2),
            QuestionModel(4, "MBA", "What is the square root of 16?", "2", "4", "8", "6", 1),
        )

        for (q in sampleQuestions) {
            val values = ContentValues().apply {
                put("examType", q.examType)
                put("question", q.questionText)
                put("optionA", q.optionA)
                put("optionB", q.optionB)
                put("optionC", q.optionC)
                put("optionD", q.optionD)
                put("correctAnswer", q.correctIndex)
                put("userAnswer", -1)
            }
            db.insert("questions", null, values)
        }
    }

    fun getQuestionsByExamType(examType: String): List<QuestionModel> {
        val questionList = mutableListOf<QuestionModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM questions WHERE examType = ?", arrayOf(examType))

        if (cursor.moveToFirst()) {
            do {
                questionList.add(
                    QuestionModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        examType = cursor.getString(cursor.getColumnIndexOrThrow("examType")),
                        questionText = cursor.getString(cursor.getColumnIndexOrThrow("question")),
                        optionA = cursor.getString(cursor.getColumnIndexOrThrow("optionA")),
                        optionB = cursor.getString(cursor.getColumnIndexOrThrow("optionB")),
                        optionC = cursor.getString(cursor.getColumnIndexOrThrow("optionC")),
                        optionD = cursor.getString(cursor.getColumnIndexOrThrow("optionD")),
                        correctIndex = cursor.getInt(cursor.getColumnIndexOrThrow("correctAnswer")),
                        Attemp = cursor.getInt(cursor.getColumnIndexOrThrow("userAnswer"))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return questionList
    }

    fun getAllQuestions(): List<QuestionModel> {
        val list = mutableListOf<QuestionModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM questions", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    QuestionModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        examType = cursor.getString(cursor.getColumnIndexOrThrow("examType")),
                        questionText = cursor.getString(cursor.getColumnIndexOrThrow("question")),
                        optionA = cursor.getString(cursor.getColumnIndexOrThrow("optionA")),
                        optionB = cursor.getString(cursor.getColumnIndexOrThrow("optionB")),
                        optionC = cursor.getString(cursor.getColumnIndexOrThrow("optionC")),
                        optionD = cursor.getString(cursor.getColumnIndexOrThrow("optionD")),
                        correctIndex = cursor.getInt(cursor.getColumnIndexOrThrow("correctAnswer")),
                        Attemp = cursor.getInt(cursor.getColumnIndexOrThrow("userAnswer"))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }

    fun saveUserAnswer(questionId: Int, selectedIndex: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userAnswer", selectedIndex)
        }
        db.update("questions", values, "id = ?", arrayOf(questionId.toString()))
    }

    fun resetUserAnswers() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userAnswer", -1)
        }
        db.update("questions", values, null, null)
    }

    fun calculateScore(examType: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT correctAnswer, userAnswer FROM questions WHERE examType = ?",
            arrayOf(examType)
        )

        var score = 0
        if (cursor.moveToFirst()) {
            do {
                val correct = cursor.getInt(0)
                val user = cursor.getInt(1)
                if (user == correct) score++
            } while (cursor.moveToNext())
        }

        cursor.close()
        return score
    }

    fun saveResult(score: Int, examType: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("examType", examType)
            put("score", score)
            put("timestamp", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
        }
        db.insert("results", null, values)
    }

    fun getAllResults(): List<Triple<String, Int, String>> {
        val results = mutableListOf<Triple<String, Int, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT examType, score, timestamp FROM results ORDER BY id DESC", null)

        if (cursor.moveToFirst()) {
            do {
                results.add(
                    Triple(
                        cursor.getString(cursor.getColumnIndexOrThrow("examType")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("score")),
                        cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return results
    }
    fun insertQuestion(
        examType: String,
        questionText: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctIndex: Int
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("examType", examType)
            put("question", questionText)
            put("optionA", optionA)
            put("optionB", optionB)
            put("optionC", optionC)
            put("optionD", optionD)
            put("correctAnswer", correctIndex)
            put("userAnswer", -1)
        }
        return db.insert("questions", null, values)
    }
    fun deleteQuestion(id: Int): Int {
        val db = writableDatabase
        return db.delete("questions", "id = ?", arrayOf(id.toString()))
    }


}
