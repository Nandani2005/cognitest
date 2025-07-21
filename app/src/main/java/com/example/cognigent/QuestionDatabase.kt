package com.example.cognigent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*
import java.security.MessageDigest

class QuestionDatabase(context: Context) : SQLiteOpenHelper(context, "quiz.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                examType TEXT NOT NULL,
                type INTEGER DEFAULT 1,
                question TEXT NOT NULL,
                explanation TEXT,
                imageResId INTEGER DEFAULT 0,
                optionA TEXT NOT NULL,
                optionB TEXT NOT NULL,
                optionC TEXT NOT NULL,
                optionD TEXT NOT NULL,
                correctAnswer INTEGER NOT NULL,
                selectedIndex INTEGER DEFAULT -1,
                Attemp INTEGER DEFAULT 0
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

//        insertSampleQuestions(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS questions")
        db.execSQL("DROP TABLE IF EXISTS results")
        onCreate(db)
    }

        val sampleQuestions = listOf(
            QuestionModel(1, "What is the largest planet in our Solar System?", null,
                listOf("Earth", "Mars", "Jupiter", "Saturn"), 2,
                "Jupiter is the largest planet in our Solar System."),
            QuestionModel(1, "Who invented the telephone?", null,
                listOf("Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Isaac Newton"), 0,
                "Alexander Graham Bell is credited with inventing the telephone."),
            QuestionModel(
                2, "What is the capital of France?", R.drawable.acc_logo,
                listOf("Berlin", "Madrid", "Paris", "Rome"), 2,
                "Paris is the capital of France."
            ),
            QuestionModel(1, "Which continent is the Sahara Desert located on?", null,
                listOf("Asia", "South America", "Africa", "Australia"), 2,
                "The Sahara Desert is located in northern Africa.") ,
                    QuestionModel(3, "Match the languages with type", null, listOf("A-1, B-2, C-3", "A-2, B-1, C-3", "A-3, B-2, C-1", "A-1, B-3, C-2"), 0, "Java is OOP, Python is scripting, C++ is low level",
            matchA = listOf("A. Java", "B. Python", "C. C++"),
            matchB = listOf("1. Object Oriented", "2. Scripting", "3. Low Level"))
        )


    fun getAllQuestions(): List<QuestionModel> {
        val db = readableDatabase
        val questionList = mutableListOf<QuestionModel>()
        val cursor = db.rawQuery("SELECT * FROM questions", null)

        if (cursor.moveToFirst()) {
            do {
                val question = QuestionModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("type")),
                    cursor.getString(cursor.getColumnIndexOrThrow("question")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("imageResId")).takeIf { it != 0 },
                    listOf(
                        cursor.getString(cursor.getColumnIndexOrThrow("optionA")),
                        cursor.getString(cursor.getColumnIndexOrThrow("optionB")),
                        cursor.getString(cursor.getColumnIndexOrThrow("optionC")),
                        cursor.getString(cursor.getColumnIndexOrThrow("optionD"))
                    ),
                    cursor.getInt(cursor.getColumnIndexOrThrow("correctAnswer")),
                    cursor.getString(cursor.getColumnIndexOrThrow("explanation")),
                    null, null,
                    cursor.getInt(cursor.getColumnIndexOrThrow("selectedIndex")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("Attemp"))
                )
                questionList.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return questionList
    }

    fun updateAttempt(id: Int, attemp: Int, selectedIndex: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Attemp", attemp)
            put("selectedIndex", selectedIndex)
        }
        db.update("questions", values, "id = ?", arrayOf(id.toString()))
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
            put("selectedIndex", -1)
            put("Attemp", 0)
        }
        db.update("questions", values, null, null)
    }

    fun calculateScore(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT correctAnswer, selectedIndex FROM questions", null)
        var score = 0
        if (cursor.moveToFirst()) {
            do {
                val correct = cursor.getInt(0)
                val user = cursor.getInt(1)
                if (user == correct) score++
            } while (cursor.moveToNext())
        }
       db.close()
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
        db.close()
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
        db.close()
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
        db.close()
        return db.insert("questions", null, values)
    }
    fun deleteQuestion(id: Int): Int {
        val db = writableDatabase
        val s= db.delete("questions", "id = ?", arrayOf(id.toString()))
        db.close()
        return s
    }
}
