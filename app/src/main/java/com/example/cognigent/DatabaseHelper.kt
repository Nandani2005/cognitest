package com.example.cognigent// Change to your package name

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : android.database.sqlite.SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "QuizDatabase.db"
        private const val DATABASE_VERSION = 1

        // Questions Table
        private const val TABLE_QUESTIONS = "questions"
        private const val COLUMN_QUESTION_ID = "question_id"
        private const val COLUMN_QUESTION_TEXT = "question_text"
        private const val COLUMN_CORRECT_ANSWER = "correct_answer"

        // Attempts Table
        private const val TABLE_ATTEMPTS = "attempts"
        private const val COLUMN_ATTEMPT_ID = "attempt_id"
        private const val COLUMN_ATTEMPT_QUESTION_ID = "question_id" // Foreign key to questions table
        private const val COLUMN_USER_ANSWER = "user_answer"
        private const val COLUMN_IS_CORRECT = "is_correct" // 0 for false, 1 for true
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_QUESTIONS_TABLE = "CREATE TABLE $TABLE_QUESTIONS (" +
                "$COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_QUESTION_TEXT TEXT," +
                "$COLUMN_CORRECT_ANSWER TEXT)"
        db?.execSQL(CREATE_QUESTIONS_TABLE)

        val CREATE_ATTEMPTS_TABLE = "CREATE TABLE $TABLE_ATTEMPTS (" +
                "$COLUMN_ATTEMPT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_ATTEMPT_QUESTION_ID INTEGER," +
                "$COLUMN_USER_ANSWER TEXT," +
                "$COLUMN_IS_CORRECT INTEGER," +
                "FOREIGN KEY($COLUMN_ATTEMPT_QUESTION_ID) REFERENCES $TABLE_QUESTIONS($COLUMN_QUESTION_ID) ON DELETE CASCADE)"
        db?.execSQL(CREATE_ATTEMPTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ATTEMPTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        onCreate(db)
    }

    // --- Question Operations ---
    fun addQuestion(question: Question): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT, question.questionText)
            put(COLUMN_CORRECT_ANSWER, question.correctAnswer)
        }
        val success = db.insert(TABLE_QUESTIONS, null, contentValues)
        db.close()
        return success
    }

    fun getAllQuestions(): List<Question> {
        val questionList = mutableListOf<Question>()
        val selectQuery = "SELECT * FROM $TABLE_QUESTIONS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUESTION_ID))
                    val text = it.getString(it.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT))
                    val correctAnswer = it.getString(it.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER))
//                    questionList.add(
//                        _root_ide_package_.com.example.cognigent.Question(
//                            toString(),
//                            text,
//                            cor
//                        )
//                    )
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return questionList
    }

    // --- Attempt Operations ---
    fun addAttempt(attempt: Attempt): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_ATTEMPT_QUESTION_ID, attempt.questionId)
            put(COLUMN_USER_ANSWER, attempt.userAnswer)
            put(COLUMN_IS_CORRECT, if (attempt.isCorrect) 1 else 0)
        }
        val success = db.insert(TABLE_ATTEMPTS, null, contentValues)
        db.close()
        return success
    }

    fun getAllAttempts(): List<Attempt> {
        val attemptList = mutableListOf<Attempt>()
        val selectQuery = "SELECT * FROM $TABLE_ATTEMPTS ORDER BY $COLUMN_ATTEMPT_QUESTION_ID ASC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ATTEMPT_ID))
                    val questionId = it.getInt(it.getColumnIndexOrThrow(COLUMN_ATTEMPT_QUESTION_ID))
                    val userAnswer = it.getString(it.getColumnIndexOrThrow(COLUMN_USER_ANSWER))
                    val isCorrect = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_CORRECT)) == 1
                    attemptList.add(Attempt(id, questionId, userAnswer, isCorrect))
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return attemptList
    }

    fun getCorrectAttemptsCount(): Int {
        val db = this.readableDatabase
        val countQuery = "SELECT COUNT(*) FROM $TABLE_ATTEMPTS WHERE $COLUMN_IS_CORRECT = 1"
        val cursor = db.rawQuery(countQuery, null)
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return count
    }

    fun getTotalAttemptsCount(): Int {
        val db = this.readableDatabase
        val countQuery = "SELECT COUNT(*) FROM $TABLE_ATTEMPTS"
        val cursor = db.rawQuery(countQuery, null)
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return count
    }
}