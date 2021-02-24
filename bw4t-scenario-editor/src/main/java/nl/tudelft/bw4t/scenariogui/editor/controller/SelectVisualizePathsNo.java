package nl.tudelft.bw4t.scenariogui.editor.controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import nl.tudelft.bw4t.scenariogui.editor.gui.MainPanel;

/**
 * Listens to the visualize checkboxes and updates the
 * {@link nl.tudelft.bw4t.scenariogui.BW4TClientConfig} object with the update
 * in the checkbox.
 */
public class SelectVisualizePathsNo implements ItemListener {
	// private MainPanel view;

	/**
	 * Create a new listener to the select visualize paths checkboxes.
	 * 
	 * @param newView
	 *            The parent view.
	 */
	public SelectVisualizePathsNo(final MainPanel newView) {
		// this.view = newView;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// It's not there anymore. Quick hack to get this compiling
		// view.getClientConfig().setVisualizePaths(false);
	}
}
