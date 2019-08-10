package com.example.insa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private String TAG;
    public boolean mIsShowPanel = false;
    boolean first =true;
    DatabaseReference mDatabase;
    FusedLocationSource mLocationSource;
    NaverMap mNaverMap;
    UiSettings mNaverMapUiSettings;

    LatLng myLatLng;


    @BindView(R.id.user_iv_bottom_menu_list)
    ImageView menuList;
    @BindView(R.id.user_iv_bottom_menu_user)
    ImageView menuUser;

    @OnClick({
            R.id.user_iv_bottom_menu_list
            , R.id.user_iv_bottom_menu_user})
    public void OnClickMenu(View v) {

        if (v.equals(menuList)) {

        } else if (v.equals(menuUser)) {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);

        }
    }

    @BindView(R.id.tv_panel_name)
    TextView name;


    @BindView(R.id.rv_panel_star)
    RatingBar star;
    @BindView(R.id.tv_panel_pay)
    TextView pay;


    @BindView(R.id.main_fl_map)
    FrameLayout map;


    @BindView(R.id.main_fl_panel)
    FrameLayout flPanel;

    @OnClick({R.id.main_fl_panel ,R.id.main_fl_map})
    public void onclick(View v){

        if(v.equals(flPanel)){

            Intent intent = new Intent(MainActivity.this,ItemActivity.class);
            intent.putExtra("KEY",TAG);
            startActivity(intent);




        }

    }

    @BindView(R.id.main_iv_panel)
    ImageView ivPanel;


    private HashMap<String, MapData> mapdata = new HashMap<>();
    private HashMap<String, MapData> Data = new HashMap<>();
    private ArrayList<Marker> markerArrayList = new ArrayList<>();




    private Overlay.OnClickListener markerClickListener = new Overlay.OnClickListener() {
        @Override
        public boolean onClick(@NonNull Overlay overlay) {
            TAG = overlay.getTag().toString();

            showPanel(overlay.getTag().toString());

            return true; //true로 반환해야 map클릭으로 안감. false반환시 맵까지 클락하는걸로 전파
        }
    };

    private void showPanel(String id) {
        mIsShowPanel = true;
        setPanel(id);
        flPanel.setVisibility(View.VISIBLE);
        float distance = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 0
                , getResources().getDisplayMetrics());
        flPanel.animate().translationY(distance).setDuration(700).start();

    }

    private void setPanel(String id) {

        MapData data = mapdata.get(id);

        Log.e("LatLng", myLatLng.latitude+""+myLatLng.longitude);



        name.setText(data.getName());

        pay.setText(data.getSimple());


        Glide.with(this).load(data.getUrl()).into(ivPanel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        mLocationSource = new FusedLocationSource(this, 1000);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if(mapFragment == null)
        {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull NaverMap naverMap) {
                mNaverMap = naverMap;
                mNaverMap.setLocationSource(mLocationSource);
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

                setMapUiSetting();
                setMapLocationChanged();
                setCameraChanged();

                setData();
            }
        });






    }


    private void setMapUiSetting() {


        mNaverMapUiSettings = mNaverMap.getUiSettings();
        mNaverMapUiSettings.setLocationButtonEnabled(true);
        mNaverMapUiSettings.setZoomControlEnabled(false);
        mNaverMapUiSettings.setScaleBarEnabled(false);
    }

    private void setMapLocationChanged() {
        mNaverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                if(first){
                    first = false;

                    CameraPosition cameraPosition = new CameraPosition(myLatLng, 1);
                    mNaverMap.setCameraPosition(cameraPosition);

                   /* CameraUpdate cameraUpdate = CameraUpdate.scrollTo(myLatLng);

                    mNaverMap.moveCamera(cameraUpdate); */
                }

            }
        });
    }




    private void setCameraChanged() {
        mNaverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int i, boolean b) {

            }
        });
    }


    private void setData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("insa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    MapData data = ds.getValue(MapData.class);
                    data.setKey(ds.getKey());
                    mapdata.put(ds.getKey(),ds.getValue(MapData.class));

                }
                setMarker();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setMarker() {
        OverlayImage onLocationImage = OverlayImage.fromResource(R.drawable.ic_location_on_red_300_24dp);
        OverlayImage offLocationImage = OverlayImage.fromResource(R.drawable.ic_location_on_grey_400_24dp);

        for(Marker m: markerArrayList){
            m.setMap(null);

        }
        markerArrayList.clear();

        for(Map.Entry<String,MapData> entry : mapdata.entrySet()){
            MapData data = entry.getValue();
            try{

                Marker marker = new Marker();
                //위치정보 LatLng 타입으로
                LatLng position = data.getLatLng();



                marker.setIcon(onLocationImage);

                marker.setPosition(position);
                marker.setMap(mNaverMap);
                marker.setTag(entry.getKey());
                marker.setOnClickListener(markerClickListener);



                markerArrayList.add(marker);

            }catch (Exception e){

            }

        }
    }

}
