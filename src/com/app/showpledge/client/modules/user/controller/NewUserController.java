package com.app.showpledge.client.modules.user.controller;

import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.view.EditUserForm;
import com.app.showpledge.client.modules.visitor.controller.WelcomeController;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserService.UserError;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.widgetideas.client.SliderBar;
import com.google.gwt.widgetideas.client.SliderBar.LabelFormatter;

/**
 * Controls the creation of a new user
 * 
 * @author mjdowell
 * 
 */
public class NewUserController extends EditUserController {

	private static final int VERF_CEILING = 20;

	public NewUserController(User myUser, String inLabel) {
		super(myUser, inLabel);
	}

	public NewUserController(String inLabel) {
		super(inLabel);
		myUser = new User();
	}

	public void startNewUser() {
		myForm.buildUserForm(buildContext());
	}

	EditUserForm.Context buildContext() {
		return new EditUserForm.Context() {

			@Override
			public boolean shouldShowAdvancedFeatures() {
				return false;
			}

			@Override
			public void onSubmitClick(ClickEvent inEvent) {

				List<String> errs = validate();
				if (errs.size() == 0) {
					userSubmittedForm(inEvent);
				} else {
					ContentContainer.showErrors(errs);
				}
			}

			@Override
			public User getUser() {
				return myUser;
			}

			@Override
			public void onValueChange(String inEmail) {
				checkEmailAlreadyUsed();
			}

			@Override
			public int getSliderCeiling() {
				return VERF_CEILING;
			}

			@Override
			public LabelFormatter getSliderFormatter() {
				return buildFormatter();
			}

			@Override
			public boolean shouldShowSlider() {
				return true;
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
				return true;
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
				generateCaptchaVal();
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	private SliderBar.LabelFormatter buildFormatter() {
		class LabelFrmtImpl implements SliderBar.LabelFormatter {

			@Override
			public String formatLabel(SliderBar slider, double value) {
				try {
					String v = String.valueOf(value);

					if (v.indexOf(".") > 0) {
						v = v.substring(0, v.indexOf("."));
					}
					return v;
				} catch (Exception e) {
					ContentContainer.showError("Exception: " + e + " Val:" + value);
					return "ERR";
				}
			}

		}
		return new LabelFrmtImpl();
	}

	/**
	 * Go to the server and generate a captcha val
	 */
	public void generateCaptchaVal() {
		Callback<Integer> callback = new Callback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				captchaVal = result;
				myForm.setCaptchaNumber(captchaVal);
			}
		};
		service.getCaptchaNumber(VERF_CEILING, callback);
	}

	/**
	 * 
	 * @param event
	 */
	private void userSubmittedForm(ClickEvent event) {

		// Set up the callback object.
		Callback<Long> callback = new Callback<Long>() {

			public void onSuccess(Long result) {
				if (result != null && result.longValue() > 0) {

					// Show a window that says they will be getting an email and
					// they need to verify their address
					WelcomeController c = new WelcomeController();
					c.show();

				} else {
					UserError ue = UserService.UserError.getErrorFromNumber(Integer.parseInt(String.valueOf(result)));
					ContentContainer.showError("Account Creation FAILED. Error: " + ue.getText());
				}
			}
		};

		int capInt = 0;
		try {
			updateUserVariables();
			String cap = String.valueOf(myForm.getSlider().getCurrentValue());

			// If there is a 0.0
			if (cap.indexOf(".") > 0) {
				cap = cap.substring(0, cap.indexOf("."));
			}
			capInt = Integer.parseInt(cap);
			service.createUser(myUser, capInt, callback);
		} catch (NumberFormatException e) {
			ContentContainer.showError("Problem with the slider: " + e.toString() + " Slider Val: " + myForm.getSlider().getCurrentValue());
		}
	}

	void checkEmailAlreadyUsed() {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Boolean result) {
				if (result) {
					myForm.getEmailUsageLbl().setText("That email is already being used");
					myForm.getEmailUsageLbl().setStyleName("error_text");
					myForm.getEmailUsageLbl().setVisible(true);
				} else {
					myForm.getEmailUsageLbl().setText("OK");
					myForm.getEmailUsageLbl().setStyleName("info_text");
					myForm.getEmailUsageLbl().setVisible(true);
				}
			}
		};

		String emailAdd = myForm.getEmail().getText();
		service.doesEmailAlreadyExist(emailAdd, callback);
	}
}
