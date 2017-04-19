package com.ustb.shellbox.shelllife.databean;

import android.graphics.Bitmap;

/**
 * Created by 37266 on 2016/12/30.
 */
public class BannerBean {
    private Bitmap bannerImage;
    public BannerBean(Bitmap bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Bitmap getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(Bitmap bannerImage) {
        this.bannerImage = bannerImage;
    }
}
