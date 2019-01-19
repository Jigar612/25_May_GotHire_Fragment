package com.jigar.android.gothire;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.jigar.android.gothire.Adapter.Adapter_ApplyForJob;

import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;
import com.nex3z.notificationbadge.NotificationBadge;

import org.ankit.gpslibrary.MyTracker;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class JobSearchFilter extends AppCompatActivity {

    SharedPreferences sharedpreferences_counter;
    public static final String myPref_counter = "mypref_counter";
    public static final String Counter_key = "counter_key";
    int new_notification_counter;
    NotificationBadge mBadge;

    String webservice_url = UrlString.URL + "GetAllHomeJob/";
    String webservice_AllIndustery_url = UrlString.URL + "GetAllIndusteryList";
    SeekBar seekBar_km, seekBar_salary;
    TextView tv_within_km, tv_salary;
    Spinner spinner_industry;
    //For used Seekbar km
    float ans_km = 0;//float
    float start_km = 0;//float
    float end_km = 100;//float
    float start_pos_km = 0;//float
    int start_position_km = 0;
    //**********
    //For used Seekbar salary
    float ans_salary = 0;//float
    float start_salary = 0;//float
    float end_salary = 100000;//float
    float start_pos_salary = 0;//float
    int start_position_salary = 0;

    //**********

    //New changes 24-sept
    int next_page = 1;
    String no_of_record = "10";
    public boolean isLoading = false;
    public View ftView;
    public android.os.Handler mHandler;
    int count = 0;

    // AutoCompleteTextView auto_industry;
    // AutoCompleteTextView ed_location;
    EditText ed_location;
    Button btn_full_time, btn_part_time, btn_contract;
    Button btn_volunteer, btn_intership;
    Button btn_done;
    ProgressDialog progressDialog;

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String Candidate_id;

    ListView listView_job_search;
    TextView tv_empty;

    ArrayList<RowItem_ApplyForJob> arrayList;
    ArrayList<RowItem_ApplyForJob> arrayList_search;
    //AdapterHomeAdapter adapter_home;
    Adapter_ApplyForJob adapter_applyForJob;

    SearchView searchView;
    String keyword_upper, keyword_lower, keyword_filst_lt_capital;

    String search_keyword;
    LinearLayout linear_main, linear_listview;
    LinearLayout linear_main_scrolling;

    boolean click_full_time = true;
    boolean click_part_time = true;
    boolean click_contrac = true;
    boolean click_volunteer = true;
    boolean click_intership = true;
    String full_time, part_time, contract;
    String volunteer, intership;

    ArrayList<String> arrayList_industry;
    //   ArrayList<String> arrayList_industry_id;
    ArrayAdapter<String> adapter_industry;
    // View view;
    Toolbar toolbar;
    Bundle args;


    private FusedLocationProviderClient client;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search_filter);

        MainContainer.TAG = "JobSearch";
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        int location_coa = ContextCompat.checkSelfPermission(JobSearchFilter.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int location_fine = ContextCompat.checkSelfPermission(JobSearchFilter.this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (location_fine != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location_coa != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(JobSearchFilter.this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);
        }

        getPermission();
         client = LocationServices.getFusedLocationProviderClient(JobSearchFilter.this);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

            return;
        }
        client.getLastLocation().addOnSuccessListener(JobSearchFilter.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if(location != null)
                {

                   // lattitude= location.getLatitude();
                  //  longitude =location.getLongitude();
                    ioc_function(location);
                }
                else
                {

                }

            }
        });

//        try {
//            if (ActivityCompat.checkSelfPermission(this, mPermission)!= PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this, new String[]{mPermission, Manifest.permission.READ_PHONE_STATE},
//                        REQUEST_CODE_PERMISSION);
//            }else{
//                //read location
//                getLocation();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        GPSTracker gps = new GPSTracker(JobSearchFilter.this);
//        if(gps.canGetLocation()){
//            String latitude = Double.toString(gps.getLatitude());
//            String longitude = Double.toString(gps.getLongitude());
//            // \n is for new line
//        }else{
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }

        TextView tv_job_location = (TextView) findViewById(R.id.tv_disp_location);
        TextView tv_salary_range = (TextView) findViewById(R.id.tv_salary_range);
        TextView tv_job_type = (TextView) findViewById(R.id.tv_job_type);
        TextView tv_industry = (TextView) findViewById(R.id.tv_industry);

        mHandler = new MyHandler();

        seekBar_km = (SeekBar) findViewById(R.id.seekbar_location_with_km_job_search); // initiate the Seek bar
        seekBar_salary = (SeekBar) findViewById(R.id.seekbar_salary_job_search); // initiate the Seek bar
        tv_within_km = (TextView) findViewById(R.id.tv_seekbar_km_job_search);
        tv_salary = (TextView) findViewById(R.id.tv_salary_job_search);

        spinner_industry = (Spinner) findViewById(R.id.spinner_industry_job_search);
        //   auto_industry=(AutoCompleteTextView) findViewById(R.id.spinner_industry_job_search);
        btn_full_time = (Button) findViewById(R.id.btn_full_time_job_search);
        btn_part_time = (Button) findViewById(R.id.btn_part_time_job_search);
        btn_contract = (Button) findViewById(R.id.btn_contract_job_search);
        btn_volunteer = (Button) findViewById(R.id.btn_volunteer_job_search);
        btn_intership = (Button) findViewById(R.id.btn_intership_job_search);

        btn_done = (Button) findViewById(R.id.btn_done_job_search);
        //  ed_location = (AutoCompleteTextView) findViewById(R.id.ed_location_job_search);
        ed_location = (EditText) findViewById(R.id.ed_location_job_search);

        listView_job_search = (ListView) findViewById(R.id.listview_job_search_filter);
        tv_empty = (TextView) findViewById(R.id.tv_empty_job_search);//tv_empty_list_home

        linear_main = (LinearLayout) findViewById(R.id.linear_job_search_main);
        linear_main_scrolling = (LinearLayout) findViewById(R.id.linear_for_scrolling);
        linear_listview = (LinearLayout) findViewById(R.id.linear_listview_job_search_filter);



        //Set Spinner Industry List Height*********
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_industry);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        //*************End ***************

