package jp.co.example.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.ConditionVariable;

public class DatabaseHelper extends SQLiteOpenHelper {

	/** �J�������̒�` */
	public static final String ID = "_id";
	public static final String DATA = "data";
	public static final String CHECKED = "checked";

	/** 
	 * �f�[�^�x�[�X�t�@�C���̃t�@�C����
	 * /data/data/jp.co.example.testapp/database �̉��ɍ����
	 */
	private static final String DATABASE_NAME = "TodoApp_DB";
	
	/** �f�[�^�x�[�X�̃o�[�W�����B */
    private static final int DATABASE_VERSION = 1;

    /** �e�[�u���� */
    public static final String TODO_TABLE_NAME = "tododata";
    
    /** �e�[�u�����쐬����SQL */
    private static final String TODO_TABLE_CREATE =
                "CREATE TABLE " + TODO_TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DATA + " TEXT NOT NULL, " +
                CHECKED + " INTEGER NOT NULL" +
                ");";
    
    /* �R���X�g���N�^ */
	public DatabaseHelper(Context context) {
		/* �f�[�^�x�[�X���ƁA�f�[�^�x�[�X�o�[�W�����͎w�肷�� */
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* ����N�����Ȃǃf�[�^�x�[�X�t�@�C�����쐬����Ă��Ȃ��ꍇ�ɌĂяo�����  */
	@Override
	public void onCreate(SQLiteDatabase db) {
		/* �f�[�^�x�[�X�e�[�u���쐬SQL�����s */
		db.execSQL(TODO_TABLE_CREATE);
		
		/* �_�~�[�f�[�^��2���ǉ� */
		ContentValues values = new ContentValues();
		
		values.put(DATA, "�e�X�g");
		values.put(CHECKED, 0);
		db.insert(TODO_TABLE_NAME, null, values);

		values.put(DATA, "�e�X�g�Q");
		values.put(CHECKED, 0);
		db.insert(TODO_TABLE_NAME, null, values);
	}

	/* �A�v���P�[�V�����̃o�[�W�����A�b�v���Ńf�[�^�x�[�X�̍\���ɕύX���������ꍇ�ɌĂяo�����
	 * ���̃T���v���A�v���ł͎������Ȃ�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
