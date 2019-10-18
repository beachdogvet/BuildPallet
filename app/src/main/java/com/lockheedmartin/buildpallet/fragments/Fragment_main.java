package com.lockheedmartin.buildpallet.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lockheedmartin.buildpallet.Common;
import com.lockheedmartin.buildpallet.MainActivity;
import com.lockheedmartin.buildpallet.R;
import com.lockheedmartin.buildpallet.ZebraScanner8500;
import com.lockheedmartin.buildpallet.repositories.BarcodeRepository;
import com.lockheedmartin.buildpallet.repositories.LocationRepository;

import java.util.ArrayList;


public class Fragment_main extends Fragment  {


    private ZebraScanner8500 zebraScanner8500;
    LocationRepository locationRepository;
    BarcodeRepository barcodeRepository;
    ListView BarcodeDisplayList;
    ArrayList<String> barcodeArrayList;
    ArrayAdapter<String> baggageShortTagsarrayAdapter;

    public Fragment_main() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

       // menu.add(1, Common.OPTIONS_MENU_ITEM_ID_1, 1, "Connect / Pair");
         menu.add(1, Common.OPTIONS_MENU_ITEM_ID_1, 1, "Clear barcode list");
         menu.add(1, Common.EXIT_APPLICATION, 2, "Exit");
      //  menu.add(1, Common.EXIT_APPLICATION, 3, "Exit");
        inflater.inflate(R.menu.fragment_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case Common.OPTIONS_MENU_ITEM_ID_1:
              //  MainActivity.zebraScanner8500.connectToScanner();
                barcodeRepository.DeleteAll();
                GetAndDisplayBarcodes();

                break;

            case Common.OPTIONS_MENU_ITEM_ID_2:
//                Common.showMessage("Option Selected", "You selected Fragment 1 option 2",
//                        getContext(), Common.MESSAGE_TYPE_INFORMATION);
              //  barcodeRepository.DeleteAll();
             //   GetAndDisplayBarcodes();

                break;

            case Common.EXIT_APPLICATION:

                getActivity().finishAndRemoveTask();
                System.exit(0);
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //*************  Get and display locations from database  ****************
//        locationRepository = new LocationRepository(getActivity());
//
//        Spinner spinner = (Spinner)getActivity().findViewById(R.id.dlLocations);
//
//        java.util.List<SpinnerItem> locations = locationRepository.GetAll();
//
//        ArrayAdapter<SpinnerItem> spinnerAdapter = new ArrayAdapter<SpinnerItem>(getActivity(),
//                android.R.layout.simple_spinner_item, locations);
//
//        // Specify the layout to use when the list of choices appears
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinner.setAdapter(spinnerAdapter);

        GetAndDisplayBarcodes();

    }


     private void GetAndDisplayBarcodes()
     {

         barcodeRepository = new BarcodeRepository(getActivity());
         barcodeArrayList = barcodeRepository.GetAllBaggageShortTags();

         baggageShortTagsarrayAdapter =
                 new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, barcodeArrayList);

         BarcodeDisplayList = (ListView)getActivity().findViewById(R.id.lvBarcodes);
         BarcodeDisplayList.setAdapter(baggageShortTagsarrayAdapter);

     }


}
