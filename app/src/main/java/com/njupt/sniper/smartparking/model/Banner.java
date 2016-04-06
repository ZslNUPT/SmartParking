package com.njupt.sniper.smartparking.model;

import java.io.Serializable;

/**
 * Created by lmw on 2015/9/22.
 */
public class Banner implements Serializable {

    private String image;
    private String url;
	private String title;

	public Banner(String image, String url, String title) {
		this.image = image;
		this.url = url;
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
