package com.android.yelplite;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
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

        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);

        mProgressDialog = new ProgressDialog(getActivity());

        // TODO: FOR TESTING
        mTestBtn = (Button) view.findViewById(R.id.testing_btn);
        mTestView = (TextView) view.findViewById(R.id.no_list_text);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runQuery();
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



    // TODO: process the query
    private void runQuery() {
        String tag_json_obj = "json_obj_req";
        String url2 ="https://api.github.com/users/TriDangContact";
        String url = "https://api.yelp.com/v3/businesses/north-india-restaurant-san-francisco";
        final String key = "VnI34nhRvMkQX8AniZeKqvQCj" +
                "-pahO3sTvdrmu_nDVTSBDBQHTQUJQxEgvNNTnk_ApqnqfzL38vCqSWvBZWBnTsFBKfpPi3JLSsYtpkh3BS0cnfWB5jB4cLMHNrIXHYx";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Getting Request...");
        mProgressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String values = "";
                        JSONArray ja = new JSONArray();
                        HashMap<String, String> hm = new HashMap<>();
                        try {
//                            values = response.getString("categories");
                            // testing retrieval of JSONArray inside JSON
                            ja = (JSONArray) response.get("categories");
                            // testing retrieval of JSONObjects inside JSONArrayy
                            for (int i = 0; i < ja.length(); ++i) {
                                JSONObject rec = ja.getJSONObject(i);
                                String alias = rec.getString("alias");
                                String title = rec.getString("title");
                                mTestView.setText("VOLLEY: " + alias + " " + title);
                                mProgressDialog.hide();
                            }

                        } catch (JSONException error){
                            Log.d(LOG_TAG, error.toString());
                        }
//                        mTestView.setText("VOLLEY: " +ja);
//                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTestView.setText("VOLLEY ERROR: " + error.getMessage());
                        mProgressDialog.hide();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + key);
                return headers;
            }
        };
        VolleyQueueSingleton
                .getInstance(getActivity()
                .getApplicationContext())
                .addToRequestQueue(jsonObjReq, tag_json_obj);
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
