package com.app.showpledge.client.modules.visitor.view;

import java.util.List;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.util.widget.WidgetFactory;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PromoScreen {

	public interface Context {
		List<Show> getShows();

		int getRows();

		String getPromoText();
	}

	private Context myContext = null;

	public PromoScreen(Context inCtx) {
		myContext = inCtx;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void displayPromotionShows() {

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("promo-container");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label title = new Label(myContext.getPromoText());
		title.setStyleName("page-title");
		verticalPanel.add(title);

		HTML hotShows = new HTML("Hot Shows");
		hotShows.setStyleName("promo-sub-title");
		verticalPanel.add(hotShows);
		
		
		// Rows / Cols
		Grid grid = new Grid(myContext.getRows(), 3);
		grid.setBorderWidth(0);
		grid.setCellSpacing(5);
		grid.setCellPadding(5);
		verticalPanel.add(grid);

		int showNum = 0;
		int row = 0;
		int col = 0;
		for (Show show : myContext.getShows()) {
			grid.setWidget(row, col, WidgetFactory.buildShowPromoWidget(show));
			showNum++;

			if ((showNum % 3) == 0) {
				row++;

				if (row >= myContext.getRows()) {
					break;
				}
			}

			col++;

			if (col > 2) {
				col = 0;
			}
		}

		ContentContainer.replaceContentOnly(verticalPanel);
	}
}
