package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.setting.INumberSetting;
import com.lukflug.panelstudio.theme.IRenderer;

/**
 * Component that represents a number-valued setting through a {@link Slider}.
 * @author lukflug
 */
public class NumberComponent extends Slider {
	/**
	 * The setting in question.
	 */
	protected INumberSetting setting;
	/**
	 * The name of the setting.
	 */
	protected String text;
	
	/**
	 * Constructor.
	 * @param text name of the setting
	 * @param description the description for this component
	 * @param renderer {@link IRenderer} for the component
	 * @param setting the setting in question
	 * @param min minimum value for the setting
	 * @param max maximum value for the setting
	 */
	public NumberComponent(String text, String description, IRenderer renderer, INumberSetting setting, double min, double max) {
		super("",description,renderer);
		this.setting=setting;
		this.text=text;
	}
	
	/**
	 * Render the component, with the caption being composed of the setting name and current value.
	 */
	@Override
	public void render (Context context) {
		if (setting.getPrecision()==0) title=String.format("%s: \u00A77%d",text,(int)setting.getNumber());
		else  title=String.format("%s: \u00A77%."+setting.getPrecision()+"f",text,setting.getNumber());
		super.render(context);
	}

	/**
	 * Implementation of {@link Slider#getValue()}.
	 */
	@Override
	protected double getValue() {
		return (setting.getNumber()-setting.getMinimumValue())/(setting.getMaximumValue()-setting.getMinimumValue());
	}

	/**
	 * Implementation of {@link Slider#setValue(double)}.
	 */
	@Override
	protected void setValue(double value) {
		setting.setNumber(value*(setting.getMaximumValue()-setting.getMinimumValue())+setting.getMinimumValue());
	}
}