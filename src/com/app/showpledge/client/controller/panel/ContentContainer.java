package com.app.showpledge.client.controller.panel;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class ContentContainer {

	private static final String CONTENT_DIV_TAG = "content-container";
	private static final String MSG_DIV_TAG = "msg-container";
	private static final String LOADING_DIV_TAG = "loading";

	public static void showLoading() {
		RootPanel loadingPanel = RootPanel.get(LOADING_DIV_TAG);
		loadingPanel.setVisible(true);
	}

	public static void hideLoading() {
		RootPanel loadingPanel = RootPanel.get(LOADING_DIV_TAG);
		loadingPanel.setVisible(false);
		// loadingPanel.clear();
	}

	/**
	 * 
	 * @param inContent
	 */
	public static void setContent(Widget inContent) {
		clearAll();
		addContentAbove(inContent);
	}

	/**
	 * Only replaces the body (or content) but not the error messages
	 * 
	 * @param inContent
	 */
	public static void replaceContentOnly(Widget inContent) {
		clearBody();
		addContentAbove(inContent);
	}

	/**
	 * Todo: Fix
	 * 
	 * @param inContent
	 */
	public static void addContentBelow(Widget inContent) {
		RootPanel contentPanel = RootPanel.get(CONTENT_DIV_TAG);
		contentPanel.insert(inContent, 1);
	}

	/**
	 * * Todo: Fix
	 * 
	 * @param inContent
	 */
	public static void addContentAbove(Widget inContent) {
		RootPanel contentPanel = RootPanel.get(CONTENT_DIV_TAG);
		contentPanel.insert(inContent, 0);
	}

	/**
	 * Adds the error icon, sets the DIV style, and adds the widget
	 * 
	 * @param inContent
	 */
	public static void showError(Widget inContent) {
		clearMsgs();

		// Get the root panel
		RootPanel errPanel = RootPanel.get(MSG_DIV_TAG);
		errPanel.setStyleName("error_box");
		errPanel.setVisible(true);

		// Creat a HP where the image is on the left
		HorizontalPanel errP = new HorizontalPanel();
		errP.add(new Image("images/erroricon.png"));
		errP.add(inContent);
		errP.setStyleName("inner_msg_box");

		// Add to the root panel
		errPanel.insert(errP, 0);
	}

	/**
	 * Adds the error icon, sets the DIV style, and adds the widget
	 * 
	 * TODO: Add an info style type
	 * 
	 * @param inContent
	 */
	public static void showInfo(Widget inContent) {
		clearMsgs();

		// Get the root panel
		RootPanel errPanel = RootPanel.get(MSG_DIV_TAG);
		errPanel.setStyleName("info_box");
		errPanel.setVisible(true);

		// Creat a HP where the image is on the left
		HorizontalPanel errP = new HorizontalPanel();
		errP.add(new Image("images/infoicon.png"));
		errP.add(inContent);
		errP.setStyleName("inner_msg_box");

		// Add to the root panel
		errPanel.insert(errP, 0);
	}

	/**
	 * 
	 * @param inContent
	 */
	public static void showInfo(String inContent) {
		if (inContent != null) {
			clearMsgs();
			showInfo(buildInfoTextWidget(inContent));
		}
	}

	/**
	 * 
	 * @param inContent
	 */
	public static void showError(String inContent) {
		if (inContent != null) {
			clearMsgs();
			showError(buildErrorTextWidget(inContent));
		}
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	private static Widget buildErrorTextWidget(String in) {
		Label text = new Label();
		text.setText(in);
		text.setStyleName("error_text");
		return text;
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	private static Widget buildInfoTextWidget(String in) {
		Label text = new Label();
		text.setText(in);
		text.setStyleName("info_text");
		return text;
	}

	/**
	 * Clears the body
	 * 
	 * @param inContent
	 */
	public static void showErrorOnly(Widget inContent) {
		clearBody();
		showError(inContent);
	}

	/**
	 * Shows a list of errors
	 * 
	 * @param inContent
	 */
	public static void showErrors(List<String> inContent) {
		VerticalPanel wid = new VerticalPanel();
		wid.setStyleName("validation_error_box");
		for (String s : inContent) {
			wid.add(buildErrorTextWidget(s));
		}
		showError(wid);
	}

	public static void showError(Throwable caught) {
		showError(caught.getMessage());
	}

	private static void clearAll() {
		clearMsgs();
		clearBody();
	}

	public static void clearMsgs() {
		RootPanel errPanel = RootPanel.get(MSG_DIV_TAG);
		errPanel.clear();
		errPanel.setVisible(false);
	}

	public static void clearBody() {
		RootPanel contentPanel = RootPanel.get(CONTENT_DIV_TAG);
		contentPanel.clear();
	}

}
