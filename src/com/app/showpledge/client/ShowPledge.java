package com.app.showpledge.client;

import com.app.showpledge.client.controller.menu.MenuCommands;
import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.user.controller.LogoutController;
import com.app.showpledge.client.modules.user.controller.NewUserController;
import com.app.showpledge.client.modules.visitor.controller.LoginController;
import com.app.showpledge.client.modules.visitor.controller.PromoShowsController;
import com.app.showpledge.client.modules.visitor.controller.ShowDetailsController;
import com.app.showpledge.client.util.Callback;
import com.app.showpledge.client.util.stub.ShowService;
import com.app.showpledge.client.util.stub.ShowServiceAsync;
import com.app.showpledge.client.util.stub.UserService;
import com.app.showpledge.client.util.stub.UserServiceAsync;
import com.app.showpledge.client.util.widget.WidgetFactory;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * Capturing request paramerer code can be found here: http://stackoverflow.com/questions
 * /590580/gwt-capturing-url-parameters-in-get-request
 */
public class ShowPledge implements EntryPoint {

	// ONLY THE LOGIN/LOGOUT should change this value;
	// TODO: This is dangerous. We need to be doing server side session checking also!
	public static boolean isUserLoggedIn = false;

	public static User myUser = null;

	private static final UserServiceAsync userService = GWT.create(UserService.class);
	private static final ShowServiceAsync showService = GWT.create(ShowService.class);

