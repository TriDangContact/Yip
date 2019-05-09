package com.android.yip;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmptyFragment.OnEmptyFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmptyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmptyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = "EmptyFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText mEditText;
    private Button mTestBtn;
    private TextView mTestView;
    private ProgressDialog mProgressDialog;

    private OnEmptyFragmentInteractionListener mListener;

    public EmptyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmptyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmptyFragment newInstance(String param1, String param2) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);

        mProgressDialog = new ProgressDialog(getActivity());

        // TODO: FOR TESTING
        mEditText = (EditText) view.findViewById(R.id.text_input);
        mTestBtn = (Button) view.findViewById(R.id.testing_btn);
        mTestView = (TextView) view.findViewById(R.id.no_list_text);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint =
                        getString(R.string.yelp_endpoint_business) + getString(R.string.yelp_endpoint_business_search);
                String location = "San Diego, CA";
                String text = mEditText.getText().toString();

                // Build the URI using various parameters
                HashMap<String, String> parameters = new HashMap<>();

                // business search parameters
                // Req: term, location, lat/long (if location == null), limit, offset
                parameters.put("term", text);
                parameters.put("location", location);
                parameters.put("limit", "10");
//              parameters.put("latitude", "32.775807");
//              parameters.put("longitude", "-117.069864");

                // autocomplete parameters
                // Req: text, lat, long, locale(optional)
