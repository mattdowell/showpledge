package com.app.showpledge.client.modules.visitor.view;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.client.util.ImageUtil;
import com.app.showpledge.client.util.widget.ClickableImageCell;
import com.app.showpledge.client.util.widget.WidgetFactory;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AbstractDataProvider;

/**
 * What are this classes reponsibilities?
 * 
 * @author mjdowell
 *
 */
public class PageableShowRows {

	public interface Context {
		AbstractDataProvider<Show> getDataProvider();

		void onRowClick(Show inShow);

		boolean isSortable();

		int totalRows();

		int getPageSize();
	}

	private VerticalPanel mainPanel = null;
	private CellTable<Show> showsTable = null;
	private SimplePager myPager = null;
	private Context myContext = null;

	public PageableShowRows(Context inCtx) {
		myContext = inCtx;
	}

	/**
	 * Loop through all the shows and show them in a table
	 * 
	 * @param inUsers
	 */
	public void display() {

		mainPanel = new VerticalPanel();

		// Build the table
		showsTable = buildCellTable();

		// Add the table to the data provider
		myContext.getDataProvider().addDataDisplay(showsTable);

		mainPanel.add(showsTable);

		// Build the pager
		myPager = buildPager(showsTable);
		mainPanel.add(myPager);

		// Set the content
		ContentContainer.setContent(mainPanel);
	}

	/**
	 * Create a Pager to control the table.
	 * 
	 * @param inTable
	 * @return
	 */
	private SimplePager buildPager(CellTable<Show> inTable) {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(inTable);
		pager.setPageSize(myContext.getPageSize());
		return pager;
	}

	/**
	 * Build a pageable table
	 * 
	 * @return
	 */
	private CellTable<Show> buildCellTable() {

		CellTable<Show> table = new CellTable<Show>();
		
		// Generic row click updater for any clickable columns
		FieldUpdater<Show, String> rowClick = new FieldUpdater<Show, String>(){
		    @Override
		    public void update(int index, Show inShow, String value){
		    	myContext.onRowClick(inShow);
		    }
		};

		
		// Create name column.
		Column<Show, String> nameColumn = new Column<Show, String>(new ClickableTextCell()) {
			@Override
			public String getValue(Show inShow) {
				return inShow.getName();
			}
		};
		
		nameColumn.setFieldUpdater(rowClick);		
		


		// Image column
		Column<Show, String> imageColumn = new Column<Show, String>(new ClickableImageCell()) {
			@Override
			public String getValue(Show show) {
				return ImageUtil.makeThumb(show.getPrimaryImageUrl());
			}
		};
		
		imageColumn.setFieldUpdater(rowClick);

		
		// Create desc column.
		Column<Show, String> descColumn = new Column<Show, String>(new ClickableTextCell()) {
			@Override
			public String getValue(Show inShow) {
				return WidgetFactory.trimLongText(inShow.getDescription(), 200);
			}
		};
		
		descColumn.setFieldUpdater(rowClick);

		// Create Total pledges column.
		TextColumn<Show> totalPledgesColumn = new TextColumn<Show>() {
			@Override
			public String getValue(Show inShow) {
				return "$" + String.valueOf(inShow.getPledgeTotal());
			}
		};

		ButtonCell pledgeNowButton = new ButtonCell();
		Column<Show, String> pledgeCol = new Column<Show, String>(pledgeNowButton) {
			public String getValue(Show object) {
				return "Pledge Now!";
			}
		};

		pledgeCol.setFieldUpdater(new FieldUpdater<Show, String>() {

			@Override
			public void update(int index, Show show, String value) {
				ShowDetailsController cont = new ShowDetailsController(show);
				cont.display();
			}
		});
		
		// Add the columns.
		table.addColumn(nameColumn, "Name");
		table.addColumn(imageColumn, "Image");
		table.addColumn(descColumn, "Description");
		table.addColumn(totalPledgesColumn, "Total Pledges");
		table.addColumn(pledgeCol, "Pledge");
		 

		return table;
	}
}
