package com.dodelivery;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by juhee on 1/6/17.
 */

public class LaunchActivity extends Activity implements OnLocationSelectionChangeListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity_main);

        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }

            DeliveryLocationListFragment deliveryListFragment = new DeliveryLocationListFragment();
            deliveryListFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, deliveryListFragment)
                    .commit();
        }
    }

    @Override
    public void OnSelectionChanged(int deliveryLocationIndex) {
        DisplayMapFragment displayMapFragment = (DisplayMapFragment) getFragmentManager()
                .findFragmentById(R.id.description_fragment);

        if (displayMapFragment != null){
            displayMapFragment.setDescription(deliveryLocationIndex);
            displayMapFragment.setUpMap();
        } else {
            DisplayMapFragment mapFragment = new DisplayMapFragment();
            Bundle args = new Bundle();
            args.putInt(DisplayMapFragment.KEY_POSITION,deliveryLocationIndex);
            mapFragment.setArguments(args);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,mapFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}