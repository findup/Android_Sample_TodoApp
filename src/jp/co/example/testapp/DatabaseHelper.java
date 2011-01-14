package jp.co.example.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.ConditionVariable;

public class DatabaseHelper extends SQLiteOpenHelper {

	/** カラム名の定義 */
	public static final String ID = "_id";
	public static final String DATA = "data";
	public static final String CHECKED = "checked";

	/** 
	 * データベースファイルのファイル名
	 * /data/data/jp.co.example.testapp/database の下に作られる
	 */
	private static final String DATABASE_NAME = "TodoApp_DB";
	
	/** データベースのバージョン。 */
    private static final int DATABASE_VERSION = 1;

    /** テーブル名 */
    public static final String TODO_TABLE_NAME = "tododata";
    
    /** テーブルを作成するSQL */
    private static final String TODO_TABLE_CREATE =
                "CREATE TABLE " + TODO_TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DATA + " TEXT NOT NULL, " +
                CHECKED + " INTEGER NOT NULL" +
                ");";
    
    /* コンストラクタ */
	public DatabaseHelper(Context context) {
		/* データベース名と、データベースバージョンは指定する */
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* 初回起動時などデータベースファイルが作成されていない場合に呼び出される  */
	@Override
	public void onCreate(SQLiteDatabase db) {
		/* データベーステーブル作成SQLを実行 */
		db.execSQL(TODO_TABLE_CREATE);
		
		/* ダミーデータを2件追加 */
		ContentValues values = new ContentValues();
		
		values.put(DATA, "テスト");
		values.put(CHECKED, 0);
		db.insert(TODO_TABLE_NAME, null, values);

		values.put(DATA, "テスト２");
		values.put(CHECKED, 0);
		db.insert(TODO_TABLE_NAME, null, values);
	}

	/* アプリケーションのバージョンアップ等でデータベースの構成に変更があった場合に呼び出される
	 * このサンプルアプリでは実装しない
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
