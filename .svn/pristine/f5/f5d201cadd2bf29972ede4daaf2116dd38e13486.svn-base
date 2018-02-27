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
 * @file Contains MTrackerDataSource class. This class provides methods to insert, update and
 * query the application database. It also provide methods to compute certain values, like the
 * Rank and the Stationary Time, among others.
 *
 */

package eu.uloop.mobilitytracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.io.*;

import android.net.wifi.ScanResult;
import android.os.Environment;
import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class provides methods to insert, update and
 * query the application database. It also provide
 * methods to compute certain values, like the
 * Rank and the Stationary Time, among others.
 *
 * @author Jonnahtan Saltarin (ULHT)
 * @author Rute Sofia (ULHT)
 * @author Christian da Silva Pereira (ULHT)
 * @author Luis Amaral Lopes (ULHT)
 *
 * @version 3.0
 *
 *
 */
public class MTrackerDataSource {
	
	/*
	 * COMMON OPERATIONS
	 */
	private SQLiteDatabase db;
	private MTrackerSQLiteHelper dbHelper;
	private boolean isDbOpen;
	private GregorianCalendar cal;
	
	/**
	 * Constructor that takes Android Context as input.
	 * 
	 * @param context
	 */
	public MTrackerDataSource (Context context) {
		dbHelper = new MTrackerSQLiteHelper(context);
		isDbOpen = false;
		cal = new GregorianCalendar();
		
	}

	/**
	 * Opens the predefined MTracker database.
	 * 
	 * @param writable
	 * @throws SQLException
	 */
	public void openDB(boolean writable) throws SQLException {
		if (!isDbOpen) {
			if (writable)
				db = dbHelper.getWritableDatabase();
			else
				db = dbHelper.getReadableDatabase();
		}
	}

	/**
	 * Close the predefined MTracker database.
	 */
	public void closeDB() {
		dbHelper.close();
		isDbOpen = false;
	}
	
	/*
	 * ACCESS_POINTS TABLE: This table stores information
	 * regarding each access point visited in the past.
	 * The information stored is:
	 * 		- SSID of the network
	 * 		- BSSID of the network
	 * 		- Attractiveness of the network.
	 *      - IP of the gateway.
	 */
	
	
	
	/**
	 * List of all columns on the ACCESS_POINTS table.
	 */
	private String[] allColumnsAccessPoint = { 
			MTrackerSQLiteHelper.COLUMN_BSSID,
			MTrackerSQLiteHelper.COLUMN_SSID,
			MTrackerSQLiteHelper.COLUMN_ATTRACTIVENESS,
			MTrackerSQLiteHelper.COLUMN_LASTGATEWAYIP
	};
	
	/**
	 * Converts a cursor pointing to a record in the ACCESS_POINTS table to a MTrackerAP object.
	 * 
	 * @param cursor Cursor pointing to a record of the ACESS_POINTS table.
	 * @return the MTrackerAP object
	 */
	private MTrackerAP cursorToAP(Cursor cursor) {
		MTrackerAP ap = new MTrackerAP();
		ap.setBSSID(cursor.getString(0));
		ap.setSSID(cursor.getString(1));
		ap.setAttractiveness(cursor.getDouble(2));
		ap.setLastGatewayIp(cursor.getInt(3));
		return ap;
	}

	/**
	 * Gets the number of records in the ACCESS_POINTS table. This is, the number of AP registered on the application.
	 * 
	 * @return the number of AP registered by the application.
	 */
	public long getNumAP(){
		return DatabaseUtils.queryNumEntries(db, MTrackerSQLiteHelper.TABLE_ACCESSPOINTS);
	}
	
	/**
	 * Register a new AP in the application. It creates a new record on the ACCESS_POINTS table, with the information passed as MTrackerAP.
	 * 
	 * @param ap Access point information.
	 * @return the row ID of the newly inserted row, or -1 if an error occurred.
	 */
	public long registerNewAP (MTrackerAP ap) {
		ContentValues values = new ContentValues();
	    values.put(MTrackerSQLiteHelper.COLUMN_BSSID, ap.getBSSID());
	    values.put(MTrackerSQLiteHelper.COLUMN_SSID, ap.getSSID());
	    values.put(MTrackerSQLiteHelper.COLUMN_ATTRACTIVENESS, ap.getAttractiveness());
	    values.put(MTrackerSQLiteHelper.COLUMN_LASTGATEWAYIP, ap.getLastGatewayIp());
	    
	    return db.insert(MTrackerSQLiteHelper.TABLE_ACCESSPOINTS, null, values);
	}
	
