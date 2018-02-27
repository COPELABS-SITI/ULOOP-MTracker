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
 */

package eu.uloop.mobilitytracker;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import lib.api.UloopMTrackerMessage;
import lib.api.UloopMessageAPI;
import lib.api.UloopMessageAPIImp;
import lib.api.UloopMTrackerMessage.MTrackerPredictedM;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * This class is contains the core functionalities of
 * the application. The MTrackerService will run in background, getting WI-FI parameters and
 * storing the required information in the database.
 *
 * @author Jonnahtan Saltarin (ULHT)
 * @author Rute Sofia (ULHT)
 * @author Christian da Silva Pereira (ULHT)
 * @author Luis Amaral Lopes (ULHT)
 *
 * @version 3.0
 *
 */
public class MTrackerService extends Service {
	private double uloopDispositionalTrust = 1.0;

	private MTrackerWifiManager wifiManager;
	private MTrackerServiceWifiListener wifiListener;

	MTrackerDataSource dataSource;
	private ArrayList<DataBaseChangeListener> listeners = new ArrayList<DataBaseChangeListener> ();
    private final IBinder mBinder = new LocalBinder();
			
    public class LocalBinder extends Binder {
    	MTrackerService getService() {
            return MTrackerService.this;
        }
    }
    
    protected List<MTrackerAP> getData () {
    	if (dataSource != null)
    		return new ArrayList<MTrackerAP>(dataSource.getAllAP().values());
    	else
    		return null;
    }

    /**
     * Starts the periodic scanning. This will call the adequate function in the MTrackerWifiManager,
     * which will start a scan periodically. The time between each scan is defined in the
     * MTrackerWifiManager class.
     *
     */
	public void startPeriodicScanning () {
		wifiManager.startPeriodicScanning();
	}

    /**
     * Stops the periodic scanning.
     *
     */
	public void stopPeriodicScanning () {
		wifiManager.stopPeriodicScanning();
	}
	
