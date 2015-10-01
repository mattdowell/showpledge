package com.app.showpledge.client.controller.menu;

import com.app.showpledge.client.controller.panel.ContentContainer;
import com.app.showpledge.client.modules.admin.controller.AcceptedShowsController;
import com.app.showpledge.client.modules.admin.controller.AllPledgesController;
import com.app.showpledge.client.modules.admin.controller.AllShowsController;
import com.app.showpledge.client.modules.admin.controller.AllUsersController;
import com.app.showpledge.client.modules.admin.controller.AppManagementController;
import com.app.showpledge.client.modules.admin.controller.NominatedShowsController;
import com.app.showpledge.client.modules.admin.controller.PromoShowsListController;
import com.app.showpledge.client.modules.user.controller.ContactUsController;
import com.app.showpledge.client.modules.user.controller.DisplayPledgesController;
import com.app.showpledge.client.modules.user.controller.LogoutController;
import com.app.showpledge.client.modules.user.controller.NewShowController;
import com.app.showpledge.client.modules.user.controller.NewUserController;
import com.app.showpledge.client.modules.user.controller.UserSettingsController;
import com.app.showpledge.client.modules.visitor.controller.AboutUsController;
import com.app.showpledge.client.modules.visitor.controller.LoginController;
import com.app.showpledge.client.modules.visitor.controller.PromoShowsController;
import com.app.showpledge.client.modules.visitor.controller.SearchShowsController;
import com.app.showpledge.client.modules.visitor.controller.VisibleShowsController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;

/**
 * TODO: All menu commands should call controllers
 * 
 * @author mjdowell
 * 
 */
public class MenuCommands {
	
	public static Command getShowAllPledgesCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						AllPledgesController con = new AllPledgesController();
						con.display();
						ContentContainer.hideLoading();
					}
				});
			}
		}
		;

		return new C();	
	}

	public static Command getShowMyPledgesCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						DisplayPledgesController screen = new DisplayPledgesController();
						screen.display();
						ContentContainer.hideLoading();
					}
				});
			}
		}
		;

		return new C();
	}

	public static Command getNominateShowCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						NewShowController con = new NewShowController();
						con.startNominateShow();
						ContentContainer.hideLoading();
					}
				});
			}
		}
		;

		return new C();
	}

	public static Command getLoginCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						LoginController lf = new LoginController();
						lf.begin();
						ContentContainer.hideLoading();
					}
				});
			}
		}
		;

		return new C();
	}

	public static Command getCreateAccount() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						NewUserController cont = new NewUserController("User Registation");
						cont.startNewUser();
						ContentContainer.hideLoading();
					}
				});
			}
		}
		;

		return new C();
	}

	public static Command getListAllUsersCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						AllUsersController c = new AllUsersController();
						c.begin();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getListAllNominatedShowsCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						NominatedShowsController c = new NominatedShowsController();
						c.show();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getAllShowsCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						// Only ADMINS should get this command
						AllShowsController c = new AllShowsController(true);
						c.show();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getPromoShowsCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						ContentContainer.clearMsgs();
						PromoShowsController c = new PromoShowsController();
						c.show();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getSearchShowsCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						SearchShowsController c = new SearchShowsController();
						c.startNewSearch();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	/**
	 * TODO: Move this to a controller class!
	 * 
	 * @return
	 */
	public static Command getLogout() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						LogoutController c = new LogoutController();
						c.begin();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;
		return new C();
	}

	public static Command getShowMyPreferencesCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						UserSettingsController cont = new UserSettingsController("Settings");
						cont.begin();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getAllAcceptedShowsCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						AcceptedShowsController c = new AcceptedShowsController();
						c.show();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getAllVisibleShowsCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						VisibleShowsController c = new VisibleShowsController();
						c.show();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getAdminPromoShowsCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						PromoShowsListController c = new PromoShowsListController(true);
						c.begin();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getAppManagementCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						AppManagementController c = new AppManagementController();
						c.show();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getShowAboutUsScreenCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						AboutUsController c = new AboutUsController();
						c.show();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}

	public static Command getShowContactUsScreenCommand() {
		final class C implements Command {
			@Override
			public void execute() {
				ContentContainer.showLoading();
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						ContentContainer.showError("Code download failed");
					}

					public void onSuccess() {
						ContactUsController c = new ContactUsController();
						c.begin();
						ContentContainer.hideLoading();
					}
				});

			}
		}
		;

		return new C();
	}
}
