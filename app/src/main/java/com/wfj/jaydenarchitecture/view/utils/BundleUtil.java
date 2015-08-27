package com.wfj.jaydenarchitecture.view.utils;

import android.os.Bundle;

import com.wfj.jaydenarchitecture.model.bean.Artwork;
import com.wfj.jaydenarchitecture.model.bean.User;

/**
 * Create by：ml_bright on 2015/7/21 18:08
 * Email: 2504509903@qq.com
 */
public class BundleUtil {

    public static final int REQUEST_CODE_FOR_ART_DETAIL = 0x2010;

    public static final String KEY_ART_DETAIL_ID = "key_detail_id";
    public static final String KEY_ART_DETAIL_COLLECT_USER = "key_detail_collect_user";
    public static final String KEY_ART_DETAIL_ADAPTER_POSITION = "key_detail_adapter_position";
    public static final String KEY_ART_DETAIL_ARTWORK = "key_detail_artwork";

    public static Bundle buildArtDetailBundle(long artworkId){
        return buildArtDetailBundle( -1, artworkId);
    }

    public static Bundle buildArtDetailBundle(int adapterPosition, long artworkId) {
        return buildArtDetailBundle(adapterPosition, artworkId, null);
    }

    public static Bundle buildArtDetailBundle(int adapterPosition, long artworkId, User collectUser) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ART_DETAIL_ID, artworkId);
        bundle.putSerializable(KEY_ART_DETAIL_COLLECT_USER, collectUser);
        bundle.putInt(KEY_ART_DETAIL_ADAPTER_POSITION, adapterPosition);
        return bundle;
    }

    public static Bundle buildArtDetailFinishBundle(int adapterPosition, Artwork artwork) {
        if(adapterPosition == -1 || artwork == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ART_DETAIL_ADAPTER_POSITION, adapterPosition);
        bundle.putSerializable(KEY_ART_DETAIL_ARTWORK, artwork);
        return bundle;
    }



}
