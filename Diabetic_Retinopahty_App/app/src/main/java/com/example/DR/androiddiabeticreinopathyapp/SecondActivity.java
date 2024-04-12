package com.example.DR.androiddiabeticretinopathyapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Util.Util;

import static Util.Util.isNetworkAvailable;


public class SecondActivity extends Activity implements View.OnClickListener {
    Bitmap bitmap;
    ImageView bmpImageView;
    EditText editname, editseverity, editpercenatge;
    String name = null;
    String strseverity = null, strpercentage = null;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Button sharebutton;
    LinearLayout shareLinearLayout1;
    TableLayout tablelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivity);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {

            } else {
                requestPermission();
            }
        } else {
        }
        shareLinearLayout1= (LinearLayout)findViewById(R.id.shareLinearLayout1);
        shareLinearLayout1.setDrawingCacheEnabled(true);
        shareLinearLayout1.getDrawingCache();

        tablelayout = (TableLayout)findViewById(R.id.tablelayout);
        tablelayout.setDrawingCacheEnabled(true);
        tablelayout.getDrawingCache();


        editname = (EditText) findViewById(R.id.editname);
        editseverity = (EditText) findViewById(R.id.editseverity);
        editpercenatge = (EditText) findViewById(R.id.editpercenatge);
        bmpImageView = (ImageView) findViewById(R.id.imageViewbmp);
        sharebutton= (Button)findViewById(R.id.sharebutton);
        sharebutton.setOnClickListener(this);
        Intent intent = getIntent();
        strseverity = intent.getStringExtra("severity");
        strpercentage = intent.getStringExtra("perecntage");

        byte[] bytes = intent.getByteArrayExtra("BMP");
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        name =  intent.getStringExtra("name");

        editname.setText(name);
        editseverity.setText(strseverity);
        editpercenatge.setText(strpercentage + "");
        bmpImageView.setImageBitmap(bitmap);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showSystemDialog();


        } else {
            ActivityCompat.requestPermissions(SecondActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    void showSystemDialog() {

        AlertDialog.Builder alertDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(SecondActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(SecondActivity.this);
        }



        alertDialog.setMessage("You need to enable permissions for storage to download APK ?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });


        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sharebutton){

            if (Util.isNetworkAvailable(this)) {
                Bitmap bitmap = Bitmap.createBitmap(shareLinearLayout1.getWidth(),shareLinearLayout1.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                shareLinearLayout1.draw(canvas);
                bitmap = getResizedBitmap(bitmap, 500);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAILID_TO});
                share.putExtra(Intent.EXTRA_SUBJECT, "Diabetes level");
                share.putExtra(Intent.EXTRA_TEXT, "Image is attached");
                share.setData(Uri.parse(Constants.EMAILID_FROM));
                share.setType("message/rfc822");
                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));

            } else {
                Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }
}
