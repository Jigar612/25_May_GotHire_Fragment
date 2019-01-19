package com.jigar.android.gothire;

import android.*;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jigar.android.gothire.Adapter.AdapterHomeAdapter;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ProfileSetup extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String webservice_url = UrlString.URL + "GetCandidate/";
    EditText ed_location;
    EditText ed_firts_nm,ed_last_nm,ed_email_add,ed_phone;
    Button btn_nextStep,btn_back;
    CircleImageView upload_image;
    ProgressDialog progressDialog;
    private static final int CAMERA_PIC_REQUEST = 2500;
    private static final int GALERY_PIC_REQUEST=2501;
    int FILE_OPEN = 1000;
    Bitmap bitmap_camera_gallery;
    LinearLayout linaer_main;

//    EditText edit_location;

    //private FusedLocationProviderClient client;

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String Candidate_id;
    //****

    android.net.Uri selectedImageURI;
    int notify_id = 101;
    String real_path;
    String type;
    String filename_gui;
    String ans_file;
    String ans_file_webservices;
    String profile_path="";
   // View view;
    Bundle args;
    TextView upload_resume;
    int reply;//server conneted or not check
    int reply_resume;////server conneted or not check

    String str_education="null",str_school="null",str_job_title="null",str_company_nm="null",str_skills="null";

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//       // finish();
//        //Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
//    }
    @Override
    public void onBackPressed() {
       // Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        MainContainer.TAG="Profile";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


      //  edit_location = (EditText)findViewById(R.id.autocomplet_place);
       // autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autocomplet_place);
        //autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_places));
        //autoCompleteTextView.setOnItemClickListener(this);


//        final GooglePlacesAutocompleteAdapter adapter;
//        adapter = new GooglePlacesAutocompleteAdapter(this,R.layout.list_item_places);
//
//           ListView listView = (ListView) findViewById(R.id.listView1);
//            // Assign adapter to ListView
//           listView.setAdapter(adapter);
//
//            //enables filtering for the contents of the given ListView
//            listView.setTextFilterEnabled(true);
//
//        edit_location.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//              //  autoCompleteTextView.getFilter().filter(s.toString());
//                adapter.getFilter().filter(s.toString());
//            }
//        });

//        view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN){
//                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//                return true;
//            }
//        });

        upload_image=(CircleImageView)findViewById(R.id.img_user_profile);

        ed_location = (EditText) findViewById(R.id.ed_location_profile);
        ed_firts_nm = (EditText) findViewById(R.id.ed_first_name_profile);
        ed_last_nm = (EditText)findViewById(R.id.ed_last_name_profile);
        ed_email_add = (EditText) findViewById(R.id.ed_email_name_profile);
        ed_phone = (EditText) findViewById(R.id.ed_Phone_profile);
       // btn_back=(Button)findViewById(R.id.btn_back_profile_setup);
        btn_nextStep=(Button)findViewById(R.id.btn_next_profile);
        upload_resume = (TextView)findViewById(R.id.tv_upload_resume);
        TextView tv_basik_info = (TextView)findViewById(R.id.tv_basic_info);
        linaer_main = (LinearLayout)findViewById(R.id.linear_main);





        Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");
        Typeface roboto_Med = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Bold.ttf");

        // set TypeFace to the TextView or Edittext
        upload_resume.setTypeface(roboto_bold);
        tv_basik_info.setTypeface(roboto_Med);
        ed_firts_nm.setTypeface(roboto_Light);
        ed_last_nm.setTypeface(roboto_Light);
        ed_email_add.setTypeface(roboto_Light);
        ed_location.setTypeface(roboto_Light);
        ed_email_add.setTypeface(roboto_Light);
        ed_phone.setTypeface(roboto_Light);
      //  btn_back.setTypeface(roboto_Light);
        btn_nextStep.setTypeface(roboto_Light);






        linaer_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linaer_main.getWindowToken(), 0);

            }
        });

         Intent intent = getIntent();
         Candidate_id = intent.getStringExtra("Key_candidate_id");
