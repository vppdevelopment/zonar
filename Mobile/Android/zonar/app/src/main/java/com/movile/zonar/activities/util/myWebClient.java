package com.movile.zonar.activities.util;

import android.support.v7.app.AppCompatActivity;
import android.webkit.*;

import com.movile.zonar.activities.MenuActivity;


public class myWebClient extends WebViewClient {

    MenuActivity activity;

    public myWebClient(MenuActivity activity){
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
        // Load the given URL on our WebView.
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(android.webkit.WebView view, String url) {

        // When the page has finished loading, hide progress dialog and
        // progress bar in the title.
        super.onPageFinished(view, url);
        activity.setProgressBarIndeterminateVisibility(false);
        activity.progressDialog.dismiss();
    }
}

