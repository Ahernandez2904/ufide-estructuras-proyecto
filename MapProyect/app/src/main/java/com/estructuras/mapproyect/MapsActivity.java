package com.estructuras.mapproyect;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    TextView mensaje1;
    TextView mensaje2;
    private GoogleMap mMap;
    Button start;
    Button stop;

    // Esta es la Funcion main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsActivity mp = new MapsActivity(); //ejecuta el constructor para iniciar raíz en null.

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mensaje1 = (TextView) findViewById(R.id.mensaje_id);
        mensaje2 = (TextView) findViewById(R.id.mensaje_id2);
        start  = (Button) findViewById(R.id.btn_start);
        stop  = (Button) findViewById(R.id.btn_stop);
        stop.setVisibility(View.GONE);
        ///  Esta condicion pregunta si la app tiene permiso o no de obtener los datos del GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }


        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationStart();
            }
        });


    }

    // Practicamente inicializa todos los recursos para obtener datos de GPS
    private void locationStart() {

        start.setText("Espere...");
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        mensaje1.setText("Localizando");
        mensaje2.setText("");
    }

    //  Esta funcion nos da el nombre de donde estamos la direccion de la calle a partir de la latitud y la longitud
    @SuppressLint("SetTextI18n")
    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    mensaje2.setText("Mi direccion es: \n"
                            + DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //  Esta funcion es la del Mapa
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(18);
        LatLng myPos = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(myPos));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
    }

    //Constructor
    public MapsActivity() {
        raiz = null;
    }

    //Timer - cronómetro
    public int cronometro (int seg) {
        System.out.println(seg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ListaGenericaDoblementeEnlazada.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (seg >= 0) {

            return cronometro(seg + 1);
        }
        return 0;
    }

    //ListaGenericaDoblementeEnlazada
    class Nodo {
        String lat, lon;
        Nodo ant, sig;
    }

    int posNodo = 1;

    private Nodo raiz;

    public ListaGenericaDoblementeEnlazada() {
        raiz = null;
    }

    void insertar(int pos, String x, String y) {
        if (pos <= cantidad() + 1) {
            Nodo nuevo = new Nodo();
            nuevo.lat = x;
            nuevo.lon = y;
            if (pos == 1) {
                nuevo.sig = raiz;
                if (raiz != null) {
                    raiz.ant = nuevo;
                }
                raiz = nuevo;
            } else if (pos == cantidad() + 1) {
                Nodo reco = raiz;
                while (reco.sig != null) {
                    reco = reco.sig;
                }
                reco.sig = nuevo;
                nuevo.ant = reco;
                nuevo.sig = null;
            } else {
                Nodo reco = raiz;
                for (int f = 1; f <= pos - 2; f++) {
                    reco = reco.sig;
                }
                Nodo siguiente = reco.sig;
                reco.sig = nuevo;
                nuevo.ant = reco;
                nuevo.sig = siguiente;
                siguiente.ant = nuevo;
            }
        }
    }

    public int extraer(int pos) {
        if (pos <= cantidad()) {
            String latitude, longitude;
            if (pos == 1) {
                latitude = raiz.lat;
                longitude = raiz.lon;
                raiz = raiz.sig;
                if (raiz != null) {
                    raiz.ant = null;
                }
            } else {
                Nodo reco;
                reco = raiz;
                for (int f = 1; f <= pos - 2; f++) {
                    reco = reco.sig;
                }
                Nodo prox = reco.sig;
                reco.sig = prox.sig;
                Nodo siguiente = prox.sig;
                if (siguiente != null) {
                    siguiente.ant = reco;
                }
                latitude = prox.lat;
                longitude = prox.lon;
            }
            return latitude + " " + longitude;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public void borrar(int pos) {
        if (pos <= cantidad()) {
            if (pos == 1) {
                raiz = raiz.sig;
                if (raiz != null) {
                    raiz.ant = null;
                }
            } else {
                Nodo reco;
                reco = raiz;
                for (int f = 1; f <= pos - 2; f++) {
                    reco = reco.sig;
                }
                Nodo prox = reco.sig;
                prox = prox.sig;
                reco.sig = prox;
                if (prox != null) {
                    prox.ant = reco;
                }
            }
        }
    }

    public int cantidad() {
        int cant = 0;
        Nodo reco = raiz;
        while (reco != null) {
            reco = reco.sig;
            cant++;
        }
        return cant;
    }

    public boolean vacia() {
        if (raiz == null) {
            return true;
        } else {
            return false;
        }
    }

    public void imprimir() {
        Nodo reco = raiz;
        while (reco != null) {
            System.out.print(reco.lat + " " + reco.lon);
            reco = reco.sig;
        }
        System.out.println();
    }

    public int PosicionElemento(String x, String y) {
        Nodo reco = raiz;
        int cont = 1;
        while (reco != null) {
            if (x.equals(reco.lan) && y.equals(reco.lon)) {
                return cont;
            }
            reco = reco.sig;
            cont = cont + 1;
        }
        return 0;
    }

    public int GetPos(String x, String y) {
        int pos = 0;
        Nodo reco = raiz;
        while (reco != null) {
            pos++;
            if (reco.lat == x && reco.lon == y) {
                return pos;
            }
            reco = reco.sig;
        }

        return 0;
    }

    public class Localizacion implements LocationListener {
        MapsActivity mainActivity;
        public MapsActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MapsActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la deteccion de un cambio de ubicacion
        @Override
        public void onLocationChanged(Location loc) {

            loc.getLatitude();
            loc.getLongitude();

            //agregar nodo
            insertar(loc.getLatitude(), loc.getLongitude());

            LatLng myPos = new LatLng( loc.getLatitude(), loc.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myPos));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Lng = " + loc.getLongitude();
            mensaje1.setText(Text);
            start.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            this.mainActivity.setLocation(loc);
        }

        // Este metodo se ejecuta cuando el GPS es desactivado
        @Override
        public void onProviderDisabled(String provider) {
            mensaje1.setText("GPS Desactivado");
        }

        // Este metodo se ejecuta cuando el GPS es activado
        @Override
        public void onProviderEnabled(String provider) {
            mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

    }

}
