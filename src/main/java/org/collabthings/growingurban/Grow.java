package org.collabthings.growingurban;

import java.net.MalformedURLException;

import org.collabthings.app.CTApp;
import org.collabthings.util.LLog;

import waazdoh.client.WClient;
import waazdoh.common.vo.AppLoginVO;

public class Grow {

	private CTApp app;
	private LLog log = LLog.getLogger(this);

	private void start() throws MalformedURLException, InterruptedException {
		app = new CTApp();

		WClient client = app.getLClient().getClient();
		if (!client.isLoggedIn() && !client.trySavedSession()) {
			AppLoginVO applogin = client.requestAppLogin();
			log.info("applogin " + applogin.getUrl() + applogin.getId());
			while ((applogin = client.checkAppLogin(applogin.getId())).getSessionid() == null) {
				synchronized (this) {
					this.wait(1000);
				}
			}

			log.info("logged in as " + applogin.getUserid() + client.getUser(client.getUserID()).getName());
		}

		app.close();
	}

	public static void main(String[] args) {
		try {
			new Grow().start();
		} catch (MalformedURLException | InterruptedException e) {
			LLog.getLogger("FAIL").error("FAIL", "main", e);
		}
	}

}
