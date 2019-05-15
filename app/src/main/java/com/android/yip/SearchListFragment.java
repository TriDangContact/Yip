package com.android.yip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SearchListFragment extends Fragment {
    private OnSearchListFragmentInteractionListener mListener;

    public SearchListFragment() {
    }

    @SuppressWarnings("unused")
    public static SearchListFragment newInstance() {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        // put any argument you want into bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // retrieve the arguments from bundle here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        ContentList list = ContentList.get(getContext());
        List<Business> businessList = list.getBusinesses();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new SearchRecyclerViewAdapter(businessList, mListener, getContext() ));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchListFragmentInteractionListener) {
            mListener = (OnSearchListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSearchListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchListFragmentInteraction(Business item);
    }
}