//        locationManager = (LocationManager) JobSearchFilter.this.getSystemService(LOCATION_SERVICE);
//
//        locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                // Called when a new location is found by the network location provider.
//                Log.d("Location Jigar", location.toString());
//                ed_location.setText(location.toString());
//                //ioc_function(location);
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            public void onProviderEnabled(String provider) {
//            }
//
//            public void onProviderDisabled(String provider) {
//            }
//        };
//
//        if (Build.VERSION.SDK_INT <23)
//        {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        }
//        else
//        {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
//
//
//
//            }else{
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            }
//
//        }
      //  Criteria criteria=new Criteria();
        //location manager will take the best location from the criteria
//        locationManager.getBestProvider(criteria, true);
//        Location location=locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria,true));

      //  double aaa =location.getLatitude();

// Register the listener with the Location Manager to receive location updates

        //   onLocationChanged(location);
        // ioc_function(location);


        Typeface roboto_Med = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Reg = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");

        tv_job_location.setTypeface(roboto_Med);
        tv_salary_range.setTypeface(roboto_Med);
        tv_industry.setTypeface(roboto_Med);
        tv_job_type.setTypeface(roboto_Med);
        tv_empty.setTypeface(roboto_Light);
        ed_location.setTypeface(roboto_Reg);

        btn_full_time.setTypeface(roboto_Reg);
        btn_part_time.setTypeface(roboto_Reg);
        btn_contract.setTypeface(roboto_Reg);
        btn_volunteer.setTypeface(roboto_Reg);
        btn_intership.setTypeface(roboto_Reg);
        tv_within_km.setTypeface(roboto_Reg);
        tv_salary.setTypeface(roboto_Reg);
        btn_done.setTypeface(roboto_Light);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        arrayList = new ArrayList<RowItem_ApplyForJob>();
        arrayList_search = new ArrayList<RowItem_ApplyForJob>();

        //For Industry.
        //  arrayList_industry_id=new ArrayList<>();
        arrayList_industry = new ArrayList<>();

        Intent get_intent = getIntent();
        Candidate_id = get_intent.getStringExtra("Key_candidate_id");
        search_keyword = get_intent.getStringExtra("Key_Search_keyword");
//        Bundle get_data = getArguments();
//        Candidate_id = get_data.getString("Key_candidate_id");
//        search_keyword = get_data.getString("Key_Search_keyword");

        // this.setTitle("");
//        arrayList_search_job =get_intent.getStringArrayListExtra("List_arralistSearchJobNm");

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_home_listview, null);

        listView_job_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(JobSearchFilter.this, " Click on list item" + view.getTag(), Toast.LENGTH_LONG).show();
            }
        });
        listView_job_search.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Check when last items of listview with scrollview
                if (view.getLastVisiblePosition() == totalItemCount - 1 && listView_job_search.getCount() >= 10 && isLoading == false) {
                    isLoading = true;
                    Thread thread = new ThreadGetMoreData();
                    thread.start();
                }

            }
        });


        runTask();
        //  return view;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            }
//        }
//    }



    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);

