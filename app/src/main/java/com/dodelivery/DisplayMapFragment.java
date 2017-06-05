package com.dodelivery;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by juhee on 31/5/17.
 */

public class DisplayMapFragment extends Fragment {

    static final String TAG = DisplayMapFragment.class.getSimpleName();
    static final String KEY_POSITION = "position";
    int mCurrentPosition = 0;
    MapView mMapView;
    GoogleMap mGoogleMap;
    int zoom = 7;
    CustomMapTileProvider mProvider = null;
    double lat, lng;
    TextView textView;
    String addr;


    public DisplayMapFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
        }

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        textView = (TextView) view.findViewById(R.id.text_view);

        Bundle args = getArguments();
        if (args != null) {
            setDescription(args.getInt(KEY_POSITION));
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGoogleMap = mMapView.getMap();
        setUpMap();
        return view;
    }

    public void setDescription(int descriptionIndex) {
        mCurrentPosition = descriptionIndex;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION, mCurrentPosition);
    }


    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }


    public void setUpMap() {
        mGoogleMap.clear();
        mGoogleMap = mMapView.getMap();
        lat = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getLocation().getLatitude();
        lng = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getLocation().getLongitude();
        addr = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getDescription();
        if (isNetworkAvailable()) {
            // loading map online
            reloadMap(mCurrentPosition);
            textView.setText(addr + "(Online Map)");
            new ImageDownloaderTask().execute();
        } else {
            // retrieve offline map
            mProvider = new CustomMapTileProvider();
            mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 7);
            mGoogleMap.moveCamera(upd);
            textView.setText(addr + "(Offline Map)");
        }
    }


    // method 1
    public void reloadMap(int descriptionIndex) {
        mGoogleMap.clear();
        mGoogleMap = mMapView.getMap();
        lat = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getLocation().getLatitude();
        lng = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getLocation().getLongitude();
        String locationName = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getDescription();
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(lat, lng)).title(locationName);
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        mGoogleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng)).zoom(7).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }


    public class ImageDownloaderTask extends AsyncTask<String, Void, InputStream> {
        private Point p;
        double latitude, longitude;

        public ImageDownloaderTask() {
        }

        @Override
        protected InputStream doInBackground(String... params) {
            return downloadMapAsImage(null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            latitude = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getLocation().getLatitude();
            longitude = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList().get(mCurrentPosition).getLocation().getLongitude();
            this.p = project(latitude, longitude, zoom);
        }

        private InputStream downloadMapAsImage(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL("http://mt1.google.com/vt/lyrs=m&x=" + p.x + "&y=" + p.y + "&z=" + zoom);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != 200) {
                    return null;
                }
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    if (mProvider == null)
                        mProvider = new CustomMapTileProvider();

                    File file = new File(Environment.getExternalStorageDirectory() + "/" + DeliveryBaseApplication.instance().getPackageName() + "/" + mProvider.getTileFilename(p.x, p.y, zoom));
                    if (!file.exists()) {
                        File dirs = new File(file.getParent());
                        if (!dirs.exists())
                            dirs.mkdirs();

                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        copyFile(inputStream, new FileOutputStream(file));
                    }
                    return inputStream;
                }
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            } catch (Exception e) {
                Log.d("Url connection error", e.toString());
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                Log.d(TAG, "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();

                }
            }
            return null;
        }
    }

    public static boolean copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            return false;
        }
        return true;
    }

    public Point project(double lat, double lng, int zoom) {
        int x, y, scale;
        double siny = Math.sin(lat * Math.PI / 180);
        scale = 1 << zoom;
        siny = Math.min(Math.max(siny, -0.9999), 0.9999);
        x = (int) Math.floor(scale * (.5 + lng / 360));
        y = (int) Math.floor(scale * (0.5 - Math.log((1 + siny) / (1 - siny)) / (4 * Math.PI)));
        return new Point(x, y);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
