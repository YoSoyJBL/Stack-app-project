package com.stackstudios.stack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class Versions_Activity extends Activity {

	ImageView back;
	ImageView btn_options;
	ViewGroup ListLayout;
	private AlertDialog alertDialog;
	private AlertDialog ListOptions;
	int start_download = 1;
	int NormalVersions = 0;

	private AlertDialog MessageCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.versions_activity);
		back = findViewById(R.id.versionsactivityImageView1);
		btn_options = findViewById(R.id.versionsactivityImageView2);
		ListLayout = findViewById(R.id.versionsactivityLinearLayout1);

		ListMinecraftVersion("datastackmod.txt");

		MessageCustom();

        back.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					onBackPressed();
				}
			});
		btn_options.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					ListOptionsMenu();
				}
			});
    }

	private void ListMinecraftVersion(String MinecraftVersionInformationFile) {
		File RutaPricipal = new File(getExternalFilesDir(null), MinecraftVersionInformationFile);

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

					

					addLayoutListVersions(PackageName, Name, sizeAPK, urlDownload);

				} catch (Exception e) {
					Toast.makeText(getApplication(), "LinePerLine : " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			Toast.makeText(getApplication(), "error1: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

    @SuppressLint("InlinedApi")
    private void addLayoutListVersions(final String PackageName, final String Name, String sizeAPK, final String urlDownload) {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearlayou = (LinearLayout) inflater.inflate(R.layout.ListContainer, null, false);

        TextView NameView = linearlayou.findViewById(R.id.ListContainerTextView1);
		TextView SizeView = linearlayou.findViewById(R.id.ListContainerTextView2);
		TextView urlDownloadView = linearlayou.findViewById(R.id.ListContainerTextView3);
		TextView OpenApp = linearlayou.findViewById(R.id.ListContainerTextView4);
		TextView Administr = linearlayou.findViewById(R.id.ListContainerTextView5);

		if (appInstalled(PackageName)) {
			if(!PackageName.equals("com.mojang.minecraftpe")){
				OpenApp.setVisibility(View.VISIBLE);
			}else{
				urlDownloadView.setVisibility(View.VISIBLE);
			}
		} else {
			urlDownloadView.setVisibility(View.VISIBLE);
		}


		NameView.setText(Name);
		SizeView.setText(sizeAPK);



		urlDownloadView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					urlDirectDownload(urlDownload, Name);
					CustomLoading();
				}
			});
		OpenApp.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					
					Boolean AccessStorage = new tools(Versions_Activity.this).AccessStorage();
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

        ListLayout.addView(linearlayou);
    }


	private void urlDirectDownload(final String url, final String Name) {
        new BackgroundTaskAPI(Versions_Activity.this) {

			String urlDownload = "";
			String DownloadFail= "";
            @Override
            public void doInBackground() {
                try {
                    Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").get();
                    Elements urlDownloa = doc.getElementsByClass("btn btn-primary btn-block");
                    urlDownload = urlDownloa.attr("href");

                } catch (Exception e) {
					DownloadFail = e.getMessage();
                }
            }

            @Override
            public void onPostExecute() {
				if (urlDownload.equals("")) {
					Toast.makeText(getApplication(), "Error al procesar la descarga: " + DownloadFail, Toast.LENGTH_SHORT).show();
					alertDialog.dismiss();
				} else {
					//executeDownload(urlDownload, Name + ".apk");
					OpenLinkDownload(urlDownload);


				}

            }


        }.execute();
    }

	private void OpenLinkDownload(String link) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		startActivity(intent);
		alertDialog.dismiss();

//		try {
//			CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();   
//			CustomTabsIntent customTabsIntent = builder.build();
//			customTabsIntent.launchUrl(Versions_Activity.this, Uri.parse("googlechrome://navigate?url=" + link));
//			alertDialog.dismiss();
//		} catch (Exception e) {
//			Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
//			alertDialog.dismiss();
//		}
	}

	private void CustomLoading() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.LoadingLayout, null);

        builder.setView(v);

        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
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



	private void ListOptionsMenu() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.ListOptionLayout, null);

		final TextView Op1 = v.findViewById(R.id.ListOptionLayoutTextView1);
		TextView Op2 = v.findViewById(R.id.ListOptionLayoutTextView2);
	    TextView Op3 = v.findViewById(R.id.ListOptionLayoutTextView3);

		if (NormalVersions == 0) {
			Op1.setText("Cambiar a las versiones estandar");
		} else {
			Op1.setText("Cambiar a las versiones individuales");
		}

		Op1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ListLayout.removeAllViews();
					if (NormalVersions == 0) {
						ListMinecraftVersion("datastacknormal.txt");
						NormalVersions = 1;
					} else {
						ListMinecraftVersion("datastackmod.txt");
						NormalVersions = 0;
					}
					ListOptions.dismiss();
				}
			});
		Op2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Toast.makeText(getApplication(), "Proximamente", Toast.LENGTH_SHORT).show();
					ListOptions.dismiss();
				}

			});
		Op3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Toast.makeText(getApplication(), "Proximamente", Toast.LENGTH_SHORT).show();
					ListOptions.dismiss();
				}
			});
		builder.setView(v);
        builder.setCancelable(true);
        ListOptions = builder.create();
        ListOptions.show();
    }
	private void MessageCustom() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.MessageCustom, null);

		TextView btn_ok = v.findViewById(R.id.MessageCustomTextView4);

		btn_ok.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					MessageCustom.dismiss();
				}
			});

        builder.setView(v);
        builder.setCancelable(true);

        MessageCustom = builder.create();
        MessageCustom.show();
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
}
//MinecraftVersionByStack
