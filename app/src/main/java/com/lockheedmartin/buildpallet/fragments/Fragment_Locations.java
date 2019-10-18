package com.lockheedmartin.buildpallet.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.lockheedmartin.buildpallet.Common;
import com.lockheedmartin.buildpallet.R;
import com.lockheedmartin.buildpallet.SpinnerItem;
import com.lockheedmartin.buildpallet.repositories.LocationRepository;


public class Fragment_Locations extends Fragment {

    private TextView textView;
    private int selectedLocationId;
    private String newLocationText;
    private ArrayAdapter<SpinnerItem> spinnerAdapter;
    private Spinner spinner;
    private Spinner spinner2;
    LocationRepository locationRepository;

    public Fragment_Locations() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        locationRepository = new LocationRepository(getActivity());

        GetLocations();

        /*  User selected an item from the Current Locations list */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                View view1 = getView();
                if(view1 != null)
                {
                    SpinnerItem selectedItem = (SpinnerItem)adapterView.getItemAtPosition(pos);
                    TextView currentLocationText  = view1.findViewById(R.id.txtCurrentLocation);
                    currentLocationText.setText(selectedItem.value);
                    selectedLocationId = selectedItem.valueId;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

          /*
           Update Location Button
         */
        Button btnUpdateLocation = getView().findViewById(R.id.btnUpdateLocation);
        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view2 = getView();
                if(view2 != null) {

                    TextView tv  = view2.findViewById(R.id.txtCurrentLocation);
                    newLocationText  = tv.getText().toString().trim();

                    if(newLocationText.length() > 0) {

                        boolean status = locationRepository.UpdateLocation(selectedLocationId,newLocationText);
                        if(status) {
                            Common.showMessage("Update Status","Location updated",getActivity(),Common.MESSAGE_TYPE_SUCCESS );
                            GetLocations();
                            tv.setText("");
                        }
                        else {
                            Common.showMessage("Update Status","Error updating Location",getActivity(),Common.MESSAGE_TYPE_ERROR );
                        }
                    }
                    else {
                        Common.showMessage("Input Error","Please enter a value before submitting.",getActivity(),Common.MESSAGE_TYPE_ERROR );
                    }
                }

            }
        });


         /*
          Add New Location button
         */
        Button btnNewLocation = getView().findViewById(R.id.btnNewLocation);
        btnNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view3 = getView();
                if(view3 != null) {

                    TextView tv  = view3.findViewById(R.id.txtNewLocation);
                    newLocationText  = tv.getText().toString().trim();

                    if(newLocationText.length() > 0)
                    {
                          boolean status = locationRepository.Create(newLocationText);

                        if(status) {
                            Common.showMessage("Add New Location Status","Location added",getActivity(),Common.MESSAGE_TYPE_SUCCESS );
                            GetLocations();
                            tv.setText("");
                        }
                        else {
                            Common.showMessage("Add New Location Status","Error adding Location",getActivity(),Common.MESSAGE_TYPE_ERROR );
                        }
                    }
                    else {
                        Common.showMessage("Input Error","Please enter a value before submitting.",getActivity(),Common.MESSAGE_TYPE_ERROR );
                    }
                }

            }
        });



          /*
            Confirm delete action before Deleting
         */
        Button btnDeleteLocation = getView().findViewById(R.id.btnDeleteLocation);
        btnDeleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view4 = getView();
                if(view4 != null)
                {
                    TextView tv  = view4.findViewById( R.id.txtCurrentLocation);
                    if(tv != null) {
                        String LocationText = tv.getText().toString();
                        ConfirmDelete(LocationText);
                    }
                }

            }
        });

    }

    /*****************************************
     * Get Locations for display
     ******************************************/
    private void GetLocations() {

        Activity a = getActivity();
        if(a != null)
        {
            spinner = a.findViewById(R.id.dlCurrentLocations);
           // spinner2 = a.findViewById(R.id.dlLocations);
            java.util.List<SpinnerItem> locations = locationRepository.GetAll();

            spinnerAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, locations);

            // Specify the layout to use when the list of choices appears
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(spinnerAdapter);
           // spinner2.setAdapter(spinnerAdapter);

        }

    }








    /***************************
     * Call Delete action
     ***************************/
    private void DeleteLocation() {


        boolean status =  locationRepository.Delete(selectedLocationId);

        if(status) {
            Common.showMessage("Delete Location Status","Location deleted",getActivity(),Common.MESSAGE_TYPE_SUCCESS );
            GetLocations();
        }
        else {
            Common.showMessage("Delete Location Status","Error deleting Location",getActivity(),Common.MESSAGE_TYPE_ERROR );
        }
    }


    private void ConfirmDelete(String locationText) {

        Activity a = getActivity();
        if(a != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Delete Confirmation");
            alertDialog.setMessage("Delete Location => " + locationText + "?");
            alertDialog.setIcon(R.drawable.info_icon);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteLocation();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.create().show();
        }

    }







}