	/**
	 * This is the entry point method.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad() {

		isUserLoggedIn();

		// We first check to see if the link is a direct link in to the system
		if (!processLinksFromExternalSource()) {

			showPromoScreen();

		}
		
		makeHeaderClickable();
	}

	private void showPromoScreen() {
		PromoShowsController c = new PromoShowsController();
		c.show();

	}

	private void makeHeaderClickable() {
		RootPanel appNameHeader = RootPanel.get("appname-header");
		RootPanel headerSubText = RootPanel.get("header-sub-text");
		WidgetFactory.addViewPromoScreenEvent(appNameHeader);
		WidgetFactory.addViewPromoScreenEvent(headerSubText);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void buildMenu(boolean isUserLoggedIn) {

		buildTopLinks();

		// Configure and clear the panel
		RootPanel menuPanel = RootPanel.get("menu-bar");
		menuPanel.clear();
		menuPanel.setSize("100%", "100%");

		// Build the menu bar(s)
		MenuBar menuBar = new MenuBar(false);
		MenuBar showsMenuBar = new MenuBar(true);

		MenuItem mntmShows = new MenuItem("Shows", false, showsMenuBar);

		MenuItem mntmPromoShows = new MenuItem("Promo Shows", false, MenuCommands.getPromoShowsCommand());
		showsMenuBar.addItem(mntmPromoShows);

		MenuItem allPublishedShows = new MenuItem("All Shows", false, MenuCommands.getAllVisibleShowsCommand());
		showsMenuBar.addItem(allPublishedShows);

		MenuItem mntmSearchShows = new MenuItem("Search Shows", false, MenuCommands.getSearchShowsCommand());
		showsMenuBar.addItem(mntmSearchShows);

		MenuItem mntmNominateAShow = new MenuItem("Nominate a Show", false, MenuCommands.getNominateShowCommand());
		showsMenuBar.addItem(mntmNominateAShow);

		menuBar.addItem(mntmShows);

		MenuBar myAccntMenuBar = new MenuBar(true);

		MenuItem mntmMyAccount_1 = new MenuItem("My Account", false, myAccntMenuBar);

		if (isUserLoggedIn) {
			MenuItem mntmMyShows = new MenuItem("My Pledges", false, MenuCommands.getShowMyPledgesCommand());
			myAccntMenuBar.addItem(mntmMyShows);
			MenuItem mntmMyPreferences = new MenuItem("Settings", false, MenuCommands.getShowMyPreferencesCommand());
			myAccntMenuBar.addItem(mntmMyPreferences);
			MenuItem mntmLogout = new MenuItem("Logout", false, MenuCommands.getLogout());
			myAccntMenuBar.addItem(mntmLogout);

		} else {

			MenuItem mntmLogin = new MenuItem("Login", false, MenuCommands.getLoginCommand());
			myAccntMenuBar.addItem(mntmLogin);

			MenuItem mntmCreateAccount = new MenuItem("Create Account", false, MenuCommands.getCreateAccount());
			myAccntMenuBar.addItem(mntmCreateAccount);

		}

		menuBar.addItem(mntmMyAccount_1);

		MenuBar abboutUsMenuBar = new MenuBar(true);
		MenuItem aboutPulldown = new MenuItem("About", false, abboutUsMenuBar);
		menuBar.addItem(aboutPulldown);

		MenuItem whatWeDoMenuItem = new MenuItem("What We Do", false, MenuCommands.getShowAboutUsScreenCommand());
		abboutUsMenuBar.addItem(whatWeDoMenuItem);

		MenuItem contactUsMenuItem = new MenuItem("Contact Us", false, MenuCommands.getShowContactUsScreenCommand());
		abboutUsMenuBar.addItem(contactUsMenuItem);

		// Publisher
		if (myUser != null && myUser.isPublisher()) {
			MenuBar publisherBar = new MenuBar(true);
			MenuItem mntmAdmin = new MenuItem("Publisher", false, publisherBar);

			MenuItem mntmAllShows = new MenuItem("All Shows", false, MenuCommands.getAllShowsCommand());
			publisherBar.addItem(mntmAllShows);

			MenuItem mntmAllNomShows = new MenuItem("Nominated Shows", false, MenuCommands.getListAllNominatedShowsCommand());
			publisherBar.addItem(mntmAllNomShows);

			MenuItem mntmAllAcceptedShows = new MenuItem("Accepted Shows", false, MenuCommands.getAllAcceptedShowsCommand());
			publisherBar.addItem(mntmAllAcceptedShows);

			MenuItem mntmAllPromoShows = new MenuItem("Promo Shows", false, MenuCommands.getAdminPromoShowsCommand());
			publisherBar.addItem(mntmAllPromoShows);

			menuBar.addItem(mntmAdmin);
		}

		// Admin
		if (myUser != null && myUser.isAdmin()) {
			MenuBar adminMenuBbar = new MenuBar(true);
			MenuItem mntmAdmin = new MenuItem("Admin", false, adminMenuBbar);

			MenuItem mntmAllUsers = new MenuItem("All Users", false, MenuCommands.getListAllUsersCommand());
			adminMenuBbar.addItem(mntmAllUsers);

			MenuItem mntmAllShows = new MenuItem("All Shows", false, MenuCommands.getAllShowsCommand());
			adminMenuBbar.addItem(mntmAllShows);

			MenuItem mntmAllPledges = new MenuItem("All Pledges", false, MenuCommands.getShowAllPledgesCommand());
			adminMenuBbar.addItem(mntmAllPledges);
			
			MenuItem mntmAllNomShows = new MenuItem("Nominated Shows", false, MenuCommands.getListAllNominatedShowsCommand());
			adminMenuBbar.addItem(mntmAllNomShows);

			MenuItem mntmAllAcceptedShows = new MenuItem("Accepted Shows", false, MenuCommands.getAllAcceptedShowsCommand());
			adminMenuBbar.addItem(mntmAllAcceptedShows);

			MenuItem mntmAllPromoShows = new MenuItem("Promo Shows", false, MenuCommands.getAdminPromoShowsCommand());
			adminMenuBbar.addItem(mntmAllPromoShows);

			MenuItem manage = new MenuItem("App Management", false, MenuCommands.getAppManagementCommand());
			adminMenuBbar.addItem(manage);

			menuBar.addItem(mntmAdmin);
		}

		menuPanel.add(menuBar);
	}

	private static void buildTopLinks() {
		// Also handle this top menu item
		RootPanel loginLogout = RootPanel.get("login-panel");
		loginLogout.clear();
		
		if (isUserLoggedIn) {
			loginLogout.add(buildLogoutLinks());
		} else {
			loginLogout.add(buildLoginLinks());
		}
	}

	private static Panel buildLoginLinks() {
		HorizontalPanel p = new HorizontalPanel();
		p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		Hyperlink login = new Hyperlink("[Login] ", "login");
		
		login.addHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				LoginController lf = new LoginController();
				lf.begin();			
			}}, ClickEvent.getType());
		

		Hyperlink register = new Hyperlink(" [Register]", "register");
		register.addHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				NewUserController cont = new NewUserController("User Registation");
				cont.startNewUser();				
			}}, ClickEvent.getType());
		
		p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		p.add(login);
		p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		p.add(new Label(""));
		p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		p.add(register);
		return p;
	}

	private static Panel buildLogoutLinks() {
		HorizontalPanel p = new HorizontalPanel();
		p.setStyleName("paddingFive");
		Hyperlink logout = new Hyperlink("[Logout]", "logout");
		logout.setStyleName("paddingFive");
		
		logout.addHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				LogoutController c = new LogoutController();
				c.begin();	
			}}, ClickEvent.getType());		
		
		p.add(logout);
		return p;
	}

	/**
	 * Is the user logged in?
	 */
	private static void isUserLoggedIn() {
		Callback<Boolean> callback = new Callback<Boolean>() {
			public void onSuccess(Boolean result) {

				if (result) {
					getUserObject();
				} else {
					buildMenu(result);
				}
				isUserLoggedIn = result.booleanValue();
			}
		};
		userService.isUserLoggedIn(callback);
	}

