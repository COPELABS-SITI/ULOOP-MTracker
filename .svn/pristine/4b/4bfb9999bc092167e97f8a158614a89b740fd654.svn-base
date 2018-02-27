/**
 *  Copyright (C) 2013 ULHT
 *  Author(s): jonnahtan.saltarin@ulusofona.pt
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by  the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program.  If not, see
 *  <http://www.gnu.org/licenses/>.
 *
 * ULOOP Mobility tracking plugin: Mtracker
 *
 * Mtracker is an Android app that collects information concerning visited APs
 * It computes a rank and then estimates a potential handover - time and target AP
 * v1.0 - pre-prototype, D3.3, July 2012
 * v2.0 - prototype on September 2012 - D3.6
 * v3.0 - prototype on June 2013
 *
 * @author Jonnahtan Saltarin
 * @author Rute Sofia
 * @author Christian da Silva Pereira
 * @author Luis Amaral Lopes
 *
 * @version 3.0
 *
 * @file Contains MTrackerAP class. This class represents what MTracker considers an AP.
 *       The information kept in this object are SSID, BSSID, attractiveness, and last IP.
 *
 */

package eu.uloop.mobilitytracker;

import java.math.BigInteger;

/**
 *  This class represents what MTracker considers an AP.
 *  The information kept in this object are SSID, BSSID, attractiveness, and last IP.
 *
 * @author Jonnahtan Saltarin (ULHT)
 * @author Rute Sofia (ULHT)
 * @author Christian da Silva Pereira (ULHT)
 * @author Luis Amaral Lopes (ULHT)
 *
 * @version 3.0
 */
public class MTrackerAP {

	private String SSID;
	private String BSSID;
	private double attractiveness;
	private String lastGatewayIp;
	
	
	/**
     * Get the SSID of this AP
	 * @return the sSID
	 */
	public String getSSID() {
		return SSID;
	}
	/**
     * Get the BSSID of this AP
	 * @return the bSSID
	 */
	public String getBSSID() {
		return BSSID;
	}
	/**
     * Get the attractiveness of this AP
	 * @return the attractiveness
	 */
	public double getAttractiveness() {
		return attractiveness;
	}
	/**
     * Get the last IP shown by this AP
	 * @return the lastGatewayIp
	 */
	public String getLastGatewayIp() {
		return lastGatewayIp;
	}

	/**
     * Set the SSID of this AP
	 * @param sSID the sSID to set
	 */
	public void setSSID(String sSID) {
		SSID = sSID;
	}
	/**
     * Set the BSSID of this AP
	 * @param bSSID the bSSID to set
	 */
	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}
	/**
     * Set the attractiveness of this AP
	 * @param attractiveness the attractiveness to set
	 */
	public void setAttractiveness(double attractiveness) {
		this.attractiveness = attractiveness;
	}
	/**
     * Set the last IP shown by this AP
	 * @param lastGatewayIp String representing the last gateway IP to set
	 */
	public void setLastGatewayIp(String lastGatewayIp) {
		this.lastGatewayIp = lastGatewayIp;
	}

    /**
     *  Set the last IP shown by this AP
     * @param lastGatewayIp Integer representing the last gateway IP to set
     */
	public void setLastGatewayIp(int lastGatewayIp) {
		byte[] bytes = BigInteger.valueOf(lastGatewayIp).toByteArray();
		if (bytes.length == 4) {
			this.lastGatewayIp = (bytes[3] & 0xFF) + "." + (bytes[2] & 0xFF) + "." + (bytes[1] & 0xFF) + "." + (bytes[0] & 0xFF);
			System.out.println(this.lastGatewayIp);
		}
	}

    /**
     * MTracker AP Constructor
     */
	public MTrackerAP() {
		super();
	}

    /**
     * Set some parameters to default
     */
	public void setToDefault(double uloopDispositionalTrust) {
		this.attractiveness = uloopDispositionalTrust;
		this.lastGatewayIp = "";
	}

    /**
     * Return a string containing the SSID, BSSID and the Attractiveness.
     */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SSID: " + this.SSID + "\n");
		sb.append("BSSID: " + this.BSSID + "\n");
		sb.append("Attractiveness: " + this.attractiveness + "\n");
		//sb.append("Last Gateway IP: " + this.lastGatewayIp + "\n");
		
		return sb.toString();
	}
}