//
//            ed_location.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ed_location.setFocusableInTouchMode(true);
//                }
//            });
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
                                .build(JobSearchFilter.this);
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
                        // ed_location.clearFocus();

                    } else {
                        try {
                            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                                    .build();
                            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setFilter(autocompleteFilter)
                                    .build(JobSearchFilter.this);
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
            //For Industry...
            call_allIndustries_webservice();
            adapter_industry = new ArrayAdapter<String>(JobSearchFilter.this,
                    R.layout.spinner_text, arrayList_industry);
            adapter_industry.setDropDownViewResource(R.layout.spinner_text);
            //**End industry*******

            //For km Seekbar*************
            start_km = 0;      //you need to give starting value of SeekBar
            end_km = 100;         //you need to give end value of SeekBar
            start_pos_km = 0;    //you need to give starting position value of SeekBar
            start_position_km = (int) (((start_pos_km - start_km) / (end_km - start_km)));//casting with (int)
            ans_km = start_pos_km;
            seekBar_km.setProgress(start_position_km);

            seekBar_km.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub
                    // To convert it as discrete value
                    float temp = progress;//float
                    float dis = end_km - start_km;//float
                    ans_km = (start_km + ((temp / 100) * dis));
                    //    Toast.makeText(getBaseContext(), "discrete = "+String.valueOf(ans_km), Toast.LENGTH_SHORT).show();
                    tv_within_km.setText("within " + String.valueOf((int) ans_km) + " km");
                }
            });
            //**End*seekbar*km**************************
            //For salary Seekbar*************
            start_salary = 0;      //you need to give starting value of SeekBar
            end_salary = 200000;         //you need to give end value of SeekBar
            start_pos_salary = 0;    //you need to give starting position value of SeekBar
            start_position_salary = (int) (((start_pos_salary - start_salary) / (end_km - start_salary)));//casting with (int)
            ans_salary = start_pos_salary;
            seekBar_salary.setProgress(start_position_salary);
            seekBar_salary.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub
                    // To convert it as discrete value
                    float temp = progress;//float
                    float dis = end_salary - start_salary;//float
                    ans_salary = (start_salary + ((temp / 100) * dis));

                    DecimalFormat formatter = new DecimalFormat("##,##,###");
                    String yourFormattedString = formatter.format(ans_salary);

                    //    Toast.makeText(getBaseContext(), "discrete = "+String.valueOf(ans_salary), Toast.LENGTH_SHORT).show();
                    //tv_salary.setText("min $" + String.valueOf((int) ans_salary));
                    tv_salary.setText("min $" + String.valueOf(yourFormattedString));
                }
            });
            //***seekbar*Salary**************************

            btn_full_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (click_full_time) {
                        //   btn_full_time.setBackgroundResource(R.color.my_sky_dark);
                        btn_full_time.setSelected(true);
                        btn_full_time.setBackgroundResource(R.drawable.button_sky_selected_effect);

                        click_full_time = false;
                        full_time = "Full Time";
                    } else {
                        //btn_full_time.setBackgroundResource(R.color.back_darkLight);
                        btn_full_time.setSelected(false);
                        btn_full_time.setBackgroundResource(R.drawable.button_sky_selected_effect);

                        click_full_time = true;
                        full_time = null;
                    }
                }
            });

            btn_part_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click_part_time) {
                        //  btn_part_time.setBackgroundResource(R.color.my_sky_dark);
                        btn_part_time.setSelected(true);
                        btn_part_time.setBackgroundResource(R.drawable.button_sky_selected_effect);

                        click_part_time = false;
                        part_time = "Part Time";
                    } else {
                        btn_part_time.setSelected(false);
                        btn_part_time.setBackgroundResource(R.drawable.button_sky_selected_effect);
                        click_part_time = true;
                        part_time = null;
                    }
                }
            });
            btn_contract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click_contrac) {
//                        btn_contract.setBackgroundResource(R.color.my_sky_dark);
                        btn_contract.setSelected(true);
                        btn_contract.setBackgroundResource(R.drawable.button_sky_selected_effect);

                        click_contrac = false;
                        contract = "Contract";
                    } else {
                        //btn_contract.setBackgroundResource(R.color.back_darkLight);
                        btn_contract.setSelected(false);
                        btn_contract.setBackgroundResource(R.drawable.button_sky_selected_effect);
                        click_contrac = true;
                        contract = null;
                    }
                }
            });

            btn_volunteer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click_volunteer) {
//                        btn_volunteer.setBackgroundResource(R.color.my_sky_dark);
                        btn_volunteer.setSelected(true);
                        btn_volunteer.setBackgroundResource(R.drawable.button_sky_selected_effect);
                        click_volunteer = false;
                        volunteer = "Volunteer";
                    } else {
//                        btn_volunteer.setBackgroundResource(R.color.back_darkLight);
                        btn_volunteer.setSelected(false);
                        btn_volunteer.setBackgroundResource(R.drawable.button_sky_selected_effect);
                        click_volunteer = true;
                        volunteer = null;
                    }
                }
            });
            btn_intership.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click_intership) {
//                        btn_intership.setBackgroundResource(R.color.my_sky_dark);
                        btn_intership.setSelected(true);
                        btn_intership.setBackgroundResource(R.drawable.button_sky_selected_effect);
                        click_intership = false;
                        intership = "Internship";
                    } else {
                        btn_intership.setSelected(false);
                        btn_intership.setBackgroundResource(R.drawable.button_sky_selected_effect);
                        click_intership = true;
                        intership = null;
                    }
                }
            });
            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linear_main.setVisibility(View.GONE);
                    linear_listview.setVisibility(View.VISIBLE);
                    call_webservice();
                }
            });
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(JobSearchFilter.this);
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
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Place mPlace = PlaceAutocomplete.getPlace(JobSearchFilter.this, data);
        //    ed_location.setText(mPlace.getAddress());
            String addresses= String.valueOf(mPlace.getAddress());

            ed_location.setText(addresses);
            //Remove country name then used*************
//            int endIndex = addresses.lastIndexOf(",");
//            if (endIndex != -1)
//            {
//                String city_state = addresses.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
//                ed_location.setText(city_state);
//            }
//            else
//            {
//                ed_location.setText(addresses);
//            }
            //*******************************************


            //  ed_location.clearFocus();
            ed_location.setFocusable(false);
            // spinner_industry.requestFocus();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setHasOptionsMenu(true);