	/**
	 * Populates the currently logged in user
	 */
	private static void getUserObject() {
		Callback<User> callback = new Callback<User>() {
			public void onSuccess(User result) {
				myUser = result;
				if (myUser != null) {
					buildMenu(true);
				}
			}
		};
		userService.getLoggedInUser(callback);
	}

	/**
	 * This is used for direct links in to ShowPledge.
	 */
	private boolean processLinksFromExternalSource() {
		boolean theReturn = linkDirectToShow();
		if (!theReturn) {
			theReturn = checkForUserValidation();
		}
		return theReturn;
	}
	
	
	/**
	 * Did the user click on a direct show link?
	 * @return
	 */
	private boolean linkDirectToShow() {
		boolean hasLinkedFromExternalSource = false;

		// SHOW ID
		String showIdString = Window.Location.getParameter("sid");

		if (showIdString != null) {
			try {
				Long sid = Long.parseLong(showIdString);

				showService.get(sid, new Callback<Show>() {

					@Override
					public void onSuccess(Show result) {
						
						// Display the ShowDetails screen
						ShowDetailsController con = new ShowDetailsController(result);
						con.display();
						ContentContainer.hideLoading();
						
					}});
				

				hasLinkedFromExternalSource = true;

			} catch (Exception e) {
				GWT.log("Cound not parse show id: " + showIdString);
			}
		}

		return hasLinkedFromExternalSource;		
	}

	
	/**
	 * Did the user click on a validation email link?
	 * 
	 * @return
	 */
	private boolean checkForUserValidation() {
		
		boolean linkFromValidationEmail = false;
		
		// SHOW ID
		String uid = Window.Location.getParameter("uid");
		String cd = Window.Location.getParameter("cd");

		if (uid != null && cd != null) {
			userService.validateUser(uid, cd, new Callback<Void>() {

				@Override
				public void onSuccess(Void result) {
					ContentContainer.showInfo("Your account is now validated. Welcome.");
					ContentContainer.hideLoading();

				}
			});
			
			linkFromValidationEmail = true;
		}
		return linkFromValidationEmail;
	}
}
