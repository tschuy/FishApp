package com.tschuy.fishapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProductActivity extends Activity {

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);
        Bundle b = getIntent().getExtras();
        product = b.getParcelable("com.tschuy.materialtest.Product");
        if (product == null) {
            getProduct(getIntent().getLongExtra("product_key", 0));
        } else {
            loadProduct(product);
        }
    }

    private void getProduct(long product_id) {
        Ion.with(this).load(String.format("http://seagrant-staging-api.osuosl.org/1/products/%d", product_id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        product = new Product(result);
                        loadProduct(product);
                    }
                });
    }

    private void loadProduct(Product product) {
        setTitle(product.toString());
        ((TextView) findViewById(R.id.product_description)).setText(product.description);
        if (product.alternate_name != null && !product.alternate_name.equals("")) {
            ((TextView) findViewById(R.id.alternate_name)).setText("Also known as " + product.alternate_name);
        } else {
            findViewById(R.id.alternate_name).setVisibility(View.INVISIBLE);
        }
        if (product.season != null && !product.season.equals("")) {
            ((TextView) findViewById(R.id.season)).setText(product.season);
        } else {
            findViewById(R.id.season).setVisibility(View.INVISIBLE);
            findViewById(R.id.season_title).setVisibility(View.INVISIBLE);
        }
        if (product.origin != null && !product.origin.equals("")) {
            ((TextView) findViewById(R.id.origin)).setText(product.origin);
        } else {
            findViewById(R.id.origin).setVisibility(View.INVISIBLE);
            findViewById(R.id.origin_title).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vendor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
