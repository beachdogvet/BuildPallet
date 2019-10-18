package com.lockheedmartin.buildpallet;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lockheedmartin.buildpallet.adapters.ViewPagerAdapter;
import com.lockheedmartin.buildpallet.repositories.BarcodeRepository;
import com.lockheedmartin.buildpallet.repositories.BaseRepository;
import com.lockheedmartin.buildpallet.ZebraScanner8500;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ICallback {


    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    BarcodeRepository barcodeRepository;
    public  static ZebraScanner8500 zebraScanner8500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //BaseRepository.DeleteCurrentDatabase(this);
        BaseRepository.InitializeDB(this);
        BaseRepository.InitFileOutputLocation();

        barcodeRepository = new BarcodeRepository(this);

      //  zebraScanner8500 = new ZebraScanner8500(this);
      //  zebraScanner8500.init();



    }






    /**
     * CallBack method to Inssrt a scanned barcode into the database and
     * then display in UI list
     *
     * @param scannedBarcode - Barcode object
     *
     */
    public void ProcessScannedBarcode(Barcode scannedBarcode) {

        Boolean returnStatus = false;

        try {
                ArrayList<String> barcodeArrayList = barcodeRepository.GetAllBaggageShortTags();

                if(scannedBarcode.getIsBaggageTag()) {
                    returnStatus = barcodeRepository.InsertBaggageTag(scannedBarcode);
                }
                else {
                    returnStatus = barcodeRepository.InsertTag(scannedBarcode);
                }


                if (returnStatus)
                {
                        ListView BarcodeDisplayList = (ListView)this.findViewById(R.id.lvBarcodes);
                        barcodeArrayList.add(scannedBarcode.getBagggeTagShort());

                        ArrayAdapter<String> baggageShortTagsarrayAdapter =
                                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, barcodeArrayList);
                        BarcodeDisplayList.setAdapter(baggageShortTagsarrayAdapter);
                        Common.PlaySounnd(this,Common.SCAN_BEEP);
                }
                else {

                    Common.showMessage("Error", "Unable to insert barcode to database", this,Common.MESSAGE_TYPE_ERROR);
                }

        }
        catch (Exception e) {
            Log.e("Exception thrown", "ProcessScannedBarcode() " + e.getMessage());
            e.printStackTrace();
        }
    }





}
