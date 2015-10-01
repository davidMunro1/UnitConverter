package com.davidtanerconverter.unitconverter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends ActionBarActivity {

    static Spinner currencyFrom, currencyTo, spinnerFrom, spinnerTo;
    static String convertTo, convertFrom, too;
    static EditText input;
    static TextView units, result, conversionRes;
    static double amount;
    static Button convert;
    static DecimalFormat df = new DecimalFormat("#.############");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_ThemeOverlay_AppCompat_Dark);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);
        ImageView im = (ImageView)findViewById(R.id.startupImage);
        im.setVisibility(View.INVISIBLE);

        final CountDownTimer timer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ImageView im = (ImageView)findViewById(R.id.startupImage);
                im.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void ButtonOnClick(View v){
        switch (v.getId()){
            case R.id.currency:

                if(checkNetwork()){
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                            .replace(R.id.container, new CurrencyConverter())
                            .commit();
                }
                else if(!checkNetwork()){
                    final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("No network connection").setIcon(R.drawable.icon_warning);
                    alert.setMessage("No network connection, please connect to a network and restart application")
                            .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }

                break;
            case R.id.time:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new TimeConverter())
                        .commit();
                break;
            case R.id.speed:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new SpeedConverter())
                        .commit();
                break;
            case R.id.weightmass:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new WeightMassConverter())
                        .commit();
                break;
            case R.id.distance:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new DistanceConverter())
                        .commit();
                break;
            case R.id.percent_frag:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new PercentageCalculator())
                        .commit();
                break;
            case R.id.datasize:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new DatasizeConverter())
                        .commit();
                break;
            case R.id.hexBinaryDec:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new HexBinaryDecimal())
                        .commit();
                break;
            case R.id.temperature:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new TemperatureConverter())
                        .commit();
                break;
            case R.id.area:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new AreaConverter())
                        .commit();
                break;
            case R.id.angle:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out)
                        .replace(R.id.container, new AngleConverter())
                        .commit();
                break;
        }
    }

    private boolean checkNetwork(){
        ConnectivityManager connectionMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionMan.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class CurrencyConverter extends Fragment{
        ImageButton imageButton;
        float amount;
        String [] curr;

        public CurrencyConverter() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            getActivity().setContentView(R.layout.activity_main);
            Resources res = getResources();
            curr = res.getStringArray(R.array.currencies);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.currency, container, false);
            currencyConvert(rootView);
            return rootView;
        }

        private void currencyConvert(View root){
            currencyFrom = (Spinner)root.findViewById(R.id.fromSpinner);
            currencyTo = (Spinner)root.findViewById(R.id.toSpinner);
            convert = (Button)root.findViewById(R.id.convertButton);
            input = (EditText)root.findViewById(R.id.userInput);
            conversionRes = (TextView)root.findViewById(R.id.conversionResult);
            units = (TextView)root.findViewById(R.id.units);
            imageButton = (ImageButton)root.findViewById(R.id.countryButton);
            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            ArrayAdapter <String> currencyAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, curr);
            currencyFrom.setAdapter(currencyAdapter);
            currencyTo.setAdapter(currencyAdapter);

            currencyTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(currencyTo.getSelectedItem().toString().equalsIgnoreCase("USD")){
                        imageButton.setImageResource(R.drawable.ic_usa);
                    }else if(currencyTo.getSelectedItem().toString().equalsIgnoreCase("GBP")){
                        imageButton.setImageResource(R.drawable.ic_gb);
                    }else if(currencyTo.getSelectedItem().toString().equalsIgnoreCase("SEK")){
                        imageButton.setImageResource(R.drawable.ic_sweden);
                    }else if(currencyTo.getSelectedItem().toString().equalsIgnoreCase("DKK")){
                        imageButton.setImageResource(R.drawable.ic_denmark);
                    }else if(currencyTo.getSelectedItem().toString().equalsIgnoreCase("NOK")){
                        imageButton.setImageResource(R.drawable.ic_norway);
                    }else if(currencyTo.getSelectedItem().toString().equalsIgnoreCase("EUR")){
                        imageButton.setImageResource(R.drawable.ic_euro);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if (input.getText().length() == 0) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                        alert.setTitle("ERROR");
                                        alert.setIcon(R.drawable.icon_warning);
                                        alert.setMessage("Amount can not be 0").setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();
                                    }
                                });
                            } else {

                                float m = getCurrency(currencyFrom.getSelectedItem().toString(), currencyTo.getSelectedItem().toString());
                                float d = Float.parseFloat(String.valueOf(input.getText()));
                                amount = m * d;

                                v.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        conversionRes.setText(String.valueOf(amount));
                                        units.setText(currencyTo.getSelectedItem().toString());
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });
        }

        public float getCurrency(String from, String to){

            //create HTTP client
            HttpClient client = new DefaultHttpClient();

            //specify the httpget command with the url, that will return a csv file
            HttpGet get = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + from + to + "=X&f=l1&e=.csv");
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = null;
            try {
                responseBody = client.execute(get, responseHandler);
            } catch (IOException e) {
                System.out.println(e);
            }
            client.getConnectionManager().shutdown();

            //the float that will be returned is the value of 1 (from) to however many to
            //DKK 1 -> 1.21 SEK
            return Float.parseFloat(responseBody);
        }
    }

    public static class TimeConverter extends Fragment implements AdapterView.OnItemSelectedListener {
        private String [] time;

        public TimeConverter() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            Resources res = getResources();
            time = res.getStringArray(R.array.time);
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.time, container, false);

            ArrayAdapter <String> timeAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, time);
            spinnerFrom = (Spinner) rootView.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) rootView.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(timeAdapter);
            spinnerTo.setAdapter(timeAdapter);
            convert = (Button)rootView.findViewById(R.id.convertButton);
            result = (TextView)rootView.findViewById(R.id.conversionResult);
            units = (TextView)rootView.findViewById(R.id.units);
            input = (EditText)rootView.findViewById(R.id.userInput);
            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();

                    if(too.equalsIgnoreCase("")){
                        too = "0.0";
                    }
                    amount = Double.valueOf(too);

                    if(convertFrom.equalsIgnoreCase("milliseconds")){
                        fromMilli(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                    else if(convertFrom.equalsIgnoreCase("seconds")){
                        fromSeconds(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                    else if(convertFrom.equalsIgnoreCase("minutes")){
                        fromMinutes(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                    else if(convertFrom.equalsIgnoreCase("hours")){
                        fromHours(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                    else{
                        result.setText(too);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                }
            });
            return rootView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

        public void fromMilli(double amt){
            double ms;
            if(convertTo.equalsIgnoreCase("milliseconds")){
                result.setText(input.getText());
            }
            else if(convertTo.equalsIgnoreCase("seconds")){
                ms = amt / 1000;
                result.setText(String.valueOf(df.format(ms)));
            }
            else if(convertTo.equalsIgnoreCase("minutes")){
                ms = amt / 60000;
                result.setText(String.valueOf(df.format(ms)));
            }
            else if(convertTo.equalsIgnoreCase("hours")){
                ms = amt / 3600000;
                result.setText(String.valueOf(df.format(ms)));
            }
        }

        public void fromSeconds(double amt){
            double s;
            DecimalFormat df = new DecimalFormat("#.##########");
            if(convertTo.equalsIgnoreCase("milliseconds")){
                s = amt * 1000;
                result.setText(String.valueOf(df.format(s)));
            }
            else if(convertTo.equalsIgnoreCase("seconds")){
                result.setText(input.getText());
            }
            else if(convertTo.equalsIgnoreCase("minutes")){
                s = amt / 60;
                result.setText(String.valueOf(df.format(s)));
            }
            else if(convertTo.equalsIgnoreCase("hours")){
                s = amt / 3600;
                result.setText(String.valueOf(df.format(s)));
            }
        }

        public void fromMinutes(double amt){
            double m;
            DecimalFormat df = new DecimalFormat("#.##########");
            if(convertTo.equalsIgnoreCase("milliseconds")){
                m = amt * 60000;
                result.setText(String.valueOf(df.format(m)));
            }
            else if(convertTo.equalsIgnoreCase("seconds")){
                m = amt * 60;
                result.setText(String.valueOf(df.format(m)));
            }
            else if(convertTo.equalsIgnoreCase("minutes")){
                result.setText(input.getText());
            }
            else if(convertTo.equalsIgnoreCase("hours")){
                m = amt / 60;
                result.setText(String.valueOf(df.format(m)));
            }
        }

        public void fromHours(double amt){
            double h;
            DecimalFormat df = new DecimalFormat("#.##########");
            if(convertTo.equalsIgnoreCase("milliseconds")){
                h = amt * 3600000;
                result.setText(String.valueOf(df.format(h)));
            }
            else if(convertTo.equalsIgnoreCase("seconds")){
                h = amt * 3600;
                result.setText(String.valueOf(df.format(h)));
            }
            else if(convertTo.equalsIgnoreCase("minutes")){
                h = amt * 60;
                result.setText(String.valueOf(df.format(h)));
            }
            else if(convertTo.equalsIgnoreCase("hours")){
                result.setText(input.getText());
            }
        }
    }

    public static class SpeedConverter extends Fragment {
        String[] differentSpeed;

        public SpeedConverter() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Resources res = getResources();
            differentSpeed = res.getStringArray(R.array.differendSpeed);
            View rootView = inflater.inflate(R.layout.speed, container, false);
            ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, differentSpeed);
            spinnerFrom = (Spinner) rootView.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) rootView.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(timeAdapter);
            spinnerTo.setAdapter(timeAdapter);
            convert = (Button) rootView.findViewById(R.id.convertButton);
            result = (TextView) rootView.findViewById(R.id.conversionResult);
            units = (TextView)rootView.findViewById(R.id.units);
            input = (EditText) rootView.findViewById(R.id.userInput);

            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();

                    if (too.equalsIgnoreCase("")) {
                        too = "0.0";
                    }
                    amount = Double.valueOf(too);

                    if (convertFrom.equalsIgnoreCase("Km/h")) {
                        fromKmH(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    } else if (convertFrom.equalsIgnoreCase("Knot")) {
                        fromKnot(amount);units.setText(spinnerTo.getSelectedItem().toString());
                    } else if (convertFrom.equalsIgnoreCase("Mach")) {
                        fromMach(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    } else if (convertFrom.equalsIgnoreCase("Meters/s")) {
                        fromMs(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                    else if (convertFrom.equalsIgnoreCase("Miles/h")) {
                        fromMilesH(amount);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                    else {
                        result.setText(too);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                }
            });
            return rootView;

        }

        private void fromKmH(double amt) {
            double kmH;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Km/h")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Knot")) {
                kmH = amt * 0.53995;
                result.setText(String.valueOf(df.format(kmH)));
            } else if (convertTo.equalsIgnoreCase("Mach")) {
                kmH = amt * 0.00082;
                result.setText(String.valueOf(df.format(kmH)));
            } else if (convertTo.equalsIgnoreCase("Meters/s")) {
                kmH = amt * 0.2777;
                result.setText(String.valueOf(df.format(kmH)));
            } else if (convertTo.equalsIgnoreCase("Miles/h")) {
                kmH = amt * 0.6213;
                result.setText(String.valueOf(df.format(kmH)));
            }
        }

        private void fromKnot(double amt) {
            double knot;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Knot")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Km/h")) {
                knot = amt * 1.85200;
                result.setText(String.valueOf(df.format(knot)));
            } else if (convertTo.equalsIgnoreCase("Mach")) {
                knot = amt * 0.001511;
                result.setText(String.valueOf(df.format(knot)));
            } else if (convertTo.equalsIgnoreCase("Meters/s")) {
                knot = amt * 0.5144;
                result.setText(String.valueOf(df.format(knot)));
            } else if (convertTo.equalsIgnoreCase("Miles/h")) {
                knot = amt * 1.1507;
                result.setText(String.valueOf(df.format(knot)));
            }
        }

        private void fromMach(double amt) {
            double mach;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Mach")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Km/h")) {
                mach = amt * 1225.0;
                result.setText(String.valueOf(df.format(mach)));
            } else if (convertTo.equalsIgnoreCase("Knot")) {
                mach = amt * 661.47;
                result.setText(String.valueOf(df.format(mach)));
            } else if (convertTo.equalsIgnoreCase("Meters/s")) {
                mach = amt * 340.29;
                result.setText(String.valueOf(df.format(mach)));
            } else if (convertTo.equalsIgnoreCase("Miles/h")) {
                mach = amt * 761.207;
                result.setText(String.valueOf(df.format(mach)));
            }
        }

        private void fromMs(double amt) {
            double ms;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Meters/s")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Km/h")) {
                ms = amt * 3.6;
                result.setText(String.valueOf(df.format(ms)));
            } else if (convertTo.equalsIgnoreCase("Knot")) {
                ms = amt * 1.9438;
                result.setText(String.valueOf(df.format(ms)));
            } else if (convertTo.equalsIgnoreCase("Mach")) {
                ms = amt * 0.002938;
                result.setText(String.valueOf(df.format(ms)));
            } else if (convertTo.equalsIgnoreCase("Miles/h")) {
                ms = amt * 2.2369;
                result.setText(String.valueOf(df.format(ms)));
            }
        }

        private void fromMilesH(double amt) {
            double milesH;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Miles/h")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Km/h")) {
                milesH = amt * 1.6093;
                result.setText(String.valueOf(df.format(milesH)));
            } else if (convertTo.equalsIgnoreCase("Knot")) {
                milesH = amt * 0.8689;
                result.setText(String.valueOf(df.format(milesH)));
            } else if (convertTo.equalsIgnoreCase("Mach")) {
                milesH = amt * 0.001313;
                result.setText(String.valueOf(df.format(milesH)));
            }
            else if (convertTo.equalsIgnoreCase("Meters/s")) {
                milesH = amt * 0.4470;
                result.setText(String.valueOf(df.format(milesH)));
            }
        }
    }

    public static class AngleConverter extends Fragment {

        private String [] differentAngles;

        public AngleConverter() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            Resources res = getResources();
            differentAngles = res.getStringArray(R.array.differentAngles);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.angle, container, false);
            convertAngle(rootView);
            return rootView;
        }

        private void convertAngle(View root){
            ArrayAdapter<String> angleAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, differentAngles);
            spinnerFrom = (Spinner) root.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) root.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(angleAdapter);
            spinnerTo.setAdapter(angleAdapter);
            convert = (Button) root.findViewById(R.id.convertButton);
            result = (TextView) root.findViewById(R.id.conversionResult);
            units = (TextView)root.findViewById(R.id.units);
            input = (EditText) root.findViewById(R.id.userInput);
            df = new DecimalFormat("#.##########");

            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();

                    if (too.equalsIgnoreCase("")) {
                        too = "0.0";
                    }
                    amount = Double.valueOf(too);

                    if (convertFrom.equalsIgnoreCase("Degree")) {
                        result.setText(String.valueOf(df.format(fromDegree(amount))));
                        units.setText(spinnerTo.getSelectedItem().toString());
                    } else if (convertFrom.equalsIgnoreCase("Radian")) {
                        result.setText(String.valueOf(df.format(fromRadian(amount))));
                        units.setText(spinnerTo.getSelectedItem().toString());
                    } else if (convertFrom.equalsIgnoreCase("Full circle")) {
                        result.setText(String.valueOf(df.format(fromFullCircle(amount))));
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }else {
                        result.setText(too);
                        units.setText(spinnerTo.getSelectedItem().toString());
                    }
                }
            });
        }

        private double fromDegree(double amt){
            double degree = 0;
            if(convertTo.equalsIgnoreCase("degree")){
                degree = amt;
            }else if(convertTo.equalsIgnoreCase("radian")){
                degree = amt*0.01745329252;
            }else if(convertTo.equalsIgnoreCase("full circle")){
                degree = amt/360;
            }
            return degree;
        }

        private double fromRadian(double amt){
            double radian = 0;
            if(convertTo.equalsIgnoreCase("degree")){
                radian = amt*57.2957795;
            }else if(convertTo.equalsIgnoreCase("radian")){
                radian = amt;
            }else if(convertTo.equalsIgnoreCase("full circle")){
                radian = amt*0.159155;
            }
            return radian;
        }

        private double fromFullCircle(double amt){
            double fullCircle = 0;
            if(convertTo.equalsIgnoreCase("degree")){
                fullCircle = amt*360;
            }else if(convertTo.equalsIgnoreCase("radian")){
                fullCircle = amt*6.28319;
            }else if(convertTo.equalsIgnoreCase("full circle")){
                fullCircle = amt;
            }
            return fullCircle;
        }
    }

    public static class DatasizeConverter extends Fragment {
        String[] differentDataSizes;
        BigDecimal bigDOfAmt;
        BigDecimal multi;
        BigDecimal res;

        public DatasizeConverter() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.datasize, container, false);
            Resources res = getResources();
            differentDataSizes = res.getStringArray(R.array.differentDataSize);
            ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, differentDataSizes);
            spinnerFrom = (Spinner) rootView.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) rootView.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(timeAdapter);
            spinnerTo.setAdapter(timeAdapter);
            convert = (Button) rootView.findViewById(R.id.convertButton);
            result = (TextView) rootView.findViewById(R.id.conversionResult);
            input = (EditText) rootView.findViewById(R.id.userInput);
            units = (TextView) rootView.findViewById(R.id.units);

            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();

                    if (too.equalsIgnoreCase("")) {
                        too = "0.0";
                    }
                    amount = Double.valueOf(too);

                    if (convertFrom.equalsIgnoreCase("Bit")) {
                        fromBit(amount);
                    } else if (convertFrom.equalsIgnoreCase("Byte")) {
                        fromByte(amount);
                    } else if (convertFrom.equalsIgnoreCase("Kilobyte")) {
                        fromKilobyte(amount);
                    } else if (convertFrom.equalsIgnoreCase("Megabyte")) {
                        fromMegabyte(amount);
                    } else if (convertFrom.equalsIgnoreCase("Gigabyte")) {
                        fromGigabyte(amount);
                    } else if(convertFrom.equalsIgnoreCase("Terabyte")){
                        fromTerabyte(amount);
                    }else if(convertFrom.equalsIgnoreCase("Petabyte")){
                        fromPetabyte(amount);
                    }else{
                        result.setText(too);
                    }
                }
            });
            return rootView;
        }

        private void fromBit(double amt) {
            double bit;

            df = new DecimalFormat("#.###############");

            if (convertTo.equalsIgnoreCase("bit")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("byte")) {
                bit = amt * 0.125;
                result.setText(String.valueOf(df.format(bit)));
            } else if (convertTo.equalsIgnoreCase("Kilobyte")) {
                bit = amt * 0.000125;
                result.setText(String.valueOf(df.format(bit)));
            } else if (convertTo.equalsIgnoreCase("Megabyte")) {
                bit = amt * 0.000000125;
                result.setText(String.valueOf(df.format(bit)));

            } else if (convertTo.equalsIgnoreCase("Gigabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.0000000001164153);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
                System.out.println(r);
            } else if (convertTo.equalsIgnoreCase("Terabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.0000000000001137);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("Petabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.00000000000000008);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            }
            units.setText(convertTo +"s");
        }

        private void fromByte(double amt) {
            double bytes;

            df = new DecimalFormat("#.#############");

            if (convertTo.equalsIgnoreCase("byte")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("bit")) {
                bytes = amt * 8;
                result.setText(String.valueOf(df.format(bytes)));
            } else if (convertTo.equalsIgnoreCase("Kilobyte")) {
                bytes = amt * 0.0009765625;
                result.setText(String.valueOf(df.format(bytes)));
            } else if (convertTo.equalsIgnoreCase("Megabyte")) {
                bytes = amt * 0.0000009536743164;
                result.setText(String.valueOf(df.format(bytes)));
            } else if (convertTo.equalsIgnoreCase("Gigabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.0000000009313226);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("Terabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.0000000000009095);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("Petabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.0000000000000009);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            }
            units.setText(convertTo +"s");
        }

        private void fromKilobyte(double amt) {
            double kilobytes;

            if (convertTo.equalsIgnoreCase("Kilobyte")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("bit")) {
                kilobytes = amt * 8192;
                result.setText(String.valueOf(df.format(kilobytes)));
            } else if (convertTo.equalsIgnoreCase("byte")) {
                kilobytes = amt * 1024;
                result.setText(String.valueOf(df.format(kilobytes)));
            } else if (convertTo.equalsIgnoreCase("Megabyte")) {
                kilobytes = amt * 0.0009765625;
                result.setText(String.valueOf(df.format(kilobytes)));
            } else if (convertTo.equalsIgnoreCase("Gigabyte")) {
                kilobytes = amt * 0.0000009536743164;
                result.setText(String.valueOf(df.format(kilobytes)));
            } else if (convertTo.equalsIgnoreCase("Terabyte")) {
                kilobytes = amt*0.0000000009313226;
                result.setText(String.valueOf(df.format(kilobytes)));
            } else if (convertTo.equalsIgnoreCase("Petabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.0000000000009095);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            }
            units.setText(convertTo.toString() +"s");
        }

        private void fromMegabyte(double amt) {
            double megabyte;
            if (convertTo.equalsIgnoreCase("Megabyte")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("bit")) {
                megabyte = amt * 8388608;
                result.setText(String.valueOf(df.format(megabyte)));
            } else if (convertTo.equalsIgnoreCase("byte")) {
                megabyte = amt * 1048576;
                result.setText(String.valueOf(df.format(megabyte)));
            } else if (convertTo.equalsIgnoreCase("Kilobyte")) {
                megabyte = amt * 1048.576;
                result.setText(String.valueOf(df.format(megabyte)));
            } else if (convertTo.equalsIgnoreCase("Gigabyte")) {
                megabyte = amt * 0.0009765625;
                result.setText(String.valueOf(df.format(megabyte)));
            } else if (convertTo.equalsIgnoreCase("Terabyte")) {
                megabyte = amt * 0.0000009536743164;
                result.setText(String.valueOf(df.format(megabyte)));
            } else if (convertTo.equalsIgnoreCase("Petabyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(0.0000000009313226);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            }
            units.setText(convertTo.toString() +"s");
        }

        private void fromGigabyte(double amt) {
            double gigabyte;

            if (convertTo.equalsIgnoreCase("Gigabyte")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("bit")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(8589934592.0);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);

            } else if (convertTo.equalsIgnoreCase("byte")) {
                gigabyte = amt * 1073741824;
                result.setText(String.valueOf(df.format(gigabyte)));
            } else if (convertTo.equalsIgnoreCase("Kilobyte")) {
                gigabyte = amt * 1048576;
                result.setText(String.valueOf(df.format(gigabyte)));
            } else if (convertTo.equalsIgnoreCase("Megabyte")) {
                gigabyte = amt * 1024;
                result.setText(String.valueOf(df.format(gigabyte)));
            } else if (convertTo.equalsIgnoreCase("Terabyte")) {
                gigabyte = amt * 0.0009765625;
                result.setText(String.valueOf(df.format(gigabyte)));
            } else if (convertTo.equalsIgnoreCase("Petabyte")) {
                gigabyte = amt * 0.0000009536743164;
                result.setText(String.valueOf(df.format(gigabyte)));
            }
            units.setText(convertTo +"s");
        }

        private void fromTerabyte(double amt){
            double terabyte;

            if (convertTo.equalsIgnoreCase("Terabyte")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("bit")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(8796093022210.0);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("byte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(1099511627776.0);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("Kilobyte")) {
                terabyte = amt * 1073741824;
                result.setText(String.valueOf(df.format(terabyte)));
            } else if (convertTo.equalsIgnoreCase("Megabyte")) {
                terabyte = amt * 1048576;
                result.setText(String.valueOf(df.format(terabyte)));
            } else if (convertTo.equalsIgnoreCase("Gigabyte")) {
                terabyte = amt * 1024;
                result.setText(String.valueOf(df.format(terabyte)));
            } else if (convertTo.equalsIgnoreCase("Petabyte")) {
                terabyte = amt * 0.0009765625;
                result.setText(String.valueOf(df.format(terabyte)));
            }
            units.setText(convertTo.toString() +"s");
        }

        private void fromPetabyte(double amt){
            double petabyte;

            df = new DecimalFormat("#.###########");

            if (convertTo.equalsIgnoreCase("Petabyte")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("bit")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(9007199254740000.0);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("byte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(1125899906842620.0);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("Kilobyte")) {
                bigDOfAmt = BigDecimal.valueOf(amt);
                multi = BigDecimal.valueOf(1099511627776.0);
                res = bigDOfAmt.multiply(multi);
                String r = res.toString();
                result.setText(r);
            } else if (convertTo.equalsIgnoreCase("Megabyte")) {
                petabyte = amt * 1073741824;
                result.setText(String.valueOf(df.format(petabyte)));
            } else if (convertTo.equalsIgnoreCase("Gigabyte")) {
                petabyte = amt * 1048576;
                result.setText(String.valueOf(df.format(petabyte)));
            } else if (convertTo.equalsIgnoreCase("Terabyte")) {
                petabyte = amt * 1024;
                result.setText(String.valueOf(df.format(petabyte)));
            }
            units.setText(convertTo.toString() +"s");
        }
    }

    public static class DistanceConverter extends Fragment {

        String [] differentDistances;

        public DistanceConverter() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.distance, container, false);
            Resources res = getResources();
            differentDistances = res.getStringArray(R.array.differentDistances);
            ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, differentDistances);
            spinnerFrom = (Spinner) rootView.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) rootView.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(timeAdapter);
            spinnerTo.setAdapter(timeAdapter);
            convert = (Button) rootView.findViewById(R.id.convertButton);
            result = (TextView) rootView.findViewById(R.id.conversionResult);
            input = (EditText) rootView.findViewById(R.id.userInput);
            units = (TextView) rootView.findViewById(R.id.units);

            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();

                    if (too.equalsIgnoreCase("")) {
                        too = "0.0";
                    }
                    amount = Double.valueOf(too);
                    DecimalFormat df = new DecimalFormat("#.#######");

                    if (convertFrom.equalsIgnoreCase("millimeter")) {
                        result.setText(String.valueOf(df.format(fromMilli(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("centimeter")) {
                        result.setText(String.valueOf(df.format(fromCentimeter(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("decimeter")) {
                        result.setText(String.valueOf(df.format(fromDecimeter(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("inch")) {
                        result.setText(String.valueOf(df.format(fromInch(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("foot")) {
                        result.setText(String.valueOf(df.format(fromFoot(amount))));
                        units.setText(convertTo);
                    } else if(convertFrom.equalsIgnoreCase("meter")){
                        result.setText(String.valueOf(df.format(fromMeter(amount))));
                        units.setText(convertTo);
                    }else if(convertFrom.equalsIgnoreCase("kilometer")){
                        result.setText(String.valueOf(df.format(fromKilometer(amount))));
                        units.setText(convertTo);
                    }else if(convertFrom.equalsIgnoreCase("mile")){
                        result.setText(String.valueOf(df.format(fromMile(amount))));
                        units.setText(convertTo);
                    }else{
                        result.setText(too);
                        units.setText(convertTo);
                    }
                }
            });
            return rootView;
        }

        private double fromMilli(double amt){
            double milli = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                milli = amt;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                milli = amt /10;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                milli = amt /100;
            }else if(convertTo.equalsIgnoreCase("inch")){
                milli = amt /25.4;
            }else if(convertTo.equalsIgnoreCase("foot")){
                milli = amt/304.8;
            }else if(convertTo.equalsIgnoreCase("meter")){
                milli = amt/1000;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                milli = amt/1000000;
            }else if(convertTo.equalsIgnoreCase("mile")){
                milli = amt/1609344;
            }
            return milli;
        }

        private double fromCentimeter(double amt){
            double centimeter = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                centimeter = amt/0.1;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                centimeter = amt;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                centimeter = amt /10;
            }else if(convertTo.equalsIgnoreCase("inch")){
                centimeter = amt /2.54;
            }else if(convertTo.equalsIgnoreCase("foot")){
                centimeter = amt/30.48;
            }else if(convertTo.equalsIgnoreCase("meter")){
                centimeter = amt/100;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                centimeter = amt/100000;
            }else if(convertTo.equalsIgnoreCase("mile")){
                centimeter = amt/160934.4;
            }
            return centimeter;
        }

        private double fromDecimeter(double amt){
            double decimeter = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                decimeter = amt/0.01;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                decimeter = amt/0.1;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                decimeter = amt;
            }else if(convertTo.equalsIgnoreCase("inch")){
                decimeter = amt*3.93701;
            }else if(convertTo.equalsIgnoreCase("foot")){
                decimeter = amt*0.328084;
            }else if(convertTo.equalsIgnoreCase("meter")){
                decimeter = amt*0.1;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                decimeter = amt*0.0001;
            }else if(convertTo.equalsIgnoreCase("mile")){
                decimeter = amt*0.0000621371;
            }
            return decimeter;
        }

        private double fromInch(double amt){
            double inch = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                inch = amt*25.4;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                inch = amt*2.54;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                inch = amt*0.254;
            }else if(convertTo.equalsIgnoreCase("inch")){
                inch = amt;
            }else if(convertTo.equalsIgnoreCase("foot")){
                inch = amt*0.0833333;
            }else if(convertTo.equalsIgnoreCase("meter")){
                inch = amt*0.0254;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                inch = amt*0.0000254;
            }else if(convertTo.equalsIgnoreCase("mile")){
                inch = amt*0.00001578283;
            }
            return inch;
        }

        private double fromFoot(double amt){
            double foot = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                foot = amt*304.8;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                foot = amt*30.48;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                foot = amt*3.048;
            }else if(convertTo.equalsIgnoreCase("inch")){
                foot = amt*12;
            }else if(convertTo.equalsIgnoreCase("foot")){
                foot = amt;
            }else if(convertTo.equalsIgnoreCase("meter")){
                foot = amt*0.3048;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                foot = amt*0.0003048;
            }else if(convertTo.equalsIgnoreCase("mile")){
                foot = amt*0.000189394;
            }
            return foot;
        }

        private double fromMeter(double amt){
            double meter = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                meter = amt*1000;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                meter = amt*100;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                meter = amt*10;
            }else if(convertTo.equalsIgnoreCase("inch")){
                meter = amt*39.3701;
            }else if(convertTo.equalsIgnoreCase("foot")){
                meter = amt*3.28084;
            }else if(convertTo.equalsIgnoreCase("meter")){
                meter = amt;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                meter = amt*0.001;
            }else if(convertTo.equalsIgnoreCase("mile")){
                meter = amt*0.000621371;
            }
            return meter;
        }

        private double fromKilometer(double amt){
            double kilometer = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                kilometer = amt*1000000;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                kilometer = amt*100000;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                kilometer = amt*1000;
            }else if(convertTo.equalsIgnoreCase("inch")){
                kilometer = amt*39370.1;
            }else if(convertTo.equalsIgnoreCase("foot")){
                kilometer = amt*3280.84;
            }else if(convertTo.equalsIgnoreCase("meter")){
                kilometer = amt*1000;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                kilometer = amt;
            }else if(convertTo.equalsIgnoreCase("mile")){
                kilometer = amt*0.621371;
            }
            return kilometer;
        }

        private double fromMile(double amt){
            double mile = 0;

            if(convertTo.equalsIgnoreCase("millimeter")){
                mile = amt*1609344;
            }else if(convertTo.equalsIgnoreCase("centimeter")){
                mile = amt*160934.4;
            }else if(convertTo.equalsIgnoreCase("decimeter")){
                mile = amt*16093.44;
            }else if(convertTo.equalsIgnoreCase("inch")){
                mile = amt*63360;
            }else if(convertTo.equalsIgnoreCase("foot")){
                mile = amt*5280;
            }else if(convertTo.equalsIgnoreCase("meter")){
                mile = amt*1609.344;
            }else if(convertTo.equalsIgnoreCase("kilometer")){
                mile = amt*1.609344;
            }else if(convertTo.equalsIgnoreCase("mile")){
                mile = amt;
            }
            return mile;
        }
    }

    public static class TemperatureConverter extends Fragment {

        String [] temp;

        public TemperatureConverter() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            getActivity().setContentView(R.layout.activity_main);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.temperature, container, false);
            convertTemperature(rootView);
            return rootView;
        }

        private void convertTemperature(View view){
            Resources res = getResources();
            temp = res.getStringArray(R.array.temperature);
            ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, temp);
            spinnerFrom = (Spinner) view.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) view.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(tempAdapter);
            spinnerTo.setAdapter(tempAdapter);
            convert = (Button) view.findViewById(R.id.convertButton);
            result = (TextView) view.findViewById(R.id.conversionResult);
            input = (EditText) view.findViewById(R.id.userInput);
            units = (TextView) view.findViewById(R.id.units);
            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));
            df = new DecimalFormat("#.####");

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();
                    if (too.equalsIgnoreCase("")) {
                        too = "0";
                    }
                    if (convertFrom.equalsIgnoreCase("Celsius")) {
                        fromCelsius();
                    } else if (convertFrom.equalsIgnoreCase("Fahrenheit")) {
                        fromFahrenheit();
                    } else {
                        result.setText(too);
                    }
                }
            });
        }

        private void fromCelsius(){

            if (convertTo.equalsIgnoreCase("fahrenheit")) {
                double am = Double.valueOf(input.getText().toString());
                amount = (am * 9/5.0)+32;
                result.setText(String.valueOf(df.format(amount)));
                units.setText("in " + convertTo);
            } else {
                result.setText(too);
                units.setText("in " + convertTo);
            }
        }

        private void fromFahrenheit(){
            if (convertTo.equalsIgnoreCase("celsius")) {
                double am = Double.valueOf(input.getText().toString());
                amount = (am -32)*(5/9.0);
                result.setText(String.valueOf(df.format(amount)));
                units.setText("in " + convertTo);
            } else {
                result.setText(too);
                units.setText("in " + convertTo);
            }
        }
    }

    public static class WeightMassConverter extends Fragment {
        String[] differentWeightAndMass;

        public WeightMassConverter() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.weightmass, container, false);
            Resources res = getResources();
            differentWeightAndMass = res.getStringArray(R.array.differentWeightAndMass);
            ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, differentWeightAndMass);
            spinnerFrom = (Spinner) rootView.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) rootView.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(timeAdapter);
            spinnerTo.setAdapter(timeAdapter);
            convert = (Button) rootView.findViewById(R.id.convertButton);
            result = (TextView) rootView.findViewById(R.id.conversionResult);
            units = (TextView)rootView.findViewById(R.id.units);
            input = (EditText) rootView.findViewById(R.id.userInput);

            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();

                    if (too.equalsIgnoreCase("")) {
                        too = "0.0";
                    }
                    amount = Double.valueOf(too);

                    if (convertFrom.equalsIgnoreCase("Kilogram")) {
                        fromKilogram(amount);
                        units.setText(convertTo +"/s");
                    } else if (convertFrom.equalsIgnoreCase("Gram")) {
                        fromGram(amount);
                        units.setText(convertTo +"/s");
                    } else if (convertFrom.equalsIgnoreCase("Stone")) {
                        fromStone(amount);
                        units.setText(convertTo +"/s");
                    }
                    else if (convertFrom.equalsIgnoreCase("Ounce")) {
                        fromOunce(amount);
                        units.setText(convertTo +"/s");
                    }
                    else if (convertFrom.equalsIgnoreCase("Pound")) {
                        fromPound(amount);
                        units.setText(convertTo +"/s");
                    }
                    else if (convertFrom.equalsIgnoreCase("Troy ounce")) {
                        fromTroyOunce(amount);
                        units.setText(convertTo +"/s");
                    }
                    else if (convertFrom.equalsIgnoreCase("Newton (Earth)")) {
                        fromNewton(amount);
                        units.setText(convertTo +"/s");
                    }
                    else {
                        result.setText(too);
                        units.setText(convertTo +"/s");
                    }
                }
            });
            return rootView;
        }

        public void fromKilogram(double amt) {
            double kg;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Kilogram")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Gram")) {
                kg = amt * 1000;
                result.setText(String.valueOf(df.format(kg)));
            }
            else if (convertTo.equalsIgnoreCase("Stone")) {
                kg = amt * 0.1574;
                result.setText(String.valueOf(df.format(kg)));
            }
            else if (convertTo.equalsIgnoreCase("Ounce")) {
                kg = amt * 35.2739;
                result.setText(String.valueOf(df.format(kg)));
            }
            else if (convertTo.equalsIgnoreCase("Pound")) {
                kg = amt * 2.2046;
                result.setText(String.valueOf(df.format(kg)));
            }
            else if (convertTo.equalsIgnoreCase("Troy ounce")) {
                kg = amt * 32.1507;
                result.setText(String.valueOf(df.format(kg)));
            }
            else if (convertTo.equalsIgnoreCase("Newton (Earth)")) {
                kg = amt * 9.8066;
                result.setText(String.valueOf(df.format(kg)));
            }
        }
        public void fromGram(double amt) {
            double gr;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Gram")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Kilogram")) {
                gr = amt * 0.001;
                result.setText(String.valueOf(df.format(gr)));
            }
            else if (convertTo.equalsIgnoreCase("Stone")) {
                gr = amt * 0.000157;
                result.setText(String.valueOf(df.format(gr)));
            }
            else if (convertTo.equalsIgnoreCase("Ounce")) {
                gr = amt * 0.03527;
                result.setText(String.valueOf(df.format(gr)));
            }
            else if (convertTo.equalsIgnoreCase("Troy ounce")) {
                gr = amt * 0.03215;
                result.setText(String.valueOf(df.format(gr)));
            }
            else if (convertTo.equalsIgnoreCase("Newton (Earth)")) {
                gr = amt * 0.0098;
                result.setText(String.valueOf(df.format(gr)));
            }
        }

        public void fromStone(double amt) {
            double stone;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Stone")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Kilogram")) {
                stone = amt * 6.3502;
                result.setText(String.valueOf(df.format(stone)));
            }
            else if (convertTo.equalsIgnoreCase("Gram")) {
                stone = amt * 6350.29318;
                result.setText(String.valueOf(df.format(stone)));
            }
            else if (convertTo.equalsIgnoreCase("Ounce")) {
                stone = amt * 224;
                result.setText(String.valueOf(df.format(stone)));
            }
            else if (convertTo.equalsIgnoreCase("Pound")) {
                stone = amt * 14;
                result.setText(String.valueOf(df.format(stone)));
            }
            else if (convertTo.equalsIgnoreCase("Troy ounce")) {
                stone = amt * 204.1666;
                result.setText(String.valueOf(df.format(stone)));
            }
            else if (convertTo.equalsIgnoreCase("Newton (Earth)")) {
                stone = amt * 62.28;
                result.setText(String.valueOf(df.format(stone)));
            }
        }
        public void fromOunce(double amt) {
            double ounce;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Ounce")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Kilogram")) {
                ounce = amt * 0.0283;
                result.setText(String.valueOf(df.format(ounce)));
            }
            else if (convertTo.equalsIgnoreCase("Gram")) {
                ounce = amt * 28.3495;
                result.setText(String.valueOf(df.format(ounce)));
            }
            else if (convertTo.equalsIgnoreCase("Stone")) {
                ounce = amt * 0.00446;
                result.setText(String.valueOf(df.format(ounce)));
            }
            else if (convertTo.equalsIgnoreCase("Pound")) {
                ounce = amt * 0.0625;
                result.setText(String.valueOf(df.format(ounce)));
            }
            else if (convertTo.equalsIgnoreCase("Troy ounce")) {
                ounce = amt * 0.9114;
                result.setText(String.valueOf(df.format(ounce)));
            }
            else if (convertTo.equalsIgnoreCase("Newton (Earth)")) {
                ounce = amt * 0.28;
                result.setText(String.valueOf(df.format(ounce)));
            }
        }
        public void fromPound(double amt) {
            double pound;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Pound")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Kilogram")) {
                pound = amt * 0.4535;
                result.setText(String.valueOf(df.format(pound)));
            }
            else if (convertTo.equalsIgnoreCase("Gram")) {
                pound = amt * 453.592;
                result.setText(String.valueOf(df.format(pound)));
            }
            else if (convertTo.equalsIgnoreCase("Stone")) {
                pound = amt * 0.07142;
                result.setText(String.valueOf(df.format(pound)));
            }
            else if (convertTo.equalsIgnoreCase("Ounce")) {
                pound = amt * 16;
                result.setText(String.valueOf(df.format(pound)));
            }
            else if (convertTo.equalsIgnoreCase("Troy ounce")) {
                pound = amt * 14.583;
                result.setText(String.valueOf(df.format(pound)));
            }
            else if (convertTo.equalsIgnoreCase("Newton (Earth)")) {
                pound = amt * 4.44;
                result.setText(String.valueOf(df.format(pound)));
            }
        }
        public void fromTroyOunce(double amt) {
            double troyOunce;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Troy ounce")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Kilogram")) {
                troyOunce = amt * 0.0311;
                result.setText(String.valueOf(df.format(troyOunce)));
            }
            else if (convertTo.equalsIgnoreCase("Gram")) {
                troyOunce = amt *31.1034;
                result.setText(String.valueOf(df.format(troyOunce)));
            }
            else if (convertTo.equalsIgnoreCase("Stone")) {
                troyOunce = amt * 0.00489;
                result.setText(String.valueOf(df.format(troyOunce)));
            }
            else if (convertTo.equalsIgnoreCase("Ounce")) {
                troyOunce = amt * 1.0971;
                result.setText(String.valueOf(df.format(troyOunce)));
            }
            else if (convertTo.equalsIgnoreCase("Pound")) {
                troyOunce = amt * 0.0685;
                result.setText(String.valueOf(df.format(troyOunce)));
            }
            else if (convertTo.equalsIgnoreCase("Newton (Earth)")) {
                troyOunce = amt * 0.31;
                result.setText(String.valueOf(df.format(troyOunce)));
            }
        }
        public void fromNewton(double amt) {
            double newton;
            DecimalFormat df = new DecimalFormat("#.##########");
            if (convertTo.equalsIgnoreCase("Newton (Earth)")) {
                result.setText(input.getText());
            } else if (convertTo.equalsIgnoreCase("Kilogram")) {
                newton = amt * 0.1019;
                result.setText(String.valueOf(df.format(newton)));
            }
            else if (convertTo.equalsIgnoreCase("Gram")) {
                newton = amt * 101.971;
                result.setText(String.valueOf(df.format(newton)));
            }
            else if (convertTo.equalsIgnoreCase("Stone")) {
                newton = amt * 0.0160;
                result.setText(String.valueOf(df.format(newton)));
            }
            else if (convertTo.equalsIgnoreCase("Ounce")) {
                newton = amt * 3.596;
                result.setText(String.valueOf(df.format(newton)));
            }
            else if (convertTo.equalsIgnoreCase("Pound")) {
                newton = amt * 0.2248;
                result.setText(String.valueOf(df.format(newton)));
            }
            else if (convertTo.equalsIgnoreCase("Troy ounce")) {
                newton = amt * 3.2784;
                result.setText(String.valueOf(df.format(newton)));
            }
        }
    }

    public static class PercentageCalculator extends Fragment {

        SeekBar seek;
        ImageButton convert;
        TextView amount;
        EditText userInpt;

        public PercentageCalculator() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.percentage, container, false);
            seek = (SeekBar)rootView.findViewById(R.id.percentageSeekBar);
            amount = (TextView)rootView.findViewById(R.id.percent);
            convert = (ImageButton)rootView.findViewById(R.id.imageButton);
            result = (TextView)rootView.findViewById(R.id.percentAmount);
            userInpt = (EditText)rootView.findViewById(R.id.userInput);

            seek.setMax(100);
            amount.setText(String.valueOf(seek.getProgress()));
            int seekBarAmount = seek.getProgress();
            calculatePercent(seekBarAmount, amount);
            return rootView;
        }

        private void calculatePercent(final int i, final TextView text){
            seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    text.setText(String.valueOf(seekBar.getProgress())+"%");
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String percentAmount = text.getText().toString();
                    String re = percentAmount.substring(0,percentAmount.length()-1);
                    if(percentAmount.equalsIgnoreCase("0")){
                        re = "0";
                    }
                    String amount = String.valueOf(userInpt.getText());
                    if(userInpt.getText().length() == 0){
                        amount = "0";
                    }
                    float percnt = Float.valueOf(String.valueOf(re));
                    float amtToCalc = Float.valueOf(amount) / 100;
                    float res = amtToCalc * percnt;
                    result.setText(String.valueOf(res));
                }
            });
        }
    }

    public static class HexBinaryDecimal extends Fragment {

        String [] differentHexBinDec;
        String [] withoutHex;

        public HexBinaryDecimal() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.hexbinarydecimal, container, false);

            Resources res = getResources();
            differentHexBinDec = res.getStringArray(R.array.differentHexBinaryDecimal);
            withoutHex = res.getStringArray(R.array.withoutHex);
            ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, differentHexBinDec);
            ArrayAdapter<String> hexAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, withoutHex);
            spinnerFrom = (Spinner) rootView.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) rootView.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(hexAdapter);
            spinnerTo.setAdapter(timeAdapter);
            convert = (Button) rootView.findViewById(R.id.convertButton);
            result = (TextView) rootView.findViewById(R.id.conversionResult);
            input = (EditText) rootView.findViewById(R.id.userInput);
            units = (TextView) rootView.findViewById(R.id.units);
            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinnerFrom.getSelectedItem().toString().equalsIgnoreCase("binary")){
                        input.setHint("example : 010110000");
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setKeyListener(DigitsKeyListener.getInstance("0" +"1"));

                    }else {
                        if (spinnerFrom.getSelectedItem().toString().equalsIgnoreCase("decimal")) {
                            input.setHint("example : 1234567");
                            input.setKeyListener(DigitsKeyListener.getInstance("0" + "1" + "2" + "3" + "4" + "5" + "6" + "7" + "8" + "9"));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();
                    if (too.equalsIgnoreCase("")) {
                        too = "0";
                    }
                    if(convertFrom.equalsIgnoreCase("decimal")){
                        fromDecimal(too);
                    }else if(convertFrom.equalsIgnoreCase("binary")){
                        fromBinary(too, rootView);
                    }else{
                        result.setText(too);
                    }
                }
            });
            return rootView;
        }

        private void fromDecimal(String dec){
            int paul = Integer.valueOf(dec);

            if(convertTo.equalsIgnoreCase("decimal")){
                result.setText(dec);
            }else if(convertTo.equalsIgnoreCase("binary")){
                result.setText(Integer.toBinaryString(paul));
            }else if(convertTo.equalsIgnoreCase("hexadecimal")){
                result.setText(Integer.toHexString(paul));
            }
            units.setText("in " +convertTo);
        }

        private void fromBinary(final String bin, final View view){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(bin.contains("2") || bin.contains("3") || bin.contains("4") || bin.contains("5")
                            || bin.contains("6")|| bin.contains("7")|| bin.contains("8")|| bin.contains("9")|| bin.contains(".")){

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setIcon(R.drawable.icon_warning).setTitle("Input is not a binary number, ex. 0011010")
                                        .setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();
                            }
                        });

                    }else{

                        if(convertTo.equalsIgnoreCase("Decimal")){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int decimalValue = 0;
                                    int [] array = new int[bin.length()];
                                    int powerIndex = 0;

                                    for(int i = 0; i < array.length; i++){
                                        array[i] = (int)Math.pow(2, i);
                                    }
                                    for(int i = bin.length() -1; i >= 0; i--){
                                        if(bin.charAt(i) == '1'){
                                            decimalValue = decimalValue + array[powerIndex];
                                        }
                                        powerIndex++;
                                    }
                                    final int binaryConverted = decimalValue;

                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            result.setText(String.valueOf(binaryConverted));
                                        }
                                    });
                                }
                            }).start();

                        }else if(convertTo.equalsIgnoreCase("binary")){
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    result.setText(bin);
                                }
                            });
                        }else if(convertTo.equalsIgnoreCase("hexadecimal")){

                            final String hexa = Long.toHexString(Long.parseLong(bin, 2));

                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    result.setText(hexa);
                                }
                            });
                        }
                    }
                }
            }).start();
            units.setText("in " +convertTo);
        }
    }

    public static class AreaConverter extends Fragment {

        String [] differentArea;

        public AreaConverter() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.area, container, false);
            Resources res = getResources();
            differentArea = res.getStringArray(R.array.differentArea);
            ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, differentArea);
            spinnerFrom = (Spinner) rootView.findViewById(R.id.fromSpinner);
            spinnerTo = (Spinner) rootView.findViewById(R.id.toSpinner);
            spinnerFrom.setAdapter(timeAdapter);
            spinnerTo.setAdapter(timeAdapter);
            convert = (Button) rootView.findViewById(R.id.convertButton);
            result = (TextView) rootView.findViewById(R.id.conversionResult);
            input = (EditText) rootView.findViewById(R.id.userInput);
            units = (TextView) rootView.findViewById(R.id.units);

            input.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL));

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertFrom = spinnerFrom.getSelectedItem().toString();
                    convertTo = spinnerTo.getSelectedItem().toString();
                    too = input.getText().toString();

                    if (too.equalsIgnoreCase("")) {
                        too = "0.0";
                    }
                    amount = Double.valueOf(too);
                    DecimalFormat df = new DecimalFormat("#.#######");

                    if (convertFrom.equalsIgnoreCase("Hectare")) {
                        result.setText(String.valueOf(df.format(fromHectare(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("Rood")) {
                        result.setText(String.valueOf(df.format(fromRood(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("Square Foot")) {
                        result.setText(String.valueOf(df.format(fromSquareFoot(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("Square Inch")) {
                        result.setText(String.valueOf(df.format(fromSquareInch(amount))));
                        units.setText(convertTo);
                    } else if (convertFrom.equalsIgnoreCase("Square Meter")) {
                        result.setText(String.valueOf(df.format(fromSquareMeter(amount))));
                        units.setText(convertTo);
                    } else if(convertFrom.equalsIgnoreCase("Square Kilometer")){
                        result.setText(String.valueOf(df.format(fromSquareKilometer(amount))));
                        units.setText(convertTo);
                    }
                }
            });
            return rootView;
        }

        private double fromHectare(double amt){
            double hectare = 0;

            if(convertTo.equalsIgnoreCase("Hectare")){
                hectare = amt;
            }else if(convertTo.equalsIgnoreCase("Rood")){
                hectare = amt*9.8842152;
            }else if(convertTo.equalsIgnoreCase("Square Foot")){
                hectare = amt*107639.1041671;
            }else if(convertTo.equalsIgnoreCase("Square Inch")){
                hectare = amt*15500031.00006;
            }else if(convertTo.equalsIgnoreCase("Square Meter")){
                hectare = amt*10000;
            }else if(convertTo.equalsIgnoreCase("Square Kilometer")){
                hectare = amt*0.01;
            }
            return hectare;
        }

        private double fromRood(double amt){
            double rood = 0;

            if(convertTo.equalsIgnoreCase("Hectare")){
                rood = amt*0.10117141056;
            }else if(convertTo.equalsIgnoreCase("Rood")){
                rood = amt;
            }else if(convertTo.equalsIgnoreCase("Square Foot")){
                rood = amt*10890;
            }else if(convertTo.equalsIgnoreCase("Square Inch")){
                rood = amt*1568160;
            }else if(convertTo.equalsIgnoreCase("Square Meter")){
                rood = amt*1011.7141056;
            }else if(convertTo.equalsIgnoreCase("Square Kilometer")){
                rood = amt*0.0010117141056;
            }
            return rood;
        }

        private double fromSquareFoot(double amt){
            double sqfoot = 0;

            if(convertTo.equalsIgnoreCase("Hectare")){
                sqfoot = amt*0.000009290304;
            }else if(convertTo.equalsIgnoreCase("Rood")){
                sqfoot = amt*0.00009182736455463;
            }else if(convertTo.equalsIgnoreCase("Square Foot")){
                sqfoot = amt;
            }else if(convertTo.equalsIgnoreCase("Square Inch")){
                sqfoot = amt*144;
            }else if(convertTo.equalsIgnoreCase("Square Meter")){
                sqfoot = amt*0.9290304;
            }else if(convertTo.equalsIgnoreCase("Square Kilometer")){
                sqfoot = amt*0.00000009290304;
            }
            return sqfoot;
        }

        private double fromSquareInch(double amt){
            double sqinch = 0;

            if(convertTo.equalsIgnoreCase("Hectare")){
                sqinch = amt*0.00000006451;
            }else if(convertTo.equalsIgnoreCase("Rood")){
                sqinch = amt*0.000000637690031;
            }else if(convertTo.equalsIgnoreCase("Square Foot")){
                sqinch = amt*0.00694444444444;
            }else if(convertTo.equalsIgnoreCase("Square Inch")){
                sqinch = amt;
            }else if(convertTo.equalsIgnoreCase("Square Meter")){
                sqinch = amt*0.00064516;
            }else if(convertTo.equalsIgnoreCase("Square Kilometer")){
                sqinch = amt*0.0000000006151;
            }
            return sqinch;
        }

        private double fromSquareMeter(double amt){
            double sqmeter = 0;

            if(convertTo.equalsIgnoreCase("Hectare")){
                sqmeter = amt*0.0001;
            }else if(convertTo.equalsIgnoreCase("Rood")){
                sqmeter = amt*0.0009884215258;
            }else if(convertTo.equalsIgnoreCase("Square Foot")){
                sqmeter = amt*10.76391041;
            }else if(convertTo.equalsIgnoreCase("Square Inch")){
                sqmeter = amt*1550.003100006;
            }else if(convertTo.equalsIgnoreCase("Square Meter")){
                sqmeter = amt;
            }else if(convertTo.equalsIgnoreCase("Square Kilometer")){
                sqmeter = amt*0.000001;
            }
            return sqmeter;
        }

        private double fromSquareKilometer(double amt){
            double sqkm = 0;

            if(convertTo.equalsIgnoreCase("Hectare")){
                sqkm = amt*100;
            }else if(convertTo.equalsIgnoreCase("Rood")){
                sqkm = amt*988.4215258;
            }else if(convertTo.equalsIgnoreCase("Square Foot")){
                sqkm = amt*10763910.41671;
            }else if(convertTo.equalsIgnoreCase("Square Inch")){
                sqkm = amt*1550003100.006;
            }else if(convertTo.equalsIgnoreCase("Square Meter")){
                sqkm = amt*1000000;
            }else if(convertTo.equalsIgnoreCase("Square Kilometer")){
                sqkm = amt;
            }
            return sqkm;
        }
    }
}
