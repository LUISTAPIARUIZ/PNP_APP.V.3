package com.example.proyectoua;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class MapsFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            if ((ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            googleMap.setMyLocationEnabled(true);
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    // Obtener la latitud y longitud de la ubicación actual
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Crear un objeto LatLng con la ubicación actual
                    LatLng latLng = new LatLng(latitude, longitude);

                    // Mover la cámara del mapa a la ubicación actual con un nivel de zoom adecuado
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

}