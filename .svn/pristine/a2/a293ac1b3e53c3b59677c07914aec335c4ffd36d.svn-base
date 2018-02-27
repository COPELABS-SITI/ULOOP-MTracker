/*
 *  Copyright (C) 2013 Caixa Magica Software.
 *
 *  Authors:
 *
 *      Nuno Martins <nuno.martins@caixamagica.pt>
 *
 */

package lib.api;

import eu.uloop.messages.UloopMessages.UloopMessage;

public interface UloopMessageAPI {

	void send_service_request(UloopService ulservice);
	UloopService recv_service_request();
	void send_service_reply(UloopService ulservice);
	UloopService recv_service_reply();

	void send_service_update(UloopService ulservice);
	UloopService recv_service_update();

	void send_mtracker_message(UloopMTracker ulmr);
	UloopMTracker recv_mtracker_message();
	
	public UloopMessage recv_message();
}
