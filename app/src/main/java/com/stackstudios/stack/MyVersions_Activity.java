package com.stackstudios.stack;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Environment;
import android.content.Intent;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class MyVersions_Activity extends Activity {
    
	ImageView btn_back;
	ImageView btn_ops;
	
	ViewGroup MyVersionsLayout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myversions_activity);
		
		btn_back = findViewById(R.id.myversionsactivityImageView1);
		btn_ops = findViewById(R.id.myversionsactivityImageView2);
		MyVersionsLayout = findViewById(R.id.myversionsactivityLinearLayout1);
		
		InstalledVersionsAnalyzer();
		
		
		
		btn_ops.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					
				}
			});
		
		btn_back.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					onBackPressed();
					finish();
				}
			});
    }

//	private void Andro11() {
//		if(Environment.isExternalStorageManager()){
//			File file = new File(Environment.
//		}else{
//			
//		}
//	}

	private void InstalledVersionsAnalyzer() {
		File RutaPricipal = new File(getExternalFilesDir(null), "datastackmod.txt");

		try {
			String Linea;
			BufferedReader bf = new BufferedReader(new FileReader(RutaPricipal));

			while ((Linea = bf.readLine()) != null) {
				try {
					String[] line = Linea.split("sseparatorr");

					String PackageName = line[0].toString();
					String Name = line[1];
					String sizeAPK = line[2];
					String urlDownload = line[3];
					
					if(appInstalled(PackageName)){
						ListVersionsIntalled(PackageName, Name, sizeAPK, urlDownload);
					}
						

					//addLayoutListVersions(PackageName, Name, sizeAPK, urlDownload);

				} catch (Exception e) {
					Toast.makeText(getApplication(), "LinePerLine : " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			Toast.makeText(getApplication(), "error1: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void ListVersionsIntalled(final String PackageName, String Name, String sizeAPK, String urlDownload) {
		
		LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearlayou = (LinearLayout) inflater.inflate(R.layout.ListContainer, null, false);

        TextView NameView = linearlayou.findViewById(R.id.ListContainerTextView1);
		TextView SizeView = linearlayou.findViewById(R.id.ListContainerTextView2);
		//TextView urlDownloadView = linearlayou.findViewById(R.id.ListContainerTextView3);
		TextView OpenApp = linearlayou.findViewById(R.id.ListContainerTextView4);
		TextView Administr = linearlayou.findViewById(R.id.ListContainerTextView5);

		OpenApp.setVisibility(View.VISIBLE);


		NameView.setText(Name);
		SizeView.setText(sizeAPK);
		
		OpenApp.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Boolean AccessStorage = new tools(MyVersions_Activity.this).AccessStorage();
					if(AccessStorage){
						if (!PackageName.equals("com.mojang.minecraftpe")) {
							FoldersCreator(PackageName);
						}
						PlayMinecraft(PackageName);

					}else{
						Toast.makeText(getApplication(), "Permiso necesario", Toast.LENGTH_SHORT).show();
					}
					
				}
			});

		Administr.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});

        MyVersionsLayout.addView(linearlayou);
		
	}
	private void FoldersCreator(String PackageName) {
		try{
			try {
				File MinecraftFolder0 = new File(Environment.getExternalStorageDirectory(), "/games/");
				File GameFolder = new File(MinecraftFolder0, "com.mojang/");
				File fileByStack0 = new File(GameFolder, "MinecraftVersionByStack.stackstudios");


				String nameOfDataOfFile = "";

				String Linea;
				BufferedReader bf = new BufferedReader(new FileReader(fileByStack0));
				while ((Linea = bf.readLine()) != null) {
					nameOfDataOfFile = Linea;
				}
				GameFolder.renameTo(new File(MinecraftFolder0, nameOfDataOfFile + "/"));
			} catch (Exception e) {}

			File MinecraftFolder = new File(Environment.getExternalStorageDirectory(), "/games/" + PackageName + "/");
			if (!MinecraftFolder.exists()) {
				MinecraftFolder.mkdirs();
			}

			try {
				File fileByStack = new File(MinecraftFolder, "MinecraftVersionByStack.stackstudios");
				if (!fileByStack.exists()) {
					fileByStack.createNewFile();

					FileOutputStream fileOutput = new FileOutputStream(fileByStack);
					OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
					outputStreamWriter.write(PackageName);
					outputStreamWriter.flush();
					fileOutput.getFD().sync();
					outputStreamWriter.close();
				}
			} catch (IOException e) {
				Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){}
	}
	
	private void PlayMinecraft(String PackageName) {
		try {
			try{
				File MinecraftFolder0 = new File(Environment.getExternalStorageDirectory(), "/games/");
				File GameFolder = new File(MinecraftFolder0, "com.mojang/");
				File fileByStack0 = new File(GameFolder, "MinecraftVersionByStack.stackstudios");


				String nameOfDataOfFile = "";

				String Linea;
				BufferedReader bf = new BufferedReader(new FileReader(fileByStack0));
				while ((Linea = bf.readLine()) != null) {
					nameOfDataOfFile = Linea;
				}

				GameFolder.renameTo(new File(MinecraftFolder0, nameOfDataOfFile + "/"));

			}catch(Exception e){}

			File MCFold = new File(Environment.getExternalStorageDirectory(), "/games/" + PackageName + "/");
			File MCFnew = new File(Environment.getExternalStorageDirectory(), "/games/" + "com.mojang" + "/");

			MCFold.renameTo(MCFnew);

		} catch (Exception e) {}

		Intent intent = getPackageManager().getLaunchIntentForPackage(PackageName);
		if (intent == null) {
			//No se puede abrir aplicacion.
			Toast.makeText(getApplication(), PackageName + " Error", Toast.LENGTH_SHORT).show();
		}
		startActivity(intent);
		//Abre aplicacion

	}
	
	private boolean appInstalled(String nombrePaquete) {
		PackageManager pm = getApplicationContext().getPackageManager();
		try {
			pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
}
