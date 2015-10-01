package com.app.showpledge.client.modules.user.controller;

import java.util.ArrayList;
import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.admin.controller.AllUsersController;
import com.app.showpledge.client.modules.user.view.EditUserForm;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.widgetideas.client.SliderBar.LabelFormatter;

/**
 * 
 * @author mjdowell
 * 
 */
public class EditUserController {

	public User myUser = null;
	EditUserForm myForm = new EditUserForm();
	String myTopLabel = null;
	final UserServiceAsync service = GWT.create(UserService.class);
	int captchaVal = 0;

	public EditUserController(String myTopLabel) {
		this(null, myTopLabel);
	}

	public EditUserController(final User myUser, String inLabel) {
		this.myUser = myUser;
		myTopLabel = inLabel;
	}

	public void startEditUser() {
		myForm.buildUserForm(buildContext());
		ContentContainer.setContent(myForm);
	}

	EditUserForm.Context buildContext() {
		return new EditUserForm.Context() {

			@Override
			public boolean shouldShowAdvancedFeatures() {
				return true;
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
				return true;
			}

			@Override
			public boolean shouldShowDupePwdBox() {
				return true;
			}

			@Override
			public void generateCaptchaValue() {
			}

			@Override
			public boolean isEmailEditable() {
				return true;
			}

			@Override
			public String getTitle() {
				return myTopLabel;
			}

			@Override
			public void onDeleteClick(ClickEvent e) {
				deleteUser();
			}
		};
	}
	
	/**
	 * This will only work if the current logged in user is an Admin
	 */
	void deleteUser() {
		service.delete(myUser, new Callback<Void>() {

			@Override
			public void onSuccess(Void result) {
				AllUsersController c = new AllUsersController("User sucessfully deleted");
				c.begin();
			}});
	}

	void updateUserVariables() {

		if (myForm.getAdmin() != null) {
			myUser.setAdmin(myForm.getAdmin().getValue());
		}

		if (myForm.getPublisher() != null) {
			myUser.setPublisher(myForm.getPublisher().getValue());
		}

		if (myForm.getVerified() != null) {
			myUser.setVerified(myForm.getVerified().getValue());
		}

		if (myForm.getEmail() != null) {
			myUser.setEmail(myForm.getEmail().getValue());
		}
		myUser.setPassword(myForm.getPassword().getValue());
		myUser.setFirstName(myForm.getFirst().getValue());
		myUser.setLastName(myForm.getLast().getValue());

//		if (myForm.getScreenName() != null) {
//			myUser.setScreenName(myForm.getScreenName().getValue());
//		}

	}

	List<String> validate() {
		List<String> vals = new ArrayList<String>();
		// Check Password & length
		if (myForm.getPassword().getText() == null || myForm.getPassword().getText().length() < 4) {
			vals.add("Password is required and must be at least 4 characters long.");
		}
		
		if (! myForm.getPassword().getText().equals(myForm.getPassword2().getText())) {
			vals.add("Both passwords must match");
		}
		
		if (myForm.getEmail() != null) {
			// Check email validity
			if (myForm.getEmail().getText() == null || (myForm.getEmail().getText().indexOf("@") < 0)
					|| (myForm.getEmail().getText().indexOf(".") < 0)) {
				vals.add("Email is required and must be at least valid.");
			}
		}

		return vals;
	}

}
