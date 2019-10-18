package com.lockheedmartin.buildpallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lockheedmartin.buildpallet.dialogs.CustomProgressDialog;
import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.scannercontrol.DCSScannerInfo;
import com.zebra.scannercontrol.FirmwareUpdateEvent;
import com.zebra.scannercontrol.IDcsSdkApiDelegate;
import com.zebra.scannercontrol.SDKHandler;

import java.util.ArrayList;

public class ZebraScanner8500 implements IDcsSdkApiDelegate {




    private SDKHandler sdkHandler;
    private int connectedScannerID;
    private ArrayList<DCSScannerInfo> mScannerInfoList = new ArrayList<>();
    private static final int BARCODE_RECEIVED = 30;
    int STATUS_BAR_COLOR_CONNECTED_TO_SCANNER = Color.BLUE;
    int STATUS_BAR_COLOR_NOT_CONNECTED_TO_SCANNER = Color.RED;

    ICallback callbackListener;
    Context  activityContext;


    public ZebraScanner8500(Context _activityContext) {

        this.activityContext = _activityContext;

    }

    public void init() {

        callbackListener = (ICallback)activityContext;

        sdkHandler = new SDKHandler(activityContext);
        sdkHandler.dcssdkSetDelegate(this);


        //Communicate via Bluetooth
        sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_NORMAL);

        //communicate via USB
        sdkHandler.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_SNAPI);

        //Suscribe to various events
        int notifications_mask = 0;

        // subscribe to all scanner available/not-available events
        notifications_mask |=
                DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_APPEARANCE.value |
                        DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_DISAPPEARANCE.value;

        // subscribe to all scanner connection events
        notifications_mask |=
                DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_ESTABLISHMENT.value |
                        DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_TERMINATION.value;

        // subscribe to all barcode events
        notifications_mask |= DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_BARCODE.value;


        sdkHandler.dcssdkSubsribeForEvents(notifications_mask);
        sdkHandler.dcssdkEnableAvailableScannersDetection(true);
        sdkHandler.dcssdkGetAvailableScannersList(mScannerInfoList);
        sdkHandler.dcssdkGetActiveScannersList(mScannerInfoList);
    }


    //region IDcsSdkApiDelegate Override Methods

    @Override
    public void dcssdkEventScannerAppeared(DCSScannerInfo dcsScannerInfo) {

        String scannerName = dcsScannerInfo.getScannerName();
        if (scannerName.startsWith("RFD8500")) {
            connectedScannerID = dcsScannerInfo.getScannerID();
            connectToScanner();
        }

    }

    @Override
    public void dcssdkEventScannerDisappeared(int scannerId) {
        sdkHandler.dcssdkTerminateCommunicationSession(scannerId);
    }

    @Override
    public void dcssdkEventCommunicationSessionEstablished(DCSScannerInfo dcsScannerInfo) {

        //   Toast.makeText(context, "Commnications establishled with scanner", Toast.LENGTH_LONG).show();

    }

    @Override
    public void dcssdkEventCommunicationSessionTerminated(int i) {

    }

    @Override
    public void dcssdkEventBarcode(byte[] barcodeData, int barcodeType, int fromScannerID) {

        Barcode barcode = new Barcode(barcodeData, barcodeType, fromScannerID);
        dataHandler.obtainMessage(BARCODE_RECEIVED,barcode).sendToTarget();

    }

    @Override
    public void dcssdkEventImage(byte[] bytes, int i) {

    }

    @Override
    public void dcssdkEventVideo(byte[] bytes, int i) {

    }

    @Override
    public void dcssdkEventBinaryData(byte[] bytes, int i) {

    }

    @Override
    public void dcssdkEventFirmwareUpdate(FirmwareUpdateEvent firmwareUpdateEvent) {

    }

    @Override
    public void dcssdkEventAuxScannerAppeared(DCSScannerInfo dcsScannerInfo, DCSScannerInfo dcsScannerInfo1) {

    }

//endregion

    public void connectToScanner(){

        new ProcessAsyncTask(connectedScannerID, activityContext).execute();
    }


    @SuppressLint("HandlerLeak")
    Handler dataHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            Barcode barcode = null;

            switch (msg.what) {

                case BARCODE_RECEIVED:

                    barcode=(Barcode)msg.obj;
                    callbackListener.ProcessScannedBarcode(barcode);
                    break;
            }
        }
    };




    private class ProcessAsyncTask extends AsyncTask<Void,Integer,Boolean> {

        private int scannerId;
        private Context context;
        AlertDialog progressDialog = null;
        String statusMessage = "";
        boolean isConnected = false;

        public ProcessAsyncTask(int _scannerId, Context _context){
            this.scannerId = _scannerId;
            this.context = _context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =  new CustomProgressDialog(context,"Searching for available scanners...").dialog;
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            DCSSDKDefs.DCSSDK_RESULT result =  DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_FAILURE;

            if (sdkHandler != null) {
                result = sdkHandler.dcssdkEstablishCommunicationSession(scannerId);
            }

            if(result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_SUCCESS){

                statusMessage = "SUCCESS: Connected to scanner device.";
                isConnected = true;
                return true;
            }
            else if(result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_FAILURE) {

                statusMessage = "FAILURE: Could not find an active scanner device to connect to.";
                return false;
            }
            else if(result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_SCANNER_ALREADY_ACTIVE)
            {
                statusMessage = "INFO: Already connected to Scanner device.";
                return true;
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean status) {
            // super.onPostExecute(status);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            if(isConnected) {
                ((Activity)context).getWindow().setStatusBarColor(STATUS_BAR_COLOR_CONNECTED_TO_SCANNER);
            }
            else {
                ((Activity)context).getWindow().setStatusBarColor(STATUS_BAR_COLOR_NOT_CONNECTED_TO_SCANNER);
            }

            if(statusMessage.length() > 0)
            {
                Toast.makeText(context, statusMessage, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, "FAILURE: Unable to connect to scanner device.", Toast.LENGTH_LONG).show();
            }

        }
    }


}
