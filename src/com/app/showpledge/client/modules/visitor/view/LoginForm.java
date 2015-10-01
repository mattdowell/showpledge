package com.app.showpledge.client.modules.visitor.view;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main login form
 * 
 * @author mjdowell
 * 
 */
public class LoginForm extends Composite {

	private static LoginForm2UiBinder uiBinder = GWT.create(LoginForm2UiBinder.class);

	interface LoginForm2UiBinder extends UiBinder<Widget, LoginForm> {
	}

	public LoginForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface Context {
		UserServiceAsync getUserServiceAsync();

		String getTitleText();

		String getErrorText();

		void onSubmitClick(ClickEvent event);

		void onForgotPassword();

		void onQuickSignup();
	}

	@UiField Button loginBtn;
	@UiField TextBox email;
	@UiField PasswordTextBox password;

	private Context myContext;

	public void setContext(Context inCtx) {
		myContext = inCtx;
		ContentContainer.replaceContentOnly(this);
		email.setFocus(true);
	}


	@UiHandler("loginBtn")
	void onClick(ClickEvent event) {
		myContext.onSubmitClick(event);
	}

	@UiHandler("signUpLink")
	void onClickSignup(ClickEvent event) {
		myContext.onQuickSignup();
	}

	@UiHandler("forgotPasswordLink")
	void onClickForgot(ClickEvent event) {
		myContext.onForgotPassword();
	}

	public TextBox getEmail() {
		return email;
	}

	public PasswordTextBox getPassword() {
		return password;
	}

}
