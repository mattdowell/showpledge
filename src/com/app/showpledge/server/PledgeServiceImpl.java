package com.app.showpledge.server;

import java.util.List;

import com.app.showpledge.client.util.stub.PledgeService;
import com.app.showpledge.server.persistence.dao.PledgeDao;
import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.User;

public class PledgeServiceImpl extends AbstractRemoteServlet implements PledgeService {

	private static final long serialVersionUID = 7838428364389747577L;
	
	private static final PledgeDao PLEDGEDAO = new PledgeDao();

	@Override
	public Long savePledge(Pledge inPledge) {
		performLoginCheck();
		User currentUser = super.getCurrentUser();
		PLEDGEDAO.savePledge(inPledge, currentUser);
		return inPledge.getId();
	}

	@Override
	public List<Pledge> getAllPledges() {
		List<Pledge> theReturn =  PLEDGEDAO.getAll();
		return theReturn;
	}

	/**
	 * We need to know what the old value was, and remove that value from the
	 * show. Then we can re-add the new value.
	 */
	@Override
	public void update(Pledge inPledge, double inOldPledgeAmt ) {
		PLEDGEDAO.updatePledge(inPledge, inOldPledgeAmt);
	}

	@Override
	public List<Pledge> getPledgesForCurrentUser() {
		performLoginCheck();
		User currentUser = super.getCurrentUser();
		return PLEDGEDAO.getPledgesForUser(currentUser);
	}

	@Override
	public void delete(Pledge inPledge) {
		PLEDGEDAO.deletePledge(inPledge);
	}

	@Override
	public Pledge getPledgeForShow(Show inShow) {
		Pledge p = PLEDGEDAO.getPledgeForShowAndUser(inShow, getCurrentUser());
		return p;
	}

	@Override
	public List<Pledge> getPledges(int offset, int limit) {
		return PLEDGEDAO.getPledges(offset, limit);
	}

}
