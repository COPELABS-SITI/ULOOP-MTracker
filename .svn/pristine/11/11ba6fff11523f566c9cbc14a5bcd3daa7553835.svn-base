/*
 *  Copyright (C) 2013 Caixa Magica Software.
 *
 *  Authors:
 *
 *      Nuno Martins <nuno.martins@caixamagica.pt>
 *
 */

package lib.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import eu.uloop.messages.UloopMessages.UloopMessage;

public class UloopMessageAPIImp implements UloopMessageAPI {

	protected Socket s;
	
	public UloopMessageAPIImp(InetSocketAddress address)
	{
		this.s = new Socket();
		try {
			s.connect(address);
		} catch (IOException e) {
			e.printStackTrace();
			s = null;
		}
	}

	public UloopMessageAPIImp(Socket s)
	{
		this.s = s;
	}

	private void send_data(UloopMessage ulm)
	{
		try {
			if (s != null) {
				ulm.writeTo(s.getOutputStream());
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public UloopMessage recv_message()
	{
		UloopMessage ulm = null;
		try {
			ulm = UloopMessage.parseFrom(s.getInputStream());
		}catch (IOException e)
		{
			e.printStackTrace();
		}

		return ulm;
	}
	
	@Override
	public void send_service_request(UloopService ulservice){
		UloopMessage.Builder ulmb = ulservice.encode();
		UloopMessage ulm = ulmb.build();
		send_data(ulm);
	}

	@Override
	public UloopServiceRequest recv_service_request() {
		UloopMessage ulm = recv_message();
		UloopServiceRequest usr = null;

		if (ulm != null){
			usr = new UloopServiceRequest();
			usr.decode(ulm);
		}

		return usr;
	}

	@Override
	public void send_service_reply(UloopService ulservice) {
		UloopMessage.Builder ulmb = ulservice.encode();
		UloopMessage ulm = ulmb.build();
		send_data(ulm);
	}

	@Override
	public UloopServiceReply recv_service_reply() {
		UloopMessage ulm = recv_message();
		UloopServiceReply usr = null;

		if (ulm != null){
			usr = new UloopServiceReply();
			usr.decode(ulm);
		}

		return usr;
	}

	@Override
	public void send_mtracker_message(UloopMTracker ulmr) {
		UloopMessage.Builder ulmb = ulmr.encode();
		UloopMessage ulm = ulmb.build();
		send_data(ulm);
		// TODO Auto-generated method stub
	}

	@Override
	public UloopMTracker recv_mtracker_message() {
		UloopMessage ulm = recv_message();
		UloopMTrackerMessage ulmtrackm = null;

		if (ulm != null){
			ulmtrackm = new UloopMTrackerMessage();
			ulmtrackm.decode(ulm);
		}

		return ulmtrackm;
		// TODO Auto-generated method stub
	}

	@Override
	public void send_service_update(UloopService ulservice) {
		// TODO Auto-generated method stub
		UloopMessage.Builder ulmb = ulservice.encode();
		UloopMessage ulm = ulmb.build();
		send_data(ulm);
	}

	@Override
	public UloopService recv_service_update() {
		// TODO Auto-generated method stub
		UloopMessage ulm = recv_message();
		UloopServiceUpdate usu = null;

		if (ulm != null){
			usu = new UloopServiceUpdate();
			usu.decode(ulm);
		}

		return usu;
	}

}
