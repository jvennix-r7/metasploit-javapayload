package com.metasploit.meterpreter.android;

import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;

import android.media.AudioManager;

public class speak implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_VOLUME_LEVEL = TLVPacket.TLV_META_TYPE_BOOL | (TLV_EXTENSIONS + 9023);

    @Override
    public int execute(Meterpreter meterpreter, TLVPacket request, TLVPacket response) throws Exception {
        int level = request.getIntValue(TLV_TYPE_VOLUME_LEVEL);

        AudioManager audioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.floor(getStreamMaxVolume() * 0.1 * level), AudioManager.FLAG_SHOW_UI);

        return ERROR_SUCCESS;
    }

}