	/**
	 * Update an AP already registered by the application. This modifies the corresponding record to the AP in the ACCESS_POINTS table.
	 * 
	 * @param ap Access point information.
	 * @return true, if successful.
	 */
	public boolean updateAP(MTrackerAP ap) {
		String identifier = MTrackerSQLiteHelper.COLUMN_BSSID + "='" + ap.getBSSID() + "'";
		ContentValues values = new ContentValues();
		values.put(MTrackerSQLiteHelper.COLUMN_BSSID, ap.getBSSID());
	    values.put(MTrackerSQLiteHelper.COLUMN_SSID, ap.getSSID());
	    values.put(MTrackerSQLiteHelper.COLUMN_ATTRACTIVENESS, ap.getAttractiveness());
	    values.put(MTrackerSQLiteHelper.COLUMN_LASTGATEWAYIP, ap.getLastGatewayIp());
	    
	    int rows = db.update(MTrackerSQLiteHelper.TABLE_ACCESSPOINTS, values, identifier, null);
		
	    return ((rows != 0)? true : false);
	}

	/**
	 * Gets an AP already registered by the application. 
	 * 
	 * @param bssid The ssid of the AP which information should be returned
	 * @return the MTrackerAP object, null if not found.
	 */
	public MTrackerAP getAP(String bssid) {
		MTrackerAP ap;
		Cursor cursor = db.query(MTrackerSQLiteHelper.TABLE_ACCESSPOINTS, allColumnsAccessPoint, MTrackerSQLiteHelper.COLUMN_BSSID + "='" + bssid + "'", null, null, null, null);
		if (cursor.moveToFirst())
			ap = cursorToAP(cursor);
		else
			ap = null;	
		
		cursor.close();
		return ap;
	}
	
