package com.jake.foodfinder.model;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;
import com.google.api.client.util.Key;

public class Place implements Serializable {
	/**
	 * Serial ID for Place
	 */
	private static final long serialVersionUID = -3578034817473775299L;
	@Key
	private String name;
	@Key
	private Location location;
	@Key
	private String description;
	@Key
	private String address;
	@Key
	private double lat;
	@Key
	private double longi;
	@Key
	private String iconUrl;
	@Key
	private String id;
	@Key
	private boolean isOpen;
	@Key
	private String isOpenString;
	@Key
	private GeoPoint latlong;

	@Key
	private double rating;
	@Key
	private double distance;

	public void setDistanceFromMe(double newDistance) {

		this.distance = newDistance;

	}

	public double getDistanceFromMe() {

		return distance;
	}

	public void setRating(double newRating) {
		this.rating = newRating;

	}

	public double getRating() {

		return this.rating;
	}

	public boolean getIsOpen() {

		return isOpen;
	}

	public void setIsOpen(boolean open) {

		this.isOpen = open;
	}

	public String getIsOpenString() {

		return isOpenString;
	}

	public void setIsOpenString(String open) {

		this.isOpenString = open;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	public double getLat() {
		return lat;

	}

	public void setLat(double lati) {

		this.lat = lati;
	}

	public double getLongi() {
		return longi;

	}

	public void setLongi(double longi) {

		this.longi = longi;
	}

	public void setIconUrl(String iconUrl) {

		this.iconUrl = iconUrl;

	}

	public String getIconUrl() {

		return this.iconUrl;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getId() {

		return this.id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
