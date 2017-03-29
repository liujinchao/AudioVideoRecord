package com.liujc.videoplayerlib.media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.liujc.videoplayerlib.R;
import com.liujc.videoplayerlib.media.adapter.BaseListAdapter;
import com.liujc.videoplayerlib.media.bean.MediaQualityInfo;

import java.util.List;


public class MediaQualityAdapter extends BaseListAdapter<MediaQualityInfo> {


    public MediaQualityAdapter(Context context, List<MediaQualityInfo> datas) {
        super(context, datas);
    }

    public MediaQualityAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_media_quality, parent, false);
        }
        TextView qualityDesc = (TextView) view.findViewById(R.id.tv_media_quality);
        qualityDesc.setText(mDatas.get(i).getDesc());
        qualityDesc.setSelected(mDatas.get(i).isSelect());
        return view;
    }

    private void _cleanSelected() {
        for (MediaQualityInfo info : mDatas) {
            if (info.isSelect()) {
                info.setSelect(false);
            }
        }
    }

    public void setMediaQuality(int quality) {
        for (MediaQualityInfo info : mDatas) {
            if (info.getIndex() == quality) {
                if (!info.isSelect()) {
                    _cleanSelected();
                    info.setSelect(true);
                    notifyDataSetChanged();
                }
                return;
            }
        }
    }
}
