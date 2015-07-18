package com.tschuy.fishapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

public class ProductActivity extends ActionBarActivity implements ProductVendorListFragment.OnFragmentInteractionListener {

    Product product;
    ProductVendorListFragment list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Bundle b = getIntent().getExtras();
        product = b.getParcelable("com.tschuy.materialtest.Product");
        if (product == null) {
            getProduct(getIntent().getLongExtra("product_key", 0));
        } else {
            loadProduct();
        }
    }

    private void getProduct(long product_id) {
        Ion.with(this).load(String.format("http://seagrant-staging-api.osuosl.org/1/products/%d", product_id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        product = new Product(result);
                        loadProduct();
                    }
                });
    }

    private void loadProduct() {
        setTitle(product.toString());
        ((TextView) findViewById(R.id.product_description)).setText(product.description);
        if (product.alternate_name != null && !product.alternate_name.equals("")) {
            ((TextView) findViewById(R.id.alternate_name)).setText("Also known as " + product.alternate_name + ".");
        } else {
            findViewById(R.id.alternate_name).setVisibility(View.INVISIBLE);
        }
        if (product.season != null && !product.season.equals("")) {
            ((TextView) findViewById(R.id.season)).setText(product.season);
        } else {
            findViewById(R.id.season).setVisibility(View.INVISIBLE);
            findViewById(R.id.season_title).setVisibility(View.INVISIBLE);
        }
        if (product.variety != null && !product.variety.equals("")) {
            ((TextView) findViewById(R.id.variety)).setText(product.variety);
        } else {
            findViewById(R.id.variety).setVisibility(View.INVISIBLE);
            findViewById(R.id.variety_title).setVisibility(View.INVISIBLE);
        }
        if (product.origin != null && !product.origin.equals("")) {
            ((TextView) findViewById(R.id.origin)).setText(product.origin);
        } else {
            findViewById(R.id.origin).setVisibility(View.INVISIBLE);
            findViewById(R.id.origin_title).setVisibility(View.INVISIBLE);
        }
        if (product.image != null) {
            ImageView imageView = (ImageView) findViewById(R.id.product_image);
            Ion.with(imageView).load(product.image);
        }

        list = new ProductVendorListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, list).commit();

        // dirty hack
        // Should not make this call at the end of the UI code
        Ion.with(this).load(String.format("http://seagrant-staging-api.osuosl.org/1/vendors/products/%d", product.id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray vendors_array = result.getAsJsonArray("vendors");
                        Iterator<JsonElement> vendor_interator = vendors_array.iterator();
                        while (vendor_interator.hasNext()) {
                            ProductVendor vendor = new ProductVendor(vendor_interator.next().getAsJsonObject());
                            product.addVendor(vendor);
                            list.addItem(vendor);
                        }
                    }
                });
    }

    public void pvClicked(ProductVendor pv) {
        Intent i = new Intent(this, VendorActivity.class);
        i.putExtra("vendor_key", pv.vendor_id);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_vendor, menu);
        return true;
    }

    @Override
    public void onFragmentInteractionPV(ProductVendor pv)
    {
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
