package com.wfj.jaydenarchitecture.model.bean;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Artwork implements Serializable {
    /** 发布 */
    public static final int TYPE_FROM_PUBLISH = 0;
    /** 被收藏 */
    public static final int TYPE_FROM_COLLECTION = 1;

    public final static int CAN_SALE = 1;
    public final static int CAN_NOT_SALE = 0;

    @SerializedName("id")
    public int id;

    //艺术品名称
    @SerializedName("workname")
    public String workname;

    //收藏数
    @SerializedName("collection_num")
    public int collectionNum;

    //是否收藏
    @SerializedName("is_collection")
    public int isCollection;

    //评论数
    @SerializedName("comment_num")
    public int commentNum;

    //简介
    @SerializedName("summary")
    public String summary;

    //是否可售 0：否 1：是
    @SerializedName("is_sale")
    public int isSale;

    //是否精选
    @SerializedName("is_handpick")
    public int isHandpick;

    //精选原因 0: 发布,1:被XX收藏
    @SerializedName("handpick_reason")
    public int handpickReason;

    //收藏者
    @SerializedName("collector")
    public User collector;

    //艺术家
    @SerializedName("artist")
    public Artist artist;

    //是否显示艺术家详情
    @SerializedName("is_artist_detail")
    public int showArtistDetail;

    //封面
    @SerializedName("cover")
    public Image cover;

    //列表图片
    @SerializedName("detail_images")
    public List<Image> images;

    //发布者
    @SerializedName("publisher")
    public User publisher;

    //发布时间
    @SerializedName("publish_time")
    public long publishTime;

    //尺寸
    @SerializedName("size")
    public String size;

    //材质
    @SerializedName("material")
    public String material;

    @SerializedName("is_delete")
    public int isDelete;        //是否已经被删除

    @SerializedName("share_url")
    public String shareUrl; //分享连接

    @SerializedName("title")
    public String title;

    /** 是否为发布作品 */
    public boolean isPublish() {
        return handpickReason == TYPE_FROM_PUBLISH;
    }

    /** 是否可售 */
    public boolean isSale() {
        return isSale == CAN_SALE;
    }

    /** 是否收藏 */
    public boolean isCollection() {
        return isCollection == 1;
    }

    /** 是否显示艺术家详情 */
    public boolean showArtistDetail() {
        return showArtistDetail == 1;
    }

    /** 是否被删除 **/
    public boolean isDeleted(){
        return isDelete == 1;
    }

}
