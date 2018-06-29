package com.example.uidk9044.mobilecontrol.Controler;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by uidk9044 on 22-Sep-16.
 */
public class JSON {
    public static String toJSon(Mesage mesage){
        try {
            JSONObject json = new JSONObject();
            json.put("target","main_handler");

                JSONObject jsonMes = new JSONObject();
                //jsonMes.put("name", mesage.getName());
                jsonMes.put("cmd","send");

                    JSONObject jsonData = new JSONObject();
                    jsonData.put("ID", mesage.getId());
                    jsonData.put("DLC", mesage.getDLC());
                    jsonData.put("Byte_0", mesage.getByte0());
                    jsonData.put("Byte_1", mesage.getByte1());
                    jsonData.put("Byte_2", mesage.getByte2());
                    jsonData.put("Byte_3", mesage.getByte3());
                    jsonData.put("Byte_4", mesage.getByte4());
                    jsonData.put("Byte_5", mesage.getByte5());
                    jsonData.put("Byte_6", mesage.getByte6());
                    jsonData.put("Byte_7", mesage.getByte7());

                jsonMes.put("data", jsonData);

            json.put("message",jsonMes);

//            json = jsonObj;
//          //  JSONObject con = json.getJSONObject("content");
//
//            String ceva = new String();
//            ceva = " Jesonul este : " + json.getString("name") + "cu id =" + json.getString("id");
//            ceva = ceva + "/n";
//            //ceva = ceva + "si cu biti :" + con.getString("byte1");
            Log.i("Mesage1",json.toString());
            return json.toString();
            //return  ceva;
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }
    public static JSONArray jsonForDynamicMenu(){
        try {
            JSONArray jsonObject = new JSONArray();

            JSONObject jsonRow1 = new JSONObject();
            JSONObject jsonComponents1 = new JSONObject();
            jsonRow1.put("rowNumber",1);
            jsonRow1.put("id",1);
            jsonComponents1.put("componenta", "Volan");
            jsonComponents1.put("imageName","volan");
            jsonComponents1.put("buttonName","Verifica");
            jsonRow1.put("components", jsonComponents1);
            jsonObject.put(jsonRow1);

            JSONObject jsonRow2 = new JSONObject();
            JSONObject jsonComponents2 = new JSONObject();
            jsonRow2.put("rowNumber",2);
            jsonRow2.put("id",2);
            jsonComponents2.put("componenta", "Baterie");
            jsonComponents2.put("imageName","baterie");
            jsonComponents2.put("buttonName","Verifica");
            jsonRow2.put("components", jsonComponents2);
            jsonObject.put(jsonRow2);

            JSONObject jsonRow3 = new JSONObject();
            JSONObject jsonComponents3 = new JSONObject();
            jsonRow3.put("rowNumber",3);
            jsonRow3.put("id",3);
            jsonComponents3.put("componenta", "Frana");
            jsonComponents3.put("imageName","frana");
            jsonComponents3.put("buttonName","Verifica");
            jsonRow3.put("components", jsonComponents3);
            jsonObject.put(jsonRow3);

            JSONObject jsonRow4 = new JSONObject();
            JSONObject jsonComponents4 = new JSONObject();
            jsonRow4.put("rowNumber",4);
            jsonRow4.put("id",4);
            jsonComponents4.put("componenta", "Roata");
            jsonComponents4.put("imageName","roata");
            jsonComponents4.put("buttonName","Verifica");
            jsonRow4.put("components", jsonComponents4);
            jsonObject.put(jsonRow4);

            JSONObject jsonRow5 = new JSONObject();
            JSONObject jsonComponents5 = new JSONObject();
            jsonRow5.put("rowNumber",5);
            jsonRow5.put("id",5);
            jsonComponents5.put("componenta", "Transmisie");
            jsonComponents5.put("imageName","transmisie");
            jsonComponents5.put("buttonName","Verifica");
            jsonRow5.put("components", jsonComponents5);
            jsonObject.put(jsonRow5);

            JSONObject jsonRow6 = new JSONObject();
            JSONObject jsonComponents6 = new JSONObject();
            jsonRow6.put("rowNumber",6);
            jsonRow6.put("id",6);
            jsonComponents6.put("componenta", "Ulei");
            jsonComponents6.put("imageName","ulei");
            jsonComponents6.put("buttonName","Verifica");
            jsonRow6.put("components", jsonComponents6);
            jsonObject.put(jsonRow6);

            JSONObject jsonRow7 = new JSONObject();
            JSONObject jsonComponents7 = new JSONObject();
            jsonRow7.put("rowNumber",7);
            jsonRow7.put("id",7);
            jsonComponents7.put("componenta", "Turometru");
            jsonComponents7.put("imageName","turometru");
            jsonComponents7.put("buttonName","Verifica");
            jsonRow7.put("components", jsonComponents7);
            jsonObject.put(jsonRow7);
//            Log.i("ceva",jsonObject.toString());
            return jsonObject;
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
