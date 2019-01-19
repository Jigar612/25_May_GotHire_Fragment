package com.jigar.android.gothire;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.AdapterQuestionRound;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileCapture extends Fragment {

    int FILE_OPEN = 1000;
    String path = null;
    android.net.Uri selectedImageURI;

    int notify_id = 101;
    String real_path;

    String path_conv;
    String ans_file, file_size, file_size_type=null, file_formatted_Type=null, jobname, companyname, jobdesc, accepted_dt, stage_name;
    String candidate_id, res_id, totalprocesstime, startdate;
    String roundid, jobid, Jobquesid, quest_masterid, exam_startid, processtime;
    // string filename;
    String type;
    String filename_gui;
    String final_ans;

    View view;
    Bundle args;
    int reply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_file_capture, container, false);

        MainContainer.TAG="FileQuestion";

        runTask();

        return view;

    }
    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            UUID uuid = UUID.randomUUID();
            filename_gui = uuid.toString().replace("-", "");

            Bundle get_data = getArguments();
            candidate_id =  get_data.getString("CandidateId");
            roundid =  get_data.getString("RoundID");
            jobid =  get_data.getString("JobId");
            Jobquesid =  get_data.getString("JobQuesID");
            quest_masterid = get_data.getString("Question_MasterID");
            exam_startid =  get_data.getString("Exam_StartID");
            res_id = get_data.getString("response_id");
            processtime =  get_data.getString("process_time");
            totalprocesstime =  get_data.getString("total_process");
            startdate =  get_data.getString("startdate");
            stage_name =  get_data.getString("key_round_name");
            jobdesc =  get_data.getString("Key_job_desc");
            jobname =  get_data.getString("Key_job_nm");
            companyname =  get_data.getString("Key_cpy_nm");
            accepted_dt =  get_data.getString("Key_interview_accepted_on");
            file_size =  get_data.getString("key_FileSize");
            file_size_type =  get_data.getString("key_filesize_type");
            file_formatted_Type =  get_data.getString("key_file_Formatted");


            try {
                Intent fileIntent = new Intent(Intent.ACTION_PICK);
                fileIntent.setType("image/*");
                fileIntent.setAction(Intent.ACTION_GET_CONTENT);
                fileIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(fileIntent, "Select File"), FILE_OPEN);

                if (file_size == "" || file_size == null) {
                    file_size = "2";
                }
                if (file_size_type == "" || file_size_type == null) {
                    file_size_type = "MB";
                }
            } catch (Exception e) {

            }
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
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
                if (data == null)
                {
                    Toast.makeText(getActivity(), " Please select file.", Toast.LENGTH_LONG).show();

                    //getActivity().getFragmentManager().popBackStack();
                    getActivity().getFragmentManager().beginTransaction().remove(this).commit();

                }
                if ((requestCode == FILE_OPEN) && (resultCode == getActivity().RESULT_OK) && (data != null))
                {
                    selectedImageURI = data.getData();


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        real_path = getRealPathFromURI_API19(getActivity(), data.getData());
                        if(real_path == null)
                        {
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
                    }
                    else if (Build.VERSION.SDK_INT < 19)
                        real_path = getRealPathFromURI_API11to18(getActivity(), data.getData());
                    try
                    {
                       type = MimeTypeMap.getFileExtensionFromUrl(real_path);
                        //*******************************
                        ans_file = "/answers/" + filename_gui +"."+type;
                        //ans_file = "/answers" + "/Test123";

                    }catch (Exception e)
                    {}
                    if (real_path == null || real_path=="")
                    {
                        Toast.makeText(getActivity(), "Please check file type", Toast.LENGTH_LONG).show();
                        //getActivity().getFragmentManager().popBackStack();
                        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                    }
                    else
                    {
                        File selected_file = new File(real_path);
                        long fileSizeInBytes=selected_file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = (((fileSizeInBytes / (1024 * 1024)) * 10) / 10);
                        long filesize = Long.parseLong(file_size); //geting from database
                        //***************************
                        //Chick if type is MB..
                        if (file_size_type.equals("MB"))
                        {
                            if (fileSizeInMB >= filesize )
                            {
                                Toast.makeText(getActivity(), "please select file below file size", Toast.LENGTH_LONG).show();

                                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
//                                args = new Bundle();
//                                args.putString("CandidateId",candidate_id);
//                                args.putString("RoundID",roundid);
//                                args.putString("JobId", jobid);
//                                args.putString("JobQuesID",Jobquesid);
//                                args.putString("Question_MasterID",quest_masterid);
//                                args.putString("process_time",processtime);
//                                args.putString("total_process",totalprocesstime);
//                                args.putString("response_id",res_id);
//                                args.putString("Exam_StartID", exam_startid);
//                                args.putString("startdate",startdate);
//                                args.putString("Key_job_nm", jobname);
//                                args.putString("key_round_name",stage_name);
//                                args.putString("key_FileSize", file_size);
//                                args.putString("key_filesize_type", file_size_type);
//                                args.putString("key_file_Formatted", file_formatted_Type);
//                                args.putString("Key_job_desc",jobdesc);// file format(.doc/.pdf)
//                                args.putString("Key_cpy_nm",companyname);
//                                loadFragment(new FileCapture());
                            }
                            else if (!file_formatted_Type.equals("")|| !file_formatted_Type.isEmpty()) {
                                try {

                                    if (file_formatted_Type.contains(type) || file_formatted_Type.equals(type))
                                    {
                                        UploadFileOnServer();
//                                        if (FTPReply.isPositiveCompletion(reply)) {
//                                            System.out.println("Connected Success");
//                                            Toast.makeText(getActivity(), "your selected file is uploaded", Toast.LENGTH_LONG).show();
//                                        }
//                                        else
//                                        {
//                                            Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
//                                        }
                                         final_ans = "MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ans_file + ",VideoData$" +",Rating$"+ ",Ranking$";
                                         load_submit_question();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Please check file type", Toast.LENGTH_LONG).show();
                                        //getActivity().getFragmentManager().popBackStack();
                                        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Please select file", Toast.LENGTH_LONG).show();
                                    //getActivity().getFragmentManager().popBackStack();
                                    getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                                }
                            }
                            else if (file_formatted_Type == "" || file_formatted_Type.isEmpty())
                            {
                                //type = path.substring(path.lastIndexOf("."));
                                try
                                {
                                    if (file_formatted_Type == ""|| file_formatted_Type.isEmpty())//if file_type null then selected file type is uplding...
                                    {
                                        file_formatted_Type = type;
                                    }
                                    if (file_formatted_Type.equals(type)) {
//
                                        UploadFileOnServer();
//                                        if (FTPReply.isPositiveCompletion(reply)) {
//                                            System.out.println("Connected Success");
//                                            Toast.makeText(getActivity(), "your selected file is uploaded", Toast.LENGTH_LONG).show();
//                                        }
//                                        else
//                                        {
//                                            Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
//                                        }
                                        final_ans = "MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ans_file + ",VideoData$" +",Rating$"+ ",Ranking$";
                                        load_submit_question();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Please check file type", Toast.LENGTH_LONG).show();
                                        //getActivity().getFragmentManager().popBackStack();
                                        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Please select file", Toast.LENGTH_LONG).show();
                                    //getActivity().getFragmentManager().popBackStack();
                                    getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                            }
                          }
                       }
                        if (file_size_type.equals("KB"))
                        {
                            if (fileSizeInKB >= filesize)
                            {
                                Toast.makeText(getActivity(), "please select file below file size", Toast.LENGTH_LONG).show();
                                //getActivity().getFragmentManager().popBackStack();
                                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                            }
                              else if (!file_formatted_Type.equals("") || !file_formatted_Type.isEmpty())
                                 {
                                try {
                                    //type = path.substring(path.lastIndexOf("."));
                                 //   type =path.substring(path.lastIndexOf("/")+1);
                                    if ( file_formatted_Type.contains(type) || file_formatted_Type.equals(type))
                                    {
                                        UploadFileOnServer();
                                       // SingleQuestion.btn_skip.callOnClick();
//                                        if (FTPReply.isPositiveCompletion(reply)) {
//                                            System.out.println("Connected Success");
//                                            Toast.makeText(getActivity(), "your selected file is uploaded", Toast.LENGTH_LONG).show();
//                                        }
//                                        else
//                                        {
//                                            Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
//                                        }
                                       // getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                                        //final_ans = "MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ans_file + ",VideoData$";
                                        final_ans = "MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ans_file + ",VideoData$" +",Rating$"+ ",Ranking$";
                                        load_submit_question();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Please check file type", Toast.LENGTH_LONG).show();
////                                        Intent intent_sing_que_adpt = new Intent(this, SingleQuestion.class);
                                        //getActivity().getFragmentManager().popBackStack();
                                        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
//                                        args = new Bundle();
//                                        args.putString("CandidateId",candidate_id);
//                                        args.putString("RoundID",roundid);
//                                        args.putString("JobId", jobid);
//                                        args.putString("JobQuesID",Jobquesid);
//                                        args.putString("Question_MasterID",quest_masterid);
//                                        args.putString("process_time",processtime);
//                                        args.putString("total_process",totalprocesstime);
//                                        args.putString("response_id",res_id);
//                                        args.putString("Exam_StartID", exam_startid);
//                                        args.putString("startdate",startdate);
//                                        args.putString("Key_job_nm", jobname);
//                                        args.putString("key_round_name",stage_name);
//                                        args.putString("key_FileSize", file_size);
//                                        args.putString("key_filesize_type", file_size_type);
//                                        args.putString("key_file_Formatted", file_formatted_Type);
//                                        args.putString("Key_job_desc",jobdesc);// file format(.doc/.pdf)
//                                        args.putString("Key_cpy_nm",companyname);
//                                        loadFragment(new FileCapture());
                                    }
                                }catch (Exception e)
                                {
                                    Toast.makeText(getActivity(), "please select file", Toast.LENGTH_LONG).show();
//                             //getActivity().getFragmentManager().popBackStack();
                                    getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                                    args = new Bundle();
                                    args.putString("CandidateId",candidate_id);
                                    args.putString("RoundID",roundid);
                                    args.putString("JobId", jobid);
                                    args.putString("JobQuesID",Jobquesid);
                                    args.putString("Question_MasterID",quest_masterid);
                                    args.putString("process_time",processtime);
                                    args.putString("total_process",totalprocesstime);
                                    args.putString("response_id",res_id);
                                    args.putString("Exam_StartID", exam_startid);
                                    args.putString("startdate",startdate);
                                    args.putString("Key_job_nm", jobname);
                                    args.putString("key_round_name",stage_name);
                                    args.putString("key_FileSize", file_size);
                                    args.putString("key_filesize_type", file_size_type);
                                    args.putString("key_file_Formatted", file_formatted_Type);
                                    args.putString("Key_job_desc",jobdesc);// file format(.doc/.pdf)
                                    args.putString("Key_cpy_nm",companyname);
                                    loadFragment(new FileCapture());

                                }
                            }
                            else if (file_formatted_Type == "" || file_formatted_Type.isEmpty())
                            {
                                try
                                {
                                   // type = path.substring(path.lastIndexOf("."));
                                    if (file_formatted_Type == "" || file_formatted_Type.isEmpty())
                                    {
                                        file_formatted_Type = type;
                                    }
                                    if (file_formatted_Type.equals(type))
                                    {
                                        UploadFileOnServer();
//                                        if (FTPReply.isPositiveCompletion(reply)) {
//                                            System.out.println("Connected Success");
//                                            Toast.makeText(getActivity(), "your selected file is uploaded", Toast.LENGTH_LONG).show();
//                                        }
//                                        else
//                                        {
//                                            Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
//                                        }
                                        //getActivity().getFragmentManager().popBackStack();
                                        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                                      //  SingleQuestion.btn_skip.callOnClick();
                                            Toast.makeText(getActivity(), "your selected file is uploaded", Toast.LENGTH_LONG).show();
                                            final_ans = "MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ans_file + ",VideoData$" +",Rating$"+ ",Ranking$";
                                            load_submit_question();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Please check file type", Toast.LENGTH_LONG).show();
                                        getActivity().getFragmentManager().beginTransaction().remove(FileCapture.this).commit();
                                    }
                                }catch (Exception e)
                                {
                                    Toast.makeText(getContext(), "please select file", Toast.LENGTH_LONG).show();
                                    getActivity().getFragmentManager().beginTransaction().remove(FileCapture.this).commit();
                                }
                            }
                        }
                    }
                }
   }
    public void load_submit_question()
    {
       // String submit_single_que="http://34.214.133.27:83/Default/SubmitSinelQuestion";
        String submit_single_que=" http://35.162.89.140:83/Default/SubmitSinelQuestion";
        // progressDialog.show();
        String url_singleQuestion= submit_single_que.toString();// + candidate_id1 +"/"+jobRound_id +"/"+jobPost_id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_singleQuestion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Result", response.toString());
                        String result = response.toString();
                        try {
                            if (result.equals("\"Success\""))
                            {
                                    //  getActivity().finish();
                                    //  getActivity().getFragmentManager().popBackStack();
                                    getActivity().getFragmentManager().beginTransaction().remove(FileCapture.this).commit();
                                    SingleQuestion.btn_skip.callOnClick();
                                    Toast.makeText(getActivity(), "Answer submitted successfully", Toast.LENGTH_LONG).show();

                                   // SingleQuestion.mediaController.setVisibility(View.VISIBLE);
                                    SingleQuestion.video_question.setVisibility(View.VISIBLE);
                            }
                            else  if (result.equals("\"Fail\"") )
                            {
                                Toast.makeText(getActivity(), "Try Again:", Toast.LENGTH_LONG).show();
                            }
                            else if(result.equals("\"Error\"") )
                            {
                                Toast.makeText(getActivity(),"Try Again and please select proper", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Somthing wrong", Toast.LENGTH_LONG).show();
                            }
                            //  progressDialog.dismiss();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                // progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("CandidateID",candidate_id);
                params.put("RoundID",roundid);
                params.put("JobId",jobid);
                params.put("QuestionID", Jobquesid);
                params.put("MsaterQuestionID", quest_masterid);
                params.put("ExamstartId", exam_startid);
                params.put("OutputString",final_ans);

                return params;
            }

        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(stringRequest);
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
        builder.setSmallIcon(R.drawable.ic_gh_logo_new);

        notify_id++;

//        final String user = "anonymous";
//        final String pass = "";

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
                    reply = ftp.getReplyCode();
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
//
//                    //String aResponse = msg.getData().getString("connection_code");
//                    int response_connection_code = msg.getData().getInt("connection_code");
//
//
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
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }


}
