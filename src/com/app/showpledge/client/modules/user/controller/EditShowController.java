package com.app.showpledge.client.modules.user.controller;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import java.util.ArrayList;
import java.util.List;

import com.app.showpledge.client.ShowPledge;
import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.view.EditShowForm;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.UrlUtil;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Manages the editing of an existing show.
 * 
 * @author mjdowell
 * 
 */
public class EditShowController {

	final ShowServiceAsync service = GWT.create(ShowService.class);
	// ManageShowForm myForm = null;
	EditShowForm myForm = new EditShowForm();
	Show myShow = null;
	OnLoadPreloadedImageHandler showImageHandler;
	IUploader.OnFinishUploaderHandler onFinishUploaderHandler;
	boolean shouldShowImageControl = true;
	boolean shouldShowAdminControl = false;
	String buttonText = "Save Updates";

	public EditShowController(Show inShow, boolean imageCntrl, boolean adminCntrl) {
		this(inShow);
		shouldShowImageControl = imageCntrl;
		shouldShowAdminControl = adminCntrl;		
	}
	
	public EditShowController(Show inShow) {
		myShow = inShow;
	}

	public EditShowController() {
		super();
	}

	/**
	 * Now we are editing the show
	 */
	public void begin() {
		// Place the show in the session
		service.putShowInSession(myShow, new Callback<Void>() {
			@Override
			public void onSuccess(Void result) {
				buildAndShowForm();
			}
		});
	}

	void buildAndShowForm() {
		onFinishUploaderHandler = buildOnFinishUploaderHandler();

		if (ShowPledge.myUser != null) {
			if (ShowPledge.myUser.isAdmin() || ShowPledge.myUser.isPublisher()) {
				shouldShowAdminControl = true;
			}
		}
		myForm.begin(buildContext());
		ContentContainer.setContent(myForm);
		myForm.setFocus();
	}

	IUploader.OnFinishUploaderHandler buildOnFinishUploaderHandler() {
		return new IUploader.OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {
				if (uploader.getStatus() == Status.SUCCESS) {

					// The server sends useful information to the client by default
					// UploadedInfo info = uploader.getServerInfo();

					service.getImageUrl(myShow, new Callback<String>() {
						@Override
						public void onSuccess(String result) {
							// 2) Swap the image on the form
							myForm.setImage(result);
						}
					});
				}
			}

		};
	}

	EditShowForm.Context buildContext() {
		return new EditShowForm.Context() {

			@Override
			public void onClick(ClickEvent event) {
				userClicksSubmit(event);
			}

			@Override
			public Show getShow() {
				return myShow;
			}

			@Override
			public boolean isReadOnly() {
				return false;
			}

			@Override
			public Long getShowId() {
				return null;
			}

			@Override
			public ShowServiceAsync getShowService() {
				return service;
			}

			@Override
			public OnFinishUploaderHandler getFinishUploadHandler() {
				return onFinishUploaderHandler;
			}

			@Override
			public boolean shouldShowAdminControls() {
				return shouldShowAdminControl;
			}

			@Override
			public boolean shouldShowImageControls() {
				return shouldShowImageControl;
			}

			@Override
			public String getTitle() {
				return "Edit Show";
			}

			@Override
			public String getButtonText() {
				return buttonText;
			}
		};
	}

	void userClicksSubmit(ClickEvent event) {

		ContentContainer.clearMsgs();

		updateShowVariables();

		List<String> errs = validate();

		if (errs.isEmpty()) {

			// Set up the callback object.
			Callback<Void> callback = new Callback<Void>() {
				@Override
				public void onSuccess(Void result) {
					ContentContainer.showInfo("You have saved changes to this show");
				}
			};

			service.update(myShow, callback);

		} else {
			ContentContainer.showErrors(errs);
		}
	}

	void updateShowVariables() {

		String corpUrl = UrlUtil.stripHTTPPrefix(myForm.getCorporateUrl().getText());
		String imdbUrl = UrlUtil.stripHTTPPrefix(myForm.getImdbUrl().getText());
		
		myShow.setCorporateUrl(corpUrl);
		myShow.setImdbUrl(imdbUrl);
		myShow.setName(myForm.getShowName().getText());
		myShow.setDescription(myForm.getDescription().getText());
		myShow.setShowStartDate(myForm.getAirDate().getText());
		myShow.setProducers(myForm.getProducers().getText());

		if (myForm.getSystemStatus() != null) {
			int sysStatIndx = myForm.getSystemStatus().getSelectedIndex();
			myShow.setSystemStatus(Show.SystemStatus.getFromIndex(sysStatIndx));
		}

		if (myForm.getShowStatus() != null) {
			int showStatIndx = myForm.getShowStatus().getSelectedIndex();
			myShow.setShowStatus(Show.ShowStatus.getFromIndex(showStatIndx));
		}

		if (myForm.getPromoted() != null) {
			myShow.setPromoted(myForm.getPromoted().getValue());
		}
	}

	List<String> validate() {
		List<String> errs = new ArrayList<String>();
		if (myForm.getDescription().getText() == null || myForm.getDescription().getText().length() < 20) {
			errs.add("Show description is required");
		}

		if (myForm.getImdbUrl().getText() == null || myForm.getImdbUrl().getText().length() < 10) {
			errs.add("IMDB URL is required");
		}

		if (myForm.getShowName().getText() == null || myForm.getShowName().getText().length() < 1) {
			errs.add("Show name is required");
		}
		
		if (myForm.getAirDate().getText() == null || myForm.getAirDate().getText().length() < 1) {
			errs.add("Start year is required");
		}		
		return errs;

	}
}
