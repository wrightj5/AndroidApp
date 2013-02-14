package com.jake.foodfinder.model;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;
import com.google.api.client.util.Key;

public class Location implements Serializable {
	/**
	 * Serial ID for Location
	 */
	private static final long serialVersionUID = -1861462299276634548L;
	@Key
	private double lat;
	@Key
	private double lng;

	/**
	 * @return the lat
	 */
	public double getLatitude() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLatitude(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLongitude() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLongitude(double lng) {
		this.lng = lng;
	}
	
	public GeoPoint toGeoPoint() {
		int latE6 = (int) (getLatitude() * 1e6);
		int lonE6 = (int) (getLongitude() * 1e6);
		return new GeoPoint(latE6, lonE6);
	}
}