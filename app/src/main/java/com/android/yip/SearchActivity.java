package com.android.yip;

import android.app.ProgressDialog;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SearchActivity";
    private AutoCompleteTextView mSearchInput;
    private EditText mLocationInput;
    private ImageButton mSearchBtn;
    private TextView mTestView;
    private ProgressDialog mProgressDialog;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mProgressDialog = new ProgressDialog(this);
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mSearchInput = (AutoCompleteTextView) findViewById(R.id.search_input);
        mLocationInput = (EditText) findViewById(R.id.location_input);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);
        mTestView = (TextView) findViewById(R.id.no_list_text);

        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchInput.getText().toString();
                String endpointAC = getString(R.string.yelp_endpoint_autocomplete);
                HashMap<String, String> parametersAC = new HashMap<>();
                parametersAC.put(getString(R.string.yelp_parameters_autocomplete_text), text);
                runAutocompleteQuery(endpointAC, parametersAC);

                ContentList list = ContentList.get(getApplicationContext());
                List<String> suggestionList = list.getSuggestions();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        suggestionList);
                mSearchInput.setAdapter(adapter);
                mSearchInput.setThreshold(0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint =
                        getString(R.string.yelp_endpoint_business) + getString(R.string.yelp_endpoint_business_search);
                String location = mLocationInput.getText().toString();
                if (location.isEmpty()) {
//                    requestCurrentLocation();
                    location = getString(R.string.default_location);
                }
                String text = mSearchInput.getText().toString();

                // Build the URI using various param1eters
                HashMap<String, String> parameters = new HashMap<>();

                // business search parameters
                // Req: term, location, lat/long (if location == null), limit, offset
                parameters.put(getString(R.string.yelp_parameters_business_search_term), text);
                parameters.put(getString(R.string.yelp_parameters_business_search_location), location);
                parameters.put(getString(R.string.yelp_parameters_business_search_limit), "10");
//                parameters.put(getString(R.string.yelp_parameters_business_search_offset), "0");
//              parameters.put("latitude", "32.775807");
//              parameters.put("longitude", "-117.069864");

                runSearchQuery(endpoint, parameters);
            }
        });
    }

    /**
     * @param path the endpoints for the request
     * @param hashMap the map that has the key, value pairs of parameters
     * @return the complete URL string
     */
    private String getURL(String path, HashMap<String, String> hashMap) {
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.request_protocol))
                .authority(getString(R.string.yelp_api_domain))
                .path(path);

        // get every value f
        for (HashMap.Entry<String, String> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException exception) {
                Log.d(LOG_TAG, exception.getMessage());
            }
            builder.appendQueryParameter(key, value);
        }

        builder.build();
        url = builder.toString();
        return url;
    }


    // search query to search for businesses based on text entered
    private void runSearchQuery(String endpoint, HashMap<String,String> parameters) {
        String tag_json_obj_search = "json_obj_search_req";

        String url = getURL(endpoint, parameters);
        Log.d(LOG_TAG, "URL: " + url);

        mProgressDialog.setMessage(getString(R.string.loading_request));
        mProgressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO: instantiate Business object using response, and add Business
                        // object to BusinessList

                        ContentList list = ContentList.get(getApplicationContext());
                        List<Business> businessList = list.getBusinesses();
                        businessList.clear();
                        try {
                            JSONArray businessesArray =
                                    (JSONArray) response.get(getString(R.string.yelp_response_business_search));

                            // getting every business from the business array
                            for (int i = 0; i < businessesArray.length(); i++) {
                                JSONObject businessJO = businessesArray.getJSONObject(i);
                                Business business = new Business(businessJO, getApplicationContext());
                                businessList.add(business);
                                Log.d(LOG_TAG, getString(R.string.volley_success));
//                                mTestView.setText(R.string.volley_success);
                                mProgressDialog.hide();
                                setResult(RESULT_OK);
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.d(LOG_TAG, e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = getString(R.string.volley_error) + error.getMessage();
                Log.d(LOG_TAG, message);
                mTestView.setText(getString(R.string.volley_search_error));
                mProgressDialog.hide();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // TODO: Authentication
                headers.put(getString(R.string.request_header_key),
                        getString(R.string.request_header_value) + " " + getString(R.string.yelp_api_key));
                return headers;
            }
        };
        VolleyQueueSingleton
                .getInstance(getApplicationContext())
                .addToRequestQueue(jsonObjReq, tag_json_obj_search);
    }

    // TODO: implement autocomplete query everytime user enters a new character
    private void runAutocompleteQuery(String endpoint, HashMap<String,String> parameters) {
        String tag_json_obj_autocomp = "json_obj_autocomp_req";

        String url = getURL(endpoint, parameters);
        Log.d(LOG_TAG, "URL: " + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ContentList list = ContentList.get(getApplicationContext());
                        List<String> suggestionList = list.getSuggestions();
                        suggestionList.clear();

                        try {
                            JSONArray termsArray = (JSONArray)
                                    response.get(getString(R.string.yelp_response_autocomplete_terms));

                            for (int i = 0; i < termsArray.length(); i++) {
                                JSONObject textJO = termsArray.getJSONObject(i);
                                String text =
                                        textJO.getString(getString(R.string.yelp_response_autocomplete_text));
                                suggestionList.add(text);
                                Log.d(LOG_TAG, text);
                            }
                        } catch (JSONException exception) {
                            Log.d(LOG_TAG, exception.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = getString(R.string.volley_error) + error.getMessage();
                Log.d(LOG_TAG, message);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // TODO: Authentication
                headers.put(getString(R.string.request_header_key),
                        getString(R.string.request_header_value) + " " + getString(R.string.yelp_api_key));
                return headers;
            }
        };
        VolleyQueueSingleton
                .getInstance(getApplicationContext())
                .addToRequestQueue(jsonObjReq, tag_json_obj_autocomp);
    }

//    private void requestCurrentLocation() {
//        mFusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                        }
//                    }
//                });
//    }

}
