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
import eu.uloop.messages.UloopMessages.ServiceRequest;
import eu.uloop.messages.UloopMessages.UloopMessage;
import eu.uloop.messages.UloopMessages.UloopMessageType;

public class UloopServiceRequest extends UloopService implements UloopAbstractMessage{

	private CryptoID cryptoid;
	private byte []username;
	private byte []password;
	private double tokens;
	private byte []promise_of_payment;

	public UloopServiceRequest()
	{
		this.cryptoid = null;
	}

	public UloopServiceRequest(CryptoID cryptoid)
	{
		this.cryptoid = cryptoid;
	}

	public void setUsername(byte []username)
	{
		this.username = username;
	}

	public void setPassword(byte []password)
	{
		this.password = password;
	}

	public void setPromiseOfPayment(byte []promise_of_payment)
	{
		this.promise_of_payment = promise_of_payment;
	}

	public void setTokens(double tokens)
	{
		this.tokens = tokens;
	}

	public void setCryptoID(CryptoID cryptoid)
	{
		this.cryptoid = cryptoid;
	}

	@Override
	public UloopAbstractMessage decode(UloopMessage ulm) throws RuntimeException{
		ServiceMessage srvm = ulm.getServm();
		ServiceRequest servrep = srvm.getServreq();
		
		setCryptoID(new CryptoID(servrep.getCryptoid()));
		setUsername(servrep.getUsername().toByteArray());
		setPassword(servrep.getPassword().toByteArray());
		setTokens(servrep.getTokens());
		
		setPromiseOfPayment(servrep.getPromiseOfPayment().toByteArray());
		
		return this;
	}

	@Override
	public UloopMessage.Builder encode() {
		UloopMessage.Builder ulmb = UloopMessage.newBuilder();
		ulmb.setUlt(UloopMessageType.ULOOP_EXTERNAL_SERVICE);
		ServiceMessage.Builder smb = ServiceMessage.newBuilder();
		ServiceRequest.Builder srb = ServiceRequest.newBuilder();
		
		if (this.cryptoid != null)
			srb.setCryptoid(ByteString.copyFrom(cryptoid.getBytes()));
		else
			srb.setCryptoid(ByteString.EMPTY);
		
		if (this.username != null)
			srb.setUsername(ByteString.copyFrom(this.username));
		else
			srb.setUsername(ByteString.EMPTY);

		if (this.password != null)
			srb.setPassword(ByteString.copyFrom(this.password));
		else
			srb.setPassword(ByteString.EMPTY);

		srb.setTokens(this.tokens);


		if(this.promise_of_payment != null && this.promise_of_payment.length > 0)
			srb.setPromiseOfPayment(ByteString.copyFrom(this.promise_of_payment));
		else
			srb.setPromiseOfPayment(ByteString.EMPTY);
		
		smb.setServreq(srb);
		ulmb.setServm(smb.build());

		return ulmb;
	}
	
	public CryptoID getCryptoid()
	{
		return this.cryptoid;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}
}
