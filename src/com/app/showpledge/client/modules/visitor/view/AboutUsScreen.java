package com.app.showpledge.client.modules.visitor.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AboutUsScreen extends Composite {

	private static AboutUsScreenUiBinder uiBinder = GWT.create(AboutUsScreenUiBinder.class);

	interface AboutUsScreenUiBinder extends UiBinder<Widget, AboutUsScreen> {
	}

	public AboutUsScreen() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
