package com.hospital.auth.web;

import java.awt.Desktop;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UiAutoOpenOnStartup {

	private static final Logger log = LoggerFactory.getLogger(UiAutoOpenOnStartup.class);

	@Value("${app.ui.auto-open:true}")
	private boolean autoOpen;

	@Value("${app.ui.url:http://localhost:8081/login}")
	private String uiUrl;

	@EventListener(ApplicationReadyEvent.class)
	public void openUi() {
		if (!autoOpen) {
			return;
		}
		try {
			if (!Desktop.isDesktopSupported()) {
				log.info("Desktop integration not supported; open UI manually at {}", uiUrl);
				return;
			}
			Desktop.getDesktop().browse(URI.create(uiUrl));
			log.info("Opened UI in browser: {}", uiUrl);
		}
		catch (Exception ex) {
			log.info("Could not auto-open UI. Open manually at {} ({})", uiUrl, ex.getMessage());
		}
	}
}
