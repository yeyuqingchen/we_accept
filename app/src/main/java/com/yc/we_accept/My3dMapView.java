package com.yc.we_accept;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;

/**
 * Created by hmy
 */
public class My3dMapView extends MapView {
    private Context context;

    public My3dMapView(Context context) {
        super(context);
        init(context);
    }

    public My3dMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public My3dMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public My3dMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        //view加载完成时回调
        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewGroup child = (ViewGroup) getChildAt(0);//地图框架
                        child.getChildAt(3).setVisibility(View.GONE);//logo
//                        child.getChildAt(7).setVisibility(View.GONE);//缩放
                    }
                });
    }
}
