package jp.co.example.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	/** データベースのハンドルを保持 */
	DatabaseHelper dbHelper = null;
	SQLiteDatabase database = null;
	
	private ListView listview;
	private Cursor cr;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* ListView部品のインスタンスを取得 */
        listview = (ListView)findViewById(R.id.List);
        
        /* リストに表示するダミーのデータを配列定義 */
//        String[] array = { "AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH" };
        
        /* ListViewに設定するAdapterクラスのインスタンスを生成  */
        /* ここではArrayAdapterクラスを使う */
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        
//        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				TextView textview = (TextView)view.findViewById(R.id.TextView01);
				CheckBox checkbox = (CheckBox)view.findViewById(R.id.TodoCheckBox);
				
				Bundle bundle = new Bundle();
				bundle.putString("Data", textview.getText().toString());
				bundle.putString("id", checkbox.getTag().toString());
				showDialog(0, bundle);
			}
		});
        
        /* Buttonが押された場合の処理 */
        Button btnadd = (Button)findViewById(R.id.Button_Add);
        btnadd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/* 入力チェック */
				EditText text = (EditText)findViewById(R.id.EditText);
				String str = text.getText().toString();
				
				if (str.equals("")) {
					return;
				}
				
				/* データベース登録 */
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.DATA, str);
				values.put(DatabaseHelper.CHECKED, 0);
				database.insert(DatabaseHelper.TODO_TABLE_NAME,
						null, 
						values);
				
				text.setText("");
				
				/* リスト画面の再描画 */
				cr.requery();
				((SimpleCursorAdapter)listview.getAdapter()).notifyDataSetChanged();
			}
		});
        
        registerForContextMenu(listview);
        
    }

	@Override
	protected void onResume() {
		super.onResume();
		
        /* データベースを開く */
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        
        /* Todoデータを登録順に検索し取得 */
        cr = database.query(
        		DatabaseHelper.TODO_TABLE_NAME, 
        		new String[] {DatabaseHelper.ID, DatabaseHelper.DATA, DatabaseHelper.CHECKED}, 
        		null, 
        		null, 
        		null, 
        		null, 
        		DatabaseHelper.ID + " desc",
        		null);
        
        /* ListViewへデータを渡すためのAdapterと関連付ける　*/
        SimpleCursorAdapter ca = new SimpleCursorAdapter(
        		this, 
//        		android.R.layout.simple_list_item_1, 
        		R.layout.list_layout,
        		cr, 
        		new String[] { DatabaseHelper.ID, DatabaseHelper.DATA, DatabaseHelper.CHECKED }, 
//        		new int[] { android.R.id.text1 });
				new int[] { R.id.TodoCheckBox, R.id.TextView01, R.id.TodoCheckBox });
        
        ca.setViewBinder(binder);
        listview.setAdapter(ca);
   	}

	@Override
	protected void onPause() {
		super.onPause();
		
		/* データベースを閉じる */
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
	
	SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			
			CheckBox checkbox = (CheckBox)view.findViewById(R.id.TodoCheckBox);
			TextView textview = (TextView)view.findViewById(R.id.TextView01);
			
			String columnname = cursor.getColumnName(columnIndex);

			if (columnname.equals(DatabaseHelper.ID)) {
				checkbox.setTag(cursor.getInt(columnIndex));
			} else if (columnname.equals(DatabaseHelper.DATA)) {
//				checkbox.setText(cursor.getString(columnIndex));
				textview.setText(cursor.getString(columnIndex));
			} else if (columnname.equals(DatabaseHelper.CHECKED)) {
				SpannableString spannablestring = new SpannableString(
						checkbox.getText());

				if (cursor.getInt(columnIndex) == 0) {
					checkbox.setChecked(false);
//					textview.setTextColor(Color.WHITE);
				} else {
					checkbox.setChecked(true);
//					textview.setTextColor(Color.GRAY);
					spannablestring.setSpan(new StrikethroughSpan(), 0, checkbox
							.getText().length(), 0);
//					textview.setText(spannablestring);
				}
			}
			
//			checkbox.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View paramView) {
//					// データを更新
//					ContentValues values = new ContentValues();
//					CheckBox checkbox = (CheckBox)paramView.findViewById(R.id.TodoCheckBox);
//					
//					// チェックボックスの状態に応じてデータを切り替える
//					if (checkbox.isChecked()) {
//						values.put(DatabaseHelper.CHECKED, 1);
//					} else {
//						values.put(DatabaseHelper.CHECKED, 0);
//					}
//					
//					database.update(DatabaseHelper.TODO_TABLE_NAME,
//							values,
//							DatabaseHelper.ID + " = " + checkbox.getTag(), 
//							null);
//					
//					/* リスト画面の再描画 */
//					cr.requery();
//					((SimpleCursorAdapter)listview.getAdapter()).notifyDataSetChanged();
//				}
//			});
			
			return true;
		}
	};

    @Override
	protected Dialog onCreateDialog(int id, Bundle bundle) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		//コンテキストからインフレータを取得
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

		//レイアウトXMLからビュー(レイアウト)をインフレート
		final View view = inflater.inflate(R.layout.dialog_edit, null);

		builder.setView(view)
		.setTitle("編集")
		.setPositiveButton("編集", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//				EditText edittext = (EditText)view.findViewById(R.id.EditText01);
				Dialog dialog = (Dialog)paramDialogInterface;
				EditText edittext = (EditText)dialog.findViewById(R.id.EditText01);
				
				// データを更新
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.DATA, edittext.getText().toString());
				
				database.update(DatabaseHelper.TODO_TABLE_NAME,
						values,
						DatabaseHelper.ID + " = " + edittext.getTag(), 
						null);
				
				/* リスト画面の再描画 */
				cr.requery();
				((SimpleCursorAdapter)listview.getAdapter()).notifyDataSetChanged();
			}
		});
	
		return builder.create();
	}

	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
		super.onPrepareDialog(id, dialog, bundle);
		
		EditText edittext = (EditText)dialog.findViewById(R.id.EditText01);
		edittext.setText(bundle.getString("Data"));
		edittext.setTag(bundle.get("id"));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		if(item.getItemId() == 1) {
			AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
			
			View view = adapterMenuInfo.targetView;
			TextView textview = (TextView)view.findViewById(R.id.TextView01);
			
	    	Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, textview.getText());
	    	startActivity(intent);    	
	    	
	    	return true;
		}
		
		return 	super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, R.string.send);
	}
	
	
}	