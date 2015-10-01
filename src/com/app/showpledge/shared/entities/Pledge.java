package com.app.showpledge.shared.entities;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.app.showpledge.shared.entities.iface.EntityIface;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

@Entity
@Unindexed
public class Pledge implements EntityIface {

	private static final long serialVersionUID = 2018172450877634166L;
	
	public enum Amount {
		
		Zero(0, 0.0, "$0"),
		One(1, 1.0, "$1"),
		Two(2, 2.0, "$2"),
		Three(3, 3.0, "$3"),
		Four(4, 4.0, "$4"),
		Five(5, 5.0, "$5"),
		Six(6, 6.0, "$6"),
		Seven(7, 7.0, "$7"),
		Eight(8, 8.0, "$8"),
		Nine(9, 9.0, "$9");
		
		private int index;
		private double amount;
		private String display;
		
		private Amount(int inD, double amt, String inDisp) {
			index = inD;
			amount = amt;
			display = inDisp;
		}

		public int getIndex() {
			return index;
		}

		public double getAmount() {
			return amount;
		}

		public String getDisplay() {
			return display;
		}
		
		public static int getIndexForAmt(double inAmt) {
			for (Amount amt : Amount.values()) {
				if (inAmt == amt.getAmount()) {
					return amt.getIndex();
				}
			}
			return 0;
		}
		
	}

	@Id
	public Long id;
	private double pledgeAmount;
	
	@Indexed
	private Key<User> userKey;
	
	@Indexed
	private Key<Show> showKey;
	
	private Date date = new Date();

	@Transient
	private Show show;

	@Transient
	private User user;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public double getPledgeAmount() {
		return pledgeAmount;
	}

	public void setPledgeAmount(double pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
	}

	public Key<User> getUserKey() {
		return userKey;
	}

	public void setUserKey(Key<User> user) {
		this.userKey = user;
	}

	public Key<Show> getShowKey() {
		return showKey;
	}

	public void setShowKey(Key<Show> show) {
		this.showKey = show;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
