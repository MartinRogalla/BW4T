package nl.tudelft.bw4t.epartner.epartnerframe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import nl.tudelft.bw4t.scenariogui.BW4TClientConfig;
import nl.tudelft.bw4t.scenariogui.EPartnerConfig;
import nl.tudelft.bw4t.scenariogui.epartner.controller.EpartnerController;
import nl.tudelft.bw4t.scenariogui.epartner.gui.EpartnerFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EpartnerFrameTest {
	
	private EpartnerFrame frame;
	private EpartnerFrame spyframe;
	private EpartnerController controller;
	private EPartnerConfig config;
	
	@Before
	public final void setupEpartnerFrame() {
        frame = new EpartnerFrame(new EpartnerController(new EPartnerConfig()), new BW4TClientConfig());
        spyframe= spy(frame);
	}
	
	@After
	public final void dispose() {
		frame.dispose();
	}
	
	@Test
	public final void testUpdateConfig() {
		spyframe.getGPSCheckbox().setSelected(true);
		spyframe.getForgetMeNotCheckbox().setSelected(true);
		controller.updateConfig(spyframe);
		assertTrue(config.isGps());
		assertTrue(config.isForgetMeNot());
	}
	
	@Test
	public final void testResetButton() {
		spyframe.getResetButton().doClick();
		assertEquals(config.isGps(), spyframe.getGPSCheckbox().isSelected());
		assertEquals(config.isForgetMeNot(), spyframe.getForgetMeNotCheckbox().isSelected());
	}
	
	@Test
	public final void testApplyButton() {
		spyframe.getGPSCheckbox().setSelected(false);
		spyframe.getForgetMeNotCheckbox().setSelected(true);
		spyframe.getSaveButton().doClick();
		assertEquals(config.isGps(), spyframe.getGPSCheckbox().isSelected());
		assertEquals(config.isForgetMeNot(), spyframe.getForgetMeNotCheckbox().isSelected());
	}
}
