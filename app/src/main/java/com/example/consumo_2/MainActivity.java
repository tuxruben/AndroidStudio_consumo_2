package com.example.consumo_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.sax.RootElement;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button getApiBtn, postApiBtn;
    private TextView resultTextView;
    RequestQueue requestQueue;
    private static final String TAG = MainActivity.class.getSimpleName();
    JSONObject object = new JSONObject();
    private ListView listview;
    private ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    EditText descripcion;
    EditText precio;
    int valor=0;
    String desc ="";
    Tabla tabla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        getApiBtn = (Button) findViewById(R.id.getApiBtn);
        postApiBtn = (Button)findViewById(R.id.postApiBtn);
        descripcion = (EditText) findViewById(R.id.editText5);
        precio = (EditText) findViewById(R.id.editText6);

        // RequestQueue For Handle Network Request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Click Listner for GET JSONObject
        getApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        //Click Listner for POST JSONObject
        postApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });
    }

    // Post Request For JSONObject
    public void postData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
object = new JSONObject();
        try {
            desc= descripcion.getText().toString();
           valor= Integer.parseInt(precio.getText().toString());
            //input your API parameters
            object.put("descripcion",""+desc+"");
            object.put("precio",""+valor+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = getResources().getString(R.string.urlpost);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        resultTextView.setText("String Response : "+ response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTextView.setText("Error getting response");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    // Get Request For JSONObject
    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            String url = getResources().getString(R.string.urlget);
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JsonParser parser = new JsonParser();
                    Gson gson = new GsonBuilder().create();
                    JsonArray jarray = (JsonArray) parser.parse(response.toString()).getAsJsonObject().get("body");
                    body[] cuerpo = gson.fromJson(jarray, body[].class);
                    tabla.agregarCabecera(R.array.cabecera_tabla);
                    for (body Body : cuerpo) {
                        ArrayList<String> elementos = new ArrayList<String>();
                        elementos.add(Body.getIdI());
                        elementos.add(Body.getIDescripcion());
                        elementos.add(Body.getPrecio());
                        tabla.agregarFilaTabla(elementos);
                    }// Primitives elements of object
                    Toast.makeText(getApplicationContext(), "I am OK !" + response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
