package com.app.showpledge.client.modules.user.controller;

import com.app.showpledge.client.ShowPledge;
import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.view.ContactUsForm;
import com.app.showpledge.client.modules.visitor.controller.PromoShowsController;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.shared.composite.Feedback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * 
 * @author mjdowell
 *
 */
public class ContactUsController {

	final UserServiceAsync service = GWT.create(UserService.class);
	ContactUsForm form = null;
	
	public void begin() {
		if (ShowPledge.isUserLoggedIn) {
			
			form = new ContactUsForm();
			form.setContext(new ContactUsForm.Context() {
				
				@Override
				public void onClick(ClickEvent inEvent) {
					handleSubmition();
				}
			});
			
			ContentContainer.setContent(form);
			form.setFocus();
			
		} else {
			ContentContainer.showError("Sorry, you must login or create an account first");
		}
	}
	
	private void handleSubmition() {
		if (form.getText().getText() == null || form.getText().getText().length() < 5) {
			ContentContainer.showError("Message text is required");
		} else {
			Feedback feed = new Feedback();
			feed.setText(form.getText().getText());
			
			service.submitFeedback(feed, new Callback<Void>(){

				@Override
				public void onSuccess(Void result) {
					PromoShowsController p = new PromoShowsController();
					p.show("Thanks for your feedback");
				}});
		}
	}
}
