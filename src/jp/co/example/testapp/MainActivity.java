package jp.co.example.testapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

	/** �f�[�^�x�[�X�̃n���h����ێ� */
	DatabaseHelper dbHelper = null;
	SQLiteDatabase database = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* ListView���i�̃C���X�^���X���擾 */
        ListView listview = (ListView)findViewById(R.id.List);
        
        /* ���X�g�ɕ\������_�~�[�̃f�[�^��z���` */
//        String[] array = { "AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH" };
        
        /* ListView�ɐݒ肷��Adapter�N���X�̃C���X�^���X�𐶐�  */
        /* �����ł�ArrayAdapter�N���X���g�� */
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        
//        listview.setAdapter(adapter);
        
        /* �f�[�^�x�[�X���J�� */
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        
        /* Todo�f�[�^��o�^���Ɍ������擾 */
        Cursor cr = database.query(
        		DatabaseHelper.TODO_TABLE_NAME, 
        		new String[] {DatabaseHelper.ID, DatabaseHelper.DATA}, 
        		null, 
        		null, 
        		null, 
        		null, 
        		DatabaseHelper.ID + " desc",
        		null);
        
        /* ListView�փf�[�^��n�����߂�Adapter�Ɗ֘A�t����@*/
        SimpleCursorAdapter ca = new SimpleCursorAdapter(
        		this, 
        		android.R.layout.simple_list_item_1, 
        		cr, 
        		new String[] { DatabaseHelper.DATA }, 
        		new int[] { android.R.id.text1 });
        
        listview.setAdapter(ca);
    }

	@Override
	protected void onStop() {
		super.onStop();
		
		/* �f�[�^�x�[�X����� */
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}	