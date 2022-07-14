package com.stackstudios.stack;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.app.Activity;

public class tools {

    Activity activity;
	private boolean PASAR = false;
	private AlertDialog AccessStorageDialog;

	public tools(Activity activity) {
		this.activity = activity;
	}

	public Boolean AccessStorage(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			if (!Environment.isExternalStorageManager()) {
				PermissionAccessStorageAndroidDialog();
			}else{
				PASAR = true;
			}
		}else{
			if(activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
				PASAR = true;
			}else{
				PermissionAccessStorageAndroidDialog();
			}

		}
		return PASAR;
	}


	private void PermissionAccessStorageAndroidDialog() {
		AccessStorageDialog = new AlertDialog.Builder(activity)
			.setTitle("acceso al almacenamiento interno")
			.setMessage("Para garantizar el funcionamiento de la division de carpetas de cada version Stack app necesita tener acceso al almacenamiento interno, \npara mas informacion visitar el menu de 'Informacion de Stack App'")
			.setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dia, int which) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
						PermissionAccessStorageAndroid11();
					}else{
						PermissionAccessStorage();
					}
				}
			})
			.setNegativeButton("No permitir", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					AccessStorageDialog.dismiss();
				}
			})
			.create();
		AccessStorageDialog.show();

	}
	private void PermissionAccessStorage() {
		if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			activity.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 57);
		}
    }
	private void PermissionAccessStorageAndroid11(){
		try {
			if (!Environment.isExternalStorageManager()) {
				Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
				Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
				intent.setData(uri);
				activity.startActivity(intent);
			}
		} catch (Exception e) {
			Toast.makeText(activity, "P.A.S.A.R - Erro!r desconocido : " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

}
