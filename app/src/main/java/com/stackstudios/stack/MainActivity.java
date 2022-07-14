package com.stackstudios.stack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    
	CardView OP_version;
	CardView OP_MyVersions;
	ImageView Op_menu;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		OP_version = findViewById(R.id.activitymainCardView1);
		OP_MyVersions = findViewById(R.id.activitymainCardView2);
		Op_menu = findViewById(R.id.activitymainImageView1);
		
		FileImportant();
		
		
		OP_version.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(MainActivity.this, Versions_Activity.class));
					
					
				}
			});
		OP_MyVersions.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(MainActivity.this, MyVersions_Activity.class));
				}
			});
		Op_menu.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					
				}
			});
    }
	

	

	private void FileImportant() {
		String texttt = "nulo";
		
		//normal versions
		try {
            InputStream is = getApplication().getAssets().open("datastacknormal.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            texttt = new String(buffer, "UTF-8");
            //Toast.makeText(getApplication(), ReadResult, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
			Toast.makeText(getApplication(), "Error read from asset: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		try{
			File DataStacktxt = new File(getExternalFilesDir(null), "datastacknormal.txt");
			
			DataStacktxt.createNewFile();

			FileOutputStream fileOutput = new FileOutputStream(DataStacktxt);
			OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
			outputStreamWriter.write(texttt);
			outputStreamWriter.flush();
			fileOutput.getFD().sync();
			outputStreamWriter.close();
			
		}catch(Exception e){
			Toast.makeText(getApplication(), "Error write in internal storage: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		//mod versions
		try {
            InputStream is = getApplication().getAssets().open("datastackmod.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            texttt = new String(buffer, "UTF-8");
            //Toast.makeText(getApplication(), ReadResult, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
			Toast.makeText(getApplication(), "Error read from asset: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		try{
			File DataStacktxt = new File(getExternalFilesDir(null), "datastackmod.txt");

			DataStacktxt.createNewFile();

			FileOutputStream fileOutput = new FileOutputStream(DataStacktxt);
			OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
			outputStreamWriter.write(texttt);
			outputStreamWriter.flush();
			fileOutput.getFD().sync();
			outputStreamWriter.close();

		}catch(Exception e){
			Toast.makeText(getApplication(), "Error write in internal storage: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
	}
    
}
/*don't forget to subscribe my YouTube channel for more Tutorial and mod*/
/*
https://youtube.com/channel/UC_lCMHEhEOFYgJL6fg1ZzQA */
