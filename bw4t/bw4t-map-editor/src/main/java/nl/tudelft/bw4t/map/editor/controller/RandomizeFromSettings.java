package nl.tudelft.bw4t.map.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.bw4t.map.BlockColor;
import nl.tudelft.bw4t.map.editor.gui.RandomizeFrame;

public class RandomizeFromSettings implements ActionListener{
	
	private RandomizeFrame view;
	
	private RandomizeController controller;
	
	private List<BlockColor> result = null;
	
	public RandomizeFromSettings(RandomizeFrame rf, RandomizeController rc, List<BlockColor> res) {
		this.view = rf;
		this.controller = rc;
		this.result = res;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ArrayList<BlockColor> input = new ArrayList<>();
		int amount = view.getNumberOfBlocks();
		
		if(view.isRed()) {
			input.add(BlockColor.RED);
		}
		if(view.isGreen()) {
			input.add(BlockColor.GREEN);
		}
		if(view.isYellow()) {
			input.add(BlockColor.YELLOW);
		}
		if(view.isBlue()) {
			input.add(BlockColor.BLUE);
		}
		if(view.isOrange()) {
			input.add(BlockColor.ORANGE);
		}
		if(view.isWhite()) {
			input.add(BlockColor.WHITE);
		}
		if(view.isPink()) {
			input.add(BlockColor.PINK);
		}
		
		List<BlockColor> result = controller.makeRandomSequence(input, amount);
		setResult(result);
		view.getRandomizedSequence().setText(result.toString());
	}
	
	public void setResult(List<BlockColor> res) {
		result = res;
	}
	
	public List<BlockColor> getResult() {
		return result;
	}
}