/*

Name: Stephen Cartner
Student Id: S1706321

 */

package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.icu.util.ULocale;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewFlipper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private ListView listView;
    private Button startButton;
    private Button returnButton;
    private Button mapButton;
    private Button mapReturnButton;
    private Button dateButton;
    private Button dateReturnButton;
    private Button dateSpecificButton;
    private Button dateRangeButton;
    private String result = "";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private String earthquakeLocation;
    ArrayList<EarthquakeClass> mainArrayList = null;
    ArrayList<EarthquakeClass> searchedArrayList = null;
    MySuperArrayAdapter adapter;
    private ViewFlipper viewFlipper;

    // Map Variables
    private GoogleMap mapView;
    public Float mapLat;
    public Float mapLong;
    public Float mapMagColor;

    // Date Variables
    public String searchDateStart;
    public String searchDateEnd;

    // Searched Buttons
    public Button largestMagnitudeButton;
    public Button mostNorthButton;
    public Button mostSouthButton;
    public Button mostEastButton;
    public Button mostWestButton;
    public Button deepestButton;
    public Button shallowestButton;

    // Search Ints
    private int largestEarthquake = 0;
    private int mostNorth = 0;
    private int mostSouth = 0;
    private int mostEast = 0;
    private int mostWest = 0;
    private int deepest = 0;
    private int shallowest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mainArrayList = new ArrayList<EarthquakeClass>();
        searchedArrayList = new ArrayList<EarthquakeClass>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Parse Button
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // View moving buttons
        returnButton = (Button)findViewById(R.id.returnButton);
        mapButton = (Button)findViewById(R.id.mapViewButton);
        mapReturnButton = (Button)findViewById(R.id.returnDescription);
        dateButton = (Button)findViewById(R.id.dateSearch);
        dateReturnButton = (Button)findViewById(R.id.returnDate);

        // Date Search Buttons
        dateSpecificButton = (Button)findViewById(R.id.singleDateButton);
        dateRangeButton = (Button)findViewById(R.id.dateRangeButton);

        // Date View Buttons
        largestMagnitudeButton = (Button)findViewById(R.id.largestMagnitudeButton);
        mostNorthButton = (Button)findViewById(R.id.mostNorthButton);
        mostSouthButton = (Button)findViewById(R.id.mostSouthButton);
        mostEastButton = (Button)findViewById(R.id.mostEastButton);
        mostWestButton = (Button)findViewById(R.id.mostWestButton);
        deepestButton = (Button)findViewById(R.id.deepestButton);
        shallowestButton = (Button)findViewById(R.id.shallowestButton);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
        View myFirstView = findViewById(R.id.relativeView1);
        View mySecondView = findViewById(R.id.relativeView2);
        View myMapView = findViewById(R.id.relativeView3);
        View myDateView = findViewById(R.id.relativeView4);

        adapter = new MySuperArrayAdapter(this, -1, mainArrayList);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this::onMapReady);

        // Material Date Picker Stuff
        MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker.Builder dateRangeBuilder = MaterialDatePicker.Builder.dateRangePicker();

        dateBuilder.setTitleText("SELECT A DATE");
        dateRangeBuilder.setTitleText("SELECT A RANGE OF DATES");

        MaterialDatePicker dateSpecificPicker = dateBuilder.build();
        MaterialDatePicker dateRangePicker = dateRangeBuilder.build();


        if (adapter == null)
        {
            Log.e("My Tag", "Adapter Error");
        }


        startProgress();

        listView = (ListView)findViewById(R.id.list);

        // Assign adaptor
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeClass earthquake = mainArrayList.get(position);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != myFirstView)
                {
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showNext();
                }
            }
        });

        returnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewFlipper.getCurrentView() != myFirstView)
                {
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showNext();
                }
            }
        });

        mapButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add a marker in earthquake location and move the camera
                if (mapLat != null && mapLong != null )
                {
                    LatLng earthquakeLatLng = new LatLng(mapLat, mapLong);
                    mapView.moveCamera(CameraUpdateFactory.newLatLng(earthquakeLatLng));

                    if (mapMagColor > 0 && mapMagColor <= 2)
                    {
                        mapView.addMarker(new MarkerOptions().position(earthquakeLatLng).title(earthquakeLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    else if (mapMagColor > 2 && mapMagColor <= 8) {
                        mapView.addMarker(new MarkerOptions().position(earthquakeLatLng).title(earthquakeLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    }
                    else if (mapMagColor > 8 && mapMagColor <= 10)
                    {
                        mapView.addMarker(new MarkerOptions().position(earthquakeLatLng).title(earthquakeLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                }


                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myMapView)
                {
                    viewFlipper.showNext();
                }
            }
        });

        mapReturnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mapView.clear();

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myMapView)
                {
                    viewFlipper.showNext();
                }
            }
        });

        dateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewFlipper.getCurrentView() != myFirstView)
                {
                    viewFlipper.showNext();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showPrevious();
                }
            }
        });

        dateReturnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewFlipper.getCurrentView() != myFirstView)
                {
                    viewFlipper.showNext();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showPrevious();
                }
            }
        });

        dateSpecificButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSpecificPicker.show(getSupportFragmentManager(), "SPECIFIC_DATE_PICKER");
            }
        });

        dateRangeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dateRangePicker.show(getSupportFragmentManager(), "RANGE_DATE_PICKER");
            }
        });

        dateSpecificPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                String stringDate = new SimpleDateFormat("dd/MM/yy").format(selection);

                searchDateStart = stringDate;
                searchDateEnd = stringDate;

                dateSearch();
            }
        });

        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
                String date = dateRangePicker.getHeaderText();
                String[] separated = date.split(" – ");

                try {
                    Date unformattedDateStart = currentDateFormat.parse(separated[0]);
                    Date unformattedDateEnd = currentDateFormat.parse(separated[1]);

                    String theFalseDateStart = new SimpleDateFormat("dd/MM/yy").format(unformattedDateStart);
                    String theFalseDateEnd = new SimpleDateFormat("dd/MM/yy").format(unformattedDateEnd);

                    String[] separatedStart = theFalseDateStart.split("/");
                    String[] separatedEnd = theFalseDateEnd.split("/");

                    int startYear = Integer.parseInt(separatedStart[2]) - 49;
                    int endYear = Integer.parseInt(separatedEnd[2]) - 49;

                    String theDateStart = theFalseDateStart.replace("70", String.valueOf(startYear));
                    String theDateEnd = theFalseDateEnd.replace("70", String.valueOf(endYear));

                    searchDateStart = theDateStart;
                    searchDateEnd = theDateEnd;

                    dateSearch();

                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });

        largestMagnitudeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EarthquakeClass earthquake = searchedArrayList.get(largestEarthquake);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showNext();
                    viewFlipper.showNext();
                }
            }
        });

        mostNorthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EarthquakeClass earthquake = searchedArrayList.get(mostNorth);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showNext();
                    viewFlipper.showNext();
                }
            }
        });

        mostSouthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EarthquakeClass earthquake = searchedArrayList.get(mostSouth);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showNext();
                    viewFlipper.showNext();
                }
            }
        });

        mostEastButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EarthquakeClass earthquake = searchedArrayList.get(mostEast);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showNext();
                    viewFlipper.showNext();
                }
            }
        });

        mostWestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EarthquakeClass earthquake = searchedArrayList.get(mostWest);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showNext();
                    viewFlipper.showNext();
                }
            }
        });

        deepestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EarthquakeClass earthquake = searchedArrayList.get(deepest);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showNext();
                    viewFlipper.showNext();
                }
            }
        });

        shallowestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EarthquakeClass earthquake = searchedArrayList.get(shallowest);

                mapLat = Float.parseFloat(earthquake.getGeoLat());
                mapLong = Float.parseFloat(earthquake.getGeoLong());

                earthquakeLocation = earthquake.getLocation();

                TextView txtLocationClicked = (TextView) mySecondView.findViewById(R.id.locationClicked);
                TextView txtDateClicked = (TextView) mySecondView.findViewById(R.id.dateClicked);
                TextView txtTimeClicked = (TextView) mySecondView.findViewById(R.id.timeClicked);
                TextView txtDepthClicked = (TextView) mySecondView.findViewById(R.id.depthClicked);
                TextView txtMagnitudeClicked = (TextView) mySecondView.findViewById(R.id.magnitudeClicked);
                TextView txtLatitudeClicked = (TextView) mySecondView.findViewById(R.id.latitudeClicked);
                TextView txtLongitudeClicked = (TextView) mySecondView.findViewById(R.id.longitudeClicked);

                String[] separated = earthquake.getMagnitude().split(":");

                mapMagColor = Float.parseFloat(separated[1]);


                txtLocationClicked.setText(earthquake.getLocation());
                txtDateClicked.setText("Date: " + earthquake.getDate());
                txtTimeClicked.setText("Time: " + earthquake.getTime());
                txtDepthClicked.setText(earthquake.getDepth());
                txtMagnitudeClicked.setText(earthquake.getMagnitude());
                txtLatitudeClicked.setText("Latitude: " + earthquake.getGeoLat());
                txtLongitudeClicked.setText("Longitude: " + earthquake.getGeoLong());

                if (viewFlipper.getCurrentView() != mySecondView)
                {
                    viewFlipper.showPrevious();
                    viewFlipper.showPrevious();
                }
                else if (viewFlipper.getCurrentView() != myDateView)
                {
                    viewFlipper.showNext();
                    viewFlipper.showNext();
                }
            }
        });

    }


    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        mapView.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapView.getUiSettings().setZoomControlsEnabled(true);
        mapView.getUiSettings().setZoomGesturesEnabled(true);
        mapView.getUiSettings().setCompassEnabled(true);

        // Add a marker in Sydney and move the camera
        if (mapLat != null && mapLong != null )
        {
            LatLng earthquakeLatLng = new LatLng(mapLat, mapLong);
            mapView.addMarker(new MarkerOptions().position(earthquakeLatLng).title(earthquakeLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mapView.moveCamera(CameraUpdateFactory.newLatLng(earthquakeLatLng));
        }

    }

    public void dateSearch()
    {
        for (int i = 0; i< mainArrayList.size(); i++)
        {
            EarthquakeClass earthquake = mainArrayList.get(i);

            String[] separatedStartDate = searchDateStart.split("/");
            String[] separatedEndDate = searchDateEnd.split("/");
            String[] separatedEarthquakeDate = earthquake.getDate().split("/");

            int startDateDay = Integer.parseInt(separatedStartDate[0]);
            int startDateMonth = Integer.parseInt(separatedStartDate[1]);
            int startDateYear = Integer.parseInt(separatedStartDate[2]);

            int endDateDay = Integer.parseInt(separatedEndDate[0]);
            int endDateMonth = Integer.parseInt(separatedEndDate[1]);
            int endDateYear = Integer.parseInt(separatedEndDate[2]);

            int earthquakeDateDay = Integer.parseInt(separatedEarthquakeDate[0]);
            int earthquakeDateMonth = Integer.parseInt(separatedEarthquakeDate[1]);
            int earthquakeDateYear = Integer.parseInt(separatedEarthquakeDate[2]);

            // If within year search
            if (earthquakeDateYear >= startDateYear && earthquakeDateYear <= endDateYear)
            {
                // If within month search
                if (earthquakeDateMonth >= startDateMonth && earthquakeDateMonth <= endDateMonth)
                {
                    // If start and end within same month
                    if (startDateMonth == endDateMonth)
                    {
                        // If within day search
                        if (earthquakeDateDay >= startDateDay && earthquakeDateDay <= endDateDay)
                        {
                            searchedArrayList.add(earthquake);
                        }
                    }
                    else if (endDateMonth > startDateMonth)
                    {
                        if (earthquakeDateDay > startDateDay && earthquakeDateMonth == startDateMonth)
                        {
                            searchedArrayList.add(earthquake);
                        }
                        else if (earthquakeDateDay < endDateDay && earthquakeDateMonth == endDateMonth)
                        {
                            searchedArrayList.add(earthquake);
                        }
                    }
                }
            }
        }

        // Search the list and make decisions
        for (int i = 0; i< searchedArrayList.size(); i++)
        {
            EarthquakeClass earthquake = searchedArrayList.get(i);

            // Magnitude

            String[] largestQuakeSeparated = searchedArrayList.get(largestEarthquake).getMagnitude().split(":");
            String[] searchedQuakeSeparated = earthquake.getMagnitude().split(":");

            float largestMagnitudeFloat = Float.parseFloat(largestQuakeSeparated[1]);
            float earthquakeMagnitudeFloat = Float.parseFloat(searchedQuakeSeparated[1]);

            if (largestMagnitudeFloat < earthquakeMagnitudeFloat)
            {
                largestEarthquake = i;
            }

            // Most Noth, South, East and West

            float earthquakeGeoLat = Float.parseFloat(earthquake.getGeoLat());
            float earthquakeGeoLong = Float.parseFloat(earthquake.getGeoLong());

            float biggestGeoLat = Float.parseFloat(searchedArrayList.get(mostNorth).getGeoLat());
            float smallestGeoLat = Float.parseFloat(searchedArrayList.get(mostSouth).getGeoLat());

            float biggestGeoLong = Float.parseFloat(searchedArrayList.get(mostEast).getGeoLong());
            float smallestGeoLong = Float.parseFloat(searchedArrayList.get(mostWest).getGeoLong());

            if (earthquakeGeoLat > biggestGeoLat)
            {
                mostNorth = i;
            }
            else if (earthquakeGeoLat < smallestGeoLat)
            {
                mostSouth = i;
            }

            if (earthquakeGeoLong > biggestGeoLong)
            {
                mostEast = i;
            }
            else if (earthquakeGeoLong < smallestGeoLong)
            {
                mostWest = i;
            }

            // Deepest and Shallowest

            String[] separatedDeepestEarthquake = searchedArrayList.get(deepest).getDepth().split(" ");
            String[] separatedShallowestEarthquake = searchedArrayList.get(shallowest).getDepth().split(" ");
            String[] separatedEarthquakeDepth = earthquake.getDepth().split(" ");


            float deepestEarthquake = Float.valueOf(separatedDeepestEarthquake[2]);
            float shallowestEarthquake = Float.valueOf(separatedShallowestEarthquake[2]);
            float earthquakeDepth = Float.valueOf(separatedEarthquakeDepth[2]);

            if (earthquakeDepth > deepestEarthquake)
            {
                deepest = i;
            }
            else if(earthquakeDepth < shallowestEarthquake)
            {
                shallowest = i;
            }


        }

        System.out.println(largestEarthquake);
        System.out.println(mostNorth);
        System.out.println(mostSouth);
        System.out.println(mostEast);
        System.out.println(mostWest);
        System.out.println(deepest);
        System.out.println(shallowest);

        // Update Button face info
        largestMagnitudeButton.setText(searchedArrayList.get(largestEarthquake).getLocation() +" \n Lat/Long: " + searchedArrayList.get(largestEarthquake).getGeoLat() + "," + searchedArrayList.get(largestEarthquake).getGeoLong() + " \n " + searchedArrayList.get(largestEarthquake).getMagnitude() + " " +searchedArrayList.get(largestEarthquake).getDepth());
        mostNorthButton.setText(searchedArrayList.get(mostNorth).getLocation() +" \n Lat/Long: " + searchedArrayList.get(mostNorth).getGeoLat() + "," + searchedArrayList.get(mostNorth).getGeoLong() + " \n " + searchedArrayList.get(mostNorth).getMagnitude() + " " +searchedArrayList.get(mostNorth).getDepth());
        mostSouthButton.setText(searchedArrayList.get(mostSouth).getLocation() +" \n Lat/Long: " + searchedArrayList.get(mostSouth).getGeoLat() + "," + searchedArrayList.get(mostSouth).getGeoLong() + " \n " + searchedArrayList.get(mostSouth).getMagnitude() + " " +searchedArrayList.get(mostSouth).getDepth());
        mostEastButton.setText(searchedArrayList.get(mostEast).getLocation() +" \n Lat/Long: " + searchedArrayList.get(mostEast).getGeoLat() + "," + searchedArrayList.get(mostEast).getGeoLong() + " \n " + searchedArrayList.get(mostEast).getMagnitude() + " " +searchedArrayList.get(mostEast).getDepth());
        mostWestButton.setText(searchedArrayList.get(mostWest).getLocation() +" \n Lat/Long: " + searchedArrayList.get(mostWest).getGeoLat() + "," + searchedArrayList.get(mostWest).getGeoLong() + " \n " + searchedArrayList.get(mostWest).getMagnitude() + " " +searchedArrayList.get(mostWest).getDepth());
        deepestButton.setText(searchedArrayList.get(deepest).getLocation() +" \n Lat/Long: " + searchedArrayList.get(deepest).getGeoLat() + "," + searchedArrayList.get(deepest).getGeoLong() + " \n " + searchedArrayList.get(deepest).getMagnitude() + " " +searchedArrayList.get(deepest).getDepth());
        shallowestButton.setText(searchedArrayList.get(shallowest).getLocation() +" \n Lat/Long: " + searchedArrayList.get(shallowest).getGeoLat() + "," + searchedArrayList.get(shallowest).getGeoLong() + " \n " + searchedArrayList.get(shallowest).getMagnitude() + " " +searchedArrayList.get(shallowest).getDepth());
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","in onClick");
        //startProgress();
        // Assign adaptor
        listView.setAdapter(adapter);

        Log.e("MyTag","after startProgress");
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //


    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");


                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            mainArrayList = parseData(result);

            for (int i = 0; i< mainArrayList.size(); i++)
            {
                adapter.add(mainArrayList.get(i));
            }
        }
    }

    private ArrayList<EarthquakeClass> parseData(String dataToParse)
    {
        EarthquakeClass item = null;
        ArrayList<EarthquakeClass> alist = null;
        boolean isData = false;

        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();
            // While not the end of the document
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Find the start tag
                if (eventType == XmlPullParser.START_TAG)
                {
                    // If is the data I want to parse
                    if (isData == true)
                    {
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            //System.out.println("Item Start Tag Found");
                            item = new EarthquakeClass();
                        }
                        else if (xpp.getName().equalsIgnoreCase("title"))
                        {
                            // Get associated text
                            String temp = xpp.nextText();

                            // Do stuff with text
                            //System.out.println("Title is " + temp);
                            item.setTitle(temp);
                        }
                        else if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            // Get associated text
                            String temp = xpp.nextText();
                            String[] separated = temp.split(";");

                            SimpleDateFormat currentFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);

                            String dateTimeString = separated[0].replace("Origin date/time: ", "");

                            try {
                                Date unformattedDate = currentFormat.parse(dateTimeString);

                                String theDate = new SimpleDateFormat("dd/MM/yy").format(unformattedDate);
                                String theTime = new SimpleDateFormat("hh:mm:ss").format(unformattedDate);

                                item.setDate(theDate);
                                item.setTime(theTime);

                            }catch (ParseException e){
                                e.printStackTrace();
                            }

                            // Do stuff with text
                            //System.out.println("Description is " + temp);
                            //System.out.println("Location is " + separated[1]);
                            //System.out.println("LatLong is " + separated[2]);
                            //System.out.println("Depth is " + separated[3]);
                            //System.out.println("Magnitude is " + separated[4]);
                            item.setLocation(separated[1]);
                            item.setLatLong(separated[2]);
                            item.setDepth(separated[3]);
                            item.setMagnitude(separated[4]);
                            item.setDescription(temp);


                        }
                        else if (xpp.getName().equalsIgnoreCase("link"))
                        {
                            // Get associated text
                            String temp = xpp.nextText();

                            // Do stuff with text
                            //System.out.println("Link is " + temp);
                            item.setLink(temp);
                        }
                        else if (xpp.getName().equalsIgnoreCase("category"))
                        {
                            // Get associated text
                            String temp = xpp.nextText();

                            // Do stuff with text
                            //System.out.println("Category is " + temp);
                            item.setCategory(temp);
                        }
                        else if (xpp.getName().equalsIgnoreCase("lat"))
                        {
                            // Get associated text
                            String temp = xpp.nextText();

                            // Do stuff with text
                            //System.out.println("Latitude is " + temp);
                            item.setGeoLat(temp);
                        }
                        else if (xpp.getName().equalsIgnoreCase("long"))
                        {
                            // Get associated text
                            String temp = xpp.nextText();

                            // Do stuff with text
                            //System.out.println("Longitude is " + temp);
                            item.setGeoLong(temp);
                        }
                    }
                    // Check which tag we have
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        alist = new ArrayList<EarthquakeClass>();
                    }
                }
                else if (eventType == XmlPullParser.END_TAG)
                {

                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        //System.out.println("Item is " + item.earthquakeToString());
                        alist.add(item);
                    }
                    else if (xpp.getName().equalsIgnoreCase("image"))
                    {
                        isData = true;
                    }
                    else if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = alist.size();

                        //System.out.println("Channel size is " + size);
                    }
                }
                // Get next event
                eventType = xpp.next();

            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        return alist;
    }
}