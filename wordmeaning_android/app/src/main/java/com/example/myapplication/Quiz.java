package com.example.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class Quiz extends Activity {
    int level = 0;
    int skor = 0;
    TextView tw4,tw5,tw2;
    Button button3,button4;
    EditText editText;
    String kelime;
    SQLiteDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        tw4=findViewById(R.id.textView4);
        tw5=findViewById(R.id.textView5);
        tw2=findViewById(R.id.textView2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        editText=findViewById(R.id.etdTxtQ);
        soru();

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String anlam = editText.getText().toString().toLowerCase();
                anlam=anlam.replaceAll("\\s+"," ").trim();
                Cursor cursor = db.rawQuery("Select * From WordTable where word='" + kelime + "' AND meaning1='" + anlam + "' OR meaning2='" + anlam + "' OR meaning3='" + anlam + "'",null);

                if (!anlam.isEmpty())
                {

                    if (cursor.moveToNext())
                    {
                        skor++;
                        if (skor % 10 == 0) level++;
                    }
                    else { if (skor > 0) skor--; }
                    if (skor < 40 && level == 4) level--;
                    else if (skor < 30 && level == 3) level--;
                    else if (skor < 20 && level == 2) level--;
                    else if (skor < 10 && level == 1) level--;
                    editText.setText("");
                    tw5.setText("Skor: " + skor);
                    soru();
                }
                else Toast.makeText(Quiz.this, "Lütfen Cevabı Girin", Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soru();
            }
        });
    }

    private void soru(){
        lvl_don();
        db= this.openOrCreateDatabase("WordDB.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("Select * From WordTable where level = "+level+" ORDER BY RANDOM() LIMIT 1", null);
        int c_word=cursor.getColumnIndex("word");
        cursor.moveToNext();
        kelime=cursor.getString(c_word);
        tw4.setText("Kelime: "+kelime);
        cursor.close();
    }

    private void lvl_don(){
        if(level==0) tw2.setText("Seviye: A1");
        else if(level==1) tw2.setText("Seviye: A2");
        else if(level==2) tw2.setText("Seviye: B1");
        else if(level==3) tw2.setText("Seviye: B2");
        else if(level==4) tw2.setText("Seviye: C1");
    }
}
