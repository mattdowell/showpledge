package com.app.showpledge.client.modules.visitor.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Popup window with info regarding data useage
 * 
 * @author mjdowell
 *
 */
public class HowWeUseYourInfoScreen extends PopupPanel {

	private static HowWeUseYourInfoScreenUiBinder uiBinder = GWT.create(HowWeUseYourInfoScreenUiBinder.class);

	interface HowWeUseYourInfoScreenUiBinder extends UiBinder<Widget, HowWeUseYourInfoScreen> {
	}

	public HowWeUseYourInfoScreen() {
		// The popup's constructor's argument is a boolean specifying that it
		// auto-close itself when the user clicks outside of it.
		super(true);
		add(uiBinder.createAndBindUi(this));
	}

}
