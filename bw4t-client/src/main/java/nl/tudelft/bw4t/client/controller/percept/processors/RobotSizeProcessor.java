package nl.tudelft.bw4t.client.controller.percept.processors;

import java.util.List;

import nl.tudelft.bw4t.client.controller.ClientMapController;
import nl.tudelft.bw4t.map.view.ViewEntity;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

public class RobotSizeProcessor implements PerceptProcessor {

	@Override
	public void process(List<Parameter> parameters,
			ClientMapController clientMapController) {
		long id = ((Numeral) parameters.get(0)).getValue().longValue();
		int x = ((Numeral) parameters.get(1)).getValue().intValue();
		ViewEntity theBot = clientMapController.getCreateRobot(id);
		theBot.setSize(x);
	}

}
