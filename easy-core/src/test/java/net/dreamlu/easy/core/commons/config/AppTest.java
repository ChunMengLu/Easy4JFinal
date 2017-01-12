package net.dreamlu.easy.core.commons.config;

import net.dreamlu.easy.commons.config.ApplicationConfig;
import net.dreamlu.easy.commons.owner.ConfigFactory;

public class AppTest {

	public static void main(String[] args) {
		ApplicationConfig cfg = ConfigFactory.create(ApplicationConfig.class);
		System.out.println(cfg.dbDefaultUrl());
	}
}
