package com.android.yip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnMapsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String LOG_TAG = "MapsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;

    private OnMapsFragmentInteractionListener mListener;
    private GoogleMap.OnInfoWindowClickListener mOnInfoWindowClickListener;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
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
                if (businessList.size() >= 1) {
                    // Pan the camera to the first business in our list
                    Business firstBusiness = businessList.get(0);
                    startingSpot = new LatLng(firstBusiness.mLatitude,
                            firstBusiness.mLongitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(startingSpot));
                    mMap.moveCamera((CameraUpdateFactory.zoomTo(10.0f)));

                    // Add a marker in for every business in the list
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
                    startingSpot = new LatLng(32.7157,-117.1611);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(startingSpot));
                }

                mMap.setInfoWindowAdapter(new MapsCustomInfoWindowAdapter(getContext()));

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Toast.makeText(getContext(),
                                "Marker: " +marker.getTitle() + ", Tag: " + marker.getTag(),
                                Toast.LENGTH_SHORT).show();
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
    public interface OnMapsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMapsFragmentInteraction(String string);
    }
}
