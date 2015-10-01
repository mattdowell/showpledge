package com.app.showpledge.client.modules.visitor.controller;

import com.app.showpledge.client.ShowPledge;
import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.admin.controller.AllPledgesController;
import com.app.showpledge.client.modules.user.controller.DisplayPledgesController;
import com.app.showpledge.client.modules.visitor.view.ShowDetailsView;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.PledgeService;
import com.app.showpledge.client.util.stub.PledgeServiceAsync;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.core.client.GWT;

/**
 * This controller manages the displaying of a Show's details and we have now integrated it with the Manage Pledge / New Pledge use cases to
 * simplify the design
 * 
 * @author mjdowell
 * 
 */
public class ShowDetailsController {

	private Show myShow = null;
	final ShowServiceAsync service = GWT.create(ShowService.class);
	final PledgeServiceAsync pledgeService = GWT.create(PledgeService.class);
	private final ShowDetailsView screen = new ShowDetailsView();
	private double oldPledgeAmt = 0.0;
	Pledge myPledge = null;
	boolean isPledgeEdit = false;
	NextAction myNextAction = null;

	enum NextAction {
		SHOW_PLEDGES, PROMO_SHOWS, ADMIN_ALL_PLEDGES;
	}

	/**
	 * This constructor is used to display show details or create a pledge
	 * 
	 * @param inShow
	 */
	public ShowDetailsController(Show inShow) {
		myShow = inShow;
		myNextAction = NextAction.PROMO_SHOWS;
	}

	/**
	 * This constructor is used to delete a pledge
	 * 
	 * @param inPledge
	 */
	public ShowDetailsController(Pledge inPledge) {
		myPledge = inPledge;
		myShow = inPledge.getShow();
		myNextAction = NextAction.SHOW_PLEDGES;
	}

	public ShowDetailsController(Pledge inPledge, boolean isAdmin) {
		this(inPledge);
		myNextAction = NextAction.ADMIN_ALL_PLEDGES;
	}
	
	public void display() {

		if (ShowPledge.isUserLoggedIn) {
			// first check to see if that user has already pledged
			pledgeService.getPledgeForShow(myShow, new Callback<Pledge>() {
				@Override
				public void onSuccess(Pledge result) {
					if (result != null) {
						oldPledgeAmt = result.getPledgeAmount();
						myPledge = result;
						isPledgeEdit = true;
					}
					buildAndShow();
				}
			});
		} else {
			buildAndShow();
		}
	}

	private void buildAndShow() {
		screen.buildScreen(buildContext());
	}

	/**
	 * 
	 * @return
	 */
	private ShowDetailsView.Context buildContext() {
		return new ShowDetailsView.Context() {

			@Override
			public Show getShow() {
				return myShow;
			}

			@Override
			public void onSubmitPledge() {
				if (isPledgeEdit) {
					userSubmitsPledgeForEdit();
				} else {
					userSubmitsNewPledge();
				}
			}

			@Override
			public int getPreviousPledgeIndex() {
				return Pledge.Amount.getIndexForAmt(oldPledgeAmt);
			}
		};
	}

	/**
	 * Handle a pledge on this screen
	 */
	private void userSubmitsNewPledge() {

		if (!ShowPledge.isUserLoggedIn) {
			LoginController c = new LoginController();
			c.begin();
			ContentContainer.showInfo("You must log in first");
		} else {

			Pledge pledge = new Pledge();
			pledge.setShowKey(myShow.getKey());
			pledge.setShow(myShow);
			pledge.setPledgeAmount(getPledgeAmt().doubleValue());

			Callback<Long> callback = new Callback<Long>() {
				@Override
				public void onSuccess(Long result) {
					afterActionComplete("Pledge amount $" + getPledgeAmt() + " saved. Thank you!");
				}
			};

			pledgeService.savePledge(pledge, callback);
		}
	}

	private Double getPledgeAmt() {
		int selectedInded = screen.getPledgeAmount().getSelectedIndex();
		String amt = screen.getPledgeAmount().getValue(selectedInded);
		return new Double(amt);
	}

	public void deletePledge() {
		pledgeService.delete(myPledge, new Callback<Void>() {

			@Override
			public void onSuccess(Void result) {
				afterActionComplete("Your pledge has been removed");
			}
		});
	}

	public void userSubmitsPledgeForEdit() {

		Callback<Void> callback = new Callback<Void>() {
			@Override
			public void onSuccess(Void result) {
				afterActionComplete("Pledge updated to $" + getPledgeAmt() + ". Thank you!");
			}
		};

		// VALIDATE
		int selectedInded = screen.getPledgeAmount().getSelectedIndex();
		String amt = screen.getPledgeAmount().getValue(selectedInded);
		Double amtDbl = new Double(amt);

		myPledge.setPledgeAmount(amtDbl.doubleValue());
		pledgeService.update(myPledge, oldPledgeAmt, callback);
	}

	/**
	 * TODO: Determine the use case and move to the proper cotnroller
	 * 
	 * @param in
	 */
	private void afterActionComplete(String in) {

		if (myNextAction.equals(NextAction.PROMO_SHOWS)) {
			PromoShowsController c = new PromoShowsController();
			c.show(in);
		} else if (myNextAction.equals(NextAction.ADMIN_ALL_PLEDGES)) {
			AllPledgesController c = new AllPledgesController();
			c.display(in);
		}else {
			DisplayPledgesController c = new DisplayPledgesController();
			c.display(in);
		}
	}
}
