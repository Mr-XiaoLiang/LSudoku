package liang.lollipop.lsudoku.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import liang.lollipop.lsudoku.bean.SudokuBean

/**
 * Created by lollipop on 2018/3/1.
 * @author Lollipop
 * 数独的数据库工具
 */
class SudokuDbUtil  private constructor(context: Context, dbName:String, factory: SQLiteDatabase.CursorFactory?, version:Int)
    : SQLiteOpenHelper(context,dbName,factory,version) {

    companion object {

        const val DB_NAME = "LSudoku.db"
        const val VERSION = 1

        fun read(context: Context):DBOperate{
            return DBOperate(SudokuDbUtil(context).readableDatabase)
        }

        fun write(context: Context):DBOperate{
            return DBOperate(SudokuDbUtil(context).writableDatabase)
        }

    }

    private constructor(context: Context):this(context, DB_NAME,null, VERSION)

    private object Sudoku{
        const val TABLE = "SUDOKU_TABLE"
        const val ID = "ID"
        const val LEVEL = "LEVEL"
        const val MAP = "MAP"
        const val START_TIME = "START_TIME"
        const val END_TIME = "END_TIME"
        const val TIME_LENGTH = "TIME_LENGTH"
        const val HINT_TIME = "HINT_TIME"

        const val CREATE_SQL = "create table $TABLE ( " +
                " $ID INTEGER PRIMARY KEY, " +
                " $LEVEL INTEGER , " +
                " $MAP TEXT , " +
                " $START_TIME INTEGER , " +
                " $END_TIME INTEGER , " +
                " $TIME_LENGTH INTEGER , " +
                " $HINT_TIME INTEGER " +
                " ); "

        const val SELECT_ALL = "SELECT $ID , $LEVEL , $MAP , $START_TIME , $END_TIME , $TIME_LENGTH , " +
                "$HINT_TIME " +
                " FROM  $TABLE " +
                " ORDER BY $ID DESC "

        const val SELECT_BY_ID = "SELECT $ID , $LEVEL , $MAP , $START_TIME , $END_TIME , $TIME_LENGTH , " +
                " $HINT_TIME " +
                " FROM  $TABLE " +
                " WHERE $ID = ? "

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Sudoku.CREATE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    class DBOperate (private val database:SQLiteDatabase){

        fun install(bean:SudokuBean): DBOperate{

            val values = ContentValues()
            values.put(Sudoku.LEVEL,bean.level)
            values.put(Sudoku.MAP,bean.map)
            values.put(Sudoku.TIME_LENGTH,bean.timeLength)
            values.put(Sudoku.END_TIME,bean.endTime)
            values.put(Sudoku.START_TIME,bean.startTime)
            values.put(Sudoku.HINT_TIME,bean.hintTime)
            database.insert(Sudoku.TABLE,"",values)

            return this
        }

        fun delete(id:String): DBOperate{

            if(!TextUtils.isEmpty(id)){
                try {
                    database.delete(Sudoku.TABLE," ${Sudoku.ID} = ? ",arrayOf(id))
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            return this
        }

        fun update(bean:SudokuBean): DBOperate{

            val values = ContentValues()
            values.put(Sudoku.LEVEL,bean.level)
            values.put(Sudoku.MAP,bean.map)
            values.put(Sudoku.TIME_LENGTH,bean.timeLength)
            values.put(Sudoku.END_TIME,bean.endTime)
            values.put(Sudoku.START_TIME,bean.startTime)
            values.put(Sudoku.HINT_TIME,bean.hintTime)
            database.update(Sudoku.TABLE,values," ${Sudoku.ID} = ? ", arrayOf(bean.id))

            return this
        }

        fun selectAll(beanList:ArrayList<SudokuBean>): DBOperate{

            beanList.clear()

            val cursor = database.rawQuery(Sudoku.SELECT_ALL,null)
            putData(beanList,cursor)
            cursor.close()

            return this

        }

        fun selectById(id:String,bean:SudokuBean): DBOperate{

            val cursor = database.rawQuery(Sudoku.SELECT_BY_ID, arrayOf( id ))

            while (cursor.moveToNext()) {

                putBean(bean,cursor)

                break
            }

            cursor.close()

            return this
        }

        /**
         * 整理数据，将数据库数据整理为Bean对象
         */
        private fun putData(list: ArrayList<SudokuBean>,cursor: Cursor){

            while (cursor.moveToNext()) {

                val bean = SudokuBean()

                putBean(bean,cursor)

                list.add(bean)
            }
        }

        private fun putBean(bean: SudokuBean,cursor: Cursor){
            bean.apply {
                id = ""+cursor.getInt(cursor.getColumnIndex(Sudoku.ID))
                startTime = cursor.getLong(cursor.getColumnIndex(Sudoku.START_TIME))
                endTime = cursor.getLong(cursor.getColumnIndex(Sudoku.END_TIME))
                map = cursor.getString(cursor.getColumnIndex(Sudoku.MAP))
                hintTime = cursor.getLong(cursor.getColumnIndex(Sudoku.HINT_TIME))
                timeLength = cursor.getLong(cursor.getColumnIndex(Sudoku.TIME_LENGTH))
                level = cursor.getInt(cursor.getColumnIndex(Sudoku.LEVEL))
            }
        }

        /**
         * 回收销毁当前数据库操作连接
         */
        fun close(){
            database.close()

        }

    }

}