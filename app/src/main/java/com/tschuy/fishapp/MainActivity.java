package com.tschuy.fishapp;

import android.app.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Iterator;
import java.util.HashMap;


public class MainActivity extends Activity
        implements VendorListFragment.OnFragmentInteractionListener {

    private CharSequence mTitle;
    VendorListFragment list;
    MapFragment map_fragment;
    GoogleMap map;
    HashMap <String, Object> markerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (VendorListFragment) getFragmentManager().findFragmentById(R.id.list);
        map_fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = map_fragment.getMap();
        markerMap = new HashMap<String, Object>();

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(43.897888, -122.722887));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(6);

        map.moveCamera(center);
        map.animateCamera(zoom);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                openVendor((Vendor) markerMap.get(marker.getId()));
                return true;
                }
        });

        Ion.with(this).load("http://seagrant-staging-api.osuosl.org/1/vendors")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray vendors = result.getAsJsonArray("vendors");
                        Iterator<JsonElement> vendor_array = vendors.iterator();
                        while(vendor_array.hasNext()) {
                            Vendor vendor = new Vendor(vendor_array.next().getAsJsonObject());
                            markerMap.put(map.addMarker(new MarkerOptions()
                                    .position(vendor.location).title(vendor.name)).getId(), vendor);
                            list.addItem(vendor);
                        }
                    }
                });
    }

    public void openVendor(Vendor vendor) {
        Intent i = new Intent(this, VendorActivity.class);
        i.putExtra("com.tschuy.materialtest.Vendor", vendor);
        startActivity(i);
    }

    public void vendorClicked(Vendor vendor) {
        openVendor(vendor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_location:
                Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_products:
                Intent i = new Intent(this, ProductsListActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSectionAttached(int number) {}


    public void onFragmentInteraction(String id) {
        Toast.makeText(getApplicationContext(), "Interaction!", Toast.LENGTH_LONG).show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
