package com.app.showpledge.client.modules.admin.view;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.shared.entities.Pledge;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;

public class AllPledgesView extends Composite {

	private static AllPledgesViewUiBinder uiBinder = GWT.create(AllPledgesViewUiBinder.class);
	private DateTimeFormat formatter = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

	interface AllPledgesViewUiBinder extends UiBinder<Widget, AllPledgesView> {
	}

	public AllPledgesView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public interface Context {
		public int totalRows();

		public boolean isSortable();

		public AbstractDataProvider<Pledge> getDataProvider();

		public int getPageSize();
		
		public String getMsg();
	}

	@UiField(provided = true)
	CellTable<Pledge> cellTable = new CellTable<Pledge>();
	@UiField(provided = true)
	SimplePager pager = new SimplePager();

	private Context myContext = null;

	/**
	 * Loop through all the shows and show them in a table
	 * 
	 * @param inUsers
	 */
	public void display(Context inCtx) {
		try {

			myContext = inCtx;

			// Build the table
			buildCellTable();

			// Build the pager
			buildPager(cellTable);

			// Add the table to the data provider
			myContext.getDataProvider().addDataDisplay(cellTable);

			// Set the content
			ContentContainer.setContent(this);
			ContentContainer.showInfo(myContext.getMsg());

		} catch (Exception e) {
			ContentContainer.showError(e);
		}
	}

	/**
	 * Create a Pager to control the table.
	 * 
	 * @param inTable
	 * @return
	 */
	private void buildPager(CellTable<Pledge> inTable) {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(inTable);
		pager.setPageSize(myContext.getPageSize());
	}

	/**
	 * Build a pageable table
	 * 
	 * DATE SHOW USER AMOUNT
	 * 
	 * 
	 * @return
	 */
	private void buildCellTable() {

		Column<Pledge, String> userCol = new Column<Pledge, String>(new TextCell()) {
			@Override
			public String getValue(Pledge p) {
				return p.getUser().getEmail();
			}
		};

		Column<Pledge, String> showCol = new Column<Pledge, String>(new TextCell()) {
			@Override
			public String getValue(Pledge p) {
				return p.getShow().getName();
			}
		};

		Column<Pledge, String> amountCol = new Column<Pledge, String>(new TextCell()) {
			@Override
			public String getValue(Pledge p) {
				return "$ " + String.valueOf(p.getPledgeAmount());
			}
		};

		Column<Pledge, String> dateCol = new Column<Pledge, String>(new TextCell()) {
			@Override
			public String getValue(Pledge p) {
				return formatter.format(p.getDate());
			}
		};

		Column<Pledge, String> delCol = new Column<Pledge, String>(new ButtonCell()) {
			@Override
			public String getValue(Pledge p) {
				return "Delete";
			}
		};

		delCol.setFieldUpdater(new FieldUpdater<Pledge, String>() {
			@Override
			public void update(int index, Pledge object, String value) {
				ShowDetailsController con = new ShowDetailsController(object, true);
				con.deletePledge();
			}
		});

		// Add the columns.
		cellTable.addColumn(dateCol, "Date");
		cellTable.addColumn(showCol, "Show");
		cellTable.addColumn(userCol, "User");
		cellTable.addColumn(amountCol, "Amount");
		cellTable.addColumn(delCol, "Delete");
	}

}
