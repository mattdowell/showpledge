package com.app.showpledge.client.modules.user.controller;

import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.view.EditUserForm;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.widgetideas.client.SliderBar.LabelFormatter;

public class UserSettingsController extends EditUserController {

	public UserSettingsController(String myTopLabel) {
		super(myTopLabel);
	}
	
	public void begin() {
		Callback<User> callback = new Callback<User>(){
			
			public void onSuccess(User result) {
				myUser = result;
				startEditUser();
			}
		};
		service.getLoggedInUser(callback);
	}

	EditUserForm.Context buildContext() {
		return new EditUserForm.Context() {

			@Override
			public boolean shouldShowAdvancedFeatures() {
				return false;
			}

			@Override
			public void onSubmitClick(ClickEvent inEvent) {
				// Persist to the user service
				updateUserVariables();

				Callback<Void> callback = new Callback<Void>() {

					@Override
					public void onSuccess(Void result) {
						ContentContainer.showInfo("User changes Saved");
					}
				};

				List<String> errs = validate();
				if (errs.size() == 0) {
					service.update(myUser, callback);
				} else {
					ContentContainer.showErrors(errs);
				}				
			}


			@Override
			public User getUser() {
				return myUser;
			}

			@Override
			public void onValueChange(String event) {

			}

			@Override
			public int getSliderCeiling() {
				return 0;
			}

			@Override
			public LabelFormatter getSliderFormatter() {
				return null;
			}

			@Override
			public boolean shouldShowSlider() {
				return false;
			}

			@Override
			public boolean shouldShowAccountSwitches() {
				return false;
			}

			@Override
			public boolean shouldShowDupePwdBox() {
				return true;
			}

			@Override
			public boolean isEmailEditable() {
				return false;
			}

			@Override
			public String getTitle() {
				return myTopLabel;
			}

			@Override
			public void onDeleteClick(ClickEvent e) {
			}

			@Override
			public void generateCaptchaValue() {
			}
		};
	}	
}
