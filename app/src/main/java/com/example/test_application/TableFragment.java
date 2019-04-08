package com.example.test_application;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class TableFragment extends Fragment {
    JSONObject jo = new JSONObject();
    ArrayList<TabelView> list = new ArrayList<>();
    TableLayout table;
    Context context;
    private Button btnUpdate;
    private EditText txtUpdateHw;
    private TextView datumTijd, melding;

    public TableFragment() {
        // Required empty public constructor
    }

    public static TableFragment newInstance(String param1, String param2) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String usernameApp=getArguments().getString("user");


        // get context
        context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        table = view.findViewById(R.id.tableLayout2);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        txtUpdateHw = view.findViewById(R.id.txtHuidigVermogen);
        datumTijd = view.findViewById(R.id.DatumTijd);
        melding = view.findViewById(R.id.meldingNummer);
        JSON("http://192.168.178.116:9090/ords/hr/tableview/");
        System.out.println(list.size());

        // optellen voor even oneven waardes zodat de tabel verschillende kleuren krijgen
        int i = 0;
        for (TabelView tabel : list ) {
            if(tabel.getUser().equals(usernameApp)) {
                i++;
                final TableRow row = new TableRow(context);


                if ((i % 2) == 0) {
                } else {
                    row.setBackgroundColor(Color.parseColor("#A9CCE3"));
                }


                final TextView tv1 = new TextView(context);
                String instraling = (tabel.getInstraling());
                tv1.setText(instraling);
                tv1.setGravity(Gravity.CENTER);

                final TextView tv2 = new TextView(context);
                String temp = (tabel.getTemp());
                tv2.setText(temp);
                tv2.setGravity(Gravity.CENTER);

                final TextView tv3 = new TextView(context);
                String datum = (tabel.getDatum());
                tv3.setText(datum);
                tv3.setGravity(Gravity.CENTER);


                final TextView tv4 = new TextView(context);
                String tijd = (tabel.getTijd());
                tv4.setText(tijd);
                tv4.setGravity(Gravity.CENTER);


                final TextView tv5 = new TextView(context);
                String huidVer = (tabel.getHw());
                tv5.setText(huidVer);
                tv5.setGravity(Gravity.CENTER);

                row.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        melding.setText(null);
                        String datum = tv3.getText().toString();
                        String tijd = tv4.getText().toString();
                        datumTijd.setText("Datum " + datum + " Tijd " + tijd);
                        txtUpdateHw.setText(tv5.getText().toString().trim());

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Double hw = Double.parseDouble(txtUpdateHw.getText().toString());
                                    String datum = tv3.getText().toString();
                                    String tijd = tv4.getText().toString();
                                    melding.setText("Update gelukt");
                                    melding.setTextColor(Color.GREEN);

                                } catch (NumberFormatException nfe) {
                                    melding.setText("Geen geldig getal ingevoerd");
                                    melding.setTextColor(Color.RED);


                                }

                            }
                        });


                    }
                });


                row.addView(tv1);
                row.addView(tv2);
                row.addView(tv3);
                row.addView(tv4);
                row.addView(tv5);
                table.addView(row);

            }
        }



        return view;
    }

    public void JSONPUSH() {

    }
    public void JSON(String JSONURL) {

        JSONArray ja = new JSONArray();

        try {
            JSONObject result = readUrl(JSONURL);
            JSONArray features = (JSONArray) result.get("items");


            for (Object o : features) {
                JSONObject jsonobject = (JSONObject) o;
                String user = checkValue(jsonobject, "username");
                String raspID = checkValue(jsonobject, "raspid");
                String datum = checkValue(jsonobject, "datum");
                String tijd = checkValue(jsonobject, "tijd");
                String temp = checkValue(jsonobject, "temp");
                String instraling = checkValue(jsonobject, "instraling");
                String huidigeVermogen = checkValue(jsonobject, "hw");


                TabelView tabel = new TabelView(user,raspID,datum,tijd,temp,instraling, huidigeVermogen);
                list.add(tabel);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        CheckMore(JSONURL);

    }

    private String checkValue(JSONObject jo, String attribute) {
        String waarde = "";
        try {
            if (jo.get(attribute).toString() != null) {
                waarde = jo.get(attribute).toString();
            }
        } catch (Exception e) {
        }
        return waarde;
    }

    private JSONObject readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        String data = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            data = br.readLine();
            connection.disconnect();
            jo = (JSONObject) JSONValue.parseWithException(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jo;
    }
    public void CheckMore(String sURL) {
        //url changes every time

        // Connect to the URL using java's native library
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();


            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
            String nummer = rootobj.get("hasMore").getAsString(); //just grab the zipcode


            if(nummer.equals("true")) {

                JSON2(sURL);

            }else {
                System.out.println("Geen extra pagina's");
            }





        } catch(Exception e) {
            System.err.println(e);
        }


    }
    public void JSON2(String JSONURL) {

        JSONArray ja = new JSONArray();

        try {
            JSONObject result = readUrl(JSONURL);
            JSONArray features = (JSONArray) result.get("links");


            for (Object o : features) {
                JSONObject jsonobject = (JSONObject) o;
                String rel = checkValue(jsonobject, "rel");
                String href = checkValue(jsonobject, "href");

                if(rel.equals("next")) {
                    System.out.println(href);
                    JSON(href);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}


