package com.example.proyectoua;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.location.LocationListener;

public class LlamadaFragment extends Fragment {
    private static final String API_KEY = "AIzaSyAS9DdX7eYFG6Zoxbb2qucWdkgwR-Swoss";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public String ubicacion;
    private String[][] dataArray;

    TextView _id,_distancia,_nombre,_comisaria,_telefono;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Button distanceButton;

    String toastMessage;

    public LlamadaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llamada, container, false);
        // Realizar la consulta a la API al hacer clic en un botón, o en cualquier otro evento que desees
        // Llama a la función para hacer la consulta a la API
        _nombre=view.findViewById(R.id.nombre);
        _distancia=view.findViewById(R.id.distancia);
        _comisaria=view.findViewById(R.id.comisaria);
        _telefono=view.findViewById(R.id.telefono);
        dataArray = new String[0][];
        getLocation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                processJSON(requireContext());
            }
        }, 5000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recorrerArray(encontrarMenorDistanciaYMostrarToast(dataArray));
                ;
            }
        }, 10000);

        return view;
    }
    public String getLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String coordinates = location.getLatitude() + "," + location.getLongitude();
                ubicacion=coordinates;
                stopLocationUpdates();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
        }

        return ubicacion; // Devuelve null como valor predeterminado, puedes cambiarlo para devolver algo más útil
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }


    private void requestDistance(String origen , String destino, String id, String nombre ,String comisaria , String numero) {
        OkHttpClient client = new OkHttpClient();

        String origin = origen;
        String destination = destino;
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json" +
                "?origins=" + origin +
                "&destinations=" + destination +
                "&key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    int distance = parseDistanceFromResponse(responseData);
                    showDistance(distance,id,nombre,comisaria,numero);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Error en la respuesta: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private int parseDistanceFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray rowsArray = jsonObject.getJSONArray("rows");
            JSONObject rowObject = rowsArray.getJSONObject(0);
            JSONArray elementsArray = rowObject.getJSONArray("elements");
            JSONObject elementObject = elementsArray.getJSONObject(0);
            JSONObject distanceObject = elementObject.getJSONObject("distance");
            int distanceValue = distanceObject.getInt("value");
            return distanceValue / 1000; // Convertir la distancia de metros a kilómetros
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void showDistance(final int distance,String id,String nombre,String comisaria,String telefono) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[][] newArray = new String[dataArray.length + 1][5];
                System.arraycopy(dataArray, 0, newArray, 0, dataArray.length);
                newArray[dataArray.length] = new String[]{id, String.valueOf(distance), nombre, comisaria, telefono};
                dataArray = newArray;
            }
        });
    }

    public void processJSON(Context context) {
        try {
            // Lee el archivo JSON desde la carpeta res/raw
            InputStream inputStream = getResources().openRawResource(R.raw.comisarias);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Convierte el archivo JSON en una cadena
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Convierte la cadena JSON en un objeto JSONObject
            JSONObject jsonObject = new JSONObject(json);

            // Obtén el array "comisaria" del objeto JSON
            JSONArray jsonArray = jsonObject.getJSONArray("Comisarias");

            // Recorre el array y muestra los campos en un Toast
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject comisariaObject = jsonArray.getJSONObject(i);
                String id = comisariaObject.getString("id");
                String gps = comisariaObject.getString("gps");
                String comisaria = comisariaObject.getString("comisaria");
                String telefono = comisariaObject.getString("telefono");
                String nombre= comisariaObject.getString("nombre");
                requestDistance(ubicacion,gps,id,nombre,comisaria,telefono);
                toastMessage = "ID: " + id + "\nGPS: " + gps + "\nComisaria: " + comisaria+jsonArray.length();

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String[] encontrarMenorDistanciaYMostrarToast(String[][] array) {
        int menorDistancia = Integer.MAX_VALUE;
        String[] arrayReturn = new String[0];
        String[] datosMenorDistancia = null;

        for (int i = 0; i < array.length; i++) {
            String distanceStr = array[i][1];
            int distancia = Integer.parseInt(distanceStr);

            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                datosMenorDistancia = array[i];
            }
        }

        if (datosMenorDistancia != null) {
            String id = datosMenorDistancia[0];
            String distance = datosMenorDistancia[1];
            String nombre = datosMenorDistancia[2];
            String comisaria = datosMenorDistancia[3];
            String telefono = datosMenorDistancia[4];



            arrayReturn= new String[]{id, distance, nombre, comisaria, telefono};
        } else {

        }
        return arrayReturn;
    }
    public void recorrerArray(String[] array) {
        for (int i = 0; i < array.length; i++) {

            // Obtener los datos individuales del array
            String id = array[0];
            String distancia = array[1];
            String nombre = array[2];
            String comisaria = array[3];
            String telefono = array[4];
            _distancia.setText("LA DISTANCIA ES: "+distancia+"km");
            _nombre.setText("DISTRITO: "+nombre);
            _comisaria.setText("COMISARIA: "+comisaria);
            _telefono.setText("N° TELEFONICO: "+telefono);
        }
    }
}
