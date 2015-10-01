package com.app.showpledge.server.persistence.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
/**
 * 
 * @author mjdowell
 * 
 */
public class PledgeDao extends ObjectifyGenericDao<Pledge> {

	private static final Logger LOG = Logger.getLogger(PledgeDao.class);
	
	/**
	 * 
	 */
	public List<Pledge> getAll() {
		List<Pledge> pledges = Obj.begin().query(Pledge.class).list();

		for (Pledge p : pledges) {
			this.inflate(p);
		}
		
		int count = Obj.begin().query(Pledge.class).count();
		
		if (count != pledges.size()) {
			LOG.error("The PLEDGE counts do not match: " + count + " != " + pledges.size());
		}
		return pledges;
	}

	/**
	 * For lazy searching
	 * 
	 * @param inOffset
	 * @param inLimit
	 * @return
	 */
	public List<Pledge> getPledges(int inOffset, int inLimit) {
		Objectify obj = Obj.begin();
		List<Pledge> pledges = obj.query(Pledge.class).order("id").offset(inOffset).limit(inLimit).list();

		for (Pledge p : pledges) {
			this.inflate(p);
		}
		return pledges;
	}

	/**
	 * This kind of constructor is required
	 */
	public PledgeDao() {
		super(Pledge.class);
	}

	/**
	 * Finds a pledge, if it exists, for the given user and show
	 * 
	 * @param inShow
	 * @param inUser
	 * @return Pledge or null
	 */
	public Pledge getPledgeForShowAndUser(Show inShow, User inUser) {
		Objectify obj = Obj.begin();
		Key<User> userKey = new Key<User>(User.class, inUser.getId());
		Key<Show> showKey = new Key<Show>(Show.class, inShow.getId());
		Pledge p = obj.query(Pledge.class).filter("userKey", userKey).filter("showKey", showKey).get();
		if (p != null) {
			populatePledge(p, inUser);
		}
		return p;
	}

	/**
	 * Updates a pledge along with updating the total pledges for a show
	 * 
	 * @param inPledge
	 * @param inOldPledgeAmt
	 */
	public void updatePledge(final Pledge inPledge, final double inOldPledgeAmt) {

		// Create a transaction!
		TransactionalDao.repeatInTransaction(new TransactionalDao.Transactable() {

			@Override
			public void run(TransactionalDao daot) {
				// Make it negative
				double oldAmt = inOldPledgeAmt * -1;

				// Remove the old amt.
				updateShowsPledgeTotal(inPledge.getShow(), oldAmt);

				// Add the new amt
				updateShowsPledgeTotal(inPledge.getShow(), inPledge.getPledgeAmount());

				// Persist
				put(inPledge);
			}
		});
	}

	/**
	 * 
	 * @param inShow
	 * @param inAmt
	 *            The amount to update the total
	 */
	public void updateShowsPledgeTotal(Show inShow, double inAmt) {
		// Update the show's pledge total
		Show show = Obj.begin().get(Show.class, inShow.getId());
		double pledgeTotal = show.getPledgeTotal();
		pledgeTotal += inAmt;
		show.setPledgeTotal(pledgeTotal);

		// TODO: FIX
		Obj.begin().put(show);
	}

	/**
	 * 
	 * @param inPledge
	 * @param user
	 */
	public void savePledge(final Pledge inPledge, final User user) {
		TransactionalDao.repeatInTransaction(new TransactionalDao.Transactable() {

			@Override
			public void run(TransactionalDao daot) {

				Key<User> userKey = new Key<User>(User.class, user.getId());
				Key<Show> showKey = new Key<Show>(Show.class, inPledge.getShow().getId());

				inPledge.setUserKey(userKey);
				inPledge.setShowKey(showKey);

				updateShowsPledgeTotal(inPledge.getShow(), inPledge.getPledgeAmount());

				put(inPledge);
			}
		});
	}

	/**
	 * 
	 * @param inPledge
	 * @param user
	 */
	public void deletePledge(final Pledge inPledge) {
		TransactionalDao.repeatInTransaction(new TransactionalDao.Transactable() {

			@Override
			public void run(TransactionalDao daot) {

				// Make it negative
				double amtToRemove = inPledge.getPledgeAmount() * -1;

				updateShowsPledgeTotal(inPledge.getShow(), amtToRemove);

				delete(inPledge);
			}
		});
	}

	/**
	 * Returns Pledges populated with Shows and Users
	 * 
	 * @param inUser
	 * @return
	 */
	public List<Pledge> getPledgesForUser(User inUser) {
		Objectify obj = Obj.begin();

		Key<User> userKey = new Key<User>(User.class, inUser.getId());
		List<Pledge> pledges = obj.query(Pledge.class).filter("userKey", userKey).list();

		// Set the show name
		for (Pledge p : pledges) {
			populatePledge(p, inUser);
		}

		return pledges;
	}

	/**
	 * Populates the values on the pledge
	 * 
	 * @param in
	 */
	private void populatePledge(Pledge inPledge, User inUser) {
		Objectify obj = Obj.begin();
		Show s = obj.find(inPledge.getShowKey());
		inPledge.setShow(s);
		inPledge.setUser(inUser);
	}

	/**
	 * Populate the pledge with the given user and pledge
	 * @param inPledge
	 */
	private void inflate(Pledge inPledge) {
		Objectify obj = Obj.begin();
		Show s = obj.find(inPledge.getShowKey());
		
		if (s == null) {
			s = new Show();
			LOG.error("No show found for key: " + inPledge.getShowKey());
		}
		User u = obj.find(inPledge.getUserKey());
		
		if (u == null) {
			u = new User();
			LOG.error("No user found for key: " + inPledge.getUserKey());
		}
		inPledge.setShow(s);
		inPledge.setUser(u);
	}

}
