package eu.packsolite.packsmod.feature.smashmc;

import lombok.Getter;

@Getter
public enum SmashMcFeature {
	INSTANCE;

	private final SmashApi api = new SmashApi();

	public void update() {
		api.update();
	}
	/**
	 * TODO:
	 * - Automatically glow players when joining a report.
	 * - 
	 */
}
