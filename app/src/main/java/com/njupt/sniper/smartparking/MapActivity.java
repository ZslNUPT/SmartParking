package com.njupt.sniper.smartparking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.njupt.sniper.smartparking.common.ParkingDialogFragment;
import com.njupt.sniper.smartparking.model.ParkingLocationEntity;

import java.util.ArrayList;
import java.util.List;

import mapapi.overlayutil.DrivingRouteOverlay;
import mapapi.overlayutil.OverlayManager;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MapActivity extends AppCompatActivity implements View.OnClickListener{
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    public LocationClient mLocationClient = null;

    private TextView tv_location_data;
    private Button btn_request_location;

    private List<ParkingLocationEntity> parkingLocationEntities = new ArrayList<>();


    public BDLocationListener myListener = new MyLocationListener();

    private LatLng mLatLng;

    RoutePlanSearch mSearch = null;


    // 浏览路线节点相关
    Button mBtnPre = null; // 上一个节点
    Button mBtnNext = null; // 下一个节点
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    private TextView popupText = null; // 泡泡view

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();

        tv_location_data = (TextView) findViewById(R.id.tv_location_data);
        btn_request_location = (Button) findViewById(R.id.btn_request_location);
        btn_request_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationClient.start();
                mLocationClient.requestLocation();// 发起定位请求
            }
        });

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBtnPre.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ParkingDialogFragment p=ParkingDialogFragment.newInstance(parkingLocationEntities.get(marker.getZIndex()));
                p.setGuideListener(new ParkingDialogFragment.GuideListener() {

                    @Override
                    public void guide(LatLng latLng) {
                        PlanNode stNode=PlanNode.withLocation(mLatLng);
                        PlanNode enNode=PlanNode.withLocation(latLng);
                        mSearch.drivingSearch((new DrivingRoutePlanOption())
                                .from(stNode).to(enNode));
                    }
                });
                p.show(getSupportFragmentManager(),"ParkingDialogFragment");
                return false;
            }
        });

        initLocation();

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    // result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    nodeIndex = -1;
                    mBtnPre.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.VISIBLE);
                    route = result.getRouteLines().get(0);
                    DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                    routeOverlay = overlay;
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });

        mLocationClient.start();

    }

    @Override
    public void onClick(View v) {
        if (route == null || route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && v.getId() == R.id.pre) {
            return;
        }
        // 设置节点索引
        if (v.getId() == R.id.next) {
            if (nodeIndex < route.getAllStep().size() - 1) {
                nodeIndex++;
            } else {
                return;
            }
        } else if (v.getId() == R.id.pre) {
            if (nodeIndex > 0) {
                nodeIndex--;
            } else {
                return;
            }
        }
        // 获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        } else if (step instanceof BikingRouteLine.BikingStep) {
            nodeLocation = ((BikingRouteLine.BikingStep) step).getEntrance().getLocation();
            nodeTitle = ((BikingRouteLine.BikingStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        // 移动节点至中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(MapActivity.this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private void initParkingSites() {
        double x;
        double y;
        int nub;

        if (parkingLocationEntities.size() > 0)
            return;
        for (int i = 0; i < 10; i++) {
            x = mLatLng.latitude + Math.random() * 0.03 - 0.015;
            y = mLatLng.longitude + Math.random() * 0.03 - 0.015;
            nub = (int) (Math.random() * 30);
            parkingLocationEntities.add(new ParkingLocationEntity(i, x, y, nub));
        }

    }


    //初始化我的当前位置
    public void initMyLocation(BDLocation location) {
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());//设置纬度
        builder.longitude(location.getLongitude());//设置经度
        builder.accuracy(location.getRadius());//设置精度（半径）
        builder.direction(location.getDirection());//设置方向
        builder.speed(location.getSpeed());//设置速度
        MyLocationData locationData = builder.build();

        mBaiduMap.setMyLocationData(locationData);

        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        //----------添加一个标注覆盖物在当前位置--------------
//        //构建Marker图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);

//        OverlayOptions option = new MarkerOptions()
//                .position(latlng)
//                .title("我的位置")
//                .icon(mCurrentMarker);
//        mBaiduMap.addOverlay(option);

        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(24)
                .fontColor(0xFFFF00FF)
                .text("我的位置")
                .rotate(0)
                .position(mLatLng);
        mBaiduMap.addOverlay(textOption);

        //配置定位图层显示方式(显示模式（普通，罗盘，跟随）)
        // mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态
        //enableDirection - 是否允许显示方向信息
        //customMarker - 设置用户自定义定位图标，可以为 null
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, mCurrentMarker));

        initParkingSites();

        for (ParkingLocationEntity parkingLocationEntity : parkingLocationEntities) {
            LatLng latlngPark = new LatLng(parkingLocationEntity.getxLocation(), parkingLocationEntity.getyLocation());
            OverlayOptions option2 = new MarkerOptions()
                    .position(latlngPark)
                    .title("车库" + parkingLocationEntity.getId())
                    .zIndex(parkingLocationEntity.getId())
                    .icon(mCurrentMarker);
            mBaiduMap.addOverlay(option2);
        }


        //设置我的位置为地图的中心点
        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(mLatLng, 16);
        mBaiduMap.animateMapStatus(mapStatus);
        mLocationClient.stop();

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            System.out.println(sb);
            tv_location_data.setText(sb);
            initMyLocation(location);
        }
    }
}
