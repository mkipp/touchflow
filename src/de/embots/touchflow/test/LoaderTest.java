package de.embots.touchflow.test;


import junit.framework.Assert;
import de.embots.touchflow.module.Settings;

import org.junit.Before;
import org.junit.Test;

public class LoaderTest {

	Settings s;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test public void testPutGet(){
		Assert.assertEquals("rat", Settings.getLastPath());
		Settings.setLastPath("rat");
		Assert.assertEquals("rat", Settings.getLastPath());
	}
}