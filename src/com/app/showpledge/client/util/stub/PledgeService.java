package com.app.showpledge.client.util.stub;

import java.util.List;

import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("pledge")
public interface PledgeService extends RemoteService {
	
	public Long savePledge(Pledge inPledge);
	
	public void update(Pledge inPledge, double inOldPledgeAmt);
	
	public void delete(Pledge inPledge);
	
	public List<Pledge> getAllPledges();
	
	public List<Pledge> getPledgesForCurrentUser();
	
	public Pledge getPledgeForShow(Show inShow);

	public List<Pledge> getPledges(int offset, int limit);

}
