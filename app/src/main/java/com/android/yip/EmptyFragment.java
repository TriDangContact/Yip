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
    private EditText mSearchInput;
    private EditText mLocationInput;
    private Button mSearchBtn;
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
        mSearchInput = (EditText) view.findViewById(R.id.search_input);
        mLocationInput = (EditText) view.findViewById(R.id.location_input);
        mSearchBtn = (Button) view.findViewById(R.id.search_btn);
        mTestView = (TextView) view.findViewById(R.id.no_list_text);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint =
                        getString(R.string.yelp_endpoint_business) + getString(R.string.yelp_endpoint_business_search);
                String location = mLocationInput.getText().toString();
                if (location.isEmpty()) {
                    location = getString(R.string.default_location);
                }
                String text = mSearchInput.getText().toString();

                // Build the URI using various parameters
                HashMap<String, String> parameters = new HashMap<>();

                // business search parameters
                // Req: term, location, lat/long (if location == null), limit, offset
                parameters.put(getString(R.string.yelp_parameters_business_search_term), text);
                parameters.put(getString(R.string.yelp_parameters_business_search_location), location);
                parameters.put(getString(R.string.yelp_parameters_business_search_limit), "10");
//                parameters.put(getString(R.string.yelp_parameters_business_search_offset), "0");
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
                            JSONArray businessesArray =
                                    (JSONArray) response.get(getString(R.string.yelp_response_business_search));

                            // getting every business from the business array
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
                        mTestView.setText(R.string.volley_success);
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
