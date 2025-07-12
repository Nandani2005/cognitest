import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MCQDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "MCQDatabase.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                questionText TEXT,
                optionA TEXT,
                optionB TEXT,
                optionC TEXT,
                optionD TEXT,
                correctIndex INTEGER,
                selectedIndex INTEGER DEFAULT -1
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS questions")
        onCreate(db)
    }

    fun insertQuestion(
        questionText: String, a: String, b: String, c: String, d: String, correctIndex: Int
    ) {
        val values = ContentValues().apply {
            put("questionText", questionText)
            put("optionA", a)
            put("optionB", b)
            put("optionC", c)
            put("optionD", d)
            put("correctIndex", correctIndex)
        }
        writableDatabase.insert("questions", null, values)
    }

    fun getAllQuestions(): List<QuestionModel> {
        val list = mutableListOf<QuestionModel>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM questions", null)
        if (cursor.moveToFirst()) {
            do {
                val question = QuestionModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    questionText = cursor.getString(cursor.getColumnIndexOrThrow("questionText")),
                    optionA = cursor.getString(cursor.getColumnIndexOrThrow("optionA")),
                    optionB = cursor.getString(cursor.getColumnIndexOrThrow("optionB")),
                    optionC = cursor.getString(cursor.getColumnIndexOrThrow("optionC")),
                    optionD = cursor.getString(cursor.getColumnIndexOrThrow("optionD")),
                    correctIndex = cursor.getInt(cursor.getColumnIndexOrThrow("correctIndex")),
                    selectedIndex = cursor.getInt(cursor.getColumnIndexOrThrow("selectedIndex"))
                )
                list.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun saveStudentResponse(questionId: Int, selectedIndex: Int) {
        val values = ContentValues().apply {
            put("selectedIndex", selectedIndex)
        }
        writableDatabase.update("questions", values, "id=?", arrayOf(questionId.toString()))
    }
}
