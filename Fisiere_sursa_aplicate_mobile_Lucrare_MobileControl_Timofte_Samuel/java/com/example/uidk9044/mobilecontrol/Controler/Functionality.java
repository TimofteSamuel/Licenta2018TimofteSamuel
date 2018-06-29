package com.example.uidk9044.mobilecontrol.Controler;

import android.util.Log;

import com.example.uidk9044.mobilecontrol.Activitys.StaticMenuActivity;

import java.util.Date;
import java.util.Timer;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * Created by uidk9044 on 22-Sep-16.
 */
public class Functionality {
    public static String mesage1;
    public static String mesage2;
//    public static Timer timer = new Timer();
//    public static TimerTask functie = new MyTimerTask();

    public static void setMesage(){
        Mesage mes1 = new Mesage();
        mes1.setDLC(8);
        mes1.setId(0x193);
        mes1.setByte0((byte)0);
        mes1.setByte1((byte)0);
        mes1.setByte2((byte)5);//blinker aprins
        mes1.setByte3((byte)0);
        mes1.setByte4((byte)0);
        mes1.setByte5((byte)0);
        mes1.setByte6((byte)0);
        mes1.setByte7((byte)0);

        Mesage mes2 = new Mesage();
        mes2.setDLC(8);
        mes2.setId(0x192);
        mes2.setByte0((byte)0);
        mes2.setByte1((byte)0);
        mes2.setByte2((byte)0);
        mes2.setByte3((byte)0);
        mes2.setByte4((byte)0);
        mes2.setByte5((byte)0);
        mes2.setByte6((byte)255);
        mes2.setByte7((byte)255);

        mesage1 = JSON.toJSon(mes1);
        Log.i("parsat",mesage1);
        mesage2 = JSON.toJSon(mes2);
        Log.i("parsat",mesage2);
    }


//    public static void TrimiteMesajCiclic(){
//        setMesage();
//        timer.scheduleAtFixedRate(functie, 100, 100);
//    }
//    public static void OpresteTrimiterea(){
//        timer.cancel();
//    }

}

//class MyTimerTask extends TimerTask {
//    public Connect connect =  Connect.getInstance();
//    public void run() {
//        StaticMenuActivity.this.runOnUiThread(new Runnable() {
//            public void run() {
//                connect.sendMessage(Functionality.mesage1);
//                connect.sendMessage(Functionality.mesage2);
//            }
//        });
//
//    }
//}




