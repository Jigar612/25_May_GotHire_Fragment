package com.jigar.android.gothire;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.Adapter_Interview_Invitation;
import com.jigar.android.gothire.SetterGetter.RowItem_InterviewInvitation;

import net.skoumal.fragmentback.BackFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfferNoDocument extends Fragment {
    String webservice_url = UrlString.URL + "GetViewJob_Invitation/";
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********
    String Candidate_id;
    String OfferId;
    String JobPostId;
    String downloadPath;
    View view;
    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_offer_no_document, container, false);
        MainContainer.TAG="OfferNoDocument";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");
        JobPostId = get_data.getString("Key_jobPost_id");
        OfferId= get_data.getString("Key_offer_id");
        downloadPath = get_data.getString("Key_download_path");

        TextView tv_accepted_period_offer_no = (TextView) view.findViewById(R.id.tv_accepted_period_offer_no);
        TextView tv_congratulate_msg1_offer_no = (TextView) view.findViewById(R.id.tv_congratulate_msg1_offer_no);
        TextView tv_congratulate_msg2_offer_no = (TextView) view.findViewById(R.id.tv_congratulate_msg2_offer_no);

        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_accepted_period_offer_no.setTypeface(roboto_Reg);
        tv_congratulate_msg1_offer_no.setTypeface(roboto_Reg);
        tv_congratulate_msg2_offer_no.setTypeface(roboto_Reg);


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
//    private void loadFragment(Fragment fragment) {
//        // create a FragmentManager
//        FragmentManager fm =  getActivity().getFragmentManager();
//        // create a FragmentTransaction to begin the transaction and replace the Fragment
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        // replace the FrameLayout with new Fragment
//        fragment.setArguments(args);
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit(); // save the changes
//    }
}
