package com.example.uidk9044.mobilecontrol.Activitys;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.uidk9044.mobilecontrol.Controler.Connect;
import com.example.uidk9044.mobilecontrol.Controler.Functionality;
import com.example.uidk9044.mobilecontrol.Controler.JSON;
import com.example.uidk9044.mobilecontrol.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DynamicMenuActivity extends AppCompatActivity {
    public JSONArray jsonForMenu = new JSONArray();
    public JSONObject jsonForRow = new JSONObject();
    public JSONObject jsonForComponets = new JSONObject();
    public Map<String, Integer> map = new HashMap<String, Integer>();
    public Connect conn = new Connect();
    public TextView text;
    public TextView connect_text_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_menu);

        conn.connectWebSocket();
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conneced")){
            connect_text_id.setText("Connected");
        }
        else {
            connectionError();
        }
//        Functionality.runJSon();

        //will set the drawable resuces to be used later in addRow
        setImages();

        //add a static first row and than will add a empty space
        addStaticFirstRow();
        addEmptyRow();
        jsonForMenu = JSON.jsonForDynamicMenu();
        for(int n = 0; n < jsonForMenu.length(); n++){
            try {
                jsonForRow =  jsonForMenu.getJSONObject(n);
                jsonForComponets = jsonForRow.getJSONObject("components");
                addRowCompImageButtonAnsw(jsonForComponets.getString("componenta"),
                        jsonForComponets.getString("imageName"),
                        jsonForComponets.getString("buttonName"));
            }
            catch (JSONException e) {
                Log.e("MYAPP", "unexpected JSON exception", e);
            }
        }
    }

    public void addRowCompImageButtonAnsw(String componenta, String imageName, String buttonName){
        /* Find Tablelayout defined in main.xml */
        TableLayout tl = (TableLayout) findViewById(R.id.table_layout_id);
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        TextView textView = new TextView(this);
        textView.setText(componenta);
        int i = 2;
        //textView.setId();
        tr.addView(textView);


        ImageView imageView = new ImageView(this);
        imageView.setImageResource(map.get(imageName));
        tr.addView(imageView);

        Button b = new Button(this);
        b.setText(buttonName);
//        b.callOnClick()
//        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
        tr.addView(b);
        /* Add row to TableLayout. */

        TextView statusText = new TextView(this);
        statusText.setText("aici apare statusul");
        statusText.setGravity(Gravity.CENTER);
        tr.addView(statusText);

        //tr.setBackgroundResource(R.drawable.sf_gradient_03);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void addStaticFirstRow(){
        TableLayout tl = (TableLayout) findViewById(R.id.table_layout_id);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(this);
        textView.setText("Componenta");
        tr.addView(textView);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.conti_logo);


//        //imageView.layout(0,40,40,0);
//        imageView.getLayoutParams().height = 40;
//        imageView.getLayoutParams().width = 40;
//        tr.addView(imageView);

        android.view.ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.WRAP_CONTENT,45));
        layoutParams.width= 45;
        layoutParams.height = 45;
        imageView.setLayoutParams(layoutParams);

//        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.conti_logo);
//        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 40, 40, true);
//        imageView.setImageBitmap(bMap);

//        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
//        Bitmap bMapScaled = Bitmap.createScaledBitmap(0, 40, 40, true);
//        iv.setImageBitmap(bMapScaled);

        tr.addView(imageView);

        Button b = new Button(this);
        b.setText("ButtonName");
        tr.addView(b);
        TextView statusText = new TextView(this);
        statusText.setText("aici apare statusul");
        statusText.setGravity(Gravity.CENTER);
        tr.addView(statusText);

        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


    }

    public void addEmptyRow(){
        TableLayout tl = (TableLayout) findViewById(R.id.table_layout_id);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView textView = new TextView(this);
        textView.setText(" ");
        tr.addView(textView);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void connectionError()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(DynamicMenuActivity.this,R.style.MyDialogTheme);
        builder.setMessage("You have to try to reconnect")
                .setTitle("Error in connection");
        builder.setPositiveButton("Reconnect", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reconnect();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void reconnect(){
        conn.connectWebSocket();
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conneced")){
            connect_text_id.setText("Connected");
        }
        else {
            connect_text_id.setText("Error in conection");
            AlertDialog.Builder builder = new AlertDialog.Builder(DynamicMenuActivity.this,R.style.MyDialogTheme);
            builder.setMessage("Problem at the server")
                    .setTitle("Error in reconnection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.static_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect_text_id:
                return true;
            case R.id.verify_conn_id:
                verifyConn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void verifyConn(){
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conneced")){
            connect_text_id.setText("Connected");
            //afiseaza conectat
        }
        else {
            connect_text_id.setText("Error in conection");
            connectionError();
        }
    }

    public void tryConnection(){
        //try to connect
        conn.connectWebSocket();
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conneced")){
            connect_text_id.setText("Connected");
            connect_text_id.setText("Conneced");
        }
        else {
            connect_text_id.setText("Error in conection");
        }
    }

    public void setImages(){
        map.put("baterie", R.drawable.baterie);
        map.put("frana", R.drawable.frana);
        map.put("radiator", R.drawable.radiator);
        map.put("roata", R.drawable.roata);
        map.put("transmisie", R.drawable.transmisie);
        map.put("turometru", R.drawable.turometru);
        map.put("ulei", R.drawable.ulei);
        map.put("volan", R.drawable.volan);
        map.put("conti_logo", R.drawable.conti_logo);
    }
}
