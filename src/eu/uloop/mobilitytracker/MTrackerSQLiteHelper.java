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
 * @file Contains MTrackerSQLiteHelper class. This class extends the SQLiteOpenHelper android
 * class.
 *
 */
package eu.uloop.mobilitytracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class extends the SQLiteOpenHelper android class.
 *
 * @author Jonnahtan Saltarin (ULHT)
 * @author Rute Sofia (ULHT)
 * @author Christian da Silva Pereira (ULHT)
 * @author Luis Amaral Lopes (ULHT)
 *
 * @version 3.0
 *
 */
public class MTrackerSQLiteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "mtracker.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_ACCESSPOINTS = "accesspoints";
	public static final String TABLE_VISITS = "visits";
	public static final String TABLE_CONTEXT = "context";
	
	// IDENTIFICATION
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SSID = "ssid";
	public static final String COLUMN_BSSID = "bssid";
	public static final String COLUMN_GROUPID = "groupid";
	
	// ACCESS POINTS
	public static final String COLUMN_ATTRACTIVENESS = "attractiveness";
	public static final String COLUMN_LASTGATEWAYIP = "lastgatewayip";
	
	// VISITS
	public static final String COLUMN_TIMEON = "timeon";
	public static final String COLUMN_TIMEOUT = "timeout";
	public static final String COLUMN_DAYOFTHEWEEK = "dayoftheweek";
	public static final String COLUMN_HOUR = "hour";
	
	  private static final String CREATE_ACCESSPOINTS_TABLE = "create table "
		      + TABLE_ACCESSPOINTS + "("
		      + COLUMN_ID + " integer primary key autoincrement, "
		      + COLUMN_BSSID + " text not null unique, "
		      + COLUMN_SSID + " text not null unique, "
		      + COLUMN_ATTRACTIVENESS + " integer not null, "
		      + COLUMN_LASTGATEWAYIP + " text"
		      + ");";
	  
	  private static final String CREATE_VISITS_TABLE = "create table "
		      + TABLE_VISITS + "("
		      + COLUMN_ID + " integer primary key autoincrement, "
		      + COLUMN_SSID + " text not null, "
		      + COLUMN_BSSID + " text not null, "
		      + COLUMN_TIMEON + " integer, "
		      + COLUMN_TIMEOUT + " integer, "
		      + COLUMN_DAYOFTHEWEEK + " integer, "
		      + COLUMN_HOUR + " integer"
		      + ");";

	public MTrackerSQLiteHelper(Context context) {
		 super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase dataBase) {
		dataBase.execSQL(CREATE_ACCESSPOINTS_TABLE);
		dataBase.execSQL(CREATE_VISITS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
	    dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESSPOINTS);
	    dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITS);
	    onCreate(dataBase);
	}

}
