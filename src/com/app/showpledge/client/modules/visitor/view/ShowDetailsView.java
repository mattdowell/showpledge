package com.app.showpledge.client.modules.visitor.view;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.util.ImageUtil;
import com.app.showpledge.shared.entities.Pledge;
import com.app.showpledge.shared.entities.Pledge.Amount;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Show full show details
 * 
 * @author mjdowell
 * 
 */
public class ShowDetailsView extends Composite {

	private static ShowDetailsUiBinder uiBinder = GWT.create(ShowDetailsUiBinder.class);

	interface ShowDetailsUiBinder extends UiBinder<Widget, ShowDetailsView> {
	}

	public ShowDetailsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface Context {
		Show getShow();

		void onSubmitPledge();
		
		public int getPreviousPledgeIndex();
	}

	private Context myContext;
	
	@UiField Anchor corpUrl;
	@UiField Anchor imdbUrl;
	@UiField Anchor showUrl;
	@UiField Label pledgeTotal;
	@UiField Label showName;
	@UiField Label showYear;
	@UiField Label producers;
	@UiField Label description;
	@UiField Image showImage;

	// Pledges
	@UiField ListBox pledgeAmount;
	
	@UiField Button pledgeBut;

	public final static String SHOW_URL_PREFIX = "http://www.showpledge.com?sid=";

	public void buildScreen(final Context inCtx) {
		myContext = inCtx;

		setImage(myContext.getShow().getPrimaryImageUrl());
		
		for (Amount amt : Pledge.Amount.values()) {
			pledgeAmount.addItem(amt.getDisplay(), String.valueOf(amt.getAmount()));
		}
		pledgeAmount.setItemSelected(myContext.getPreviousPledgeIndex(), true);
				

		showName.setText(myContext.getShow().getName());


		pledgeTotal.setText(String.valueOf(myContext.getShow().getPledgeTotal()));
		showName.setText(myContext.getShow().getName());
		showYear.setText(myContext.getShow().getShowStartDate());
		producers.setText(myContext.getShow().getProducers());
		description.setText(myContext.getShow().getDescription());

		corpUrl.setText(inCtx.getShow().getCorporateUrl());
		corpUrl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign("http://" + inCtx.getShow().getCorporateUrl());
			}
		});

		imdbUrl.setText(inCtx.getShow().getImdbUrl());
		imdbUrl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign("http://" + inCtx.getShow().getImdbUrl());
			}
		});

		final String showUrlLoc = SHOW_URL_PREFIX + inCtx.getShow().getId();
		showUrl.setText(showUrlLoc);
		showUrl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign(showUrlLoc);
			}
		});

		ContentContainer.setContent(this);
	}

	private void setImage(String inUrl) {
		showImage.setUrl(ImageUtil.makeMed(inUrl));
	}
	
	public ListBox getPledgeAmount() {
		return pledgeAmount;
	}
	
	@UiHandler("pledgeBut")
	public void onClick(ClickEvent in) {
		myContext.onSubmitPledge();
	}		

	
}
