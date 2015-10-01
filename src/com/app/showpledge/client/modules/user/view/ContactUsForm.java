package com.app.showpledge.client.modules.user.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ContactUsForm extends Composite implements ClickHandler {

	private static ContactUsFormUiBinder uiBinder = GWT.create(ContactUsFormUiBinder.class);

	interface ContactUsFormUiBinder extends UiBinder<Widget, ContactUsForm> {
	}

	public ContactUsForm() {
		initWidget(uiBinder.createAndBindUi(this));
		submitBtn.addClickHandler(this);
	}
	
	public interface Context {
		public void onClick(ClickEvent inEvent);
	}
	
	private Context myContext;


	@UiField
	TextArea text = null;

	@UiField
	Button submitBtn = null;

	public void setFocus() {
		text.setFocus(true);		
	}
	
	public TextArea getText() {
		return text;
	}

	public Button getSubmitBtn() {
		return submitBtn;
	}
	

	public Context getContext() {
		return myContext;
	}

	public void setContext(Context myContext) {
		this.myContext = myContext;
	}

	@Override
	public void onClick(ClickEvent event) {
		myContext.onClick(event);
	}
	
	
}
