package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String[] wordsA1 = new String[896];
    String[] wordsA2 = new String[865];
    String[] wordsB1 = new String[801];
    String[] wordsB2 = new String[1423];
    String[] wordsC1 = new String[1309];

    Context context=this;
    AssetDatabaseOpenHelper db;
    ArrayAdapter<String> adapter2;
    TextView textView3,tw77;
    Spinner spinner;
    Button button,btnquiz;
    EditText editText;
    ListView lv;
    String word;
    String meaning;
    int dogru_cevap=0;
    int a=0;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new AssetDatabaseOpenHelper(context);

        spinner=findViewById(R.id.spinner);

        textView3=findViewById(R.id.textView3);
        button = findViewById(R.id.button);
        btnquiz = findViewById(R.id.btnQuiz);
        editText = findViewById(R.id.edtTxt);
        lv=findViewById(R.id.listbox);
        tw77=findViewById(R.id.tw77);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.seviyeler));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        db.openDatabase();
        try {

            Cursor cursor = db.openDatabase().rawQuery("Select * From WordTable", null);
            int word = cursor.getColumnIndex("word");
            int level = cursor.getColumnIndex("level");
            int iA1 = 0, iA2 = 0, iB1 = 0, iB2 = 0, iC1 = 0;
            while (cursor.moveToNext()) {
                if (cursor.getInt(level) == 0) { wordsA1[iA1] = cursor.getString(word); iA1++; }
                else if (cursor.getInt(level) == 1) { wordsA2[iA2] = cursor.getString(word); iA2++; }
                else if (cursor.getInt(level) == 2) { wordsB1[iB1] = cursor.getString(word); iB1++; }
                else if (cursor.getInt(level) == 3) { wordsB2[iB2] = cursor.getString(word); iB2++; }
                else if (cursor.getInt(level) == 4) { wordsC1[iC1] = cursor.getString(word); iC1++; }
            }


            cursor.close();

        }
        catch (Exception e) {

        }

        try {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinner.getSelectedItem().toString().equals("A1")){
                        adapter2 = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_listview,R.id.textView3,wordsA1);
                    }
                    else if(spinner.getSelectedItem().toString().equals("A2")){
                        adapter2 = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_listview,R.id.textView3,wordsA2);
                    }
                    else if(spinner.getSelectedItem().toString().equals("B1")){
                        adapter2 = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_listview,R.id.textView3,wordsB1);
                    }
                    else if(spinner.getSelectedItem().toString().equals("B2")){
                        adapter2 = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_listview,R.id.textView3,wordsB2);
                    }
                    else if(spinner.getSelectedItem().toString().equals("C1")){
                        adapter2 = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_listview,R.id.textView3,wordsC1);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "HATA", Toast.LENGTH_SHORT).show();

                    }
                    lv.setAdapter(adapter2);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   word = lv.getItemAtPosition(position).toString();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    textView3.setText("Doğru Cevap: ");
                    meaning= editText.getText().toString().toLowerCase();
                    meaning=meaning.replaceAll("\\s+"," ").trim();
                    Cursor cursor = db.openDatabase().rawQuery("Select * From WordTable where word='" + word + "' AND meaning1='" + meaning + "' OR meaning2='" + meaning + "' OR meaning3='" + meaning + "'",null);
                    int id=cursor.getColumnIndex("id");
                    if(meaning.isEmpty()){
                        Toast.makeText(MainActivity.this, "Lütfen Kelime Girin", Toast.LENGTH_SHORT).show();}
                    else{
                    if(cursor.moveToNext()){
                        Toast.makeText(MainActivity.this, "Doğru Cevap", Toast.LENGTH_SHORT).show();
                        dogru_cevap++;
                        tw77.setText("Doğru Cevap Sayısı: "+dogru_cevap);

                    }
                    else {
                        cursor= db.openDatabase().rawQuery("Select meaning1,meaning2,meaning3 From WordTable where word='"+word+"'",null);
                        String anlam="";
                            if(cursor.moveToNext()){
                            anlam +=" "+ cursor.getString(0)+", "+ cursor.getString(1)+", "+ cursor.getString(2);
                        textView3.setText("Doğru Cevap: "+anlam);}
                            else{Toast.makeText(context, "Kelime Seçmediniz", Toast.LENGTH_SHORT).show(); a=-1;}
                            if(a==0) Toast.makeText(MainActivity.this, "Yanlış Cevap", Toast.LENGTH_SHORT).show();
                    }}
                    editText.setText("");
                    cursor.close();
                    a=0;
                }
            });

            btnquiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),Quiz.class);
                    startActivity(intent);
                }
            });
    }



}