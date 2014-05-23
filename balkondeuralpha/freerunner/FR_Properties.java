package balkondeuralpha.freerunner;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class FR_Properties {
	public float speedMultiplier = 1.1F;
	public boolean fixedGloveInSMP = true, enableFreerunToggle = false, enableAnimations = true;
	public boolean enableEdgeWood = true, enableEdgeStone = true, enableHayStack = true, enableBarWood = true;
	public boolean enableClimbingGlove = true, enableWallKick = true;

	public void loadAllProperties(File file) {
		readProperties(new Configuration(file));
	}

	private void readProperties(Configuration props) {
		enableEdgeWood = props.get("General", "Enable edgeWood", enableEdgeWood).getBoolean(enableEdgeWood);
		enableEdgeStone = props.get("General", "Enable edgeStone", enableEdgeStone).getBoolean(enableEdgeStone);
		enableHayStack = props.get("General", "Enable haystack", enableHayStack).getBoolean(enableHayStack);
		enableBarWood = props.get("General", "Enable barWood", enableBarWood).getBoolean(enableBarWood);
		enableClimbingGlove = props.get("General", "Enable climbGlove", enableClimbingGlove).getBoolean(enableClimbingGlove);
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
