package com.app.showpledge.client.modules.admin.view;

import java.util.Comparator;
import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.controller.EditShowController;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.client.util.ImageUtil;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.client.util.widget.WidgetFactory;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AllShows {

	private ShowServiceAsync showService = GWT.create(ShowService.class);
	private VerticalPanel mainPanel = null;
	private ListDataProvider<Show> myShows = null;

	private CellTable<Show> showsTable = null;
	private SimplePager myPager = null;

	/**
	 * Loop through all the users and show them in a table
	 * 
	 * @param inUsers
	 */
	public void showAllShows(List<Show> inShows) {

		mainPanel = new VerticalPanel();
		myShows = new ListDataProvider<Show>(inShows);

		// Build the table
		showsTable = buildCellTable(myShows);
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
		pager.setPageSize(5);
		return pager;
	}

	/**
	 * Build a pageable table
	 * 
	 * @return
	 */
	private CellTable<Show> buildCellTable(ListDataProvider<Show> inShowList) {
		CellTable<Show> table = new CellTable<Show>();

		// Add the sort handler to the table
		ListHandler<Show> sortHandler = new ListHandler<Show>(inShowList.getList());
		table.addColumnSortHandler(sortHandler);

		// Create name column.
		TextColumn<Show> nameColumn = new TextColumn<Show>() {
			@Override
			public String getValue(Show inShow) {
				return inShow.getName();
			}
		};

		// Make the name column sortable
		nameColumn.setSortable(true);
		sortHandler.setComparator(nameColumn, new Comparator<Show>() {
			public int compare(Show o1, Show o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		// Image column
		Column<Show, String> imageColumn = new Column<Show, String>(new ImageCell()) {
			@Override
			public String getValue(Show show) {
				return ImageUtil.makeThumb(show.getPrimaryImageUrl());
			}
		};

		// Create desc column.
		TextColumn<Show> descColumn = new TextColumn<Show>() {
			@Override
			public String getValue(Show inShow) {
				return WidgetFactory.trimLongText(inShow.getDescription(), 200);
			}
		};

		// Create Total pledges column.
		TextColumn<Show> totalPledgesColumn = new TextColumn<Show>() {
			@Override
			public String getValue(Show inShow) {
				return "$" + String.valueOf(inShow.getPledgeTotal());
			}
		};
		// Make the name column sortable
		totalPledgesColumn.setSortable(true);
		sortHandler.setComparator(totalPledgesColumn, new Comparator<Show>() {
			public int compare(Show o1, Show o2) {
				return new Double(o1.getPledgeTotal()).compareTo(new Double(o2.getPledgeTotal()));
			}
		});

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

		// Set the total row count. This isn't strictly necessary, but it
		// affects
		// paging calculations, so its good habit to keep the row count up to
		// date.
		table.setRowCount(inShowList.getList().size(), true);

		// Push the data into the widget.
		table.setRowData(0, inShowList.getList());

		inShowList.addDataDisplay(table);

		// TODO: What do these do?
//		table.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
//		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

		// Add a selection model so we can select cells.
		final SingleSelectionModel<Show> selectionModel = new SingleSelectionModel<Show>(Show.KEY_PROVIDER);
		table.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
//				ViewShowController con = new ViewShowController(selectionModel.getSelectedObject());
//				con.display();
				EditShowController con = new EditShowController(selectionModel.getSelectedObject());
				con.begin();
			}
		});

		return table;
	}

	/**
	 * 
	 * @param inShowId
	 * @return
	 */
	private AsyncCallback<Long> buildAccepShowCallback() {
		AsyncCallback<Long> call = new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				ContentContainer.showError(caught);
			}

			@Override
			public void onSuccess(Long result) {
			}

		};
		return call;
	}

}
