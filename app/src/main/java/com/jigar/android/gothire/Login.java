package com.jigar.android.gothire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Login extends AppCompatActivity {

    String webservice_url = UrlString.URL + "GetbyEmailPassword/";
    ProgressDialog progressDialog;
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    int Candidate_id;
    //****
    String Candidate_nm;
    Bundle args = new Bundle();

    Button btn_login;
    EditText ed_unm, ed_pass;
    TextView validUnm, validPass, valid_Unm_pass;
    TextView join_now, forgot_pass;
    ImageView img_logo;

    String refreshedToken, conv_refreshedToken;
    String mob_key = "1";//Android Id is 1 for used webservices ..
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        runTask();
    }
    public void runTask()
    {
            ConnectivityManager connec =
                    (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                //For Id
                sharedpreferences_id = getSharedPreferences(mypreference_id,
                        Context.MODE_PRIVATE);
                //***
                refreshedToken= FirebaseInstanceId.getInstance().getToken();
               // refreshedToken="nottoken_123";
                FirebaseInstanceId.getInstance().getToken();
                if(refreshedToken!=null)
                {
                    conv_refreshedToken = refreshedToken.replace(":", "$");
                }
                btn_login = (Button) findViewById(R.id.btn_login);
                ed_unm = (EditText)findViewById(R.id.ed_uid);
                ed_pass = (EditText)findViewById(R.id.ed_upass);

                validUnm = (TextView)findViewById(R.id.valid_usernm);
                validPass = (TextView)findViewById(R.id.valid_pass);
                valid_Unm_pass =(TextView)findViewById(R.id.txt_valid_unm_pass);
                join_now = (TextView)findViewById(R.id.txt_join_now);
                forgot_pass = (TextView)findViewById(R.id.txt_forgot_pass);
                img_logo = (ImageView) findViewById(R.id.img_logo);

                Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                        "fonts/Roboto-Light.ttf");
                ed_unm.setTypeface(roboto_Light);
                ed_pass.setTypeface(roboto_Light);

                validUnm.setTypeface(roboto_Light);
                validPass.setTypeface(roboto_Light);
                valid_Unm_pass.setTypeface(roboto_Light);
                join_now.setTypeface(roboto_Light);
                forgot_pass.setTypeface(roboto_Light);


                ed_pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                 @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        validPass.setText("");
                        valid_Unm_pass.setText("");
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                ed_unm.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        validUnm.setText("");
                        valid_Unm_pass.setText("");
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                forgot_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent_forgot = new Intent(getApplicationContext(), Home.class);
////                      intent_forgot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                     //intent_forgot.setFlags(ActivityFlags.NewTask);
//                        startActivity(intent_forgot);
                    }
                });
                join_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent_forgot = new Intent(getApplicationContext(), Home.class);
////                      intent_forgot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                      //intent_forgot.setFlags(ActivityFlags.NewTask);
//                        startActivity(intent_forgot);
                    }
                });
                ed_pass.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                // case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    //Here also used login_code for save click event
                                   login_code();
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
                btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login_code();
                    }
                });
            } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_connection);
            builder.setIcon(R.mipmap.ic_internetconnection);
                builder.setCancelable(false);
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
        public void login_code() {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            if (ed_pass.getText().toString().trim().equals("") && ed_unm.getText().toString().trim().equals(""))
            {
                validPass.setText("Please enter your password.");
                validUnm.setText("Please enter your username.");
                ed_unm.requestFocus();
                progressDialog.dismiss();
            }
            else if (ed_pass.getText().toString().trim().equals(""))
            {
                validPass.setText("Please enter your password.");
                ed_pass.requestFocus();
                progressDialog.dismiss();
            }
            else if (ed_unm.getText().toString().trim().equals(""))
            {
                validUnm.setText("Please enter your username.");
                ed_unm.requestFocus();
                progressDialog.dismiss();
            }
            else
            {
                String user_nm = ed_unm.getText().toString();
                String pass = ed_pass.getText().toString();

                String url_Login = webservice_url.toString() + user_nm + "/" + pass + "/" + conv_refreshedToken+ "/" +mob_key;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_Login, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Result", response.toString());
                    String result = response.toString();
                    try {
                       // JSONArray resultJsonArr = response.getJSONArray("GetbyEmailPasswordResult");
                        String result_status = null;
                        result_status=response.getString("GetbyEmailPasswordResult");

                        result_status= String.valueOf(result_status);
                        if(result_status.equals("Invalid EmailId and Password!"))
                        {
                            valid_Unm_pass.setText("Login failed. Please check your username and password.");
                        }
                        else if (result_status.equals("") || result_status.equals("null"))
                        {
                            valid_Unm_pass.setText("Login failed. Please check your username and password.");
                        }
                        else if(!result_status.equals(""))
                        {
                            Candidate_id= Integer.parseInt(result_status);
                            SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
                            editor_id.putInt(CandidateKey,Candidate_id);
                            editor_id.commit();
                            String candidate_id_str= String.valueOf(Candidate_id);
                            Intent intent_welcome = new Intent(getApplicationContext(),MainContainer.class); //First we opened MainContainer.class
                            intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent_welcome.putExtra("Key_candidate_id", candidate_id_str);
                            intent_welcome.putExtra("Key_candidate_nm", Candidate_nm);
                            startActivity(intent_welcome);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(Login.this, "Please check your username and password", Toast.LENGTH_LONG).show();
                    ed_unm.requestFocus();
                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(Login.this);
            rQueue.add(jsonObjReq);
        }
            progressDialog.dismiss();
        }
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }
}