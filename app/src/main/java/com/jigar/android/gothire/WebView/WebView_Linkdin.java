package com.jigar.android.gothire.WebView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jigar.android.gothire.MainContainer;
import com.jigar.android.gothire.R;

/**
 * Created by COMP11 on 25-Sep-18.
 */

public class WebView_Linkdin extends Fragment {
    WebView mWebView;
    String Candidate_id,url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.webview_linkdin, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview_linkdin);

        MainContainer.TAG="Webview_Lindin";
        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");
        url = get_data.getString("Key_Url");

        mWebView.loadUrl(url);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
            mWebView.setWebViewClient(new WebViewClient());

        return v;
    }

}
