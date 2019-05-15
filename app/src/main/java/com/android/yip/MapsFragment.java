package com.android.yip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsFragment extends Fragment {
    private static final String LOG_TAG = "MapsFragment";
    private static final String EXTRA_ID = "com.android.yip.id";
    private static final float ZOOM_TO_VALUE = 10.0f;

    private GoogleMap mMap;
    private OnMapsFragmentInteractionListener mListener;

    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                ContentList list = ContentList.get(getActivity());
                List<Business> businessList = list.getBusinesses();
                LatLng startingSpot;

                // Have to check if list is empty, or else map will crash trying to display null
                // markers
                if (businessList.size() > 0) {
                    // Pan the camera to the first business in our list
                    Business firstBusiness = businessList.get(0);
                    startingSpot = new LatLng(firstBusiness.mLatitude,
                            firstBusiness.mLongitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(startingSpot));
                    mMap.moveCamera((CameraUpdateFactory.zoomTo(ZOOM_TO_VALUE)));

                    // Add a marker for every business in the list
                    for (Business business : businessList) {
                        int index = businessList.indexOf(business) + 1;
                        LatLng businessMarker = new LatLng(business.mLatitude, business.mLongitude);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(businessMarker)
                                .title(index + ". " + business.mName));
                        // store the business id so we can use it when the info window is clicked on
                        marker.setTag(business);
                    }
                }
                else {
                    // retrieving values from resource files
                    TypedValue typedValue = new TypedValue();
                    getResources().getValue(R.dimen.map_default_latitude, typedValue, true);
                    float latitude = typedValue.getFloat();
                    getResources().getValue(R.dimen.map_default_latitude, typedValue, true);
                    float longitude = typedValue.getFloat();

                    startingSpot = new LatLng(latitude,longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(startingSpot));
                }

                // make the map utilize our custom info window
                mMap.setInfoWindowAdapter(new MapsCustomInfoWindowAdapter(getContext()));

                // display the business detail for that marker when the info window is clicked
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Business business = (Business) marker.getTag();
                        displayBusinessDetail(business.mId);
                    }
                });
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onMapsFragmentInteraction(string);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapsFragmentInteractionListener) {
            mListener = (OnMapsFragmentInteractionListener) context;
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

    private void displayBusinessDetail(String id) {
        Intent intent = new Intent(getContext(), BusinessDetailActivity.class);
        intent.putExtra(EXTRA_ID, id);
        startActivity(intent);
    }

    public interface OnMapsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMapsFragmentInteraction(String string);
    }
}
