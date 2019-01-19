package com.jigar.android.gothire;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.SetterGetter.RowItem_InterviewInvitation;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OfferWithDocument extends Fragment {
    String webservice_url = UrlString.URL + "GetViewJob_Invitation/";

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********
    String Candidate_id;
  //  BottomNavigationView bottomNavigationView;
    Button btn_accetp,btn_decline;
    Button btn_download,btn_upload;
    int notify_id = 101;
    int FILE_OPEN = 1000;
    String ans_file;
    android.net.Uri selectedImageURI;
    String real_path;
    String type;
    String filename_gui;

    String OfferId;
    String JobPostId;
    String StatusId;//1-Accept & 2- decline....
    String IsAcceptedByCandidate;
    String downloadPath;
    View view;
    Bundle args;
    long timeDiffrance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_offer_with_document, container, false);

        MainContainer.TAG="OfferWithDocument";
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");
        downloadPath = get_data.getString("Key_download_path"); //get from finalStageInterview
        OfferId =get_data.getString("Key_offer_id");
        JobPostId = get_data.getString("Key_jobPost_id");
        String OfferTimeDiff = get_data.getString("Key_OfferTimeDiff");

        btn_accetp=(Button)view.findViewById(R.id.btn_accept_offer_with);
        btn_decline=(Button)view.findViewById(R.id.btn_deckline_offer_with);
        btn_download=(Button)view.findViewById(R.id.btn_file_download_offer_with);
        btn_upload=(Button)view.findViewById(R.id.btn_file_upload_offer_with);

        //Regular
        TextView tv_accepted_period_offer_with=(TextView)view.findViewById(R.id.tv_accepted_period_offer_with);
        TextView acceptance_period=(TextView)view.findViewById(R.id.tv_remain_acceptance_period_offer_with);
        TextView tv_disp_msg_download_offer_with=(TextView)view.findViewById(R.id.tv_disp_msg_download_offer_with);
        TextView tv_disp_msg_upload_offer_with=(TextView)view.findViewById(R.id.tv_disp_msg_upload_offer_with);

        //Bold
      //  TextView tv_congratulate_msg1_offer_with=(TextView)view.findViewById(R.id.tv_congratulate_msg1_offer_with);
        TextView tv_congratulate_msg2_offer_with=(TextView)view.findViewById(R.id.tv_congratulate_msg2_offer_with);

        //Light
        TextView tv_disp_msg_offer_with=(TextView)view.findViewById(R.id.tv_disp_msg_offer_with);
        TextView tv_disp_note=(TextView)view.findViewById(R.id.tv_disp_note);
        TextView tv_disp_msg_signed_document_offer_with=(TextView)view.findViewById(R.id.tv_disp_msg_signed_document_offer_with);

        Typeface roboto_Bold = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Bold.ttf");
        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");

        tv_accepted_period_offer_with.setTypeface(roboto_Reg);
        acceptance_period.setTypeface(roboto_Reg);
        tv_disp_msg_download_offer_with.setTypeface(roboto_Reg);
        tv_disp_msg_upload_offer_with.setTypeface(roboto_Reg);

        tv_disp_msg_offer_with.setTypeface(roboto_Light);
        tv_disp_note.setTypeface(roboto_Light);
        tv_disp_msg_signed_document_offer_with.setTypeface(roboto_Light);
        btn_accetp.setTypeface(roboto_Light);
        btn_decline.setTypeface(roboto_Light);

    //    tv_congratulate_msg1_offer_with.setTypeface(roboto_Bold);
        tv_congratulate_msg2_offer_with.setTypeface(roboto_Bold);


        // bottomMenu();
        if(!OfferTimeDiff.equals(null))
        {
            timeDiffrance=Long.parseLong(OfferTimeDiff);
            double second = timeDiffrance/ 1000.0;//convert to second
            int day = (int)TimeUnit.SECONDS.toDays((long) second);
            long hours = TimeUnit.SECONDS.toHours((long)second) - (day *24);
            acceptance_period.setText(String.format("%02d Days - %02d Hr",day,hours ));//set Text in remaining period
        }
        //String ans = (String.format("%02d:%02d",TimeUnit.MILLISECONDS.toHours(timeDiffrance),TimeUnit.MILLISECONDS.toMinutes(timeDiffrance) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiffrance))));


        runTask();
        return view;
    }
    public void runTask()
    {
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED)
        {
            UUID uuid = UUID.randomUUID();
            filename_gui = uuid.toString().replace("-", "");
//            listview_invitation = (ListView)findViewById(R.id.listView_interview_invitation);
//            tv_empty = (TextView)findViewById(R.id.tv_empty_list);
//            arrayList_interv_invitation = new ArrayList<RowItem_InterviewInvitation>();


            //load_webservices_offertWithDocument();
            btn_accetp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatusId="1";
                    Load_SubmitOffer_Webservies();
                }
            });
            btn_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatusId="2";
                    Load_SubmitOffer_Webservies();
                }
            });
            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                    //New download code....
                    //http://gothire.com/
                    if(downloadPath != null && !downloadPath.isEmpty() && !downloadPath.equals("null") && downloadPath.equals(""))
                  //  if (!downloadPath.equals("") || !downloadPath.equals(null) || !downloadPath.isEmpty() )
                    {
                        DownloadManager downloadManager;
                        downloadManager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        // downloadPath ="/OfferDocument/D.Shah Employment App.pdf";
                        downloadPath= downloadPath.replace(" ","%20");//If spaces in file then remove and put %20...
                        Uri uri=Uri.parse("http://gothire.com"+downloadPath);

                        // Uri uri=Uri.parse("http://gothire.com/OfferDocument/D.ShahZ Employment App.pdf");

                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        Long refrence = downloadManager.enqueue(request);
                        Toast.makeText(getActivity(), "Your document is Downloading...", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No document.", Toast.LENGTH_LONG).show();
                    }

                }
            });
            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent fileIntent = new Intent(Intent.ACTION_PICK);
                    fileIntent.setType("image/*");
                    fileIntent.setAction(Intent.ACTION_GET_CONTENT);
                    fileIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(fileIntent, "Select File"), FILE_OPEN);

                    Toast.makeText(getActivity(), "your selected file is uploaded", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.no_connection);
            builder.setIcon(R.mipmap.ic_internetconnection);
            //builder.setMessage("Sorry there was an error getting data from the Internet.\nNetwork Unavailable!");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    runTask();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  try{
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(getActivity(), " Please select file.", Toast.LENGTH_LONG).show();

            getActivity().finish();
        }
        if ((requestCode == FILE_OPEN) && (resultCode == getActivity().RESULT_OK) && (data != null)) {
            selectedImageURI = data.getData();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                real_path = getRealPathFromURI_API19(getActivity(), data.getData());
                if (real_path == null) {
                    String wholeID = DocumentsContract.getDocumentId(selectedImageURI);
                    //    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        real_path = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
            } else if (Build.VERSION.SDK_INT < 19)
                real_path = getRealPathFromURI_API11to18(getActivity(), data.getData());
            try {

                type = MimeTypeMap.getFileExtensionFromUrl(real_path);
                //*******************************
                ans_file = "/answers/" + filename_gui + "." + type;


            } catch (Exception e) {
            }
            if (real_path == null || real_path == "") {
                Toast.makeText(getActivity(), "please select proper file, from file manager", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            else
            {
                //Uploading code
                UploadFileOnServer();
                //Toast.makeText(this, "your selected file is uploaded", Toast.LENGTH_LONG).show();
            }
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
    public void UploadFileOnServer()
    {
        // ** Start ***
        //String file_type = path.substring(path.lastIndexOf("."));
        final NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setContentTitle("Uploading file");
        builder.setContentText("Uploading in progress...");
        builder.setSmallIcon(R.mipmap.ic_gh_logo);

        notify_id++;

        final String user = "FTP_GH";
        final String pass = "Test@123";


        final File directory = new File(real_path);
        // final String filename = directory.getName();
        final String filename =filename_gui +"."+type;


        new Thread(new Runnable() {
            public void run()
            {
                StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder()
                        .permitAll()
                        .build();
                StrictMode.setThreadPolicy(policy1);

                try
                {
                    FTPClient ftp = new FTPClient();
                    //ftp.connect(InetAddress.getByName("34.214.133.27"));// Change here
                    ftp.connect(InetAddress.getByName("35.162.89.140"));

                    ftp.login(user,pass);
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    // ftp.changeWorkingDirectory("/Gothire/answers/");// change here
                    ftp.changeWorkingDirectory("/answers/");
                    ftp.enterLocalPassiveMode();
                    int reply = ftp.getReplyCode();
                    System.out.println("Received Reply from FTP Connection:" + reply);
                    if (FTPReply.isPositiveCompletion(reply)) {
                        System.out.println("Connected Success");
                    }
                 //   threadMsg(reply);
                    FileInputStream in = new FileInputStream(directory);
                    //startNotification
                    builder.setProgress(0, 0, true);
                    notificationManager.notify(notify_id, builder.build());
                    //*******
                    boolean result = ftp.storeFile(filename,in);
                    ftp.logout();
                    ftp.disconnect();
                    //endNotification .....after uploding
                    builder.setContentText("Upload completed");
                    builder.setProgress(0, 0, false);
                    notificationManager.notify(notify_id, builder.build());
                    builder.setAutoCancel(true);
                    //************
                  //  Toast.makeText(getActivity(), "your file is uploaded", Toast.LENGTH_LONG).show();


                } catch (SocketException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
//            private void threadMsg(int msg) {
//
//                if (msg!=(0)) {
//                    Message msgObj = handler.obtainMessage();
//                    Bundle b = new Bundle();
//                    b.putInt("connection_code", msg);
//                    msgObj.setData(b);
//                    handler.sendMessage(msgObj);
//                }
//            }
//            private final Handler handler = new Handler() {
//
//                public void handleMessage(Message msg) {
//                    //String aResponse = msg.getData().getString("connection_code");
//                    int response_connection_code = msg.getData().getInt("connection_code");
//                    if (FTPReply.isPositiveCompletion(response_connection_code)) {
//                        System.out.println("Connected Success");
//                        Toast.makeText(getContext(), "your file is uploaded", Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_LONG).show();
//                    }
//                }
//            };
        }).start();
    }
    private static final String LOG_TAG = "Filedownload";
    //downloadPath
//    private Boolean downloadAndSaveFile(String server,
//                                        String user, String password, String filename, File localFile)
//            throws IOException {
//        FTPClient ftp_download = null;
//        try {
//            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll()
//                    .build();
//            StrictMode.setThreadPolicy(policy1);
//
//            ftp_download = new FTPClient();
//            ftp_download.connect(server);
//            Log.d(LOG_TAG, "Connected. Reply: " + ftp_download.getReplyString());
//            ftp_download.login(user, password);
//            Log.d(LOG_TAG, "Logged in");
//            ftp_download.setFileType(FTP.BINARY_FILE_TYPE);
//            Log.d(LOG_TAG, "Downloading");
//            ftp_download.enterLocalPassiveMode();
//
//            OutputStream outputStream = null;
//
//            boolean success = false;
//            try {
//                outputStream = new BufferedOutputStream(new FileOutputStream(
//                        localFile));
//                success = ftp_download.retrieveFile(filename, outputStream);
//            } finally {
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//            return success;
//        } finally {
//            if (ftp_download != null) {
//                ftp_download.logout();
//                ftp_download.disconnect();
//            }
//        }
//    }
    public void Load_SubmitOffer_Webservies()
    {
        String webservice_url = UrlString.URL + "SubmitOffer/";
        String url_interviewInvitation = webservice_url.toString() + StatusId+"/"+ OfferId +"/"+Candidate_id +"/"+JobPostId ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_interviewInvitation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("SubmitOfferResult");
                    int count=0;
                    int count_GetJobOffer=0;
                    while (count <resultJsonArr.length()) {
                        JSONObject jObject = resultJsonArr.getJSONObject(count);
                        String getJobOfferJson= jObject.getString("GetJobOffer");
                        if(getJobOfferJson!="null" ) {
                            JSONArray JsonArrGetJobOffer = jObject.getJSONArray("GetJobOffer");
                            while (count_GetJobOffer < JsonArrGetJobOffer.length()) {
                                JSONObject jObject_inviteForInterview = JsonArrGetJobOffer.getJSONObject(count_GetJobOffer);
                                IsAcceptedByCandidate = jObject_inviteForInterview.getString("IsAcceptedByCandidate");// null then accept decline vadi screen open
                                count_GetJobOffer++;
                            }
                        }
                        count++;
                    }
                    if(IsAcceptedByCandidate.equals("true") || IsAcceptedByCandidate.equals(true))
                    {
                        // FinalStageInterview.this.finish();
//                        Intent intent_HireContra = new Intent(getActivity(),OfferNoDocument.class);
//                        intent_HireContra.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_HireContra.putExtra("Key_candidate_id", Candidate_id);
//                        startActivity(intent_HireContra);

                        args = new Bundle();
                        args.putString("Key_candidate_id", Candidate_id);
                        loadFragment(new OfferNoDocument());
                    }
                    if(IsAcceptedByCandidate.equals("false") || IsAcceptedByCandidate.equals(false))
                    {
                        // FinalStageInterview.this.finish();
//                        Intent intent_rjectOffer = new Intent(getActivity(),RejectedOffer.class);
//                        intent_rjectOffer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_rjectOffer.putExtra("Key_candidate_id", Candidate_id);
//                        startActivity(intent_rjectOffer);
                        args = new Bundle();
                        args.putString("Key_candidate_id", Candidate_id);
                        loadFragment(new RejectedOffer());
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();

            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm =  getActivity().getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
