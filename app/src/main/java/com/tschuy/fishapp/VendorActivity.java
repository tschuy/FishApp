package com.tschuy.fishapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Iterator;
import java.util.Locale;


public class VendorActivity extends ActionBarActivity implements VendorProductListFragment.OnFragmentInteractionListener {

    Vendor vendor;

    MapFragment map_fragment;
    VendorProductListFragment list;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        Bundle b = getIntent().getExtras();
        vendor = b.getParcelable("com.tschuy.materialtest.Vendor");

        if (vendor == null) {
            getVendor(getIntent().getLongExtra("vendor_key", 0));
        } else {
            loadVendor();
        }
        // TODO: eww
        ((ScrollView) findViewById(R.id.scroll_container)).smoothScrollTo(0,0);
    }

    private void getVendor(long vendor_id) {
        Ion.with(this).load(String.format("http://seagrant-staging-api.osuosl.org/1/vendors/%d", vendor_id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        vendor = new Vendor(result);
                        loadVendor();
                    }
                });
    }

    private void loadVendor() {

        list = new VendorProductListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, list).commit();
        getFragmentManager().executePendingTransactions();

        map_fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.vendor_map);
        map = map_fragment.getMap();

        map.addMarker(new MarkerOptions().position(vendor.location).title(vendor.name)).getId();
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(vendor.location);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(13);

        map.moveCamera(center);
        map.animateCamera(zoom);

        setTitle(vendor.name);
        ((TextView) findViewById(R.id.vendor_description)).setText(vendor.description);

        if("".equals(vendor.contact_name)) {
            findViewById(R.id.contact_name).setVisibility(View.GONE);
            findViewById(R.id.contact_name_label).setVisibility(View.GONE);

        } else {
            ((TextView) findViewById(R.id.contact_name)).setText(vendor.contact_name);
        }

        if("".equals(vendor.email)) {
            findViewById(R.id.email).setVisibility(View.GONE);
            findViewById(R.id.email_label).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.email)).setText(vendor.email);
        }

        StringBuilder address = new StringBuilder(5);
        address.append(vendor.city).append(", ").append(vendor.state).append(" ").append(vendor.zip);
        ((TextView) findViewById(R.id.street_address)).setText(vendor.street);

        ((TextView) findViewById(R.id.rest_address)).setText(address.toString());

        if("".equals(vendor.location_description)) {
            findViewById(R.id.location_description).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.location_description)).setText(vendor.location_description);
        }

        ImageButton navigation = (ImageButton) findViewById(R.id.map_button);
        navigation.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String encoded_string = String.format(Locale.ENGLISH, "%s, %s %s %s", vendor.street, vendor.city, vendor.state, vendor.zip);
                String uri = String.format(Locale.ENGLISH, "geo:0,0f?q=%s",
                        encoded_string);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        Iterator<VendorProduct> product_iterator = vendor.products.iterator();
        while(product_iterator.hasNext()) {
            list.addItem(product_iterator.next());
        }
    }

    @Override
    public void onFragmentInteractionVP(VendorProduct vp)
    {
        return;
    }

    public void vpClicked(VendorProduct vp) {
        Intent i = new Intent(this, ProductActivity.class);
        i.putExtra("product_key", vp.product_id);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this screen has no menu
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_vendor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
