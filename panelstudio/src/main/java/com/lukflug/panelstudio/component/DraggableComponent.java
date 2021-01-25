package com.lukflug.panelstudio.component;

import java.awt.Point;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.config.IPanelConfig;

/**
 * Fixed component wrapper that can be dragged by another component.
 * @author lukflug
 */
public class DraggableComponent extends ComponentProxy implements IFixedComponent {
	/**
	 * The fixed component to be dragged.
	 */
	protected IFixedComponent fixedComponent;
	/**
	 * The component to cause the drag.
	 */
	protected IComponent dragComponent;
	/**
	 * Flag indicating whether the user is dragging the component with the mouse.
	 */
	protected boolean dragging=false;
	/**
	 * Point storing the point, were the user started to drag the panel.
	 */
	protected Point attachPoint;

	public DraggableComponent (IFixedComponent fixedComponent, IComponent dragComponent) {
		super(fixedComponent);
		this.fixedComponent=fixedComponent;
		this.dragComponent=new DragComponent(dragComponent);
	}

	@Override
	public Point getPosition(IInterface inter) {
		if (dragging) {
			Point point=fixedComponent.getPosition(inter);
			point.translate(inter.getMouse().x-attachPoint.x,inter.getMouse().y-attachPoint.y);
			return point;
		}
		return fixedComponent.getPosition(inter);
	}

	@Override
	public void setPosition(IInterface inter, Point position) {
		fixedComponent.setPosition(inter,position);
	}

	@Override
	public int getWidth(IInterface inter) {
		return fixedComponent.getWidth(inter);
	}

	@Override
	public void saveConfig(IInterface inter, IPanelConfig config) {
		fixedComponent.saveConfig(inter,config);
	}

	@Override
	public void loadConfig(IInterface inter, IPanelConfig config) {
		fixedComponent.loadConfig(inter,config);
	}
	
	/**
	 * Returns the wrapped dragging component.
	 */
	public IComponent getWrappedDragComponent() {
		return dragComponent;
	}
	
	
	/**
	 * Class to wrap drag component.
	 * @author lukflug
	 */
	public class DragComponent extends ComponentProxy {
		/**
		 * Constructor.
		 * @param component drag component
		 */
		public DragComponent(IComponent component) {
			super(component);
		}
		
		@Override
		public void handleButton (Context context, int button) {
			super.handleButton(context,button);
			if (context.isClicked() && button==IInterface.LBUTTON) {
				dragging=true;
				attachPoint=context.getInterface().getMouse();
			} else if (!context.getInterface().getButton(IInterface.LBUTTON) && dragging) {
				Point mouse=context.getInterface().getMouse();
				dragging=false;
				Point p=fixedComponent.getPosition(context.getInterface());
				p.translate(mouse.x-attachPoint.x,mouse.y-attachPoint.y);
				fixedComponent.setPosition(context.getInterface(),p);
			}
		}
	}
}