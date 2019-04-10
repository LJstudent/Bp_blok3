package com.example.test_application;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;



public class ChartFragment extends Fragment {
    private EditText txtdatum;
    private Button btnBevestigendatum;
    private DatePickerDialog picker;
    GraphView graph;
    JSONObject jo = new JSONObject();
    ArrayList<TabelView> list = new ArrayList<>();
    Context context;




    // Fragment van navigation bar in dit fragment wordt er een grafiek getoond aan de hand van de gekozen data
    // op de calendar.
    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
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

        // JSON alles ophalen voor datum check
        JSONonReady();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        // get context is nodig anders werkt er iets niet
        context = container.getContext();

        txtdatum = view.findViewById(R.id.datepicker);
        txtdatum.setInputType(InputType.TYPE_NULL);
        btnBevestigendatum = view.findViewById(R.id.btnBevestigenDate);

        // get graph
        graph = view.findViewById(R.id.grafiek);

        // Instellingen voor graph zo zie je een tweede grafiek
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(1000);
        //series2.setDrawDataPoints(true);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(60);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);
        graph.getGridLabelRenderer().setNumHorizontalLabels(24);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);


        // Calendar dialog
        txtdatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear ++;
                                String monthString = String.valueOf(monthOfYear);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                String dayString = String.valueOf(dayOfMonth);
                                if (dayString.length() == 1) {
                                    dayString = "0" + dayString;
                                }


                                txtdatum.setText(dayString + "-" + monthString + "-" + year );
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        btnBevestigendatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetData();
                // JSON alles ophalen voor datum check
                JSONonReady();

                String datum = txtdatum.getText().toString();
                System.out.println(datum);


                    String usernameApp = getArguments().getString("user");
                    System.out.println(usernameApp);

                    if(checkDatum(usernameApp,datum)) {

                        list.clear();
                        System.out.println(list.size());
                        JSON("http://192.168.178.116:9090/ords/hr/demo/tableview/"+datum);

                        System.out.println(list.size());

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(usernameApp, datum));
                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(generateDataInstraling(usernameApp, datum));
                        graph.addSeries(series);
                        graph.getSecondScale().addSeries(series2);
                        series2.setColor(Color.RED);
                        series.setTitle("Temperatuur");
                        series2.setTitle("Instraling");
                        graph.getLegendRenderer().setVisible(true);
                        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

                    } else {
                        DialogFragment dialog = new DatumMelding();
                        dialog.show(getFragmentManager(), "MyDialogFragmentTag");
                    }

            }
        });



        return view;
    }
    // reset data dit is voor een foutmelding
    private void resetData() {
        graph.removeAllSeries();
        graph.getSecondScale().removeAllSeries();
    }

    // genereren van grafiek 1
    private DataPoint[] generateData(String username, String datum) {
        int count = list.size();
        System.out.println(list.size());
        DataPoint[] values = new DataPoint[count];
        int i = 0;
        while (i <count) {
            for (TabelView tabel : list) {
                if(tabel.getUser().equals(username) && tabel.getDatum().equals(datum)) {
                        String tijd = tabel.getTijd();
                        char[] timeChars = tijd.toCharArray();
                        timeChars[2] = '.';
                        tijd = String.valueOf(timeChars);
                        double x = Double.parseDouble(tijd);
                        double y = Double.parseDouble(tabel.getTemp());
                        DataPoint v = new DataPoint(x, y);
                        values[i] = v;
                        ++i;
                }
            }
        }
        return values;
    }
    //genereren van grafiek 2
    private DataPoint[] generateDataInstraling(String username, String datum) {
        int count = list.size();
        DataPoint[] values = new DataPoint[count];
        int i = 0;
        while (i <count) {
            for (TabelView tabel : list) {
                if(tabel.getUser().equals(username) && tabel.getDatum().equals(datum)) {
                    String tijd = tabel.getTijd();
                    char[] timeChars = tijd.toCharArray();
                    timeChars[2] = '.';
                    tijd = String.valueOf(timeChars);
                    double x = Double.parseDouble(tijd);
                    double y = Double.parseDouble(tabel.getInstraling());
                    DataPoint v = new DataPoint(x, y);
                    values[i] = v;
                    ++i;
                }
            }
        }
        return values;
    }

    // JSON alles ophalen voor datum check
    public void JSONonReady() {
        // JSON string data dit komt omdat die later verandert als er meer dan 25 records zijn zoveel gaat er op een pagina
        JSON("http://192.168.178.116:9090/ords/hr/demo/tableview");
        System.out.println(list.size());
    }
    // JSON
    public void JSON(String JSONURL) {
        System.out.println(JSONURL);

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
    // Check of er meer pagina's zijn
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
    // JSON voor het halen van de link
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
    // Check datum in tabel
    public boolean checkDatum(String username, String datum) {
        TabelView tabel = zoekDatum( username, datum);
        if( tabel != null )
            return true;
        else
            return false;
    }

    public TabelView zoekDatum( String username, String datum ) {
        for( TabelView tabel : list ) {
            if( username.equals( tabel.getUser())&& datum.equals( tabel.getDatum()))
                return tabel;
        }
        return null;
    }


}


