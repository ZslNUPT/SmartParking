package com.njupt.sniper.smartparking.common;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.njupt.sniper.smartparking.R;
import com.njupt.sniper.smartparking.model.ParkingLocationEntity;


/**
 * Created by Zsl on 2016/3/29.
 */
public class ParkingDialogFragment extends DialogFragment {

    private TextView title;
    private TextView nub;
    private TextView distance;
    private TextView address;
    private TextView guide;

    private GuideListener guideListener;

    private ParkingLocationEntity parkingLocationEntity;
    private LatLng myLocation;

    public void setGuideListener(GuideListener guideListener) {
        this.guideListener = guideListener;
    }

    public static ParkingDialogFragment newInstance(ParkingLocationEntity parkingLocationEntity, LatLng myLocation) {
        ParkingDialogFragment callDialogFragment = new ParkingDialogFragment();
        callDialogFragment.parkingLocationEntity = parkingLocationEntity;
        callDialogFragment.myLocation = myLocation;
        return callDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.park_dialog_layout, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        title = (TextView) view.findViewById(R.id.title);
        nub = (TextView) view.findViewById(R.id.nub);
        distance = (TextView) view.findViewById(R.id.distance);
        address = (TextView) view.findViewById(R.id.address);
        guide = (TextView) view.findViewById(R.id.btn_guide);

        title.setText("停车场" + (parkingLocationEntity.getId() + 1));
        nub.setText("剩余停车位" + parkingLocationEntity.getNub());

        final LatLng parkingLatLng = new LatLng(parkingLocationEntity.getxLocation(), parkingLocationEntity.getyLocation());


        distance.setText("距离：" +(int) DistanceUtil.getDistance(myLocation, parkingLatLng) + "米");
        //address.setText();

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideListener.guide(parkingLatLng);
                dismiss();
            }
        });

    }

    public interface GuideListener {
        void guide(LatLng latLng);
    }
}
