package com.app.showpledge.client.modules.user.view;

import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.shared.entities.Pledge;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Displays (in a table) all the users pledge's
 * 
 * @author mjdowell
 * 
 */
public class UserPledgesScreen {

	private FlexTable pledgeTable;
	private VerticalPanel verticalPanel;
	private Label lblMyPledges;
	private DateTimeFormat formatter = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

	public interface Context {
		List<Pledge> getPledges();
	}

	private Context myContext;

	public UserPledgesScreen(Context inCtx) {
		super();
		myContext = inCtx;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void show() {

		verticalPanel = new VerticalPanel();

		lblMyPledges = new Label("My Pledges");
		lblMyPledges.setStyleName("page-title");
		verticalPanel.add(lblMyPledges);

		List<Pledge> pledges = myContext.getPledges();

		if (pledges == null || pledges.isEmpty()) {
			Label empty = new Label("No pledges yet!");
			empty.setStyleName("italic-body");
			verticalPanel.add(empty);

		} else {
			pledgeTable = new FlexTable();
			verticalPanel.add(pledgeTable);

			buildHeader();

			int row = 1;
			for (Pledge p : pledges) {
				addPledge(p, row);
				row++;
			}
		}

		ContentContainer.setContent(verticalPanel);
	}

	/**
	 * 
	 * @param inPledge
	 * @param inRowId
	 */
	private void addPledge(final Pledge inPledge, final int inRowId) {
		// Add a button to remove this stock from the table.
		Button editPledgeBtn = new Button("Edit");
		editPledgeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					ShowDetailsController con = new ShowDetailsController(inPledge);
					con.display();
				} catch (Exception e) {
					ContentContainer.showError(e.toString());
				}
			}
		});
		
		Button deletePledgeBtn = new Button("Delete");
		deletePledgeBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					ShowDetailsController con = new ShowDetailsController(inPledge);
					// Delete the pledge
					con.deletePledge();
					// Reshow this page with info text
				} catch (Exception e) {
					ContentContainer.showError(e.toString());
				}
			}
		});		

		pledgeTable.setText(inRowId, 0, inPledge.getShow().getName());
		pledgeTable.setText(inRowId, 1, "(US) $" + String.valueOf(inPledge.getPledgeAmount()));

		if (inPledge.getDate() != null) {
			pledgeTable.setText(inRowId, 2, formatter.format(inPledge.getDate()));
		} else {
			pledgeTable.setText(inRowId, 2, "");
		}
		pledgeTable.setWidget(inRowId, 3, editPledgeBtn);
		pledgeTable.setWidget(inRowId, 4, deletePledgeBtn);
	}

	/**
	 * 
	 */
	private void buildHeader() {

		Label lblShow = new Label("Show");
		lblShow.setStyleName("table-header");
		pledgeTable.setWidget(0, 0, lblShow);

		Label lblPledge = new Label("Pledge");
		lblPledge.setStyleName("table-header");
		pledgeTable.setWidget(0, 1, lblPledge);

		Label lblPledgeDate = new Label("Date");
		lblPledgeDate.setStyleName("table-header");
		pledgeTable.setWidget(0, 2, lblPledgeDate);

		Label lblEdit = new Label("Edit");
		lblEdit.setStyleName("table-header");
		pledgeTable.setWidget(0, 3, lblEdit);
		
		Label lblDelete = new Label("Delete");
		lblEdit.setStyleName("table-header");
		pledgeTable.setWidget(0, 4, lblDelete);		

	}

}
