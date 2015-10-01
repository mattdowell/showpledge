package com.app.showpledge.client.modules.user.view;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.ImageUtil;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.Show.ShowStatus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Create and edit (Nominate) shows
 * 
 * @author mjdowell
 * 
 */
public class EditShowForm extends Composite {

	private static EditShowScreenUiBinder uiBinder = GWT.create(EditShowScreenUiBinder.class);

	interface EditShowScreenUiBinder extends UiBinder<Widget, EditShowForm> {
	}

	public EditShowForm() {
		initWidget(uiBinder.createAndBindUi(this));

		for (ShowStatus showStat : Show.ShowStatus.values()) {
			showStatus.addItem(showStat.name());
		}

		for (Show.SystemStatus sysStat : Show.SystemStatus.values()) {
			systemStatus.addItem(sysStat.name());
		}
	}

	public interface Context {
		void onClick(ClickEvent event);

		Show getShow();

		Long getShowId();

		boolean isReadOnly();

		ShowServiceAsync getShowService();

		IUploader.OnFinishUploaderHandler getFinishUploadHandler();

		boolean shouldShowAdminControls();

		boolean shouldShowImageControls();

		String getTitle();

		String getButtonText();
	}

	@UiField
	TextBox showName;
	@UiField
	ListBox showStatus;
	@UiField
	TextBox corporateUrl;
	@UiField
	TextBox imdbUrl;
	@UiField
	TextArea description = null;
	@UiField
	TextArea producers = null;
	@UiField
	TextBox airDate;
	@UiField
	Button submitBtn = null;
	@UiField
	Label title;

	// ////// Admin Controls
	@UiField
	ListBox systemStatus = null;
	@UiField
	CheckBox promoted = null;
	@UiField
	Grid adminGrid;
	// //////////////////////////

	// ////// Image Controls
	@UiField
	Grid imageGrid;

	@UiField
	Image showImage;

	@UiField
	Label imageLabel;
	// //////////////////////////

	Context myContext;

	// https://groups.google.com/forum/#!topic/gwtupload/w8IhAerhs5k

	@UiField
	MultiUploader uploader = null;

	/**
	 * 
	 */
	public void begin(Context inContext) {
		myContext = inContext;
		if (myContext.getShow() != null) {
			buildShowForm(myContext.getShow());
		} else if (myContext.getShowId() != null) {
			Callback<Show> callback = new Callback<Show>() {
				@Override
				public void onSuccess(Show result) {
					buildShowForm(result);
				}
			};
			myContext.getShowService().get(myContext.getShowId(), callback);
		} else {
			ContentContainer.showError("No show, or show id specified");
		}

		// Set the image
		uploader.fileUrl();

		// Add a finish handler which will load the image once the upload finishes
		uploader.addOnFinishUploadHandler(myContext.getFinishUploadHandler());
		
	}

	public void setFocus() {
		showName.setFocus(true);		
	}
	
	/**
	 * Build and populate the form
	 * 
	 * @param inShow
	 */
	private void buildShowForm(Show inShow) {

		showName.setText(inShow.getName());
		imdbUrl.setText(inShow.getImdbUrl());
		corporateUrl.setText(inShow.getCorporateUrl());
		description.setText(inShow.getDescription());
		title.setText(myContext.getTitle());
		submitBtn.setText(myContext.getButtonText());
		promoted.setValue(inShow.isPromoted());
		producers.setText(inShow.getProducers());

		if (inShow.getPrimaryImageUrl() != null) {
			setImage(inShow.getPrimaryImageUrl());
		}

		if (inShow.getShowStatus() != null) {
			showStatus.setItemSelected(inShow.getShowStatus().getIndex(), true);
		}
		if (inShow.getSystemStatus() != null) {
			systemStatus.setItemSelected(inShow.getSystemStatus().getIndex(), true);
		}

		if (myContext.isReadOnly()) {
			submitBtn.setVisible(false);
			submitBtn.setEnabled(false);
		} else {
			submitBtn.setVisible(true);
			submitBtn.setEnabled(true);
		}

		if (myContext.shouldShowImageControls()) {
			uploader.setEnabled(true);
			uploader.setVisible(true);
			showImage.setVisible(true);
			imageLabel.setVisible(true);
			imageGrid.setVisible(true);
		} else {
			uploader.setEnabled(false);
			uploader.setVisible(false);
			showImage.setVisible(false);
			imageLabel.setVisible(false);
			imageGrid.setVisible(false);
		}

		if (myContext.shouldShowAdminControls()) {
			systemStatus.setEnabled(true);
			systemStatus.setVisible(true);
			promoted.setEnabled(true);
			promoted.setVisible(true);
			adminGrid.setVisible(true);
		} else {
			systemStatus.setEnabled(false);
			systemStatus.setVisible(false);
			promoted.setEnabled(false);
			promoted.setVisible(false);
			adminGrid.setVisible(false);
		}
	}

	public void setImage(Image in) {
		showImage = in;
	}

	public void setImage(String inUrl) {
		showImage.setUrl(ImageUtil.makeMed(inUrl));
	}

	public Image getShowImage() {
		return showImage;
	}

	public MultiUploader getUploader() {
		return uploader;
	}

	public void setUploader(MultiUploader uploader) {
		this.uploader = uploader;
	}

	public TextBox getShowName() {
		return showName;
	}

	public ListBox getShowStatus() {
		return showStatus;
	}

	public TextBox getCorporateUrl() {
		return corporateUrl;
	}

	public TextBox getImdbUrl() {
		return imdbUrl;
	}

	public TextArea getDescription() {
		return description;
	}

	public TextBox getAirDate() {
		return airDate;
	}

	public Label getImageLabel() {
		return imageLabel;
	}

	public ListBox getSystemStatus() {
		return systemStatus;
	}

	public CheckBox getPromoted() {
		return promoted;
	}

	public TextArea getProducers() {
		return producers;
	}

	@UiHandler("submitBtn")
	void onClick(ClickEvent e) {
		myContext.onClick(e);
	}

}
