package com.app.showpledge.client.modules.user.controller;

import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.Date;
import java.util.List;

import com.app.showpledge.client.ShowPledge;
import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.view.EditShowForm;
import com.app.showpledge.client.modules.visitor.controller.PromoShowsController;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Create a new show
 * 
 * @author mjdowell
 * 
 */
public class NewShowController extends EditShowController {

	// When this is TRUE, we move to the promo screen after clicking "save"
	private boolean finalSave = false;

	public NewShowController() {
		super(new Show());
		shouldShowImageControl = false;
	}

	public void startNominateShow() {

		if (ShowPledge.isUserLoggedIn) {
			buttonText = "Next";
			finalSave = false;
			// We have to save the show first, then we can add the image
			super.begin();
		} else {
			ContentContainer.showError("Sorry, you must login or create an account first");
		}

	}

	void buildAndShowForm() {
		super.buildAndShowForm();
	}

	void handleImageUpload() {
		shouldShowImageControl = true;
		buttonText = "Save";
		buildAndShowForm();
		ContentContainer.showInfo("Next, upload a show graphic");
		finalSave = true;
	}

	void finishSubmittingShow() {
		PromoShowsController c = new PromoShowsController();
		c.show("Thank You! Your nomination has been submitted.");
	}

	/**
	 * We need to save the show, then re-build the screen with the image control
	 */
	void userClicksSubmit(ClickEvent event) {

		myShow.setNominationDate(new Date());
		updateShowVariables();

		List<String> errs = validate();

		if (errs.isEmpty()) {

			Callback<Long> callback = new Callback<Long>() {

				@Override
				public void onSuccess(Long result) {

					if (finalSave) {
						finishSubmittingShow();
					} else {
						myShow.setId(result);

						// TODO: We now want to give feedback to the user that they next need to upload in image
						handleImageUpload();
					}
				}
			};

			service.nominateShow(myShow, callback);
		} else {
			ContentContainer.showErrors(errs);
		}
	}

	/**
	 * Builds the nominate show context
	 */
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
				return "Nominate Show";
			}

			@Override
			public String getButtonText() {
				return buttonText;
			}
		};
	}
}
