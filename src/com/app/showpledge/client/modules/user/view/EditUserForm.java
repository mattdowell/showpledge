package com.app.showpledge.client.modules.user.view;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.view.HowWeUseYourInfoScreen;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

/**
 * Form used for editing or creating a user
 * 
 * @author mjdowell
 * 
 */
public class EditUserForm extends Composite {

	private static EditUserFormUiBinder uiBinder = GWT.create(EditUserFormUiBinder.class);

	interface EditUserFormUiBinder extends UiBinder<Widget, EditUserForm> {
	}

	public EditUserForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface Context {

		boolean shouldShowAdvancedFeatures();

		void onSubmitClick(ClickEvent inEvent);

		void onValueChange(String inEmail);

		User getUser();

		int getSliderCeiling();

		SliderBar.LabelFormatter getSliderFormatter();

		boolean shouldShowSlider();

		boolean shouldShowAccountSwitches();

		boolean shouldShowDupePwdBox();

		boolean isEmailEditable();

		void generateCaptchaValue();

		String getTitle();

		void onDeleteClick(ClickEvent e);
	}

	// Actions
	@UiField
	Button saveBtn;
	@UiField
	Button deleteBtn;

	Context myContext = null;
	
	HowWeUseYourInfoScreen popup = new HowWeUseYourInfoScreen();

	// Fields
	@UiField
	TextBox email;
	@UiField
	TextBox password;
	@UiField
	PasswordTextBox password2;
	@UiField
	TextBox first;
	@UiField
	TextBox last;
	@UiField
	CheckBox verified;
	@UiField
	CheckBox suspended;
	@UiField
	CheckBox admin;
	@UiField
	CheckBox publisher;
	@UiField
	Label title = new Label();
	@UiField
	Label emailUsageLbl = new Label();
	
	@UiField Anchor emailUsagePopup;

	// Grids
	@UiField
	Grid dupePasswordGrid;
	@UiField
	Grid sliderGrid;
	@UiField
	Grid accountGrid;

	// New accounts only
	@UiField
	SimplePanel sliderPanel;
	SliderBar slider = null;
	@UiField
	Label lblSlideToPosition = new Label("");
	private int captchaNumber = 0;

	/**
	 * 
	 */
	private void setUserValues() {
		email.setValue(myContext.getUser().getEmail());
		password.setValue(myContext.getUser().getPassword());
		password2.setValue(myContext.getUser().getPassword());
		first.setValue(myContext.getUser().getFirstName());
		last.setValue(myContext.getUser().getLastName());
		verified.setValue(myContext.getUser().isVerified());
		suspended.setValue(myContext.getUser().isLocked());
		admin.setValue(myContext.getUser().isAdmin());
		publisher.setValue(myContext.getUser().isPublisher());
	}

	/**
	 * 
	 * @param inContext
	 */
	public void buildUserForm(Context inContext) {
		myContext = inContext;

		title.setText(myContext.getTitle());

		// Check to see if the email has already been used
		email.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				myContext.onValueChange(event.toString());
			}
		});
		
		emailUsagePopup.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    popup.setGlassEnabled(true); 
			    // This is the only way to get the popup index to work. the CSS was not working
			    popup.getElement().getStyle().setZIndex(10);
			    popup.center(); 
			    popup.show(); 
			}});

		if (myContext.isEmailEditable()) {
			email.setReadOnly(false);
		} else {
			email.setReadOnly(true);
		}

		if (myContext.shouldShowDupePwdBox()) {
			dupePasswordGrid.setVisible(true);
		}

		if (myContext.shouldShowSlider()) {
			slider = new SliderBar(0, myContext.getSliderCeiling(), myContext.getSliderFormatter());
			slider.setStepSize(1);
			slider.setCurrentValue(0);
			slider.setNumTicks(myContext.getSliderCeiling());
			slider.setNumLabels(10);
			slider.setWidth("300px");
			sliderPanel.add(slider);

			myContext.generateCaptchaValue();
			sliderGrid.setVisible(true);
		}

		if (myContext.shouldShowAccountSwitches()) {
			accountGrid.setVisible(true);
			deleteBtn.setVisible(true);
			deleteBtn.setEnabled(true);
		}

		setUserValues();
		
		ContentContainer.setContent(this);
		
		email.setFocus(true);
	}

	@UiHandler("saveBtn")
	void onClickSave(ClickEvent e) {
		myContext.onSubmitClick(e);
	}

	@UiHandler("deleteBtn")
	void onClickDelete(ClickEvent e) {
		myContext.onDeleteClick(e);
	}

	public TextBox getPassword() {
		return password;
	}

	public TextBox getEmail() {
		return email;
	}

	public PasswordTextBox getPassword2() {
		return password2;
	}

	public TextBox getFirst() {
		return first;
	}

	public TextBox getLast() {
		return last;
	}

	public CheckBox getVerified() {
		return verified;
	}

	public CheckBox getSuspended() {
		return suspended;
	}

	public CheckBox getAdmin() {
		return admin;
	}

	public CheckBox getPublisher() {
		return publisher;
	}

	public Label getEmailUsageLbl() {
		return emailUsageLbl;
	}

	public int getCaptchaNumber() {
		return captchaNumber;
	}

	public void setCaptchaNumber(int captchaNumber) {
		this.captchaNumber = captchaNumber;
		lblSlideToPosition.setText("Slide to position: " + this.captchaNumber);
	}

	public Grid getSliderGrid() {
		return sliderGrid;
	}

	public SliderBar getSlider() {
		return slider;
	}

}