//}
    public void call_webservice() {
        progressDialog.show();
        //String url_welcome = webservice_url.toString() + Candidate_id;
        String url_welcome = webservice_url.toString() + Candidate_id + "/" + next_page + "/" + no_of_record;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllHomeJobResult");
                    int count = 0;
                    String candidat_id = String.valueOf(Candidate_id);
                    while (count < resultJsonArr.length()) {
                        //    RowItemGetAcceptedJobResult rowItem_home = new RowItemGetAcceptedJobResult();
                        RowItem_ApplyForJob rowItem_home = new RowItem_ApplyForJob();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);


                        String JobTitle = jObject.getString("jobtitle");
                        String cpyName = jObject.getString("company");
                        String JobType = jObject.getString("EmploymentType");
                        String JobPostId = jObject.getString("jobID");
                        String JobDescription = jObject.getString("snippet");
                        String JobType_db = jObject.getString("EmploymentType");
                        String city = jObject.getString("Location");
                        String SalaryFrom = jObject.getString("salaryFrom");
                        String SalaryTo = jObject.getString("salaryTo");
                        //New
                        // String Image_path="/TestimonialsImageVedio/GotHireSmall.png";//New
                        String Image_path = jObject.getString("CompanyLogo");//ImagePath
                        String postedOn = jObject.getString("postedOn");
                        String url = jObject.getString("url");
                        int max_salary=0,min_sal=0;
                        // int max_salary= Integer.parseInt(newStr_bb);
                        if(SalaryFrom != null && !SalaryFrom.isEmpty() && !SalaryFrom.equals("null")&& !SalaryFrom.equals(""))
                        {
                            min_sal  = Integer.parseInt(SalaryFrom);
                        }
                        if(SalaryTo != null && !SalaryTo.isEmpty() && !SalaryTo.equals("null")&& !SalaryTo.equals(""))
                        {
                             max_salary = Integer.parseInt(SalaryTo);
                        }
                        String Indusrty = "IT";

                        String get_location = ed_location.getText().toString();
                        String get_industry = spinner_industry.getSelectedItem().toString();
                        int ans_salary_iint = (int) ans_salary;

                        if (JobType.equals(full_time) || JobType.equals(part_time) || JobType.equals(contract) || JobType.equals(volunteer) || JobType.equals(intership))// && (city.equals(get_location)) || (Indusrty.equals(get_industry)) ||(max_salary >= ans_salary_iint)   )
                        {
                            if ((city.equals(get_location)) && Indusrty.equals(get_industry) && (max_salary >= ans_salary_iint)) {
                                rowItem_home.setCandidateid(candidat_id);
                                rowItem_home.setJobTitle(JobTitle);
                                rowItem_home.setCompanyName(cpyName);
                                rowItem_home.setJobPostId(JobPostId);
                                rowItem_home.setJobDescription(JobDescription);
                                rowItem_home.setImage_path(Image_path);//new
                                rowItem_home.setJobType(JobType_db);
                                rowItem_home.setJobLocation(city);//Not in webservices
                                rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                rowItem_home.setUrl(url);
                                rowItem_home.setPostedOn(postedOn);
                                arrayList.add(rowItem_home);
                            }
                            // else if(Indusrty.equals(get_industry) && (get_location.equals("") || city.equals(get_location)))
                            else if ((Indusrty.equals(get_industry)) && ((get_location.equals("")) || (city.equals(get_location))) && ((max_salary >= ans_salary_iint) || ans_salary_iint <= 0)) {
                                rowItem_home.setCandidateid(candidat_id);
                                rowItem_home.setJobTitle(JobTitle);
                                rowItem_home.setCompanyName(cpyName);
                                rowItem_home.setJobPostId(JobPostId);
                                rowItem_home.setJobDescription(JobDescription);
                                rowItem_home.setImage_path(Image_path);//new
                                rowItem_home.setJobType(JobType_db);
                                rowItem_home.setJobLocation(city);//Not in webservices
                                rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                rowItem_home.setUrl(url);
                                rowItem_home.setPostedOn(postedOn);
                                arrayList.add(rowItem_home);
                            }
                        } else if (full_time == null && part_time == null && contract == null && volunteer == null && intership == null)// && get_location ==null && get_industry==null && ans_salary_iint==0)
                        {
                            if (JobType.equals("Part Time") || JobType.equals("Full Time") || JobType.equals("Contract") || JobType.equals("Volunteer") || JobType.equals("Internship")) {
                                if ((city.equals(get_location)) && Indusrty.equals(get_industry) && (max_salary >= ans_salary_iint)) {
                                    rowItem_home.setCandidateid(candidat_id);
                                    rowItem_home.setJobTitle(JobTitle);
                                    rowItem_home.setCompanyName(cpyName);
                                    rowItem_home.setJobPostId(JobPostId);
                                    rowItem_home.setJobDescription(JobDescription);
                                    rowItem_home.setImage_path(Image_path);//new
                                    rowItem_home.setJobType(JobType_db);
                                    rowItem_home.setJobLocation(city);//Not in webservices
                                    rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                    rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                    rowItem_home.setUrl(url);
                                    rowItem_home.setPostedOn(postedOn);
                                    arrayList.add(rowItem_home);
                                } else if ((Indusrty.equals(get_industry)) && ((get_location.equals("")) || (city.equals(get_location))) && ((max_salary >= ans_salary_iint) || ans_salary_iint <= 0)) {
                                    rowItem_home.setCandidateid(candidat_id);
                                    rowItem_home.setJobTitle(JobTitle);
                                    rowItem_home.setCompanyName(cpyName);
                                    rowItem_home.setJobPostId(JobPostId);
                                    rowItem_home.setJobDescription(JobDescription);
                                    rowItem_home.setImage_path(Image_path);//new
                                    rowItem_home.setJobType(JobType_db);
                                    rowItem_home.setJobLocation(city);//Not in webservices
                                    rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                    rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                    rowItem_home.setUrl(url);
                                    rowItem_home.setPostedOn(postedOn);
                                    arrayList.add(rowItem_home);
                                }
                            }
                        }
                        count++;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                adapter_applyForJob = new Adapter_ApplyForJob(arrayList, getLayoutInflater(), JobSearchFilter.this);
                //   adapter_home.updateResults(arrayList);
                listView_job_search.setAdapter(adapter_applyForJob);
                tv_empty.setText("Oops! no matches found");
                listView_job_search.setEmptyView(tv_empty);

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(JobSearchFilter.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                listView_job_search.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(JobSearchFilter.this);
        rQueue.add(jsonObjReq);
    }


    public void ioc_function(Location location)
    {
        try {
            double latitude =location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(JobSearchFilter.this);
            List<Address>addresses = null;
            addresses = geocoder.getFromLocation(latitude,longitude,1);

            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
           // String country = addresses.get(0).getCountryName();
            ed_location.setText(city+","+state);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void getPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);

    }

    public class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listView_job_search.addFooterView(ftView);
                    break;
                case 1:
                    adapter_applyForJob.addListItemAdapter((ArrayList<RowItem_ApplyForJob>) msg.obj);
                    //remove fotterview after update listview
                    listView_job_search.removeFooterView(ftView);
                    isLoading = false;
                    break;
                default:
                    break;

            }
        }
    }

    private ArrayList<RowItem_ApplyForJob> getMoreData() {
        ArrayList<RowItem_ApplyForJob> list = new ArrayList<>();
        // call_webservice();
        next_page++;
        call_webservice_with_pageNo(next_page);

        return list;
    }

    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            //Add Footer after get data here
            mHandler.sendEmptyMessage(0);
            //Search More data here
            ArrayList<RowItem_ApplyForJob> listResult = getMoreData();

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = mHandler.obtainMessage(1, listResult);
            mHandler.sendMessage(msg);


        }
    }

    public void call_webservice_with_pageNo(int next_page) {
        // progressDialog.show();


        String url_welcome = webservice_url.toString() + Candidate_id + "/" + next_page + "/" + no_of_record;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();

                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllHomeJobResult");
                    int count2 = 0;
                    RowItem_ApplyForJob rowItem_home = new RowItem_ApplyForJob();
                    while (count2 < resultJsonArr.length()) {
                        //  RowItem_ApplyForJob rowItem_applyForJob = new RowItem_ApplyForJob();
                        JSONObject jObject = resultJsonArr.getJSONObject(count2);

                        String JobTitle = jObject.getString("jobtitle");
                        String cpyName = jObject.getString("company");
                        String JobType = jObject.getString("EmploymentType");
                        String JobPostId = jObject.getString("jobID");
                        String JobDescription = jObject.getString("snippet");
                        String JobType_db = jObject.getString("EmploymentType");
                        String city = jObject.getString("Location");
                        String SalaryFrom = jObject.getString("salaryFrom");
                        String SalaryTo = jObject.getString("salaryTo");
                        String Image_path = jObject.getString("CompanyLogo");//ImagePath
                        String postedOn = jObject.getString("postedOn");
                        String url = jObject.getString("url");

                        int min_sal = Integer.parseInt(SalaryFrom);
                        int max_salary = Integer.parseInt(SalaryTo);
                        String Indusrty = "IT";

                        String get_location = ed_location.getText().toString();
                        String get_industry = spinner_industry.getSelectedItem().toString();
                        int ans_salary_iint = (int) ans_salary;

                        if (JobType.equals(full_time) || JobType.equals(part_time) || JobType.equals(contract) || JobType.equals(volunteer) || JobType.equals(intership))// && (city.equals(get_location)) || (Indusrty.equals(get_industry)) ||(max_salary >= ans_salary_iint)   )
                        {
                            if ((city.equals(get_location)) && Indusrty.equals(get_industry) && (max_salary >= ans_salary_iint)) {
                                rowItem_home.setCandidateid(Candidate_id);
                                rowItem_home.setJobTitle(JobTitle);
                                rowItem_home.setCompanyName(cpyName);
                                rowItem_home.setJobPostId(JobPostId);
                                rowItem_home.setJobDescription(JobDescription);
                                rowItem_home.setImage_path(Image_path);//new
                                rowItem_home.setJobType(JobType_db);
                                rowItem_home.setJobLocation(city);//Not in webservices
                                rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                rowItem_home.setUrl(url);
                                rowItem_home.setPostedOn(postedOn);
                                arrayList.add(rowItem_home);
                            }
                            // else if(Indusrty.equals(get_industry) && (get_location.equals("") || city.equals(get_location)))
                            else if ((Indusrty.equals(get_industry)) && ((get_location.equals("")) || (city.equals(get_location))) && ((max_salary >= ans_salary_iint) || ans_salary_iint <= 0)) {
                                rowItem_home.setCandidateid(Candidate_id);
                                rowItem_home.setJobTitle(JobTitle);
                                rowItem_home.setCompanyName(cpyName);
                                rowItem_home.setJobPostId(JobPostId);
                                rowItem_home.setJobDescription(JobDescription);
                                rowItem_home.setImage_path(Image_path);//new
                                rowItem_home.setJobType(JobType_db);
                                rowItem_home.setJobLocation(city);//Not in webservices
                                rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                rowItem_home.setUrl(url);
                                rowItem_home.setPostedOn(postedOn);
                                arrayList.add(rowItem_home);
                            }
                        } else if (full_time == null && part_time == null && contract == null && volunteer == null && intership == null)// && get_location ==null && get_industry==null && ans_salary_iint==0)
                        {
                            if (JobType.equals("Part Time") || JobType.equals("Full Time") || JobType.equals("Contract") || JobType.equals("Volunteer") || JobType.equals("Internship")) {
                                if ((city.equals(get_location)) && Indusrty.equals(get_industry) && (max_salary >= ans_salary_iint)) {
                                    rowItem_home.setCandidateid(Candidate_id);
                                    rowItem_home.setJobTitle(JobTitle);
                                    rowItem_home.setCompanyName(cpyName);
                                    rowItem_home.setJobPostId(JobPostId);
                                    rowItem_home.setJobDescription(JobDescription);
                                    rowItem_home.setImage_path(Image_path);//new
                                    rowItem_home.setJobType(JobType_db);

                                    rowItem_home.setJobLocation(city);//Not in webservices
                                    rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                    rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                    rowItem_home.setUrl(url);
                                    rowItem_home.setPostedOn(postedOn);
                                    arrayList.add(rowItem_home);
                                } else if ((Indusrty.equals(get_industry)) && ((get_location.equals("")) || (city.equals(get_location))) && ((max_salary >= ans_salary_iint) || ans_salary_iint <= 0)) {
                                    rowItem_home.setCandidateid(Candidate_id);
                                    rowItem_home.setJobTitle(JobTitle);
                                    rowItem_home.setCompanyName(cpyName);
                                    rowItem_home.setJobPostId(JobPostId);
                                    rowItem_home.setJobDescription(JobDescription);
                                    rowItem_home.setImage_path(Image_path);//new
                                    rowItem_home.setJobType(JobType_db);
                                    rowItem_home.setJobLocation(city);//Not in webservices
                                    rowItem_home.setSalaryFrom(SalaryFrom);// Not in webservices
                                    rowItem_home.setSalaryTo(SalaryTo);// Not in webservices
                                    rowItem_home.setUrl(url);
                                    rowItem_home.setPostedOn(postedOn);
                                    arrayList.add(rowItem_home);
                                }
                            }
                        }
                        count2++;
                    }
                    count = count + count2;
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //  Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                // progressDialog.dismiss();
                //   tv_empty.setText("Oops! no jobs found");
                //listView_jobs.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(JobSearchFilter.this);
        rQueue.add(jsonObjReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

//        MenuItem item = menu.findItem(R.id.notification);
//        MenuItemCompat.setActionView(item, R.layout.notification_count);
//        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
//
//        ImageView imageBtn_notification=(ImageView) notifCount.findViewById(R.id.img_notification);
//        //tv_counter= (TextView) notifCount.findViewById(R.id.badge_notification_count);
//        mBadge = (NotificationBadge)notifCount.findViewById(R.id.badge);
//
//        sharedpreferences_counter = getActivity().getSharedPreferences(myPref_counter,
//                Context.MODE_PRIVATE);
//
//        if (sharedpreferences_counter.contains(Counter_key)) {
//            new_notification_counter = sharedpreferences_counter.getInt(Counter_key, 0);
//        }
//        if(new_notification_counter==0)
//        {
//            mBadge.setNumber(0);
//        }
//        else
//        {
//            mBadge.setNumber(new_notification_counter);
//        }
//
//
//        imageBtn_notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                sharedpreferences_counter = getActivity().getSharedPreferences(myPref_counter,
//                        Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
//                editor_counter.clear();
//                editor_counter.commit();
//
//                String candidat_id = String.valueOf(Candidate_id);
//
//                JobSearchFilter jobSearchFilterFragment = new JobSearchFilter();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidat_id);
//
//                FragmentManager fm =  getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                jobSearchFilterFragment.setArguments(args);
//                fragmentTransaction.replace(R.id.frameLayout, jobSearchFilterFragment);
//                fragmentTransaction.commit(); // save the changes
//
//
//
//
////                args = new Bundle();
//                // args.putString("Key_candidate_id",candidat_id);
//                Notification notification = new Notification();
//                FragmentManager fm_noti=  getFragmentManager();
//                FragmentTransaction fragmentTransaction_noti = fm_noti.beginTransaction();
//                notification.setArguments(args);
//                fragmentTransaction_noti.addToBackStack(null);
//                fragmentTransaction_noti.add(R.id.frameLayout, notification);
//                fragmentTransaction_noti.commit(); // save the ch
//            }
//        });

        //Open Jobsarchpage from menu button

//        ImageView imageBtn_menu_job_search_filter = (ImageView)getActivity().findViewById(R.id.job_search_filter);
//        imageBtn_menu_job_search_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Home homeFragment = new Home();
//                args = new Bundle();
//                args.putString("Key_candidate_id",Candidate_id);
//
//                FragmentManager fm =  getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                homeFragment.setArguments(args);
//                fragmentTransaction.replace(R.id.frameLayout, homeFragment);
//                fragmentTransaction.commit(); // save the changes
//
//                JobSearchFilter jobSearchFilter = new JobSearchFilter();
//                // args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_noti=  getFragmentManager();
//                FragmentTransaction fragmentTransaction_noti = fm_noti.beginTransaction();
//                jobSearchFilter.setArguments(args);
//                fragmentTransaction_noti.addToBackStack(null);
//                fragmentTransaction_noti.add(R.id.frameLayout, jobSearchFilter);
//                fragmentTransaction_noti.commit(); // save the ch
//            }
//        });
        //*************

        ImageView imageBtn_menu_job_search_filter = (ImageView)findViewById(R.id.job_search_filter1);
        imageBtn_menu_job_search_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Home homeFragment = new Home();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//
                Intent intent_welcome = new Intent(getApplicationContext(),JobSearchFilter.class); //First we opened MainContainer.class
                 intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_welcome.putExtra("Key_candidate_id", Candidate_id);
                //intent_welcome.putExtra("Key_candidate_nm", );
                startActivity(intent_welcome);
            }
        });


        //Now Job_search_filter xml file edittext box*********
        final EditText searchView = (EditText) findViewById(R.id.searchEditText_job_search);

        if (!(searchView instanceof EditText)) {
            searchView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(JobSearchFilter.this);
                    return false;
                }
            });
        }


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s1, int start, int before, int count) {

                String s = searchView.getText().toString().toLowerCase(Locale.getDefault());
                //Log.d("Search data",s);
                arrayList_search.clear();
                keyword_upper = s.toUpperCase(); //Get keyword from searchbar editbox.
                keyword_lower = s.toLowerCase();
                keyword_filst_lt_capital = StringUtils.capitalize(s);
                //checking language arraylist for items containing search keyword
                for (int i = 0; i < arrayList.size(); i++) {
                    //  RowItemGetAcceptedJobResult rowItemGetAcceptedJobResult = new RowItemGetAcceptedJobResult();
                    RowItem_ApplyForJob rowItemGetAcceptedJobResult = new RowItem_ApplyForJob();
                    // if(arrayList.get(i).getJobTitle().contains(keyword)){
                    if (arrayList.get(i).getJobTitle().toLowerCase().contains(keyword_lower)) {// || arrayList.get(i).getJobTitle().contains(keyword_lower) || arrayList.get(i).getJobTitle().contains(keyword_filst_lt_capital)){
                        rowItemGetAcceptedJobResult.setJobTitle(arrayList.get(i).getJobTitle().toString());
                        rowItemGetAcceptedJobResult.setCompanyName(arrayList.get(i).getCompanyName());
                        rowItemGetAcceptedJobResult.setSalaryFrom(arrayList.get(i).getSalaryFrom());
                        rowItemGetAcceptedJobResult.setJobLocation(arrayList.get(i).getJobLocation());
                        rowItemGetAcceptedJobResult.setImage_path(arrayList.get(i).getImage_path());
                        rowItemGetAcceptedJobResult.setUrl(arrayList.get(i).getUrl());
                        rowItemGetAcceptedJobResult.setPostedOn(arrayList.get(i).getPostedOn());

                        String JobTitle = arrayList.get(i).getJobTitle();
                        String cpyName = arrayList.get(i).getCompanyName();
                        String JobPostId = arrayList.get(i).getJobPostId();
                        String JobDescription = arrayList.get(i).getCompanyName();
                        String JobType = arrayList.get(i).getJobType();
                        String Image_path = arrayList.get(i).getImage_path();
                        String url = arrayList.get(i).getUrl();
                        String postedOn = arrayList.get(i).getPostedOn();

                        rowItemGetAcceptedJobResult.setCandidateid(Candidate_id);
                        rowItemGetAcceptedJobResult.setJobTitle(JobTitle);
                        rowItemGetAcceptedJobResult.setCompanyName(cpyName);
                        rowItemGetAcceptedJobResult.setJobPostId(JobPostId);
                        rowItemGetAcceptedJobResult.setJobDescription(JobDescription);
                        rowItemGetAcceptedJobResult.setImage_path(Image_path);
                        rowItemGetAcceptedJobResult.setJobType(JobType);
                        rowItemGetAcceptedJobResult.setUrl(url);
                        rowItemGetAcceptedJobResult.setPostedOn(postedOn);
                        arrayList_search.add(rowItemGetAcceptedJobResult);//add items in new arraylist_search
                    }
                }
                //New array adapter for searching and add in custome adapter.
                adapter_applyForJob = new Adapter_ApplyForJob(arrayList_search, getLayoutInflater(), JobSearchFilter.this);
                listView_job_search.setAdapter(adapter_applyForJob);
//                return false;*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return true;
    }
    public void call_allIndustries_webservice()
    {
        progressDialog.show();
        String url_Skill = webservice_AllIndustery_url.toString() ;

        JsonObjectRequest json_skill = new JsonObjectRequest(Request.Method.GET, url_Skill, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllIndusteryListResult");

                    int count = 0;

                    while (count < resultJsonArr.length()) {
                        JSONObject jObject = resultJsonArr.getJSONObject(count);
                        String industy_Id = jObject.getString("industryId");
                        String industy_nm= jObject.getString("industryname");
                        arrayList_industry.add(industy_nm);
                        count++;
                    }
                   // String selection ="";
                 //   arrayList_industry.add("All Industries");
                    //arrayList_industry.add("IT");
                    arrayList_industry .add(0,"All industries");
                    arrayList_industry .add(1,"IT");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
              //  int pos=arrayList_industry.size()-1;

                spinner_industry.setAdapter(adapter_industry);
              //  spinner_industry.setSelection(pos);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(JobSearchFilter.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(JobSearchFilter.this);
        rQueue.add(json_skill);
    }
  //  @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        String candidate_id_value = String.valueOf(Candidate_id);
//        switch (item.getItemId()) {
//            case R.id.job_search_filter:
//                JobSearchFilter jobSearchFilter = new JobSearchFilter();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_job_search =  getFragmentManager();
//                FragmentTransaction fragmentTransaction_job_search = fm_job_search.beginTransaction();
//                jobSearchFilter.setArguments(args);
//                fragmentTransaction_job_search.addToBackStack(null);
//                fragmentTransaction_job_search.add(R.id.frameLayout, jobSearchFilter);
//                fragmentTransaction_job_search.commit(); // save the ch
//                break;
//            case R.id.view_invitation:
//                Interview_Invitation interview_invitation = new Interview_Invitation();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_interview_invitation =  getFragmentManager();
//                FragmentTransaction fragmentTransaction_interview_invitation = fm_interview_invitation.beginTransaction();
//                interview_invitation.setArguments(args);
//                fragmentTransaction_interview_invitation.addToBackStack(null);
//                fragmentTransaction_interview_invitation.add(R.id.frameLayout, interview_invitation);
//                fragmentTransaction_interview_invitation.commit(); // save the ch
//                break;
//            case R.id.job_apply:
//                ApplyForJob applyForJob = new ApplyForJob();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_apply_job =  getFragmentManager();
//                FragmentTransaction fragmentTransaction_apply_job = fm_apply_job.beginTransaction();
//                applyForJob.setArguments(args);
//                fragmentTransaction_apply_job.addToBackStack(null);
//                fragmentTransaction_apply_job.add(R.id.frameLayout, applyForJob);
//                fragmentTransaction_apply_job.commit(); // save the ch
//                break;
//            case R.id.notification:
//
//                Notification notification = new Notification();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//
//                FragmentManager fm_noti=  getFragmentManager();
//                FragmentTransaction fragmentTransaction_noti = fm_noti.beginTransaction();
//                notification.setArguments(args);
//                fragmentTransaction_noti.addToBackStack(null);
//                fragmentTransaction_noti.add(R.id.frameLayout, notification);
//                fragmentTransaction_noti.commit(); // save the ch
//                break;
//            case R.id.profile:
//                Intent profile_intent = new Intent(getActivity(), ProfileSetup.class);
//                profile_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                profile_intent.putExtra("Key_candidate_id",candidate_id_value);
//                startActivity(profile_intent);
////                ProfileSetup profileSetup = new ProfileSetup();
////                args = new Bundle();
////                args.putString("Key_candidate_id",candidate_id_value);
////
////                FragmentManager fm_profile =  getFragmentManager();
////                FragmentTransaction fragmentTransaction_profile = fm_profile.beginTransaction();
////                profileSetup.setArguments(args);
////                fragmentTransaction_profile.addToBackStack(null);
////                fragmentTransaction_profile.add(R.id.frameLayout, profileSetup);
////                fragmentTransaction_profile.commit(); // save the ch
//                break;
//            case R.id.logout:
////                Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
////                sharedpreferences_id = getActivity().getSharedPreferences(mypreference_id,
////                        Context.MODE_PRIVATE);
////                SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
////                editor_id.clear();
////                editor_id.commit();
////
////                Intent logout_intent = new Intent(getActivity(), Login_with.class);
////                Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
////                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                this.startActivity(logout_intent);
////                break;
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                // Setting Dialog Title
//                alertDialog.setTitle("Confirm Logout");
//                alertDialog.setCancelable(false);
//                alertDialog.setMessage("Are you sure you want logout ?");
//                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        sharedpreferences_id = getActivity().getSharedPreferences(mypreference_id,
//                                Context.MODE_PRIVATE);
//
//                        SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
//                        editor_id.clear();
//                        editor_id.commit();
//                        Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(getActivity(), Login_with.class));
//                    }
//                });
//                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
//                        dialog.cancel();
//                    }
//                });
//                alertDialog.show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//  private void showGPSDisabledAlertToUser(){
//      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//      alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
//              .setCancelable(false)
//              .setPositiveButton("Goto Settings Page To Enable GPS",
//                      new DialogInterface.OnClickListener(){
//                          public void onClick(DialogInterface dialog, int id){
//                              Intent callGPSSettingIntent = new Intent(
//                                      Settings.ACTION_SETTINGS);
//                              startActivity(callGPSSettingIntent);
//                          }
//                      });
//      alertDialogBuilder.setNegativeButton("Cancel",
//              new DialogInterface.OnClickListener(){
//                  public void onClick(DialogInterface dialog, int id){
//                      dialog.cancel();
//                  }
//              });
//      AlertDialog alert = alertDialogBuilder.create();
//      alert.show();
//  }
//    void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_LOCATION:
//                getLocation();
//                break;
//        }
//    }
//  @Override
//  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//      switch (requestCode) {
//          case REQUEST_CODE_PERMISSION:
//              if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                  getLocation();
//              } else {
//                  System.out.println("permission denied!");
//              }
//              break;
//      }
//  }
//    void getLocation(){
//
//        MyTracker tracker=new MyTracker(this);
//        System.out.println(tracker.getLatitude());
//        System.out.println(tracker.getLongitude());
//        System.out.println(tracker.getLocation());
//        System.out.println(tracker.address);
//        System.out.println(tracker.cityName);
//        System.out.println(tracker.state);
//        System.out.println(tracker.countryName);
//        System.out.println(tracker.countryCode);
//        System.out.println(tracker.ipAddress);
//        System.out.println(tracker.macAddress);
//
//        ed_location.setText(tracker.address);
//    }
  @Override
  public boolean onTouchEvent(MotionEvent event) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      if (getCurrentFocus() != null) {
          imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
      }
      return true;
  }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
