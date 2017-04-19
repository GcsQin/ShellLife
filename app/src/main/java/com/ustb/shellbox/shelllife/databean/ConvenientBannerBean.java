package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/20.
 */

public class ConvenientBannerBean {
    String bannerHref;
    String bannerTitle;
    String bannerImgSrc;

    public ConvenientBannerBean(String bannerHref, String bannerTitle, String bannerImgSrc) {
        this.bannerHref = bannerHref;
        this.bannerTitle = bannerTitle;
        this.bannerImgSrc = bannerImgSrc;
    }

    public String getBannerHref() {
        return bannerHref;
    }

    public void setBannerHref(String bannerHref) {
        this.bannerHref = bannerHref;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerImgSrc() {
        return bannerImgSrc;
    }

    public void setBannerImgSrc(String bannerImgSrc) {
        this.bannerImgSrc = bannerImgSrc;
    }
}