	/**
	 * Checks if a given AP has already been registered by the application.
	 * 
	 * @param bssid The ssid of the AP
	 * @return true, if AP has already been registered by the application, false otherwise.
	 */
	public boolean hasAP (String bssid) {
        return (DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + MTrackerSQLiteHelper.TABLE_ACCESSPOINTS + " WHERE " + MTrackerSQLiteHelper.COLUMN_BSSID + "='" + bssid + "'", null) == 0)? false : true;
	}
	
	/**
	 * Gets the all the AP recorded by the application on the ACCESS_POINTS table.
	 * 
	 * @return A map with the AP objects, and the bssid as key.
	 */
	public Map<String, MTrackerAP> getAllAP() {
		Map<String, MTrackerAP> apMap = new TreeMap<String, MTrackerAP>();

		Cursor cursor = db.query(MTrackerSQLiteHelper.TABLE_ACCESSPOINTS, allColumnsAccessPoint, null, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			MTrackerAP ap = cursorToAP(cursor);
			apMap.put(ap.getBSSID(), ap);
			cursor.moveToNext();
		}

	    cursor.close();
	    return apMap;
	}
	
	/**
	 * Gets the all the AP recorded by the application on the ACCESS_POINTS table, and the return only the ones that are also available in the List of ScanResult.
	 * 
	 * @return A map with the AP objects, and the bssid as key.
	 */
	public Map<String, MTrackerAP> getAllAP(List <ScanResult> availableAP) {
		Map<String, MTrackerAP> apMap = new TreeMap<String, MTrackerAP>();
		Set<String> scanUniques = new LinkedHashSet<String>();
    	
		for (ScanResult result : availableAP) {
        	scanUniques.add(result.BSSID);
    	}
	
		Cursor cursor = db.query(MTrackerSQLiteHelper.TABLE_ACCESSPOINTS,
			allColumnsAccessPoint, null, null, null, null, null);
	
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			MTrackerAP ap = cursorToAP(cursor);
			if (scanUniques.contains(ap.getBSSID())) {
				apMap.put(ap.getBSSID(), ap);
			}
			cursor.moveToNext();
		}

	    cursor.close();
	    return apMap;
	}

	/**
	 * Checks all the AP registered by the application and return the one with the highest Rank.
	 * 
	 * @return the best AP registered by the application.
	 */
	public MTrackerAP getBestAP() {
		Map<String, MTrackerAP> aps = getAllAP();
		if (!aps.isEmpty()) {
			MTrackerAP bestAp = new MTrackerAP();
			double bestRank = -1.0;
			double rank;
			for (MTrackerAP ap : aps.values()) {
				rank = getRank(ap);
				if (rank > bestRank) {
					bestRank = rank;
					bestAp = ap;
				}
			}
			return bestAp;
		} else {
			return null;
		}
	}
	
	/**
	 * Checks the APs registered by the application and available in the List of ScanResult, and the return the one with the highest rank.
	 * 
	 * @return the best AP registered by the application.
	 */
	public MTrackerAP getBestAP(List <ScanResult> availableAP) {
		Map<String, MTrackerAP> aps = getAllAP(availableAP);
		if (!aps.isEmpty()) {
			MTrackerAP bestAp = new MTrackerAP();
			double bestRank = -1.0;
			double rank;
			for (MTrackerAP ap : aps.values()) {
				rank = getRank(ap);
				if (rank > bestRank) {
					bestRank = rank;
					bestAp = ap;
				}
			}
			return bestAp;
		} else {
			return null;
		}
	}
	
	/**
	 * Write all the AP registered by the application into a text file (MTracker.txt), located in the root of the directory.
	 */
	public void writeAPListToFile (){
		 
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "MTracker.txt");
			 
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
		
			OutputStreamWriter osw = null;
			osw = new OutputStreamWriter(fOut);
			Map<String,MTrackerAP> apEntries = getAllAP();
			for (MTrackerAP ap : apEntries.values()) {
				osw.write(ap.toString());
				osw.write(".....\n");
			}
			osw.close();
			fOut.close();
		}
		catch(Exception e){
			 e.printStackTrace(System.err);
		}
	}
	
	/*
	 * VISITS TABLE: This table stores the visits
	 * to each access point. The information stored
	 * is:
	 * 		- SSID of the network
	 * 		- BSSID of the network
	 * 		- Time at which the connection started.
	 * 		- Time at which the connection ended.
	 * 		- Day of the Week (Start).
	 * 		- Hour of the Day (Start).
	 * 
	 */
	
	
	private String[] allColumnsVisit = { 
			MTrackerSQLiteHelper.COLUMN_SSID,
			MTrackerSQLiteHelper.COLUMN_BSSID,
			MTrackerSQLiteHelper.COLUMN_TIMEON,
			MTrackerSQLiteHelper.COLUMN_TIMEOUT,
			MTrackerSQLiteHelper.COLUMN_DAYOFTHEWEEK,
			MTrackerSQLiteHelper.COLUMN_HOUR
	};
	
	private MTrackerVisit cursorToVisit(Cursor cursor) {
		MTrackerVisit visit = new MTrackerVisit();
		visit.setSSID(cursor.getString(0));
		visit.setBSSID(cursor.getString(1));
		visit.setStartTime(cursor.getLong(2));
		visit.setEndTime(cursor.getLong(3));
		visit.setDayOfTheWeek(cursor.getInt(2));
		visit.setHourOfTheDay(cursor.getInt(3));
		return visit;
	}

    /**
     * Computes the Stationary Time for a given AP.
     *
     * @param ap The MTrackerAP whose Stationary Time is to be computed.
     * @return The stationary time for the given AP.
     */
	public long getStationaryTime(MTrackerAP ap) {		
		String bssid = ap.getBSSID();
		long sationaryTime = 0;
		long count = 0;
		long startTime = 0;
		long endTime = 0;
		Cursor cursor = db.query(MTrackerSQLiteHelper.TABLE_VISITS, allColumnsVisit, MTrackerSQLiteHelper.COLUMN_BSSID + "='" + bssid + "'", null, null, null, null);
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				startTime = cursor.getLong(2);
				endTime = cursor.getLong(3);
				if ((endTime - startTime) > 0) {
					sationaryTime = sationaryTime + (endTime - startTime);
					count++;
				}
				cursor.moveToNext();
			}
			cursor.close();
			
			if (count > 0)
				sationaryTime = sationaryTime/count;
	
		}
		else {
			cursor.close();
		}
		
		return sationaryTime/1000;
	}

    /**
     * Computes the Stationary Time for a given AP, only taking into consideration records for a given Day of the Week.
     *
     * @param ap The MTrackerAP whose Stationary Time is to be computed.
     * @param dayOfTheWeek Day of the week that will restrict the computation of the stationary time.
     * @return The stationary time for the given AP.
     */
	public long getStationaryTimeByMoment (MTrackerAP ap, int dayOfTheWeek) {		
		String bssid = ap.getBSSID();
		long sationaryTime = 0;
		long count = 0;
		long startTime = 0;
		long endTime = 0;
		Cursor cursor = db.query(MTrackerSQLiteHelper.TABLE_VISITS, allColumnsVisit, MTrackerSQLiteHelper.COLUMN_BSSID + "='" + bssid + "' AND " + MTrackerSQLiteHelper.COLUMN_DAYOFTHEWEEK + "=" + dayOfTheWeek, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				startTime = cursor.getLong(2);
				endTime = cursor.getLong(3);
				if ((endTime - startTime) > 0) {
					sationaryTime = sationaryTime + (endTime - startTime);
					count++;
				}
				cursor.moveToNext();
			}
			cursor.close();
			
			if (count > 0)
				sationaryTime = sationaryTime/count;
	
		}
		else {
			cursor.close();
		}
		
		return sationaryTime/1000;
	}

    /**
     * Computes the Number of visits that the node has done to a given AP.
     *
     * @param ap The MTrackerAP whose Stationary Time is to be computed.
     * @return The number of visits.
     */
	public long countVisits(MTrackerAP ap) {
		String bssid = ap.getBSSID();
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + MTrackerSQLiteHelper.TABLE_VISITS + " WHERE " + MTrackerSQLiteHelper.COLUMN_BSSID + "='" + bssid + "'", null);
	}

    /**
     * Computes the Rank of this node towards a given AP. The Rank is computed as
     *
     * @param ap The MTrackerAP whose Stationary Time is to be computed.
     * @return The number of visits.
     */
	public double getRank (MTrackerAP ap)
	{		 
		return ap.getAttractiveness() * getStationaryTime(ap) * countVisits(ap);
	}

    /**
     * Test Method to compute the Rank of this node towards a given AP, taking into consideration the current
     * visit time.
     *
     * @param ap The MTrackerAP whose Stationary Time is to be computed.
     * @param currentDuration current connection time.
     * @return The number of visits.
     */
	public double getInstantaneousRank(MTrackerAP ap, Long currentDuration) { 
		 if (ap == null) {
			 return currentDuration;
		 }
		 else {
			 return (0.3*getStationaryTime(ap) + 0.7*currentDuration) * (countVisits(ap) + 1) * ap.getAttractiveness();
		 }
	}

    /**
     * Register a new visit into the database.
     *
     * @param SSID SSID
     * @param BSSID BSSID
     * @param startTime Time at which the connection started.
     * @param endTime Time at which the connection ended.
     * @return id of the created record, -1 if an error occurs.
     */
	public long registerNewVisit (String SSID, String BSSID, Long startTime, Long endTime) {
		cal.setTimeInMillis(startTime);
		ContentValues values = new ContentValues();
	    values.put(MTrackerSQLiteHelper.COLUMN_SSID, SSID);
	    values.put(MTrackerSQLiteHelper.COLUMN_BSSID, BSSID);
	    values.put(MTrackerSQLiteHelper.COLUMN_TIMEON, startTime);
	    values.put(MTrackerSQLiteHelper.COLUMN_TIMEOUT, endTime);
	    
	    try {
	    	int dayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK);
	    	values.put(MTrackerSQLiteHelper.COLUMN_DAYOFTHEWEEK, dayOfTheWeek);
	    } catch (Exception e) {
	    	//STORE DEFAULT AND WRITE TO LOG
	    }
	    
	    try {
	    	int hourOfTheDay = cal.get(Calendar.HOUR_OF_DAY);
	    	values.put(MTrackerSQLiteHelper.COLUMN_HOUR, hourOfTheDay);
	    } catch (Exception e) {
	    	//STORE DEFAULT AND WRITE TO LOG
	    }
	    
	    return db.insert(MTrackerSQLiteHelper.TABLE_VISITS, null, values);
	}

    /**
     * Updates an existing visit in the database.
     *
     * @param _id id of the record to update
     * @param SSID SSID
     * @param BSSID BSSID
     * @param startTime Time at which the connection started.
     * @param endTime Time at which the connection ended.
     * @return id of the created record, -1 if an error occurs.
     */
	public boolean updateVisit (long _id, String SSID, String BSSID, Long startTime, Long endTime) {
		String identifier = MTrackerSQLiteHelper.COLUMN_ID + "=" + _id;
		
		ContentValues values = new ContentValues();
		
		if (SSID != null)
			values.put(MTrackerSQLiteHelper.COLUMN_SSID, SSID);
		
		if (SSID != null)
			values.put(MTrackerSQLiteHelper.COLUMN_BSSID, BSSID);
	    
		if (startTime != null) {
			values.put(MTrackerSQLiteHelper.COLUMN_TIMEON, startTime);
			
			cal.setTimeInMillis(startTime);
		    
			try {
		    	int dayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK);
		    	values.put(MTrackerSQLiteHelper.COLUMN_DAYOFTHEWEEK, dayOfTheWeek);
		    } catch (Exception e) {
		    	//STORE DEFAULT AND WRITE TO LOG
		    }
		    
		    try {
		    	int hourOfTheDay = cal.get(Calendar.HOUR_OF_DAY);
		    	values.put(MTrackerSQLiteHelper.COLUMN_HOUR, hourOfTheDay);
		    } catch (Exception e) {
		    	//STORE DEFAULT AND WRITE TO LOG
		    }
		}
		
		if (endTime != null)
			values.put(MTrackerSQLiteHelper.COLUMN_TIMEOUT, endTime);
	    	    
		int rows;
		
		if (values.size() > 0)
	    	rows = db.update(MTrackerSQLiteHelper.TABLE_VISITS, values, identifier, null);
		else
			rows = 0;
		
	    return ((rows != 0)? true : false);
	}

    /**
     * Get a List with all the visit objects stored in the database.
     *
     */
	public List<MTrackerVisit> getAllVisits() {
		List<MTrackerVisit> visitList = new LinkedList<MTrackerVisit>();
	
		Cursor cursor = db.query(MTrackerSQLiteHelper.TABLE_VISITS,
			allColumnsVisit, null, null, null, null, null);
	
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			MTrackerVisit visit = cursorToVisit(cursor);
			visitList.add(visit);
			cursor.moveToNext();
		}

	    cursor.close();
	    return visitList;
	}
	
	public List<String> getAllVisitsString(MTrackerAP ap) {
		List<String> visitList = new LinkedList<String>();
	
		Cursor cursor = db.query(MTrackerSQLiteHelper.TABLE_VISITS, allColumnsVisit, MTrackerSQLiteHelper.COLUMN_BSSID + "='" + ap.getBSSID() + "'", null, null, null, null);
	
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			MTrackerVisit visit = cursorToVisit(cursor);
			visitList.add(visit.toString());
			cursor.moveToNext();
		}

	    cursor.close();
	    return visitList;
	}

    /**
     * Get the number of visits registered in the database.
     *
     */
	public long getNumVisits(){
		return DatabaseUtils.queryNumEntries(db, MTrackerSQLiteHelper.TABLE_VISITS);
	}

    /**
     * Writes the Visit List to the file MTrackerVisits.txt.
     *
     */
	public void writeVisitListToFile (){
		 
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "MTrackerVisits.txt");
			 
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
		
			OutputStreamWriter osw = null;
			osw = new OutputStreamWriter(fOut);
			List<MTrackerVisit> visitEntries = getAllVisits();
			for (MTrackerVisit visitEntry : visitEntries) {
				osw.write(visitEntry.toString());
				osw.write(".....\n");
			}
			osw.close();
			fOut.close();
		}
		catch(Exception e){
			 e.printStackTrace(System.err);
		}
	}
}