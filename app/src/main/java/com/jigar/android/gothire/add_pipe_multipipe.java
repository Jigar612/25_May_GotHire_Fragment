package com.jigar.android.gothire;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class add_pipe_multipipe extends AppCompatActivity {

    ProgressDialog pDialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String add_pipe_url = "https://s3b.addpipe.com/upload";
    int FILE_OPEN = 1000;

    android.net.Uri selectedImageURI;
    String real_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pipe_multipipe);

        pDialog = new ProgressDialog(add_pipe_multipipe.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        checkAndRequestPermissions();

        Intent fileIntent = new Intent(Intent.ACTION_PICK);
        fileIntent.setType("image/*");
        fileIntent.setAction(Intent.ACTION_GET_CONTENT);
        fileIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(fileIntent, "Select File"), FILE_OPEN);




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  try{
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, " Please select file.", Toast.LENGTH_LONG).show();

            //getActivity().getFragmentManager().popBackStack();
        }
        if ((requestCode == FILE_OPEN) && (resultCode == this.RESULT_OK) && (data != null)) {
            selectedImageURI = data.getData();

          //  Uri videoUri = data.getData();
        //    String selectedPath = getPath(videoUri);

            // Actually upload the video to the server



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                real_path = getRealPathFromURI_API19(this, data.getData());
                if (real_path == null) {
                    String wholeID = DocumentsContract.getDocumentId(selectedImageURI);
                    //    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        real_path = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
            } else if (Build.VERSION.SDK_INT < 19)
                real_path = getRealPathFromURI_API11to18(this, data.getData());
            try {

            } catch (Exception e) {
            }
            if (real_path == null || real_path == "") {
                Toast.makeText(this, "Please check file type", Toast.LENGTH_LONG).show();


            } else {

                uploadVideo(real_path);

                // new add_pipe_video().execute(video_string);

            }
        }
    }
    public void uploadVideo(String VideoPath)
    {
        try
        {
            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder()
                    .permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy1);

            File file = new File(VideoPath);
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(add_pipe_url);

            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("FileInput", new FileBody(file));
            multipartEntity.addPart("accountHash", new StringBody("08da5daa79e634fe9d7d7b45577ff706"));
            multipartEntity.addPart("payload", new StringBody("Custome Ui"));
            multipartEntity.addPart("environmentId", new StringBody("1"));
            multipartEntity.addPart("httpReferer", new StringBody("1539254262160.mp4"));
            multipartEntity.addPart("mrt", new StringBody("1000"));
            multipartEntity.addPart("audioOnly", new StringBody("0"));
            post.setEntity(multipartEntity);

            client.execute(post, new PhotoUploadResponseHandler());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private class PhotoUploadResponseHandler implements ResponseHandler<Object> {

        @Override
        public Object handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {

            HttpEntity r_entity = response.getEntity();
            String responseString = EntityUtils.toString(r_entity);
            Log.d("UPLOAD", responseString);
            Toast.makeText(add_pipe_multipipe.this, "Sucess"+responseString, Toast.LENGTH_SHORT).show();
            return null;
        }

    }


    @SuppressLint("NewApi")
    public String getRealPathFromURI_API19(Context context, Uri uri){
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }

            // TODO handle non-primary volumes
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    //if  api 11 to 18 then
    public  String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int capt_audio = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        int read_extrnal = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int internet = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.INTERNET);

        int location_coa = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int location_fine = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        int mic = ContextCompat.checkSelfPermission(add_pipe_multipipe.this, android.Manifest.permission.RECORD_AUDIO);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (mic != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (capt_audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read_extrnal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (location_fine != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location_coa != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(add_pipe_multipipe.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
