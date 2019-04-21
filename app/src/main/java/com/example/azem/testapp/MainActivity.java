package com.example.azem.testapp;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;



import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;



public class MainActivity extends AppCompatActivity implements LocationListener {


    TextView txtInfo, txtPolluant, txtPollutionLevel, txtyourLocation, txtstatiionLocation;
    private BroadcastReceiver minuteUpdateRecievier;
    int a;
    Button btnMetric;


    LocationManager locationManager;
    String longitude, latitude;
    LinearLayout layout;
    int found;
    TextView txtDes,txtTit;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    ImageView btnStud,btnIndoor,btnSport,btnSensetiveGroup;
    ImageView btnOutdoor;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        btnMetric =(Button) findViewById(R.id.btnMetric);
        layout = (LinearLayout) findViewById(R.id.layout);

        txtPolluant = (TextView) findViewById(R.id.txtPolluant);
        txtPollutionLevel = (TextView) findViewById(R.id.txtPollutionLevel);
       txtTit=(TextView) findViewById(R.id.txtTit);
        txtDes=(TextView)findViewById(R.id.txtDes);
        btnStud=(ImageView) findViewById(R.id.btnStud);
       btnOutdoor = (ImageView) findViewById(R.id.btnOutdoor);
       btnSport=(ImageView) findViewById(R.id.btnSports);
       btnSensetiveGroup=(ImageView)findViewById(R.id.btnSensitiveGruop);
       btnIndoor=(ImageView)findViewById(R.id.btnIndoor);

