package com.example.caculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class SaveToSqlite {
	protected void save(Bundle savedInstanceState) {
		
		// �򿪻򴴽�test.db���ݿ�
		SQLiteDatabase db = SQLiteDatabase
				.openOrCreateDatabase("downlog.db", null);
		db.execSQL("DROP TABLE IF EXISTS person");
		// ����person��
		db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
		Person person = new Person();
		person.name = "john";
		person.age = 30;
		// ��������
		db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[] {
				person.name, person.age });

		person.name = "david";
		person.age = 33;
		// ContentValues�Լ�ֵ�Ե���ʽ�������
		ContentValues cv = new ContentValues();
		cv.put("name", person.name);
		cv.put("age", person.age);
		// ����ContentValues�е�����
		db.insert("person", null, cv);

		cv = new ContentValues();
		cv.put("age", 35);
		// ��������
		db.update("person", cv, "name = ?", new String[] { "john" });

		Cursor c = db.rawQuery("SELECT * FROM person WHERE age >= ?",
				new String[] { "33" });
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));
			String name = c.getString(c.getColumnIndex("name"));
			int age = c.getInt(c.getColumnIndex("age"));

		}
		c.close();

		// ɾ������
		// db.delete("person", "age < ?", new String[]{"35"});

		// �رյ�ǰ���ݿ�
		db.close();

		// ɾ��test.db���ݿ�
		// deleteDatabase("test.db");
	}

}

class Person {
	String name;
	int age;
}
