package com.cw.farmer.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.cw.farmer.HandleConnectionAppCompatActivity;
import com.cw.farmer.R;
import com.cw.farmer.offlinefunctions.OfflineFeature;


import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

public class NetworkTestingActivity extends HandleConnectionAppCompatActivity {

    //SHOW INTENET CONNECTION
    // No Internet Dialog
    private NoInternetDialog noInternetDialog;

    // No Internet Snackbar
    private NoInternetSnackbar noInternetSnackbar;
    @Override
    protected void onResume() {
        super.onResume();

//        // No Internet Dialog
//        NoInternetDialog.Builder builder1 = new NoInternetDialog.Builder(this);
//
//        builder1.setConnectionCallback(new ConnectionCallback() { // Optional
//            @Override
//            public void hasActiveConnection(boolean hasActiveConnection) {
//                // ...
//                //Snackbar.make()
////
//
//            }
//        });
//        builder1.setCancelable(false); // Optional
//        builder1.setNoInternetConnectionTitle("No Internet"); // Optional
//        builder1.setNoInternetConnectionMessage("Check your Internet connection and try again"); // Optional
//        builder1.setShowInternetOnButtons(true); // Optional
//        builder1.setPleaseTurnOnText("Please turn on"); // Optional
//        builder1.setWifiOnButtonText("Wifi"); // Optional
//        builder1.setMobileDataOnButtonText("Mobile data"); // Optional
//
//        builder1.setOnAirplaneModeTitle("No Internet"); // Optional
//        builder1.setOnAirplaneModeMessage("You have turned on the airplane mode."); // Optional
//        builder1.setPleaseTurnOffText("Please turn off"); // Optional
//        builder1.setAirplaneModeOffButtonText("Airplane mode"); // Optional
//        builder1.setShowAirplaneModeOffButtons(true); // Optional
//
//        noInternetDialog = builder1.build();


        // No Internet Snackbar
        NoInternetSnackbar.Builder builder2 = new NoInternetSnackbar.Builder(this, (ViewGroup) findViewById(android.R.id.content));

        builder2.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });
        builder2.setIndefinite(true); // Optional
        builder2.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
        builder2.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
        builder2.setSnackbarActionText("Settings");
        builder2.setShowActionToDismiss(true);
        builder2.setSnackbarDismissActionText("Exit");

        noInternetSnackbar = builder2.build();

    }
    @Override
    protected void onPause() {
        super.onPause();

        // No Internet Dialog
        if (noInternetDialog != null) {
            noInternetDialog.destroy();
        }

        // No Internet Snackbar
        if (noInternetSnackbar != null) {
            noInternetSnackbar.destroy();
        }


        // Location
        dismissProgress();
    }

    private void disableButtonAfterClick(int id){
        findViewById(id).setEnabled(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.networking_testing_activity_layout);

        findViewById(R.id.monitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String  packageName = getApplicationContext().getPackageName().toLowerCase();

                try {
                    //Open the specific App Info page:
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);

                } catch ( ActivityNotFoundException e ) {
                    //e.printStackTrace();

                    //Open the generic Apps page:
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    startActivity(intent);

                }

            }
        });

        findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.one);

               new OfflineFeature(0,true).silentDataDump(getApplicationContext());
//                new SweetAlertDialog(NetworkTestingActivity.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("No network connection")
//                        .setContentText("Connect to the internet and try again.")
//                        .setConfirmText("OK")
//                        .show();
            }
        });

        findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.two);
                new OfflineFeature(2,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.three);
                new OfflineFeature(3,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.four);
                new OfflineFeature(4,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.five);
                new OfflineFeature(5,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.six).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.six);
                new OfflineFeature(6,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.seven).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.seven);
                new OfflineFeature(7,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.eight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.eight);
                new OfflineFeature(8,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.nine);
                new OfflineFeature(9,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.ten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.ten);
                new OfflineFeature(10,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.eleven).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.eleven);
                new OfflineFeature(11,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.twelve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.twelve);
                new OfflineFeature(12,true).silentDataDump(getApplicationContext());
            }
        });


        findViewById(R.id.thirteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.thirteen);
                new OfflineFeature(13,true).silentDataDump(getApplicationContext());
            }
        });


        findViewById(R.id.fourteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.fourteen);
                new OfflineFeature(14,true).silentDataDump(getApplicationContext());
            }
        });


        findViewById(R.id.fiveteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.fiveteen);
                new OfflineFeature(15,true).silentDataDump(getApplicationContext());
            }
        });

        findViewById(R.id.sixteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.sixteen);
                new OfflineFeature(16,true).silentDataDump(getApplicationContext());
            }
        });


        findViewById(R.id.seventeen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.seventeen);
                new OfflineFeature(17,true).silentDataDump(getApplicationContext());
            }
        });



        findViewById(R.id.eighteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.eighteen);
                new OfflineFeature(18,true).silentDataDump(getApplicationContext());
            }
        });


        findViewById(R.id.nineteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.nineteen);
                new OfflineFeature(19,true).silentDataDump(getApplicationContext());
            }
        });


        findViewById(R.id.twenty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.twenty);
                new OfflineFeature(20,true).silentDataDump(getApplicationContext());
            }
        });


        findViewById(R.id.twentyone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.twentyone);
                new OfflineFeature(21,true).silentDataDump(getApplicationContext());
            }
        });



        findViewById(R.id.twentytwo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtonAfterClick(R.id.twentytwo);
                new OfflineFeature(22,true).silentDataDump(getApplicationContext());
            }
        });


    }
}
