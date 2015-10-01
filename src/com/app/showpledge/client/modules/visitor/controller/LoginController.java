package com.app.showpledge.client.modules.visitor.controller;

import com.app.showpledge.client.ShowPledge;
import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.controller.NewUserController;
import com.app.showpledge.client.modules.visitor.view.LoginForm;
import com.app.showpledge.client.modules.visitor.view.LoginForm.Context;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.shared.composite.LoginResults;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationProcessor;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.strings.StringLengthValidator;

/**
 * 
 * @author mjdowell
 * 
 */
public class LoginController {

	private final UserServiceAsync userService = GWT.create(UserService.class);
	private LoginForm myForm = new LoginForm();
	private String errorText;
	private ValidationProcessor validator = new DefaultValidationProcessor();


	public void begin() {
		myForm.setContext(buildContext());
	}

	private Context buildContext() {
		return new LoginForm.Context() {

			@Override
			public void onSubmitClick(ClickEvent event) {

				if (validate()) {
					handleUserSubmit(event);
				}
			}

			@Override
			public UserServiceAsync getUserServiceAsync() {
				return userService;
			}

			@Override
			public String getTitleText() {
				return "Login to Showpledge.com";
			}

			@Override
			public String getErrorText() {
				return errorText;
			}

			@Override
			public void onForgotPassword() {

				if (validateForgotPassword()) {
					handleForgotPassword();
				}
			}

			@Override
			public void onQuickSignup() {
				NewUserController cont = new NewUserController("Quick Sign Up");
				cont.startNewUser();
			}
		};
	}

	/**
	 * The user has clicked the Forgot Password link
	 */
	private void handleForgotPassword() {
		userService.userForgotPassowrd(myForm.getEmail().getText(), new Callback<Void>() {

			@Override
			public void onSuccess(Void result) {
				ContentContainer.showInfo("Your password has been sent to your email");
			}
		});
	}

	/**
	 * The user has clicked the login button
	 * 
	 * @param inEvent
	 */
	private void handleUserSubmit(ClickEvent inEvent) {
		ContentContainer.clearMsgs();
		String username = myForm.getEmail().getText();
		String password = myForm.getPassword().getText();

		userService.login(username, password, new Callback<LoginResults>() {

			@Override
			public void onSuccess(LoginResults inResult) {

				if (inResult.isSucess()) {
					ShowPledge.isUserLoggedIn = true;
					ShowPledge.myUser = inResult.getUser();
					ShowPledge.buildMenu(true);
					PromoShowsController c = new PromoShowsController();
					c.show("Welcome Back: " + inResult.getUser().getDisplayName());
				} else {
					if (inResult.getStatus().equals(LoginResults.Status.FAILED)) {
						errorText = "Incorrect username and/or password. Please try again.";
					} else if (inResult.getStatus().equals(LoginResults.Status.NOT_VERIFIED)) {
						errorText = "That account has not been verified yet. Please check your email inbox (or spam) for verification instructions.";
					} else if (inResult.getStatus().equals(LoginResults.Status.SUSPENDED)) {
						errorText = "That account has been suspended.";
					}
					ShowPledge.isUserLoggedIn = false;
					ContentContainer.showError(errorText);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ContentContainer.showError(errorText);
			}
		});
	}

	/**
	 * Validates the user has entered values in the email and pwd boxes
	 * 
	 * @return
	 */
	private boolean validate() {

		validator.addValidators("email",
				new StringLengthValidator(myForm.getEmail(), 4, 100).addActionForFailure(new StyleAction("validationFailedBorder")));
		// .addActionForFailure(new LabelTextAction(myForm.getEmailErrorLabel())));

		validator.addValidators("pwd",
				new StringLengthValidator(myForm.getPassword(), 2, 100).addActionForFailure(new StyleAction("validationFailedBorder")));

		return validator.validate(null);
	}

	private boolean validateForgotPassword() {

		validator.addValidators("email",
				new StringLengthValidator(myForm.getEmail(), 4, 100).addActionForFailure(new StyleAction("validationFailedBorder")));

		return validator.validate(null);
	}
}
