package com.movile.zonar.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

//import com.zonar.activities.BeaconListenerActivity;
import com.movile.zonar.R;
import com.movile.zonar.activities.MenuActivity;
import com.movile.zonar.activities.util.myWebClient;
//import com.zonar.activities.R;
//import com.zonar.activities.com.zonar.activities.util.myWebClient;

public class Navigator extends DialogFragment {

    MenuActivity activity;
    private String url;

    public ProgressDialog progressDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Sets the visibility of the indeterminate progress bar in the
        // title

        // Show progress dialog


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.web_view, null);
        WebView myWebView = (WebView) v.findViewById(R.id.webNavigator);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new myWebClient(activity));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(v)
                // myWebView.setWebViewClient(new myWebClient());

                // Add action buttons
               /* .setPositiveButton("signin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })*/
                .setNegativeButton(" ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        Dialog d = builder.create();
        myWebView.loadUrl(url);
        return d;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setActivity(MenuActivity activity){
        this.activity = activity;
    }
}
