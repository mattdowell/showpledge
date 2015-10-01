package com.app.showpledge.client.modules.visitor.view;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author mjdowell
 *
 */
public class SearchForm extends Composite {

	private static SearchFormUiBinder uiBinder = GWT.create(SearchFormUiBinder.class);

	interface SearchFormUiBinder extends UiBinder<Widget, SearchForm> {
	}

	public SearchForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	public interface Context {
		void onClick(ClickEvent event);
		String getResultsText();
	}	

	@UiField TextBox searchString;
	@UiField Button searchBtn;
	

	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void build(final Context inContext) {

		searchBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				inContext.onClick(event);
			}
		});


		ContentContainer.setContent(this);
		
		if (inContext.getResultsText() != null) {
			ContentContainer.showError(inContext.getResultsText());	
		}
		
		searchString.setFocus(true);
	}
	
	public String getSearchString() {
		return searchString.getText();
	}

		

}