    @Override
    public void onCreate() {
    	super.onCreate();
    	dataSource = new MTrackerDataSource(this);
    	dataSource.openDB(true);
		wifiManager = new MTrackerWifiManager(this);
		wifiListener = new MTrackerServiceWifiListener();
		wifiManager.setOnWifiChangeListener(wifiListener);
		wifiManager.noteOngoingConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	wifiManager.startPeriodicScanning();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    	wifiManager.close(this);
		dataSource.closeDB();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
        
    public void setOnStateChangeListener (DataBaseChangeListener listener) 
    {
        this.listeners.add(listener);
    }
    
    public void clearOnStateChangeListeners () 
    {
        this.listeners.clear();
    }

    /**
     * Notifies a database change to the listeners.
     */
    private void notifyDataBaseChange () {
    	for (DataBaseChangeListener listener : this.listeners) 
    	{
    	    listener.onDataBaseChange(new ArrayList<MTrackerAP>(dataSource.getAllAP().values()));
    	}
    }

    /**
     * Notifies a new message to the listeners.
     *
     * @param newMessage The message to be notified.
     */
    private void notifyPredictedMoveChange (String newMessage) {
    	for (DataBaseChangeListener listener : this.listeners) 
    	{
    	    listener.onStatusMessageChange(newMessage);
    	}
    }

    /**
     * Writes the AP list to a text file.
     */
	public void writeAPListToFile () {
		dataSource.writeAPListToFile();
	}

    /**
     * Write visits to File
     */
	public void writeVisitListToFile () {
		dataSource.writeVisitListToFile();
	}

    /**
     * Sets the ULOOP Dispositional Trust, which is the default attractiveness.
     *
     * @param uloopDT Uloop dispositional trust.
     * @return true if uloopDT is valid [0-1], false otherwise
     */
	public boolean setUloopDispositionalTrust (double uloopDT) {
		if (uloopDT >= 0.0 && uloopDT <= 1.0) {
			this.uloopDispositionalTrust = uloopDT;
			return true;
		}
		else {
			return false;
		}
	}


    /**
     * Sends a message to the Access Point with information about sverage stationary time in this AP,
     * if this is the best AP available for this device, if a change is expected soon and to which AP.
     *
     * The message is sent using Protobuffers, and the proto definition is shown below:
     *
     * message MTrackerMessage {
     *
     * 		message MTrackerPredictedMove {
     *			required string BSSID = 1;
     *			optional uint64 stationaryTime = 2;
     *			optional string lastGatewayIP = 3;
     * 		}
     *
     * 		required uint64 timeForNextMove = 1;
     * 		required uint64 currentStationaryTime = 2;
     * 		repeated MTrackerPredictedMove = 3 [packed=true];
     * }
     *
     * @param nextBssid BSSID of the next AP (The best ranked AP available). If the best AP is the
     *                  current one, this will be the string "this".
     *
     * @param nextLastGatewayIp Last IP used by the predicted next AP.
     *
     * @param nextStationaryTime Average connection time (Stationary Time) in the predicted next AP.
     *
     * @param timetoMove Expected time to change AP. When the message is sent at the beginning of the connection,
     *                   it is equal to the currentStationaryTime.
     *
     * @param currentStationaryTime  Average connection time (Stationary Time) in the current AP.
     */
	private void announcePossibleHandover (String nextBssid, String nextLastGatewayIp, long nextStationaryTime, long timetoMove, long currentStationaryTime) {
		
		String mtrackerServer;
		
		byte[] bytes = BigInteger.valueOf(wifiManager.getGatewayIp()).toByteArray();
		if (bytes.length == 4) {
			mtrackerServer = (bytes[3] & 0xFF) + "." + (bytes[2] & 0xFF) + "." + (bytes[1] & 0xFF) + "." + "25";
		}
		else {
			mtrackerServer = "192.168.3.25";
		}
		
		new SendInformationWithGatewayTask().execute(nextBssid, nextLastGatewayIp, Long.toString(nextStationaryTime), Long.toString(timetoMove), Long.toString(currentStationaryTime), mtrackerServer);
		
		String message = "Time for next move: " + timetoMove + "\nCurrent ST: " + currentStationaryTime;
		message += "\nNext AP - BSSID: " + nextBssid + "\nNext AP - ST: " + nextStationaryTime + "\nNext AP - Gateway: " + nextLastGatewayIp;
		
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	class MTrackerServiceWifiListener implements WifiChangeListener {
		public void onWifiStateDisabled(boolean valid, String bssid, String ssid, long visitId, long connectionStart, long connectionEnd) {
			notifyPredictedMoveChange("Wifi turned OFF");
			if (valid) {
				dataSource.updateVisit(visitId, null, null, null, connectionEnd);
				notifyDataBaseChange ();
			}
		}
		public void onWifiStateEnabled () {
			notifyPredictedMoveChange("Wifi turned ON");
		}
		public void onWifiConnectionDown(boolean valid, String bssid, String ssid, long visitId, long connectionStart, long connectionEnd) {
			notifyPredictedMoveChange("Wifi connection DOWN");
			if (valid) {
				dataSource.updateVisit(visitId, null, null, null, connectionEnd);
				notifyDataBaseChange ();
			}
		}
		public long onWifiConnectionUp(String bssid, String ssid, List<ScanResult> lastScanResults) {
			notifyPredictedMoveChange("Wifi connection UP (" + ssid + ")");
			if (!dataSource.hasAP(bssid)) {
				MTrackerAP ap = new MTrackerAP();
				ap.setBSSID(bssid);
				ap.setSSID(ssid);
				ap.setAttractiveness(uloopDispositionalTrust);
				ap.setLastGatewayIp(wifiManager.getGatewayIp());
				dataSource.registerNewAP(ap);
			}
			else {
				MTrackerAP ap = dataSource.getAP(bssid);
				ap.setBSSID(bssid);
				ap.setSSID(ssid);
				ap.setAttractiveness(uloopDispositionalTrust);
				ap.setLastGatewayIp(wifiManager.getGatewayIp());
				dataSource.updateAP(ap);
			}
			
			computeBestAp(bssid, lastScanResults);
			notifyDataBaseChange ();
			
			return dataSource.registerNewVisit(ssid, bssid, wifiManager.wifiCurrentAPStart, wifiManager.wifiCurrentAPStart);
		}
		public void onWifiAvailableNetworksChange(String bssid, List<ScanResult> results) {
			computeBestAp (bssid, results);
		}
		
		private void computeBestAp (String bssid, List<ScanResult> results) {
        	MTrackerAP bestAp = dataSource.getBestAP(results);
			
			if (bestAp != null) {
				if (bestAp.getBSSID().equals(bssid)) {
					long timeToMove = dataSource.getStationaryTime(bestAp) - (System.currentTimeMillis() - wifiManager.wifiCurrentAPStart)/1000;
					notifyPredictedMoveChange("Connecting to the best AP (" + bestAp.getSSID() + ")");
					announcePossibleHandover ("this", bestAp.getLastGatewayIp(), dataSource.getStationaryTime(bestAp), timeToMove, dataSource.getStationaryTime(bestAp));

				}
				else {
					long timeToMove = dataSource.getStationaryTime(dataSource.getAP(bssid)) - (System.currentTimeMillis() - wifiManager.wifiCurrentAPStart)/1000;
					if (timeToMove >= 0) {
						notifyPredictedMoveChange("Handover to AP " + bestAp.getSSID() + " is expected to occur in about " + timeToMove + "s");
						announcePossibleHandover (bestAp.getBSSID(), bestAp.getLastGatewayIp(), dataSource.getStationaryTime(bestAp), timeToMove, dataSource.getStationaryTime(dataSource.getAP(bssid)));
					}
					else {
						notifyPredictedMoveChange("Handover to AP " + bestAp.getSSID() + " was expected to occur about " + (0-timeToMove) + "s ago");
						//announcePossibleHandover (bestAp.getBSSID(), bestAp.getLastGatewayIp(), dataSource.getStationaryTime(bestAp), (0-timeToMove), dataSource.getStationaryTime(dataSource.getAP(ssid)));
					}
				}
			}
			else {
				notifyPredictedMoveChange("No AP in DB");
			}
		}
	}
	
	private class SendInformationWithGatewayTask extends AsyncTask<String, Void, Void>
	{
		protected Void doInBackground(String... parameters)
		{
			String nextBssid = parameters[0];
			String nextLastGatewayIp = parameters[1];
			long nextStationaryTime = Long.parseLong(parameters[2]);
			long timetoMove = Long.parseLong(parameters[3]);
			long currentStationaryTime = Long.parseLong(parameters[4]);
			String mtrackerServer = parameters[5];
			
			Socket s = new Socket();
			try {
				//s.connect(new InetSocketAddress("192.168.3.25", 3002));
				s.connect(new InetSocketAddress(mtrackerServer, 3002));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			UloopMessageAPI api = new UloopMessageAPIImp(s);
			UloopMTrackerMessage mtrackermesg = new UloopMTrackerMessage();
			MTrackerPredictedM m = mtrackermesg.new MTrackerPredictedM();
			
			mtrackermesg.setTimeForNextMove(timetoMove);
			mtrackermesg.setCurrentStationaryTime(currentStationaryTime);

			m.setBSSID(nextBssid);
			m.setLastGatewatIP("192.168.1.1");
			m.setstationaryTime(nextStationaryTime);
			mtrackermesg.addNextGateway(m);

			api.send_mtracker_message(mtrackermesg);
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
			
		}
	}
}
