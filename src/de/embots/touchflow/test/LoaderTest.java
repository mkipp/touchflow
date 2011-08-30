package de.embots.touchflow.test;


import junit.framework.Assert;
import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.Settings;
import de.embots.touchflow.module.implementation.input.KinectServer;

import org.junit.Before;
import org.junit.Test;

public class LoaderTest {

	Settings s;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test public void testPutGet() throws ModulException{
		String testmsg="<Kinect_Data>\n	    <Right_foot X=\"0.58\" Y=\"-0.7\" Z=\"1.08\" Confidence=\"0\" />\n	    <Left_foot X=\"-0.04\" Y=\"-0.54\" Z=\"1.56\" Confidence=\"1\" />\n	    <Right_hand_pos X=\"0\" Y=\"0\" Z=\"0\" Confidence=\"0\" />\n	    <Left_hand_pos X=\"0\" Y=\"0\" Z=\"0\" Confidence=\"0\" />\n	    <Right_hip X=\"0.5\" Y=\"-0.9\" Z=\"1.5\" Confidence=\"1\" />\n	    <Left_hip X=\"0.4\" Y=\"-1\" Z=\"1.6\" Confidence=\"1\" />\n	    <Left_hand Height=\"-0.5\" Confidence=\"1\" />\n	    <Right_hand Height=\"-0.7\" Confidence=\"0\" />\n	    <Head X=\"0.3\" Y=\"-0.3\" Z=\"1.7\" Confidence=\"1\" />\n	    <Movement_vector X=\"0\" Y=\"0\" Z=\"0\" />\n	    <Body_angle Angle=\"36.627\" Confidence=\"1\" />\n	</Kinect_Data>";
		
		
		//testmsg="<Kinect_Data> 		<Right_foot X=\"0.58\" Y=\"-0.7\" Z=\"1.08\" Confidence=\"0\" /><Left_foot X=\"-0.04\" Y=\"-0.54\" Z=\"1.56\" Confidence=\"1\" /><Right_hand_pos X=\"0\" Y=\"0\" Z=\"0\" Confidence=\"0\" /><Left_hand_pos X=\"0\" Y=\"0\" Z=\"0\" Confidence=\"0\" /><Right_hip X=\"0.5\" Y=\"-0.9\" Z=\"1.5\" Confidence=\"1\" /><Left_hip X=\"0.4\" Y=\"-1\" Z=\"1.6\" Confidence=\"1\" /><Left_hand Height=\"-0.5\" Confidence=\"1\" /><Right_hand Height=\"-0.7\" Confidence=\"0\" /><Head X=\"0.3\" Y=\"-0.3\" Z=\"1.7\" Confidence=\"1\" /><Movement_vector X=\"0\" Y=\"0\" Z=\"0\" /><Body_angle Angle=\"36.627\" Confidence=\"1\" /></Kinect_Data>";
		//testmsg=testmsg.trim();
		KinectServer server=new KinectServer();
		
		server.parseXML(testmsg);
		
	
	}
}