//        String CandId=intent.getStringExtra("Key_candidate_id");
//        Candidate_id="18";
//        Bundle get_data = getArguments();
//        Candidate_id = get_data.getString("Key_candidate_id");

//        str_first_nm = get_data.getStringExtra("Key_first_nm");
//        str_last_nm = get_data.getStringExtra("Key_last_nm");
//        str_email_add =get_data.getStringExtra("Key_email_add");
//        str_location = get_data.getStringExtra("Key_location");
//        str_phone = get_data.getStringExtra("Key_phone");
        //******************

        //Getting Data from ProfileSetup1.java
        str_education =intent.getStringExtra("Key_education");
        str_school = intent.getStringExtra("Key_school");
        str_job_title =intent.getStringExtra("Key_job_title");
        str_company_nm =intent.getStringExtra("Key_companyNm");
        str_skills = intent.getStringExtra("Key_skills");
        profile_path= intent.getStringExtra ("Key_profile_path");






        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        checkAndRequestPermissions();
        runTask();

      //  return view;
    }
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {

        String str = (String) adapterView.getItemAtPosition(position);

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    //    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        getActivity().setRequestedOrientation(
//                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        progressDialog.dismiss();
//    }
    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            UUID uuid = UUID.randomUUID();
            filename_gui = uuid.toString().replace("-", "");

            call_profileGetData_webservices();


            upload_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Open_Camera_Gallery_image();
                }
            });

            upload_resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fileIntent = new Intent(Intent.ACTION_PICK);
                    fileIntent.setType("file/*");
                    fileIntent.setAction(Intent.ACTION_GET_CONTENT);
                    fileIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(fileIntent, "Select File"), FILE_OPEN);

                }
            });

//            btn_back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   //getFragmentManager().beginTransaction().remove(ProfileSetup.this).commit();
//                    finish();

