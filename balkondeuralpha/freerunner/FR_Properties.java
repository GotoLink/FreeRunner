package balkondeuralpha.freerunner;

import java.io.File;
import net.minecraftforge.common.Configuration;

public class FR_Properties {
	public float speedMultiplier = 1.1F;
	public boolean fixedGloveInSMP = true, enableFreerunToggle = false, enableAnimations = true;
	public boolean enableEdgeWood = true, enableEdgeStone = true, enableHayStack = true, enableBarWood = true;
	public boolean enableClimbingGlove = true, enableWallKick = true;
	public int edgeWoodId = 2139, edgeStoneId = 2140, hayStackId = 2141, barWoodId = 2142, climbGloveId = 12100;

	public void loadAllProperties(File file) {
		readProperties(new Configuration(file));
	}

	private void readProperties(Configuration props) {
		enableEdgeWood = props.get("General", "Enable edgeWood", enableEdgeWood).getBoolean(enableEdgeWood);
		enableEdgeStone = props.get("General", "Enable edgeStone", enableEdgeStone).getBoolean(enableEdgeStone);
		enableHayStack = props.get("General", "Enable haystack", enableHayStack).getBoolean(enableHayStack);
		enableBarWood = props.get("General", "Enable barWood", enableBarWood).getBoolean(enableBarWood);
		enableClimbingGlove = props.get("General", "Enable climbGlove", enableClimbingGlove).getBoolean(enableClimbingGlove);
		edgeWoodId = props.getBlock("edgeWood-ID", edgeWoodId).getInt();
		edgeStoneId = props.getBlock("edgeStone-ID", edgeStoneId).getInt();
		hayStackId = props.getBlock("haystack-ID", hayStackId).getInt();
		barWoodId = props.getBlock("barWood-ID", barWoodId).getInt();
		climbGloveId = props.getItem("climbGlove-ID", climbGloveId).getInt();
		speedMultiplier = Float.parseFloat(props.get("Specials", "speed multiplier", speedMultiplier).getString());
		enableAnimations = props.get("Specials", "Enable animations", enableAnimations).getBoolean(enableAnimations);
		fixedGloveInSMP = props.get("Specials", "Enable glove in smp", fixedGloveInSMP).getBoolean(fixedGloveInSMP);
		enableFreerunToggle = props.get("General", "Toggle freerun with key", enableFreerunToggle).getBoolean(enableFreerunToggle);
		enableWallKick = props.get("Specials", "Enable wall kick", enableWallKick).getBoolean(enableWallKick);
		props.save();
		if (speedMultiplier > 1.5F) {
			speedMultiplier = 1.5F;
		}
		if (speedMultiplier < 1.0F) {
			speedMultiplier = 1.0F;
		}
	}
}
