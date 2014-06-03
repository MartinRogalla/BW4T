package nl.tudelft.bw4t.epartner.epartnerframe;

import nl.tudelft.bw4t.scenariogui.gui.epartner.EpartnerFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
/**
 * Test for the ePartner frame.
 * @author Tim
 *
 */
public class EpartnerFrameTest {
	/**
	 * A normal frame.
	 */
	private EpartnerFrame frame;
	/**
	 * A spy frame.
	 */
	private EpartnerFrame spyframe;
	/**
	 * Set up both frames.
	 */
	@Before
	public final void setupEpartnerFrame() {
		frame = new EpartnerFrame();
		spyframe = spy(frame);
	}
	/**
	 * Delete the real frame.
	 */
	@After
	public final void dispose() {
		frame.dispose();
	}
	/**
	 * Test the initial settings.
	 */
	@Test
	public final void testInititalSetup() {
		assertFalse(spyframe.getLeftAloneCheckbox().isSelected());
		assertFalse(spyframe.getGPSCheckbox().isSelected());
	}
	/**
	 * Test selection of check boxes.
	 */
	@Test
	public final void testModifyCheckBoxes() {
		spyframe.getLeftAloneCheckbox().setSelected(true);
		spyframe.getGPSCheckbox().setSelected(true);
		assertTrue(spyframe.getLeftAloneCheckbox().isSelected());
		assertTrue(spyframe.getGPSCheckbox().isSelected());
	}
	/**
	 * Test the reset button functionality.
	 */
	@Test
	public final void testResetButton() {
		spyframe.getResetButton().doClick();
		assertFalse(spyframe.getLeftAloneCheckbox().isSelected());
		assertFalse(spyframe.getGPSCheckbox().isSelected());
	}
}