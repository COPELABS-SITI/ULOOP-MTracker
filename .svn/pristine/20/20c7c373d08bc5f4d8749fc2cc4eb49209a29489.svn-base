/*
 *  Copyright (C) 2013 Caixa Magica Software.
 *
 *  Authors:
 *
 *      Nuno Martins <nuno.martins@caixamagica.pt>
 *
 */

package lib.api;

import com.google.protobuf.ByteString;

import eu.uloop.messages.UloopMessages.ServiceMessage;
import eu.uloop.messages.UloopMessages.ServiceReply;
import eu.uloop.messages.UloopMessages.UloopMessage;
import eu.uloop.messages.UloopMessages.UloopMessage.Builder;
import eu.uloop.messages.UloopMessages.UloopMessageType;

public class UloopServiceReply extends UloopService {

	private boolean authorization;
	private double trust;
	private CryptoID cryptoid;
	
	public UloopServiceReply()
	{
		
	}
	
	public UloopServiceReply(boolean authorization, double trust)
	{
		this.authorization = authorization;
		this.trust = trust;
	}
	
	public void setAuthorization(boolean authorization)
	{
		this.authorization = authorization;
	}
	
	public void setCryptoID(CryptoID cid)
	{
		this.cryptoid = cid;
	}
	
	public void setTrust(double trust)
	{
		this.trust = trust;
	}
	
	public UloopAbstractMessage decode(UloopMessage ulm) throws RuntimeException {
		ServiceMessage srvm = ulm.getServm();
		ServiceReply servrep = srvm.getServrep();
		if(servrep.hasCryptoid())
			setCryptoID(new CryptoID(servrep.getCryptoid()));
		if(servrep.hasAuthorization())
			setAuthorization(servrep.getAuthorization());
		if(servrep.hasTrust())
			setTrust(servrep.getTrust());
		
		return this;
	}

	@Override
	public Builder encode() {
		UloopMessage.Builder ulmb = UloopMessage.newBuilder();
		ulmb.setUlt(UloopMessageType.ULOOP_EXTERNAL_SERVICE);
		ServiceMessage.Builder smb = ServiceMessage.newBuilder();
		ServiceReply.Builder srb = ServiceReply.newBuilder();
		srb.setAuthorization(this.authorization);
		srb.setTrust(this.trust);
		if(cryptoid != null)
		srb.setCryptoid(ByteString.copyFrom(cryptoid.getBytes()));
		else
			srb.setCryptoid(ByteString.EMPTY);
		
		smb.setServrep(srb);
		ulmb.setServm(smb.build());
		
		return ulmb;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

}