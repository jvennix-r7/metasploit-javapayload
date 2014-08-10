package com.metasploit.meterpreter.android;

import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;

import android.os.Vibrator;

public class vibrate implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_VIBRATE_DURATION = TLVPacket.TLV_META_TYPE_STRING | (TLV_EXTENSIONS + 9021);

    @Override
    public int execute(Meterpreter meterpreter, TLVPacket request, TLVPacket response) throws Exception {
        int duration = Float.parseFloat(request.getStringValue(TLV_TYPE_VIBRATE_DURATION));
        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        if (v.hasVibrator()) {       
          v.vibrate(duration*1000);
          return ERROR_SUCCESS;
        } else {
          return ERROR_FAILURE;
        }
    }
}
