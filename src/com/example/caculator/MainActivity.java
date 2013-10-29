package com.example.caculator;


import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author 铂金小龟
 */
public class MainActivity extends Activity implements OnClickListener {

	EditText rsText = null; // 显示器
	boolean isClear = false; // 用于是否显示器需要被清理
	SQLiteDatabase db;
	protected static final int Menu_Item1=Menu.FIRST;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		
		rsText = (EditText) findViewById(R.id.rsText);
		Button btnDel = (Button) findViewById(R.id.delete);
		Button btnPlu = (Button) findViewById(R.id.plus);
		Button btnMin = (Button) findViewById(R.id.minus);
		Button btnMul = (Button) findViewById(R.id.multiply);
		Button btnDiv = (Button) findViewById(R.id.division);
		Button btnEqu = (Button) findViewById(R.id.equ);

		// num 数字按钮
		Button num0 = (Button) findViewById(R.id.num0);
		Button num1 = (Button) findViewById(R.id.num1);
		Button num2 = (Button) findViewById(R.id.num2);
		Button num3 = (Button) findViewById(R.id.num3);
		Button num4 = (Button) findViewById(R.id.num4);
		Button num5 = (Button) findViewById(R.id.num5);
		Button num6 = (Button) findViewById(R.id.num6);
		Button num7 = (Button) findViewById(R.id.num7);
		Button num8 = (Button) findViewById(R.id.num8);
		Button num9 = (Button) findViewById(R.id.num9);
		Button dot = (Button) findViewById(R.id.dot);

		// listener
		btnDel.setOnClickListener(this);
		btnPlu.setOnClickListener(this);
		btnMin.setOnClickListener(this);
		btnMul.setOnClickListener(this);
		btnDiv.setOnClickListener(this);
		btnEqu.setOnClickListener(this);
		num0.setOnClickListener(this);
		num1.setOnClickListener(this);
		num2.setOnClickListener(this);
		num3.setOnClickListener(this);
		num4.setOnClickListener(this);
		num5.setOnClickListener(this);
		num6.setOnClickListener(this);
		num7.setOnClickListener(this);
		num8.setOnClickListener(this);
		num9.setOnClickListener(this);
		dot.setOnClickListener(this);
		
		/*GridLayout.LayoutParams params = new GridLayout.LayoutParams(5,4);
		//指定该组件占满容器
		params.setGravity(Gravity.FILL);*/
	}

	@Override
	public void onClick(View e) {
		Button btn = (Button) e;
		String exp = rsText.getText().toString();
		db = new DataBaseHelper(this).getWritableDatabase();
		//db.delete("constants", null, null);
		if (isClear
				&& (btn.getText().equals("0") || btn.getText().equals("1")
						|| btn.getText().equals("2")
						|| btn.getText().equals("3")
						|| btn.getText().equals("4")
						|| btn.getText().equals("5")
						|| btn.getText().equals("6")
						|| btn.getText().equals("7")
						|| btn.getText().equals("8")
						|| btn.getText().equals("9") || btn.getText().equals(
						".")) || btn.getText().equals("算数公式错误")) {
			rsText.setText("");
			isClear = false;
		}
		if (btn.getText().equals("C")) {
			rsText.setText("");
		} else if (btn.getText().equals("清除")) {
			if (isEmpty(exp))
				return;
			else
				rsText.setText(exp.substring(0, exp.length() - 1));
		} else if (btn.getText().equals("=")) {
			if (isEmpty(exp))
				return;
			exp = exp.replaceAll("×", "*");
			exp = exp.replaceAll("÷", "/");
			
			
			String result = CalcluateResult(exp);
			ContentValues tmp = new ContentValues();
			tmp.put("title", exp);
			tmp.put("value", result);
			db.insert("constants", "title", tmp);
			rsText.setText(result);
			isClear = false;
		} else {
			rsText.setText(rsText.getText() + "" + btn.getText());
			isClear = false;
		}
		// 操作完成后始终保持光标在最后一位
		rsText.setSelection(rsText.getText().length());
		updateList();
		db.close();
	}
	public void updateList() {
		ListView listeAlbum = (ListView) this.findViewById(R.id.caldblistView);
	    
	    ArrayList<String> output = new ArrayList<String>();

	    String[] colonnesARecup = new String[] { "title", "value" };

	    Cursor cursorResults = db.query("constants", colonnesARecup,
	            null, null, null, null, null, null);
	    if (null != cursorResults) {
	        if (cursorResults.moveToFirst()) {
	            do {
	                int columnIdxArtiste = cursorResults
	                        .getColumnIndex("title");
	                int columnIdxTitre = cursorResults.getColumnIndex("value");
	                String oItem = cursorResults.getString(columnIdxArtiste)
	                        + " =  " + cursorResults.getString(columnIdxTitre);
	                output.add(oItem);
	            } while (cursorResults.moveToNext());
	        }
	    }
	    
	    
	    adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, output);
	    listeAlbum.setAdapter(adapter);
	    listeAlbum.setOnCreateContextMenuListener(MenuLis);
	    cursorResults.close();
	}
	ListView.OnCreateContextMenuListener MenuLis=new ListView.OnCreateContextMenuListener(){
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			//添加菜单项
			menu.add(Menu.NONE,Menu_Item1,0,"删除这条记录");
		}
 
    };
    public boolean onContextItemSelected(MenuItem item){
    	//关键代码在这里
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //输出position
        adapter.remove(adapter.getItem(menuInfo.position));
        return true;
        //return super.onContextItemSelected(item); 
 
    }
	/***
	 * @param exp
	 *            算数表达式
	 * @return 根据表达式返回结果
	 */
	StringToArithmetic cacutt = new StringToArithmetic();
	private String CalcluateResult(String input) {
		try {
			// 如果没有输入,则什么都不用做
			if (input.length() <= 0)
				return "";
			// 先去掉回车换行
			input = input.replace("\n", "");
			// 先处理第一个数是负数的情况
			if (input.charAt(0) == '-')
				input = "0" + input;
			// 替换运算符,+,-,*,/分别替换为a,b,c,d
			double result = cacutt.stringToArithmetic(input);
			return String.valueOf(result);
		} catch (Exception ex) {
			return "运算式不合法！";
		}
	}

	

	/***
	 * @param str
	 * @return 字符串非空验证
	 */
	private boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

}