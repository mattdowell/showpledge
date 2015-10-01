package com.app.showpledge.client.util.widget;

import com.app.showpledge.client.modules.visitor.controller.PromoShowsController;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author mjdowell
 * 
 */
public class WidgetFactory {

	public static final int SMALL_DESC_LENGTH = 80;
	public static final int MED_DESC_LENGTH = 200;

	public static final int MAX_SHOW_ROWS_PER_PAGE = 2;

	/**
	 * Builds a consistent title label
	 * 
	 * @param in
	 * @return
	 */
	public static Label getTitle(String in) {
		Label title = new Label(in);
		title.setWordWrap(false);
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		title.setStyleName("title");
		return title;
	}

	/**
	 * Adds a handler for "click to view show details"
	 * 
	 * @param inWidget
	 */
	public static void addViewShowEvent(final Widget inWidget, final Show inShow) {
		
		if (inWidget == null) return;
		
		inWidget.sinkEvents(Event.ONCLICK);
		inWidget.addHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				ShowDetailsController c = new ShowDetailsController(inShow);
				c.display();				
			}}, ClickEvent.getType());		
	}
	
	/**
	 * Adds a handler for "click to view show details"
	 * 
	 * @param inWidget
	 */
	public static void addViewPromoScreenEvent(final Widget inWidget) {
		inWidget.sinkEvents(Event.ONCLICK);
		inWidget.addHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				PromoShowsController c = new PromoShowsController();
				c.show();
			}}, ClickEvent.getType());		
	}	

	
	/**
	 * Create a small show summary widget with a pledge button
	 * 
	 * @param inShow
	 * @return custom widget
	 */
	public static Composite buildShowPromoWidget(final Show inShow) {
		return new SmallShowInfoWidget(inShow);
	}


	/**
	 * Trims text and adds "..." to the end if its there.
	 * 
	 * @param inText
	 *            Text to trim
	 * @param inLength
	 *            maximum length
	 * @return
	 */
	public static String trimLongText(String inText, int inLength) {
		if (inText == null) {
			return "";
		}

		if (inText.length() <= inLength) {
			return inText;
		}

		String theReturn = inText.substring(0, inLength);
		theReturn += "...";
		return theReturn;
	}


	/**
	 * Create a pledge button for the given show
	 * 
	 * @param show
	 * @return
	 */
	public static Button pledgeButton(final Show show) {
		Button pledgeBut = new Button();
		pledgeButton(show, pledgeBut);
		return pledgeBut;
	}

	public static void pledgeButton(final Show inShow, final Button inButton) {
		inButton.setText("Pledge");
		inButton.setStyleName("pledgeButton");
		inButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ShowDetailsController cont = new ShowDetailsController(inShow);
				cont.display();
			}

		});
	}

	/**
	 * View the show
	 * 
	 * @param inShow
	 * @return
	 */
	public static ClickHandler buildViewShowDetailsHandler(final Show inShow) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ShowDetailsController con = new ShowDetailsController(inShow);
				con.display();
			}
		};
	}

}