//                }
//            });
            btn_nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_profile_setup1 = new Intent(ProfileSetup.this, ProfileSetup1.class);
                    intent_profile_setup1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  //  intent_profile_setup1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    //ProfileSetup data send to ProfileSetup1
                    String Fnn= ed_firts_nm.getText().toString();
                    String Lnm= ed_last_nm.getText().toString();
                    String email= ed_email_add.getText().toString();
                    String location= ed_location.getText().toString();
                    String phone= ed_phone.getText().toString();


                    if(Fnn == null || Fnn.isEmpty() || Fnn.equals("null") || Fnn.equals(""))
                    {
                        Toast.makeText(ProfileSetup.this, "Please enter first name", Toast.LENGTH_LONG).show();

                    }
                    else  if(Lnm== null || Lnm.isEmpty() || Lnm.equals("null") || Lnm.equals(""))
                    {
                        Toast.makeText(ProfileSetup.this, "Please enter last name", Toast.LENGTH_LONG).show();
                    }
                    else  if(email == null || email.isEmpty() || email.equals("null")|| email.equals(""))
                    { Toast.makeText(ProfileSetup.this, "Please enter email address", Toast.LENGTH_LONG).show();

                    }
                    else  if(location == null || location.isEmpty() || location.equals("null") || location.equals(""))
                    {
                        Toast.makeText(ProfileSetup.this, "Please enter location", Toast.LENGTH_LONG).show();
                    }
                    else  if(phone == null || phone.isEmpty() || phone.equals("null") || phone.equals(""))
                    {
                        Toast.makeText(ProfileSetup.this, "Please enter phone number", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        intent_profile_setup1.putExtra("Key_candidate_id",Candidate_id);
                        intent_profile_setup1.putExtra("Key_first_nm",Fnn);
                        intent_profile_setup1.putExtra("Key_last_nm",Lnm);
                        intent_profile_setup1.putExtra("Key_email_add",email);
                        intent_profile_setup1.putExtra("Key_location",location);
                        intent_profile_setup1.putExtra("Key_phone",phone);
                        intent_profile_setup1.putExtra("Key_profile_path",ans_file_webservices);


                        intent_profile_setup1.putExtra("Key_education",str_education);
                        intent_profile_setup1.putExtra("Key_school",str_school);
                        intent_profile_setup1.putExtra("Key_job_title",str_job_title);
                        intent_profile_setup1.putExtra("Key_companyNm",str_company_nm);
                        intent_profile_setup1.putExtra("Key_skills",str_skills);


                        startActivity(intent_profile_setup1);
                    }




//                    String Fnn= ed_firts_nm.getText().toString();
//                    String Lnm= ed_last_nm.getText().toString();
//                    String email= ed_email_add.getText().toString();
//                    String location= ed_location.getText().toString();
//                    String phone= ed_phone.getText().toString();
//
//                    args = new Bundle();
//                    args.putString("Key_candidate_id",Candidate_id);
//                    args.putString("Key_first_nm",Fnn);
//                    args.putString("Key_last_nm",Lnm);
//                    args.putString("Key_email_add",email);
//                    args.putString("Key_location",location);
//                    args.putString("Key_phone",phone);
//                    args.putString ("Key_profile_path",ans_file_webservices);
//
//                    ProfileSetup1 profileSetup1 = new ProfileSetup1();
//                    FragmentManager fm_profile =  getFragmentManager();
//                    FragmentTransaction fragmentTransaction_profile = fm_profile.beginTransaction();
//                    profileSetup1.setArguments(args);
//                    fragmentTransaction_profile.addToBackStack(null);
//                    fragmentTransaction_profile.add(R.id.frameLayout, profileSetup1);
//                    fragmentTransaction_profile.commit(); // save the ch
                }
            });
            ed_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  ed_location.setFocusableInTouchMode(true);
                    try {
                        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                                .build();
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .setFilter(autocompleteFilter)
                                .build(ProfileSetup.this);
                        startActivityForResult(intent, 1);

                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            });


            ed_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                      //    ed_location.clearFocus();
                    }else{
                        try {
                            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                                    .build();
                            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setFilter(autocompleteFilter)
                                    .build(ProfileSetup.this);
                            startActivityForResult(intent, 1);

                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }

                        //  Toast.makeText(this, "Get Focus", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetup.this);
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

    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(ProfileSetup.this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(ProfileSetup.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int capt_audio = ContextCompat.checkSelfPermission(ProfileSetup.this, android.Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        int read_extrnal = ContextCompat.checkSelfPermission(ProfileSetup.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int internet = ContextCompat.checkSelfPermission(ProfileSetup.this, android.Manifest.permission.INTERNET);

        int mic = ContextCompat.checkSelfPermission(ProfileSetup.this, android.Manifest.permission.RECORD_AUDIO);
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

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(ProfileSetup.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void Open_Camera_Gallery_image()
    {

        final CharSequence[] items = { "Camera", "Galery",
                "Cancel" };
        AlertDialog.Builder builder_camera = new AlertDialog.Builder(ProfileSetup.this);
        builder_camera.setTitle("Add Photo!");
        builder_camera.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (items[item].equals("Camera")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
                    }
                }
                else if (items[item].equals("Galery")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select file to upload "), GALERY_PIC_REQUEST);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder_camera.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Bitmap bitmap = null;
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {

            bitmap_camera_gallery = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap_camera_gallery.compress(Bitmap.CompressFormat.JPEG, 100, stream1);

            byte[] byteArray = stream1.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
           // Bitmap resizedBmp = Bitmap.createScaledBitmap(bitmap, upload_image.getWidth(), upload_image.getHeight(), false);
            upload_image.setImageBitmap(bitmap);

            selectedImageURI = getImageUri(getApplicationContext(), bitmap_camera_gallery);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(selectedImageURI));

            real_path=finalFile.getAbsolutePath();
                try
                {
                    type = MimeTypeMap.getFileExtensionFromUrl(real_path);
                 //   ans_file = "/answers/" + filename_gui +"."+type;
                    ans_file = "/CandidateImage/" + filename_gui +"."+type;
                    ans_file_webservices=filename_gui+"."+type;

                }catch (Exception e)
                {}
                if (real_path == null || real_path=="")
                {
                    Toast.makeText(ProfileSetup.this, "please select proper file, from file manager", Toast.LENGTH_LONG).show();
                   // getFragmentManager().beginTransaction().remove(this).commit();
                }
                else
                {
                    UploadFileOnServer();//Upload to server using ftp..
                    Toast.makeText(ProfileSetup.this, "your profile photo is updated", Toast.LENGTH_LONG).show();
                }
           // }
            //**************************
        }
        //For Gallery.
        Bitmap image = null;
        if (requestCode == GALERY_PIC_REQUEST && resultCode == RESULT_OK) {

            Uri pickedImage = data.getData();
            if (data != null) {
                try {
                    bitmap_camera_gallery = MediaStore.Images.Media.getBitmap(getContentResolver(), pickedImage);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap_camera_gallery.compress(Bitmap.CompressFormat.JPEG,90, stream1);
                    //arrayList_btimap.add(bitmap_gallery);
                    byte[] byteArray = stream1.toByteArray();
                    image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    //Bitmap resizedBmp = Bitmap.createScaledBitmap(image, 150, 150, false);
                    upload_image.setImageBitmap(image);




                        //Pick a image real path... upload to server using ftp..
                    selectedImageURI = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        real_path = getRealPathFromURI_API19(ProfileSetup.this, data.getData());
                        if(real_path == null)
                        {
                            String wholeID = DocumentsContract.getDocumentId(selectedImageURI);
                            //    // Split at colon, use second item in the array
                            String id = wholeID.split(":")[1];
                            String[] column = {MediaStore.Images.Media.DATA};
                            String sel = MediaStore.Images.Media._ID + "=?";
                            Cursor cursor =getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{id}, null);
                            int columnIndex = cursor.getColumnIndex(column[0]);
                            if (cursor.moveToFirst()) {
                                real_path = cursor.getString(columnIndex);
                            }
                            cursor.close();
                        }
                        else if (Build.VERSION.SDK_INT < 19)
                            real_path = getRealPathFromURI_API11to18(ProfileSetup.this, data.getData());
                        try
                        {
                            type = MimeTypeMap.getFileExtensionFromUrl(real_path);
                           // ans_file = "/answers/" + filename_gui +"."+type;
                            ans_file = "/CandidateImage/" + filename_gui +"."+type;
                            ans_file_webservices=filename_gui+"."+type;

                        }catch (Exception e)
                        {}
                        if (real_path == null || real_path=="")
                        {
                            Toast.makeText(ProfileSetup.this, "please select proper file, from file manager", Toast.LENGTH_LONG).show();
                            //getFragmentManager().beginTransaction().remove(this).commit();
                        }
                        else
                        {
                            UploadFileOnServer(); //Upload to server using ftp..
//                            if (FTPReply.isPositiveCompletion(reply)) {
//                                System.out.println("Connected Success");
                                //Toast.makeText(ProfileSetup.this, "your profile photo is updated", Toast.LENGTH_LONG).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(ProfileSetup.this, "Server error", Toast.LENGTH_LONG).show();
//                            }
                        }
                    }
                    //**************************
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == FILE_OPEN && resultCode ==RESULT_OK) {
            selectedImageURI = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                real_path = getRealPathFromURI_API19(ProfileSetup.this, data.getData());
                if(real_path == null)
                {
                    String wholeID = DocumentsContract.getDocumentId(selectedImageURI);
                    //    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        real_path = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
                else if (Build.VERSION.SDK_INT < 19)
                    real_path = getRealPathFromURI_API11to18(ProfileSetup.this, data.getData());
                try
                {
                    type = MimeTypeMap.getFileExtensionFromUrl(real_path);
                    String upload_resume_ans = "/CandidateResume/" + filename_gui +"."+type;
                    String ans_upload_resume_webservices=filename_gui+"."+type;

                }catch (Exception e)
                {}
                if (real_path == null || real_path=="")
                {
                    Toast.makeText(ProfileSetup.this, "please select proper file, from file manager", Toast.LENGTH_LONG).show();
                //   getFragmentManager().beginTransaction().remove(ProfileSetup.this).commit();
                }
                else
                {
                    UploadResumeOnServer();
//                    if (FTPReply.isPositiveCompletion(reply_resume)) {
//                        System.out.println("Connected Success");
//                        Toast.makeText(ProfileSetup.this, "your resume is uploaded", Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(ProfileSetup.this, "Server error", Toast.LENGTH_LONG).show();
//                    }
                   // Toast.makeText(getActivity(), "your resume is uploaded", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (requestCode == 1)
        {
            try {
                Place mPlace = PlaceAutocomplete.getPlace(ProfileSetup.this, data);
//                ed_location.setText(mPlace.getAddress());
                String addresses= String.valueOf(mPlace.getAddress());
                int endIndex = addresses.lastIndexOf(",");
                if (endIndex != -1)
                {
                    String city_state = addresses.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                    ed_location.setText(city_state);
                }
                else
                {
                    ed_location.setText(addresses);
                }
                ed_location.setFocusable(false);
                ed_phone.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
    public void call_profileGetData_webservices()
    {
        progressDialog.show();
        String url_welcome = webservice_url.toString() +Candidate_id ;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetCandidateResult");

                    int count = 0;

                    String FirstName = null;
                    String LastName=null;
                    String Email=null;
                    String CityName=null;
                    String StateName=null;
                    String CountryName=null;
                    String Phone=null;
                    String ProfileImage=null;

                    while (count < resultJsonArr.length()) {
                        //    RowItemGetAcceptedJobResult rowItem_home = new RowItemGetAcceptedJobResult();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        //    String CandidateID=jObject.getString("CanFirstName");
                        FirstName = jObject.getString("CanFirstName");
                        LastName = jObject.getString("CanLastName");
                        Email = jObject.getString("Email");
                        CityName = jObject.getString("CityName");
                        StateName= jObject.getString("Statename");
                        CountryName = jObject.getString("Countryname");
                        Phone = jObject.getString("MobileNo");
                        ProfileImage = jObject.getString("ProfileImage");

                        count++;
                    }
                    ed_firts_nm.setText(FirstName);
                    ed_last_nm.setText(LastName);
                    ed_email_add.setText(Email);
                    String city_state = CityName + "," + StateName;
                    city_state = city_state.replace("\r\n", "");
                    ed_location.setText(city_state);
                    ed_phone.setText(Phone);
                    ans_file_webservices =ProfileImage;


                    if(ed_location.getText().toString().equals(",,") || ed_location.getText().toString().equals(","))
                    {
                        ed_location.setText("");
                    }

                    if(Phone.equals("null"))
                    {
                        ed_phone.setText("");
                    }

//                    if(ed_location.getText().toString()==",")
//                    {
//                        getPermission();
//                        client = LocationServices.getFusedLocationProviderClient(ProfileSetup.this);
//                        if (ActivityCompat.checkSelfPermission(ProfileSetup.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {//&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//
//                            return;
//                        }
//                        client.getLastLocation().addOnSuccessListener(ProfileSetup.this, new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(android.location.Location location) {
//                                if(location != null)
//                                {
//
//                                    // lattitude= location.getLatitude();
//                                    //  longitude =location.getLongitude();
//                                    ioc_function(location);
//                                }
//                                else
//                                {
//
//                                }
//
//                            }
//                        });
//                    }

                  //  profile_path = "/answers/f79ed8e175f3496cb121dfe84501b0f3.png";
                    if(!ans_file_webservices.equals("null") || ans_file_webservices.equals(null) )
                    {
                        String path=LiveLink.LinkLive2+"/CandidateImage/"+ans_file_webservices;
                      //  String path=LiveLink.LinkLive2+ans_file_webservices;
                        Picasso.with(ProfileSetup.this)
                                .load(path)
                                .error(R.drawable.ic_no_img_avilible)//if no image on server then show..
                                .into(upload_image);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileSetup.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(ProfileSetup.this);
        rQueue.add(jsonObjReq);
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
    public void UploadFileOnServer()
    {
       // final String user = "anonymous";
        //final String pass = "";

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

                    ftp.changeWorkingDirectory("/CandidateImage/");
                    ftp.enterLocalPassiveMode();
                    reply = ftp.getReplyCode();
                    System.out.println("Received Reply from FTP Connection:" + reply);
                    if (FTPReply.isPositiveCompletion(reply)) {
                        System.out.println("Connected Success");
                    }
                  //  threadMsg(reply);
                    FileInputStream in = new FileInputStream(directory);
                    boolean result = ftp.storeFile(filename,in);
                    ftp.logout();
                    ftp.disconnect();


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
//                        Toast.makeText(ProfileSetup.this, "your profile photo is updated", Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(ProfileSetup.this, "Server error", Toast.LENGTH_LONG).show();
//                    }
//                }
//            };

        }).start();
        Toast.makeText(ProfileSetup.this, "your profile photo is updated", Toast.LENGTH_SHORT).show();
    }

    public void UploadResumeOnServer()
    {

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
                    ftp.connect(InetAddress.getByName("35.162.89.140"));

                    ftp.login(user,pass);
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);

                    ftp.changeWorkingDirectory("/CandidateResume/");
                    ftp.enterLocalPassiveMode();
                    reply_resume = ftp.getReplyCode();
                    System.out.println("Received Reply from FTP Connection:" + reply_resume);
                    if (FTPReply.isPositiveCompletion(reply_resume)) {
                        System.out.println("Connected Success");

                    }
                  //  threadMsg(reply_resume);
                    FileInputStream in = new FileInputStream(directory);
                    boolean result = ftp.storeFile(filename,in);
                    ftp.logout();
                    ftp.disconnect();




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
//                        Toast.makeText(ProfileSetup.this, "your resume is uploaded", Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(ProfileSetup.this, "Server error", Toast.LENGTH_LONG).show();
//                    }
//                }
//            };
        }).start();
        Toast.makeText(ProfileSetup.this, "your resume is uploaded", Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }


    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";



    private static final String API_KEY = "AIzaSyBXJqf2W1LSmPseH3lQp0ChkQICrfNfV2A";

    public static ArrayList autocomplete(String input) {

        ArrayList resultList = null;



        HttpURLConnection conn = null;

        StringBuilder jsonResults = new StringBuilder();

        try {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);

            sb.append("?key=" + API_KEY);

            sb.append("&components=country:gr");

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));



            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder

            int read;

            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {

                jsonResults.append(buff, 0, read);

            }

        } catch (MalformedURLException e) {

            //Log.e(LOG_TAG, "Error processing Places API URL", e);

            return resultList;

        } catch (IOException e) {

           // Log.e(LOG_TAG, "Error connecting to Places API", e);

            return resultList;

        } finally {

            if (conn != null) {

                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {

                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));

                System.out.println("============================================================");

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

            }

        } catch (JSONException e) {

       //     Log.e(LOG_TAG, "Cannot process JSON results", e);

        }
        return resultList;

    }
//    private void getPermission()
//    {
//        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
//
//    }
//    public void ioc_function(Location location)
//    {
//        try {
//            double latitude =location.getLatitude();
//            double longitude = location.getLongitude();
//            Geocoder geocoder = new Geocoder(ProfileSetup.this);
//            List<Address>addresses = null;
//            addresses = geocoder.getFromLocation(latitude,longitude,1);
//
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            ed_location.setText(city+","+state);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
	                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
        }
            };
        return filter;
        }
    }
}
