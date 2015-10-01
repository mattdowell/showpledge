package com.app.showpledge.client.util.widget;

import java.util.HashSet;
import java.util.Set;

import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * 
 * @author mjdowell
 * 
 */
public class ClickableImageCell extends ImageCell {

	@Override
	public Set<String> getConsumedEvents() {
		Set<String> consumedEvents = new HashSet<String>();
		consumedEvents.add("dblclick");
		consumedEvents.add("click");
		return consumedEvents;
	}

	/**
	 * // Todo, paramaterize this so it can be used for other things
	 */
	@Override
	public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {

		Show inShow = (Show) context.getKey();

		switch (DOM.eventGetType((Event) event)) {

		case Event.ONDBLCLICK:
			handleAction(inShow);
			break;
		case Event.ONCLICK:
			handleAction(inShow);
			break;
		default:
			break;
		}
	}

	private void handleAction(Show inShow) {
		ShowDetailsController c = new ShowDetailsController(inShow);
		c.display();
	}
}
