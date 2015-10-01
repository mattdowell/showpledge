package com.app.showpledge.client.util.stub;

import java.util.List;

import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PledgeServiceAsync {

	void savePledge(Pledge inPledge, AsyncCallback<Long> callback);

	void getAllPledges(AsyncCallback<List<Pledge>> callback);

	void update(Pledge inPledge, double inOldPledgeAmt, AsyncCallback<Void> callback);

	void getPledgesForCurrentUser(AsyncCallback<List<Pledge>> callback);

	void delete(Pledge inPledge, AsyncCallback<Void> callback);

	void getPledgeForShow(Show inShow, AsyncCallback<Pledge> callback);

	void getPledges(int offset, int limit, AsyncCallback<List<Pledge>> callback);
	

}
