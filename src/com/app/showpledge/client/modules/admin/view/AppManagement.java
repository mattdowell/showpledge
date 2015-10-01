package com.app.showpledge.client.modules.admin.view;

import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.AppManagementService;
import com.app.showpledge.client.util.stub.AppManagementServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppManagement extends Composite  {

	private final AppManagementServiceAsync managementService = GWT.create(AppManagementService.class);
	private static AppManagementUiBinder uiBinder = GWT.create(AppManagementUiBinder.class);

	interface AppManagementUiBinder extends UiBinder<Widget, AppManagement> {
	}

	public AppManagement() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button createAdminAccount;



	@UiHandler("createAdminAccount")
	void onClick(ClickEvent e) {
		managementService.createManagementUsers(new Callback<Void>(){

			@Override
			public void onSuccess(Void result) {
				Window.alert("Created!");
			}});
		

	}

}
