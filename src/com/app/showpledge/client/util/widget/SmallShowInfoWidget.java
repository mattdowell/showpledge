package com.app.showpledge.client.util.widget;

import com.app.showpledge.client.util.ImageUtil;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This is the widget that displays a show's information on the promo screen
 * 
 * @author mjdowell
 * 
 */
public class SmallShowInfoWidget extends Composite {

	SimplePanel outterBox = new SimplePanel();
	VerticalPanel showPanel = new VerticalPanel();
	VerticalPanel descriptionPanel = new VerticalPanel();
	HorizontalPanel pledgePanel = new HorizontalPanel();
	HorizontalPanel headerPanel = new HorizontalPanel();
	Label pledgeTotalLabel = new Label("Total");
	// Label producersLabel = new Label("Producers");
	Image thumbnail = null;
	Label pledgeTotal = null;
	HTMLPanel showDescription = null;
	// Label showProducers = null;
	Label showTitle = null;
	Label showDate = null;
	Show myShow = null;

	public SmallShowInfoWidget(Show inShow) {
		myShow = inShow;
		pledgeTotal = new Label("$" + String.valueOf(myShow.getPledgeTotal()));
		showDescription = new HTMLPanel(WidgetFactory.trimLongText(inShow.getDescription(), WidgetFactory.MED_DESC_LENGTH));
		// showProducers = new Label(WidgetFactory.trimLongText(inShow.getProducers(), WidgetFactory.SMALL_DESC_LENGTH));
		showTitle = new Label(WidgetFactory.trimLongText(myShow.getName(), 30));
		showDate = new Label(WidgetFactory.trimLongText(myShow.getShowStartDate(), 6));
		thumbnail = ImageUtil.getPrimaryImageThumb(myShow);
		setStyles();
		addHandlers();
		buildBox();
		initWidget(outterBox);

		// Give the overall composite a style name.
		setStyleName("promoShowWidget");
	}

	private void addHandlers() {
		WidgetFactory.addViewShowEvent(headerPanel, myShow);
		WidgetFactory.addViewShowEvent(showTitle, myShow);
		// addViewShowHandler(showProducers);
		WidgetFactory.addViewShowEvent(thumbnail, myShow);
		WidgetFactory.addViewShowEvent(showDescription, myShow);
		WidgetFactory.addViewShowEvent(showDate, myShow);
	}

	private void setStyles() {
		pledgeTotalLabel.setStyleName("pledgeTotalLabel");
		pledgeTotal.setStyleName("pledgeTotal");
		showDescription.setStyleName("showDescription");
		// producersLabel.setStyleName("producersLabel");

		if (thumbnail != null) {
			thumbnail.setStyleName("promo-thumbnail");
		}
		showTitle.setStyleName("showTitle");
		showDate.setStyleName("showDate");
		outterBox.setStyleName("promoShowOutterPanel");
		descriptionPanel.setStyleName("descriptionPanel");
		pledgePanel.setStyleName("pledgePanel");
		headerPanel.setStyleName("headerPanel");
		showPanel.setStyleName("showPanel");
	}

	/**
	 * Method that builds the widget
	 */
	private void buildBox() {
		pledgePanel.add(pledgeTotalLabel);
		pledgePanel.add(pledgeTotal);
		pledgePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		pledgePanel.add(WidgetFactory.pledgeButton(myShow));

		descriptionPanel.add(showDescription);
		// descriptionPanel.add(producersLabel);
		// descriptionPanel.add(showProducers);
		descriptionPanel.add(thumbnail);

		headerPanel.add(showTitle);
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		headerPanel.add(showDate);

		showPanel.add(headerPanel);
		showPanel.add(descriptionPanel);
		showPanel.add(pledgePanel);
		outterBox.add(showPanel);
	}

}
