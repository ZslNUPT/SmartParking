package com.njupt.sniper.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.njupt.sniper.smartparking.common.AdvertisementPager;
import com.njupt.sniper.smartparking.common.SimpleWebViewActivity;
import com.njupt.sniper.smartparking.model.Banner;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MainFragment extends Fragment {
    private TextView toMap;

    private AdvertisementPager advertisementPager;

    private Banner[] banners=new Banner[3];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toMap= (TextView) view.findViewById(R.id.tv_to_map);
        advertisementPager= (AdvertisementPager) view.findViewById(R.id.navigation_advertisement);

        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MapActivity.class));
            }
        });

        initAdvertisement();
    }

    private void initAdvertisement() {
        String img1="http://noodles-image.b0.upaiyun.com/banner/56d6ba6892310.jpg";
        String url1="https://www.baidu.com/";
        banners[0]=new Banner(img1,url1,"广告1");
        banners[1]=new Banner(img1,url1,"广告2");
        banners[2]=new Banner(img1,url1,"广告3");

        advertisementPager.setImages(banners);
        advertisementPager.setScrollAble(true);
        advertisementPager.setDelayedTime(8000);
        advertisementPager.setOnPagerItemClickListener(new AdvertisementPager.OnPagerItemClickListener() {
            @Override
            public void onPagerItemClick(Object itemData) {
                onBannerClick((Banner) itemData);
            }

        });
    }

    public void onBannerClick(Banner banner) {
        String url = banner.getUrl();
        if ("".equals(url)|| url == null)
            return;

        SimpleWebViewActivity.startSimpleWebViewActivity(getActivity(),banner.getUrl(),banner.getTitle());

    }

}
