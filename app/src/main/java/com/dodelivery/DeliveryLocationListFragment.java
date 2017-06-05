package com.dodelivery;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dodelivery.core.DeliveryDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juhee on 31/5/17.
 */

public class DeliveryLocationListFragment extends Fragment {

    List<DeliveryDetails> deliveryDetailsList=new ArrayList<>();
    DeliveryListAdapter adapter;
    ListView listView;

    public DeliveryLocationListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.fragment_delivery_list, container, false);
        listView = (ListView) vg.findViewById(R.id.list_deliveries);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        listView.requestFocus();
        return vg;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deliveryDetailsList = DeliveryBaseApplication.instance().getDeliveries().getDeliveryDetailsList();
        adapter = new DeliveryListAdapter(getActivity(),deliveryDetailsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnLocationSelectionChangeListener listener = (OnLocationSelectionChangeListener) getActivity();
                listener.OnSelectionChanged(position);
            }
        });
    }
}