//              parameters.put("text", text);
//              parameters.put("latitude", "32.775807");
//              parameters.put("longitude", "-117.069864");

                runSearchQuery(endpoint, parameters);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onEmptyFragmentInteraction(string);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmptyFragmentInteractionListener) {
            mListener = (OnEmptyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    // TODO: implement search query to search for businesses based on text entered
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

                        ContentList list = ContentList.get(getContext());
                        List<Business> businessList = list.getBusinesses();
                        businessList.clear();
                        try {
                            JSONArray businessesArray = (JSONArray) response.get("businesses");

                            // getting every bussiness from the business array
                            for (int i = 0; i < businessesArray.length(); i++) {
                                JSONObject businessJO = businessesArray.getJSONObject(i);
                                Business business = new Business(businessJO, getContext());
                                businessList.add(business);
                                mTestView.setText("VOLLEY: " +business.toString());
                                mProgressDialog.hide();
                            }
                        } catch (JSONException e) {
                            Log.d(LOG_TAG, e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = getString(R.string.volley_error) + error.getMessage();
                        mTestView.setText(message);
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
                .getInstance(getContext())
                .addToRequestQueue(jsonObjReq, tag_json_obj_search);
    }

    // TODO: implement autocomplete query everytime user enters a new character
    private void runAutocompleteQuery(String endpoint, HashMap<String,String> parameters) {
        String tag_json_obj_autocomp = "json_obj_autocomp_req";

        String url = getURL(endpoint, parameters);
        Log.d(LOG_TAG, "URL: " + url);

        mProgressDialog.setMessage(getString(R.string.loading_request));
        mProgressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTestView.setText("VOLLEY: " +response.toString());
                        mProgressDialog.hide();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = getString(R.string.volley_error) + error.getMessage();
                        mTestView.setText(message);
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
                .getInstance(getContext())
                .addToRequestQueue(jsonObjReq, tag_json_obj_autocomp);
    }


    // TODO: takes in a jsonOBject and instantiates the Business object using its values
//    public Business createBusinessFromJSON(JSONObject businessJO) {
//        Business newBusiness = new Business();
//
//        newBusiness.setBusinessId(businessJO);
//        newBusiness.setBusinessAlias(businessJO);
//        newBusiness.setBusinessName(businessJO);
//        newBusiness.setBusinessImageUrl(businessJO);
//        if (newBusiness.mImageUrl != null && !newBusiness.mImageUrl.equals("null")) {
//            newBusiness.setBusinessImage(newBusiness.mImageUrl, getContext());
//        }
//        newBusiness.setBusinessIsClosed(businessJO);
//        newBusiness.setBusinessUrl(businessJO);
//        newBusiness.setBusinessPhone(businessJO);
//        newBusiness.setBusinessDisplayPhone(businessJO);
//        newBusiness.setBusinessReviewCount(businessJO);
//        newBusiness.setBusinessRating(businessJO);
//        newBusiness.setBusinessPrice(businessJO);
//        newBusiness.setBusinessAddress(businessJO);
//        newBusiness.setBusinessCategories(businessJO);
//        newBusiness.setBusinessCoordinates(businessJO);
//        newBusiness.setBusinessPhotos(businessJO);
//        newBusiness.setBusinessHours(businessJO);
//
//        return newBusiness;
//    }

    ////////////// RETRIEVING DATA FROM JSONOBJECTS //////////////////////
//    public String getBusinessId(JSONObject businessJO) {
//        String id;
//        try {
//            id = businessJO.getString("id");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            id = "";
//        }
//        return id;
//    }
//
//    public String getBusinessAlias(JSONObject businessJO) {
//        String alias;
//        try {
//            alias = businessJO.getString("alias");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            alias = "Error retrieving alias";
//        }
//        return alias;
//    }
//
//    public String getBusinessName(JSONObject businessJO) {
//        String name;
//        try {
//            name = businessJO.getString("name");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            name = "Error retrieving name";
//        }
//        return name;
//    }
//    public String getBusinessImageUrl(JSONObject businessJO) {
//        String imageUrl;
//        try {
//            imageUrl = businessJO.getString("image_url");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            imageUrl = "null";
//        }
//        return imageUrl;
//    }
//
//    public boolean getBusinessIsClosed(JSONObject businessJO) {
//        boolean isClosed;
//        try {
//            isClosed = businessJO.getBoolean("is_closed");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            isClosed = false;
//        }
//        return isClosed;
//    }
//
//    public String getBusinessUrl(JSONObject businessJO) {
//        String url;
//        try {
//            url = businessJO.getString("url");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            url = "Error retrieving url";
//        }
//        return url;
//    }
//
//    public String getBusinessPhone(JSONObject businessJO) {
//        String phone;
//        try {
//            phone = businessJO.getString("phone");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            phone = "";
//        }
//        return phone;
//    }
//
//    public String getBusinessDisplayPhone(JSONObject businessJO) {
//        String displayPhone;
//        try {
//            displayPhone = businessJO.getString("display_phone");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            displayPhone = "";
//        }
//        return displayPhone;
//    }
//
//    public int getBusinessReviewCount(JSONObject businessJO) {
//        int reviewCount = 0;
//        try {
//            reviewCount = businessJO.getInt("review_count");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//        }
//        return reviewCount;
//    }
//
//    public String getBusinessRating(JSONObject businessJO) {
//        String rating = "0.0";
//        try {
//            rating = (String) String.valueOf(businessJO.getDouble("rating"));
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//        }
//        return rating;
//    }
//
//    public String getBusinessPrice(JSONObject businessJO) {
//        String price;
//        try {
//            price = businessJO.getString("price");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            price = "Error retrieving price";
//        }
//        return price;
//    }
//
//    public String getBusinessAddress(JSONObject businessJO) {
//        String address1 = "";
//        String address2 = "";
//        String address3 = "";
//        String city = "";
//        String state = "";
//        String zipcode = "";
//        String country = "";
//        String address = "";
//
//        try {
//            JSONObject locationObject = (JSONObject) businessJO.get("location");
//            address1 = locationObject.getString("address1");
//            address2 = locationObject.getString("address2");
//            address3 = locationObject.getString("address3");
//            city = locationObject.getString("city");
//            state = locationObject.getString("state");
//            zipcode = locationObject.getString("zip_code");
//            country = locationObject.getString("country");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//        }
//
//        if (address1 != null && !address1.equals("null") && !address1.isEmpty()) {
//            address = address + address1;
//        }
//        if (address2 != null && !address2.equals("null") && !address2.isEmpty()) {
//            address = address + ", " + address2;
//        }
//        if (address3 != null && !address3.equals("null") && !address3.isEmpty()) {
//            address = address + ", " + address3;
//        }
//        if (city != null && !city.equals("null") && !city.isEmpty()) {
//            address = address + ", " + city;
//        }
//        if (state != null && !state.equals("null") && !state.isEmpty()) {
//            address = address + ", " + state;
//        }
//        if (zipcode != null && !zipcode.equals("null") && !zipcode.isEmpty()) {
//            address = address + " " + zipcode;
//        }
//        if (country != null && !country.equals("null") && !country.isEmpty()) {
//            address = address + ", " + country;
//        }
//
//        return address;
//    }
//
//    // Getting Categories from each business
//    public String getBusinessCategories(JSONObject businessJO) {
//        String categories = "";
//        try {
//            JSONArray categoriesArray = (JSONArray) businessJO.get("categories");
//            if (categoriesArray.length() <= 0) {
//                categories = "No categories";
//            } else {
//                if (categoriesArray.length() > 1) {
//                    // get every category, up to the second to last one
//                    for (int j = 0; j < categoriesArray.length() - 1; j++) {
//                        JSONObject category = categoriesArray.getJSONObject(j);
//                        String title = category.getString("title");
//                        categories = categories.concat(title + ", ");
//                    }
//                }
//                // add the last category if more than 1, or the only category if only 1
//                JSONObject category = categoriesArray.getJSONObject(categoriesArray.length() - 1);
//                String title = category.getString("title");
//                categories = categories.concat(title);
//            }
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//            categories = "Error retrieving categories";
//        }
//        return categories;
//    }
//
//    public double[] getBusinessCoordinates(JSONObject businessJO) {
//        double coordinates[] = {0.0, 0.0};
//        try {
//            JSONObject coordinatesObject = (JSONObject) businessJO.get("coordinates");
//            coordinates[0] = coordinatesObject.getDouble("latitude");
//            coordinates[1] = coordinatesObject.getDouble("longitude");
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//        }
//        return coordinates;
//    }
//
//    public ArrayList<String> getBusinessPhotos(JSONObject businessJO) {
//        ArrayList<String> photos = new ArrayList<>();
//        try {
//            JSONArray photosArray = (JSONArray) businessJO.getJSONArray("photos");
//            if (photosArray != null) {
//                for (int i = 0; i < photosArray.length(); i++) {
//                    photos.add(photosArray.get(i).toString());
//                }
//            }
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//        }
//        return photos;
//    }
//
//    public ArrayList<JSONObject> getBusinessHours(JSONObject businessJO) {
//        ArrayList<JSONObject> hours = new ArrayList<>();
//        try {
//            JSONArray hoursArray = (JSONArray) businessJO.getJSONArray("hours");
//            if (hoursArray != null) {
//                for (int i = 0; i < hoursArray.length(); i++) {
//                    JSONObject hoursObject = (JSONObject) hoursArray.get(i);
//                    JSONArray openArray = (JSONArray) hoursObject.get("open");
//                    if (openArray != null) {
//                        for (int j = 0; j < openArray.length(); j++) {
//                            JSONObject dayObject = (JSONObject) openArray.get(j);
//                            hours.add(dayObject);
//                        }
//                    }
//                }
//            }
//        } catch (JSONException exception) {
//            Log.d(LOG_TAG, exception.toString());
//        }
//        return hours;
//    }





    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEmptyFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEmptyFragmentInteraction(String string);
    }
}
