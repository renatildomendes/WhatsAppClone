package com.cursoandroid.whatsappclone.whatsappclone.helper;

import android.util.Base64;

/**
 * Created by Renato on 04/07/2018.
 */

public class Base64Custom {

    public static String codificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }


    public static String decodificaBase64(String textoCodificado){
        return new String (Base64.decode(textoCodificado,Base64.DEFAULT));
    }

}
