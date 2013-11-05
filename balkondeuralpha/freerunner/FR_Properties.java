package balkondeuralpha.freerunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLLog;

public class FR_Properties {
	public int keyRun, barWoodModel;
	public float speedMultiplier;
	public boolean fixedGloveInSMP, enableFreerunToggle, enableAnimations;
	public boolean enableEdgeWood, enableEdgeStone, enableHayStack, enableBarWood;
	public boolean enableClimbingGlove, enableWallKick;
	public int edgeWoodId, edgeStoneId, hayStackId, barWoodId, climbGloveId;

	public void loadAllProperties() {
		Properties props = new Properties();
		if (readProperties(props))
			processProperties(props);
		else {
			setStandardSettings();
			createFile(props);
			processProperties(props);
		}
	}

	private void createFile(Properties props) {
		FMLLog.getLogger().info("[Freerunner's Mod] Creating new properties file with standard values.\n");
		try {
			String folderPath = Minecraft.getMinecraft().mcDataDir.getCanonicalPath().replace('\\', '/') + "/config";
			String propsPath = Minecraft.getMinecraft().mcDataDir.getCanonicalPath().replace('\\', '/') + "/config/freerun.properties";
			new File(folderPath).mkdirs();
			FileOutputStream f = new FileOutputStream(propsPath);
			props.setProperty("edgeWood", String.valueOf(enableEdgeWood));
			props.setProperty("edgeStone", String.valueOf(enableEdgeStone));
			props.setProperty("haystack", String.valueOf(enableHayStack));
			props.setProperty("barWood", String.valueOf(enableBarWood));
			props.setProperty("climbGlove", String.valueOf(enableClimbingGlove));
			props.setProperty("edgeWood-ID", String.valueOf(edgeWoodId));
			props.setProperty("edgeStone-ID", String.valueOf(edgeStoneId));
			props.setProperty("haystack-ID", String.valueOf(hayStackId));
			props.setProperty("barWood-ID", String.valueOf(barWoodId));
			props.setProperty("climbGlove-ID", String.valueOf(climbGloveId));
			props.setProperty("barWood-Model-ID", String.valueOf(barWoodModel));
			props.setProperty("freerun-key", Keyboard.getKeyName(keyRun).toUpperCase());
			props.setProperty("speed-multiplier", String.valueOf(speedMultiplier));
			props.setProperty("enable-animations", String.valueOf(enableAnimations));
			props.setProperty("enable-glove-in-smp", String.valueOf(fixedGloveInSMP));
			props.setProperty("toggle-freerun-key", String.valueOf(enableFreerunToggle));
			props.setProperty("enable-wallkick", String.valueOf(enableWallKick));
			f.close();
			FMLLog.getLogger().info("[Freerunner's Mod] New properties file created at " + propsPath + "\n");
		} catch (IOException e) {
			FMLLog.getLogger().warning("[Freerunner's Mod] Unable to create new properties file. Move the properties file included in the download to the .minecraft/config folder.\n");
			e.printStackTrace();
		}
	}

	private void processProperties(Properties props) {
		if (barWoodModel == 0) {
			barWoodModel = 120;
		}
		if (climbGloveId == 0) {
			climbGloveId = 2121;
		}
		if (speedMultiplier > 1.5F) {
			speedMultiplier = 1.5F;
		}
		if (speedMultiplier < 1.0F) {
			speedMultiplier = 1.0F;
		}
	}

	private boolean readProperties(Properties props) {
		try {
			String path = Minecraft.getMinecraft().mcDataDir.getCanonicalPath().replace('\\', '/') + "/config/freerun.properties";
			FileInputStream f = new FileInputStream(path);
			props.load(f);
			enableEdgeWood = Boolean.parseBoolean(props.getProperty("edgeWood"));
			enableEdgeStone = Boolean.parseBoolean(props.getProperty("edgeStone"));
			enableHayStack = Boolean.parseBoolean(props.getProperty("haystack"));
			enableBarWood = Boolean.parseBoolean(props.getProperty("barWood"));
			enableClimbingGlove = Boolean.parseBoolean(props.getProperty("climbGlove"));
			edgeWoodId = Integer.parseInt(props.getProperty("edgeWood-ID"));
			edgeStoneId = Integer.parseInt(props.getProperty("edgeStone-ID"));
			hayStackId = Integer.parseInt(props.getProperty("haystack-ID"));
			barWoodId = Integer.parseInt(props.getProperty("barWood-ID"));
			climbGloveId = Integer.parseInt(props.getProperty("climbGlove-ID"));
			barWoodModel = Integer.parseInt(props.getProperty("barWood-Model-ID"));
			keyRun = Keyboard.getKeyIndex(props.getProperty("freerun-key").toUpperCase());
			speedMultiplier = Float.parseFloat(props.getProperty("speed-multiplier"));
			enableAnimations = Boolean.parseBoolean(props.getProperty("enable-animations"));
			fixedGloveInSMP = Boolean.parseBoolean(props.getProperty("enable-glove-in-smp"));
			enableFreerunToggle = Boolean.parseBoolean(props.getProperty("toggle-freerun-key"));
			enableWallKick = Boolean.parseBoolean(props.getProperty("enable-wallkick"));
			f.close();
			FMLLog.getLogger().info("[Freerunner's Mod] Properties file read succesfully!");
		} catch (IOException e) {
			FMLLog.getLogger().warning("[Freerunner's Mod] Unable to read from properties.");
			return false;
		} catch (NumberFormatException ne) {
			FMLLog.getLogger().warning("[Freerunner's Mod] Unable to read from properties.");
			return false;
		}
		return true;
	}

	private void setStandardSettings() {
		FMLLog.getLogger().warning("[Freerunner's Mod] Using standard settings.");
		enableEdgeWood = true;
		enableEdgeStone = true;
		enableHayStack = true;
		enableBarWood = true;
		enableClimbingGlove = true;
		enableWallKick = true;
		edgeWoodId = 2139;
		edgeStoneId = 2140;
		hayStackId = 2141;
		barWoodId = 2142;
		climbGloveId = 12100;
		barWoodModel = 110;
		keyRun = Keyboard.getKeyIndex("LCONTROL");
		speedMultiplier = 1.1F;
		enableAnimations = true;
		fixedGloveInSMP = true;
		enableFreerunToggle = false;
	}
}
