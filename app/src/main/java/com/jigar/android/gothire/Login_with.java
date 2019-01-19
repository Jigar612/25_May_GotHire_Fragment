package com.jigar.android.gothire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.NoSuchPropertyException;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Login_with extends AppCompatActivity {
    Button btn_signin,btn_signUp_email;
    LoginButton loginButton;
   // TextView tv_status;
    CallbackManager callbackManager;
  //  Profile profile;
    String email,FirstName,LastName ;

    String refreshedToken, conv_refreshedToken;
    String mob_key = "1";

    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    int Candidate_id;

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login_with);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//SCREEN_ORIENTATION_SENSOR

        //  FacebookSdk.sdkInitialize(this.getApplicationContext());


        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signUp_email = (Button) findViewById(R.id.btn_signUp_email);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.btn_loginWith_fb);
       // tv_status = (TextView) findViewById(R.id.tv_status);

        Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");
        btn_signin.setTypeface(roboto_Light);
        btn_signUp_email.setTypeface(roboto_Light);
        loginButton.setTypeface(roboto_Light);

        // LoginButton loginButton = (LoginButton) findViewById(R.id.btn_loginWith_fb);
        //  loginButton.setReadPermissions(Arrays.asList(new String[]{“email”, “user_birthday”, “user_hometown”}));

        refreshedToken= FirebaseInstanceId.getInstance().getToken();
        FirebaseInstanceId.getInstance().getToken();


        if(refreshedToken!=null)
        {
            conv_refreshedToken = refreshedToken.replace(":", "$");
        }
        sharedpreferences_id = getSharedPreferences(mypreference_id,
                Context.MODE_PRIVATE);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        AccessToken token;
        token = AccessToken.getCurrentAccessToken();

        if (token != null) {

            if (sharedpreferences_id.contains(CandidateKey)) {
                Candidate_id = sharedpreferences_id.getInt(CandidateKey, 0);
            }
            if (Candidate_id == 0) {

                LoginManager.getInstance().logOut();
            }
            else
            {
                String Candidate_id_st= String.valueOf(Candidate_id);

                Intent intent_welcome = new Intent(getApplicationContext(),MainContainer.class);
                intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_welcome.putExtra("Key_candidate_id", Candidate_id_st);
                // intent_welcome.putExtra("Key_candidate_nm", Candidate_nm);
                startActivity(intent_welcome);
            }
        }
        else
        {



        }
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                tv_status.setText("Login Success \n"+loginResult.getAccessToken().getUserId() +
//                        "\n"+loginResult.getAccessToken().getToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    email = object.getString("email");
                                  //  String birthday = object.getString("birthday"); // 01/31/1980 format
                                    FirstName = object.getString("first_name");
                                    LastName= object.getString("last_name");
                                    signup_code();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,first_name,last_name,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                //tv_status.setText("Login Cancelled");
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

            btn_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_login = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent_login);
                }
            });

            btn_signUp_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_signUop = new Intent(getApplicationContext(), SignUp.class);
                    intent_signUop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent_signUop);
                }
            });

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.jigar.android.gothire",PackageManager.GET_SIGNATURES);
//            for(Signature signature : info.signatures)
//            {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
     //   }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void login_code()
    {
        String url_Login = UrlString.URL + "GetbyEmailPassword/" + email + "/" + "Test@123" + "/" + conv_refreshedToken + "/" + mob_key;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_Login, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    //String result_array = resultJsonArr.toString();
                    String result_status = null;
                    result_status = response.getString("GetbyEmailPasswordResult");

                    result_status = String.valueOf(result_status);
                    if (result_status.equals("Invalid EmailId and Password!")) {
                        //  valid_Unm_pass.setText("Login failed. Please check your username and password.");
                    } else if (result_status.equals("") || result_status.equals("null")) {
                        // valid_Unm_pass.setText("Login failed. Please check your username and password.");
                    } else if (!result_status.equals("")) {

                        sharedpreferences_id = getSharedPreferences(mypreference_id,
                                Context.MODE_PRIVATE);

                        Candidate_id = Integer.parseInt(result_status);
                        SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
                        editor_id.putInt(CandidateKey, Candidate_id);
                        editor_id.commit();

                        String candidate_id_str = String.valueOf(Candidate_id);

//                        Intent intent_profile = new Intent(getApplicationContext(), ProfileSetup.class);//first we open Loging page
//                        intent_profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_profile.putExtra("Key_candidate_id", candidate_id_str);
//                        startActivity(intent_profile);

                        Intent intent_welcome = new Intent(getApplicationContext(), MainContainer.class);
                        intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_welcome.putExtra("Key_candidate_id", candidate_id_str);
                        startActivity(intent_welcome);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(Login.this, "Please check your username and password", Toast.LENGTH_LONG).show();
                //ed_unm.requestFocus();
                //progressBar.setVisibility(View.GONE);
                //Toast.makeText(Login.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(Login_with.this);
        rQueue.add(jsonObjReq);
    }
    public void login_code_with_setup_profile()
    {
        String url_Login = UrlString.URL + "GetbyEmailPassword/" + email + "/" + "Test@123" + "/" + conv_refreshedToken + "/" + mob_key;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_Login, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    //String result_array = resultJsonArr.toString();
                    String result_status = null;
                    result_status = response.getString("GetbyEmailPasswordResult");

                    result_status = String.valueOf(result_status);
                    if (result_status.equals("Invalid EmailId and Password!")) {
                        //  valid_Unm_pass.setText("Login failed. Please check your username and password.");
                    } else if (result_status.equals("") || result_status.equals("null")) {
                        // valid_Unm_pass.setText("Login failed. Please check your username and password.");
                    } else if (!result_status.equals("")) {

                        sharedpreferences_id = getSharedPreferences(mypreference_id,
                                Context.MODE_PRIVATE);

                        Candidate_id = Integer.parseInt(result_status);
                        SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
                        editor_id.putInt(CandidateKey, Candidate_id);
                        editor_id.commit();

                        String candidate_id_str = String.valueOf(Candidate_id);

                        Intent intent_profile = new Intent(getApplicationContext(), ProfileSetup.class);//first we open Loging page
                        intent_profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_profile.putExtra("Key_candidate_id", candidate_id_str);
                        startActivity(intent_profile);

//                        Intent intent_welcome = new Intent(getApplicationContext(), MainContainer.class);
//                        intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_welcome.putExtra("Key_candidate_id", candidate_id_str);
//                        startActivity(intent_welcome);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(Login.this, "Please check your username and password", Toast.LENGTH_LONG).show();
                //ed_unm.requestFocus();
                //progressBar.setVisibility(View.GONE);
                //Toast.makeText(Login.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(Login_with.this);
        rQueue.add(jsonObjReq);
    }

    public void signup_code()
    {
      //  Profile profile = Profile.getCurrentProfile();
//        if(!profile.getFirstName().equals(null))
//        {
//            FirstName = profile.getFirstName();
//            LastName = profile.getLastName();
//        }
        String url_update_profile = UrlString.URL + "RegisterCandidate/" + FirstName + "/" + LastName + "/" + email + "/" + "Test@123";

        JsonObjectRequest json_updt_profile = new JsonObjectRequest(Request.Method.GET, url_update_profile, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    String result_status = null;
                    result_status = response.getString("RegisterCandidateResult");


                    if (result_status.equals("Your sucessfully register.Please login now.")) {
                        // Toast.makeText(getApplicationContext(), "Signup Sucessfully", Toast.LENGTH_LONG).show();
//                                                        Intent intent_login = new Intent(getApplicationContext(), Login.class);
//                                                        intent_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                        startActivity(intent_login);
                        login_code_with_setup_profile();
                    }
                    else if(result_status.equals("Email Id Alreday Register."))
                    {
                        login_code();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                // progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                //  progressDialog.dismiss();
                //  listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(Login_with.this);
        rQueue.add(json_updt_profile);
    }
}
