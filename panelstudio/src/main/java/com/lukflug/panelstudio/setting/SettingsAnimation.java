package com.lukflug.panelstudio.setting;

import com.lukflug.panelstudio.base.Animation;

/**
 * Implementation of {@link Animation} using {@link INumberSetting}.
 * @author lukflug
 */
public class SettingsAnimation extends Animation {
	/**
	 * Setting to be used for {@link #getSpeed()}.
	 */
	protected final INumberSetting speed;

	/**
	 * Constructor.
	 * @param speed speed setting
	 */
	public SettingsAnimation (INumberSetting speed) {
		this.speed=speed;
	}
	
	@Override
	protected int getSpeed() {
		return (int)speed.getNumber();
	}
}