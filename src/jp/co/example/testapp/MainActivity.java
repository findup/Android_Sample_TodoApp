package jp.co.example.testapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

	/** データベースのハンドルを保持 */
	DatabaseHelper dbHelper = null;
	SQLiteDatabase database = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* ListView部品のインスタンスを取得 */
        ListView listview = (ListView)findViewById(R.id.List);
        
        /* リストに表示するダミーのデータを配列定義 */
//        String[] array = { "AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH" };
        
        /* ListViewに設定するAdapterクラスのインスタンスを生成  */
        /* ここではArrayAdapterクラスを使う */
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        
//        listview.setAdapter(adapter);
        
        /* データベースを開く */
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        
        /* Todoデータを登録順に検索し取得 */
        Cursor cr = database.query(
        		DatabaseHelper.TODO_TABLE_NAME, 
        		new String[] {DatabaseHelper.ID, DatabaseHelper.DATA}, 
        		null, 
        		null, 
        		null, 
        		null, 
        		DatabaseHelper.ID + " desc",
        		null);
        
        /* ListViewへデータを渡すためのAdapterと関連付ける　*/
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
		
		/* データベースを閉じる */
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}	