//    Mesage mes2 = new Mesage();
//        mes2.setDLC(8);
//        mes2.setId(32);
//        mes2.setByte0((byte)0);
//        mes2.setByte1((byte)0);
//        mes2.setByte2((byte)0);
//        mes2.setByte3((byte)0);
//        mes2.setByte4((byte)0);
//        mes2.setByte5((byte)0);
//        mes2.setByte6((byte)0);
//        mes2.setByte7((byte)0);
//
//        Mesage mes3 = new Mesage();
//        mes3.setDLC(8);
//        mes3.setId(32);
//        mes3.setByte0((byte)0);
//        mes3.setByte1((byte)0);
//        mes3.setByte2((byte)0);
//        mes3.setByte3((byte)0);
//        mes3.setByte4((byte)0);
//        mes3.setByte5((byte)0);
//        mes3.setByte6((byte)0);
//        mes3.setByte7((byte)0);
//
//        Mesage mes4 = new Mesage();
//        mes4.setDLC(8);
//        mes4.setId(32);
//        mes4.setByte0((byte)0);
//        mes4.setByte1((byte)0);
//        mes4.setByte2((byte)0);
//        mes4.setByte3((byte)0);
//        mes4.setByte4((byte)0);
//        mes4.setByte5((byte)0);
//        mes4.setByte6((byte)0);
//        mes4.setByte7((byte)0);
//
//        Mesage mes5 = new Mesage();
//        mes5.setDLC(8);
//        mes5.setId(32);
//        mes5.setByte0((byte)0);
//        mes5.setByte1((byte)0);
//        mes5.setByte2((byte)0);
//        mes5.setByte3((byte)0);
//        mes5.setByte4((byte)0);
//        mes5.setByte5((byte)0);
//        mes5.setByte6((byte)0);
//        mes5.setByte7((byte)0);
//
//        Mesage mes6 = new Mesage();
//        mes6.setDLC(8);
//        mes6.setId(32);
//        mes6.setByte0((byte)0);
//        mes6.setByte1((byte)0);
//        mes6.setByte2((byte)0);
//        mes6.setByte3((byte)0);
//        mes6.setByte4((byte)0);
//        mes6.setByte5((byte)0);
//        mes6.setByte6((byte)0);
//        mes6.setByte7((byte)0);
//
//        Mesage mes7 = new Mesage();
//        mes7.setDLC(8);
//        mes7.setId(32);
//        mes7.setByte0((byte)0);
//        mes7.setByte1((byte)0);
//        mes7.setByte2((byte)0);
//        mes7.setByte3((byte)0);
//        mes7.setByte4((byte)0);
//        mes7.setByte5((byte)0);
//        mes7.setByte6((byte)0);
//        mes7.setByte7((byte)0);
//
//        Mesage mes8 = new Mesage();
//        mes8.setDLC(8);
//        mes8.setId(32);
//        mes8.setByte0((byte)0);
//        mes8.setByte1((byte)0);
//        mes8.setByte2((byte)0);
//        mes8.setByte3((byte)0);
//        mes8.setByte4((byte)0);
//        mes8.setByte5((byte)0);
//        mes8.setByte6((byte)0);
//        mes8.setByte7((byte)0);
//
//        Mesage mes9 = new Mesage();
//        mes9.setDLC(8);
//        mes9.setId(32);
//        mes9.setByte0((byte)0);
//        mes9.setByte1((byte)0);
//        mes9.setByte2((byte)0);
//        mes9.setByte3((byte)0);
//        mes9.setByte4((byte)0);
//        mes9.setByte5((byte)0);
//        mes9.setByte6((byte)0);
//        mes9.setByte7((byte)0);
//
//        Mesage mes10 = new Mesage();
//        mes10.setDLC(8);
//        mes10.setId(32);
//        mes10.setByte0((byte)0);
//        mes10.setByte1((byte)0);
//        mes10.setByte2((byte)0);
//        mes10.setByte3((byte)0);
//        mes10.setByte4((byte)0);
//        mes10.setByte5((byte)0);
//        mes10.setByte6((byte)0);
//        mes10.setByte7((byte)0);
//
//        Mesage mes11 = new Mesage();
//        mes11.setDLC(8);
//        mes11.setId(32);
//        mes11.setByte0((byte)0);
//        mes11.setByte1((byte)0);
//        mes11.setByte2((byte)0);
//        mes11.setByte3((byte)0);
//        mes11.setByte4((byte)0);
//        mes11.setByte5((byte)0);
//        mes11.setByte6((byte)0);
//        mes11.setByte7((byte)0);
//
//        Mesage mes12 = new Mesage();
//        mes12.setDLC(8);
//        mes12.setId(32);
//        mes12.setByte0((byte)0);
//        mes12.setByte1((byte)0);
//        mes12.setByte2((byte)0);
//        mes12.setByte3((byte)0);
//        mes12.setByte4((byte)0);
//        mes12.setByte5((byte)0);
//        mes12.setByte6((byte)0);
//        mes12.setByte7((byte)0);
//
//        Mesage mes13 = new Mesage();
//        mes13.setDLC(8);
//        mes13.setId(32);
//        mes13.setByte0((byte)0);
//        mes13.setByte1((byte)0);
//        mes13.setByte2((byte)0);
//        mes13.setByte3((byte)0);
//        mes13.setByte4((byte)0);
//        mes13.setByte5((byte)0);
//        mes13.setByte6((byte)0);
//        mes13.setByte7((byte)0);
//
//
//        Mesage mes14 = new Mesage();
//        mes14.setDLC(8);
//        mes14.setId(32);
//        mes14.setByte0((byte)0);
//        mes14.setByte1((byte)0);
//        mes14.setByte2((byte)0);
//        mes14.setByte3((byte)0);
//        mes14.setByte4((byte)0);
//        mes14.setByte5((byte)0);
//        mes14.setByte6((byte)0);
//        mes14.setByte7((byte)0);
//
//        Mesage mes15 = new Mesage();
//        mes15.setDLC(8);
//        mes15.setId(139);
//        mes15.setByte0((byte)0);
//        mes15.setByte1((byte)0);
//        mes15.setByte2((byte)0);
//        mes15.setByte3((byte)0);
//        mes15.setByte4((byte)0);
//        mes15.setByte5((byte)0);
//        mes15.setByte6((byte)0);
//        mes15.setByte7((byte)0);