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
import eu.uloop.messages.UloopMessages.ServiceUpdate;
import eu.uloop.messages.UloopMessages.UloopMessage;
import eu.uloop.messages.UloopMessages.UloopMessageType;
import eu.uloop.messages.UloopMessages.UloopMessage.Builder;

public class UloopServiceUpdate extends UloopService {

	private CryptoID cryptoid;

	public void setCryptoID(CryptoID cid)
	{
		this.cryptoid = cid;
	}

	@Override
	public UloopAbstractMessage decode(UloopMessage ulm)
			throws RuntimeException {
		// TODO Auto-generated method stub
		ServiceMessage srvm = ulm.getServm();
		ServiceUpdate servup = srvm.getServup();
		if(servup.hasCryptoid())
			setCryptoID(new CryptoID(servup.getCryptoid()));

		return this;
	}

	@Override
	public Builder encode() {
		// TODO Auto-generated method stub
		UloopMessage.Builder ulmb = UloopMessage.newBuilder();
		ulmb.setUlt(UloopMessageType.ULOOP_EXTERNAL_SERVICE);
		ServiceMessage.Builder smb = ServiceMessage.newBuilder();
		ServiceUpdate.Builder servup = ServiceUpdate.newBuilder();

		if(cryptoid != null)
			servup.setCryptoid(ByteString.copyFrom(cryptoid.getBytes()));
		else
			servup.setCryptoid(ByteString.EMPTY);

		smb.setServup(servup);
		ulmb.setServm(smb.build());

		return ulmb;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

}
