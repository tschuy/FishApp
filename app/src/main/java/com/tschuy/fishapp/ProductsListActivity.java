package com.tschuy.fishapp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Iterator;


public class ProductsListActivity extends ActionBarActivity
        implements ProductListFragment.OnFragmentInteractionListener {

    ProductListFragment list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Products");
        setContentView(R.layout.activity_products_list);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .commit();
        }

        list = (ProductListFragment) getFragmentManager().findFragmentById(R.id.list);

        Ion.with(this).load("http://seagrant-staging-api.osuosl.org/1/products")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray products = result.getAsJsonArray("products");
                        Iterator<JsonElement> product_array = products.iterator();
                        while(product_array.hasNext()) {
                            Product product = new Product(product_array.next().getAsJsonObject());
                            list.addItem(product);
                        }
                    }
                });
    }

    public void productClicked(Product product) {
        openProduct(product);
    }


    public void openProduct(Product product) {
        Intent i = new Intent(this, ProductActivity.class);
        i.putExtra("com.tschuy.materialtest.Product", product);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products_list, menu);
        return true;
    }


    public void onFragmentInteraction(String id) {
        Toast.makeText(getApplicationContext(), "Interaction!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        // }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_products_list, container, false);
            return rootView;
        }
    }
}
