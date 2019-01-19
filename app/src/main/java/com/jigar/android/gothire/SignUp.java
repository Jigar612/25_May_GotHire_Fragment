package com.jigar.android.gothire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUp extends AppCompatActivity {

    Button btn_signUp;
    EditText ed_first_nm,ed_last_nm,ed_email,ed_pass;
    String first_nm,last_nm,email,pass;
    ProgressDialog progressDialog;
    int Candidate_id;
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";

    String refreshedToken, conv_refreshedToken;
    String mob_key = "1";//Android Id is 1 for used webservices ..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
       // progressDialog.show();

        runTask();

    }
    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            ed_first_nm = (EditText)findViewById(R.id.ed_first_name_signUp);
            ed_last_nm = (EditText)findViewById(R.id.ed_last_name_signUp);
            ed_email = (EditText)findViewById(R.id.ed_email_signUp);
            ed_pass = (EditText)findViewById(R.id.ed_signUp_pass);

            btn_signUp = (Button) findViewById(R.id.btn_signUp);

            sharedpreferences_id = getSharedPreferences(mypreference_id,
                    Context.MODE_PRIVATE);

            Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                    "fonts/Roboto-Light.ttf");
            ed_first_nm.setTypeface(roboto_Light);
            ed_last_nm.setTypeface(roboto_Light);

            ed_email.setTypeface(roboto_Light);
            ed_pass.setTypeface(roboto_Light);
            btn_signUp.setTypeface(roboto_Light);


            refreshedToken= FirebaseInstanceId.getInstance().getToken();
            // refreshedToken="nottoken_123";
            FirebaseInstanceId.getInstance().getToken();
            if(refreshedToken!=null)
            {
                conv_refreshedToken = refreshedToken.replace(":", "$");
            }
//            if (!isValidEmail(email)) {
//                userName.setError("Valid Email Id");
//            }
//            ed_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                      //  tv_invalidUserName.setText("");
//                        ed_email.setError(null);
//                       // ed_pass.setError(null);
//                        //userName.clearFocus();
//                        //  v.setBackgroundResource(R.drawable.editbox_selector);
//
//                    }
//                    ///New For Valid Email..................****************
//                    if (!hasFocus) {
//                        email = ed_email.getText().toString().trim();
//                        if (!isValidEmail(email)) {
//                            ed_email.setError("please enter valid email");
//                          //  ed_email.requestFocus();
//                            ed_email.requestFocus();
//                            return;
//                        }
//                    }
//                    ///*****************************************************
//                }
//            });
            btn_signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update_profile_webservices();
                }
            });
        }
        else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public void update_profile_webservices()
    {
        progressDialog.show();
        first_nm=ed_first_nm.getText().toString();
        last_nm=ed_last_nm.getText().toString();
        pass=ed_pass.getText().toString();
        email = ed_email.getText().toString().trim();

    if (ed_first_nm.getText().toString().trim().equalsIgnoreCase("")) {
        ed_first_nm.setError("Please enter firstname");
        ed_first_nm.requestFocus();
        progressDialog.dismiss();
        return;
    } else if (ed_last_nm.getText().toString().trim().equalsIgnoreCase("")) {
        ed_last_nm.setError("Please enter lastname");
        ed_last_nm.requestFocus();
        progressDialog.dismiss();
        return;
    }else if (ed_email.getText().toString().trim().equalsIgnoreCase("")) {
            ed_email.setError("Please enter email");
            ed_email.requestFocus();
            progressDialog.dismiss();
            return;
     }else if (!isValidEmail(ed_email.getText().toString().trim())){
            ed_email.setError("please enter valid email");
            ed_email.requestFocus();
            progressDialog.dismiss();
            return;
    } else if (ed_pass.getText().toString().trim().equalsIgnoreCase("")) {
        ed_pass.setError("Please enter password");
        ed_pass.requestFocus();
        progressDialog.dismiss();
        return;
    }
    else
    {

            String url_update_profile = UrlString.URL + "RegisterCandidate/" + first_nm + "/" + last_nm+ "/" +email+ "/" + pass + "/" + conv_refreshedToken+ "/" +mob_key;

        JsonObjectRequest json_updt_profile = new JsonObjectRequest(Request.Method.GET, url_update_profile, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    String result_status=null;
                    result_status=response.getString("RegisterCandidateResult");


                    if(result_status.equals("Email Id Alreday Register."))
                    {
                        Toast.makeText(getApplicationContext(), "Email Id Alreday Registered", Toast.LENGTH_LONG).show();

//                        args = new Bundle();
//                        args.putString("Key_candidate_id", Candidate_id);
//                        loadFragment(new Home());
                    }
                    else if(!result_status.equals("")){
                        Candidate_id= Integer.parseInt(result_status);
                        SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
                        editor_id.putInt(CandidateKey,Candidate_id);
                        editor_id.commit();

                        String candidate_id_str= String.valueOf(Candidate_id);
                        Toast.makeText(getApplicationContext(), "Signup Sucessfully", Toast.LENGTH_LONG).show();
                        Intent intent_profile = new Intent(getApplicationContext(), ProfileSetup.class);//first we open Loging page
                        intent_profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_profile.putExtra("Key_candidate_id", candidate_id_str);
                        startActivity(intent_profile);


                    }



                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(json_updt_profile);
    }
}

    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
