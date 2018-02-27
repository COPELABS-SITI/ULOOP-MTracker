/*
 *  Copyright (C) 2013 Caixa Magica Software.
 *
 *  Authors:
 *
 *      Nuno Martins <nuno.martins@caixamagica.pt>
 *
 */

package lib.api;

import java.util.LinkedList;
import java.util.List;

import eu.uloop.messages.UloopMessages.MTrackerMessage;
import eu.uloop.messages.UloopMessages.MTrackerPredictedMove;
import eu.uloop.messages.UloopMessages.UloopMessage;
import eu.uloop.messages.UloopMessages.UloopMessage.Builder;
import eu.uloop.messages.UloopMessages.UloopMessageType;

public class UloopMTrackerMessage extends UloopMTracker {

	public class MTrackerPredictedM {
		private String BSSID;
		private long stationaryTime;
		private String lastGatewayIP;

		public void setBSSID(String bssid)
		{
			this.BSSID = bssid;
		}

		public void setstationaryTime(long time)
		{
			this.stationaryTime = time;
		}

		public void setLastGatewatIP(String gateip)
		{
			this.lastGatewayIP = gateip;
		}
	}

	private long timeForNextMove;
	private long currentStationaryTime;
	private List<MTrackerPredictedM> NextGatewayList;

	public UloopMTrackerMessage()
	{
		this.NextGatewayList = new LinkedList<UloopMTrackerMessage.MTrackerPredictedM>();
	}

	public void setTimeForNextMove(long time)
	{
		this.timeForNextMove = time;
	}

	public void setCurrentStationaryTime(long time)
	{
		this.currentStationaryTime = time;
	}

	public void addNextGateway(MTrackerPredictedM next)
	{
		this.NextGatewayList.add(next);
	}

	@Override
	public UloopAbstractMessage decode(UloopMessage ulm)
			throws RuntimeException {
		// TODO Auto-generated method stub

		MTrackerMessage mtm = ulm.getMtracker();
		setCurrentStationaryTime(mtm.getCurrentStationaryTime());

		for(MTrackerPredictedMove e:mtm.getNextGatewayListList())
		{
			MTrackerPredictedM next = new MTrackerPredictedM();
			next.setBSSID(e.getBSSID());
			next.setLastGatewatIP(e.getLastGatewayIP());
			next.setstationaryTime(e.getStationaryTime());

			addNextGateway(next);
		}

		return this;
	}

	@Override
	public Builder encode() {
		UloopMessage.Builder ulmb = UloopMessage.newBuilder();
		ulmb.setUlt(UloopMessageType.ULOOP_EXTERNAL_MTRACKER);
		MTrackerMessage.Builder mtb = MTrackerMessage.newBuilder();
		// TODO Auto-generated method stub

		mtb.setCurrentStationaryTime(this.currentStationaryTime);
		mtb.setTimeForNextMove(this.timeForNextMove);

		for(UloopMTrackerMessage.MTrackerPredictedM e:this.NextGatewayList)
		{
			MTrackerPredictedMove.Builder mb = MTrackerPredictedMove.newBuilder();
			mb.setBSSID(e.BSSID);
			mb.setStationaryTime(e.stationaryTime);
			mb.setLastGatewayIP(e.lastGatewayIP);
			mtb.addNextGatewayList(mb.build());
		}

		ulmb.setMtracker(mtb.build());
		return ulmb;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

}