        txtyourLocation = (TextView) findViewById(R.id.txtyourLocation);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        else {

           mainMethod();
        }

    }


    public String distance(String lati1, String loni1, double lat2, double lon2, String unit) {
        double lat1 = Double.parseDouble(lati1);
        double lon1 = Double.parseDouble(loni1);
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }
        return df2.format(dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void againApp() {

            Log.e("latitude1",latitude);
            Log.e("longitude1",longitude);
        handler.postDelayed(new Runnable() {

            public void run() {
                new HttpAsyncTask() {



                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        final JsonParser jsonParser = new JsonParser(s);
                        txtPollutionLevel.setText(jsonParser.getValue());
                        txtInfo.setText(jsonParser.checkAQI(jsonParser.getValue()));
                        btnMetric.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDialog(jsonParser.getCity(),jsonParser.getDate(),distance(latitude,longitude,Double.parseDouble(jsonParser.getLatitude()),Double.parseDouble(jsonParser.getLongitude()),"K"));
                            }
                        });
                        txtPolluant.setText(jsonParser.getParameter()+" Dominant");

                        int id = getResources().getIdentifier(jsonParser.getBackgroundColor(), "drawable", getPackageName());
                        Drawable drawable = getResources().getDrawable(id);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            layout.setBackground(drawable);
                        }

                        btnStud.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtTit.setText("Students");
                                txtDes.setText(jsonParser.getDesStud());

                                changeColor(btnStud,jsonParser.getBackground());
                            }
                        });
                        btnIndoor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtTit.setText("Indoor");
                                txtDes.setText(jsonParser.getDesIn());
                                changeColor(btnIndoor,jsonParser.getBackground());
                            }
                        });
                        btnOutdoor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtTit.setText("Outdoor");
                                txtDes.setText(jsonParser.getDesOut());
                                changeColor(btnOutdoor,jsonParser.getBackground());
                            }
                        });
                        btnSensetiveGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtTit.setText("Health Sensitivities");
                                txtDes.setText(jsonParser.getDesSens());
                                changeColor(btnSensetiveGroup,jsonParser.getBackground());
                            }
                        });
                        btnSport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtTit.setText("Sports Activities");
                                txtDes.setText(jsonParser.getDesSpor());
                                changeColor(btnSport,jsonParser.getBackground());
                            }
                        });


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
                            View view=contentView.getChildAt(1);
                            view.setBackgroundColor(Color.parseColor("#"+jsonParser.getBackground()));
                        } else {
                            Window window=getWindow();
                            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                                window.setStatusBarColor(Color.parseColor("#"+jsonParser.getBackground()));

                                window.setNavigationBarColor(Color.parseColor("#"+jsonParser.getBackground()));
                            }
                        }
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#"+jsonParser.getBackground())));


                    }
                }.execute("https://api.waqi.info/feed/geo:"+latitude+";"+longitude+"/?token=token");

                Log.e("latitude",latitude);
                Log.e("longitue",longitude);
                new HttpAsyncTask() {

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        JsonParser js = new JsonParser(s, "location");
                        Log.e("locationi", js.getYourLocation());
                        if(js.getYourLocation().equals(new String(js.getYourCity()))) {

                            txtyourLocation.setText(js.getYourCity() + ", " + js.getYourCountry());

                        }
                        else
                        {

                            txtyourLocation.setText(js.getYourLocation() + ", " + js.getYourCity() + ", " + js.getYourCountry());
                        }

                    }
                }.execute("http://api.geonames.org/findNearbyPlaceNameJSON?lat="+latitude+"&lng="+longitude+"&username=user");


                handler.postDelayed(this, 60000);
            }
        }, 60000);

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public void mainMethod()
    {

        if(isLocationEnabled(this.getApplicationContext())) {

            getLocation();
            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setMessage("Loading...");
            progressDialog.show();

            new HttpAsyncTask() {


                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    progressDialog.dismiss();
                    final JsonParser jsonParser = new JsonParser(s);
                    txtInfo.setText(jsonParser.checkAQI(jsonParser.getValue()));
                    txtPollutionLevel.setText(jsonParser.getValue());
                    txtPolluant.setText(jsonParser.getParameter()+" Dominant");
                    btnMetric.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog(jsonParser.getCity(),jsonParser.getDate(),distance(latitude, longitude, Double.parseDouble(jsonParser.getLatitude()), Double.parseDouble(jsonParser.getLongitude()), "K"));
                        }
                    });

                    int id = getResources().getIdentifier(jsonParser.getBackgroundColor(), "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        layout.setBackground(drawable);
                    }
                    btnIndoor.setColorFilter(Color.parseColor("#"+jsonParser.getBackground()));
                    txtTit.setText("Indoor");
                    txtDes.setText(jsonParser.getDesStud());

                    btnStud.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            txtTit.setText("Students");
                            txtDes.setText(jsonParser.getDesStud());

                            changeColor(btnStud,jsonParser.getBackground());
                        }
                    });
                    btnIndoor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            txtTit.setText("Indoor");
                            txtDes.setText(jsonParser.getDesIn());
                            changeColor(btnIndoor,jsonParser.getBackground());
                        }
                    });
                    btnOutdoor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            txtTit.setText("Outdoor");
                            txtDes.setText(jsonParser.getDesOut());
                            changeColor(btnOutdoor,jsonParser.getBackground());
                        }
                    });
                    btnSensetiveGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            txtTit.setText("Health Sensitivities");
                            txtDes.setText(jsonParser.getDesSens());
                            changeColor(btnSensetiveGroup,jsonParser.getBackground());
                        }
                    });
                    btnSport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            txtTit.setText("Sports Activities");
                            txtDes.setText(jsonParser.getDesSpor());
                            changeColor(btnSport,jsonParser.getBackground());
                        }
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
                        View view=contentView.getChildAt(1);
                        view.setBackgroundColor(Color.parseColor("#"+jsonParser.getBackground()));
                    } else {
                        Window window=getWindow();
                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                            window.setStatusBarColor(Color.parseColor("#"+jsonParser.getBackground()));

                            window.setNavigationBarColor(Color.parseColor("#"+jsonParser.getBackground()));
                        }
                    } getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#"+jsonParser.getBackground())));
                }

            }.execute("https://api.waqi.info/feed/geo:"+latitude+";"+longitude+"/?token=token");


            new HttpAsyncTask() {

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                      JsonParser js = new JsonParser(s, "location");
                    Log.e("locationi", js.getYourLocation());
                    if(js.getYourLocation().equals(new String(js.getYourCity()))) {

                        txtyourLocation.setText(js.getYourCity() + ", " + js.getYourCountry());

                    }
                    else
                    {

                        txtyourLocation.setText(js.getYourLocation() + ", " + js.getYourCity() + ", " + js.getYourCountry());
                    }

                }
            }.execute("http://api.geonames.org/findNearbyPlaceNameJSON?lat="+latitude+"&lng="+longitude+"&username=user");

            againApp();
        }

        else {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("App notificaton");
                builder.setMessage("Want to enable GPS ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtInfo.setText("If you want to use app , should enable location !");
                        txtPollutionLevel.setText("-");
                        txtPolluant.setText("-");
                        txtyourLocation.setText("-");
                    }
                });
                builder.create().show();

                return;
            }
        }



    }






    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latitude=df2.format(location.getLatitude());
            longitude=df2.format(location.getLongitude());

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        latitude=df2.format(location.getLatitude());
        longitude=df2.format(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    public void openDialog(String location,String time,String distance) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Metric Station");
        final ImageButton btnClose=(ImageButton)dialog.findViewById(R.id.btnClose);
        final TextView txtName =(TextView)dialog.findViewById(R.id.txtName);
        final TextView txtDate =(TextView)dialog.findViewById(R.id.txtDate);
        final TextView txtDistance =(TextView)dialog.findViewById(R.id.txtDistance);

        txtName.setText(location);

        txtDate.setText(time);
        txtDistance.setText(distance+" km");
        int color = Color.parseColor("#2EAD5E");
        btnClose.setColorFilter(color);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void changeColor(ImageView btnMain,String color)
    {
        btnIndoor.setColorFilter(Color.parseColor("#000000"));
        btnOutdoor.setColorFilter(Color.parseColor("#000000"));
        btnStud.setColorFilter(Color.parseColor("#000000"));
        btnSensetiveGroup.setColorFilter(Color.parseColor("#000000"));
        btnSport.setColorFilter(Color.parseColor("#000000"));
        btnMain.setColorFilter(Color.parseColor("#"+color));
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        else {

            mainMethod();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        else {
            handler.removeCallbacksAndMessages(null);
            locationManager.removeUpdates(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        locationManager.removeUpdates(this);
    }
}



