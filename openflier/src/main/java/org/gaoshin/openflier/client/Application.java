package org.gaoshin.openflier.client;


import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Rectangle;
import net.edzard.kinetic.Stage;
import net.edzard.kinetic.Text;
import net.edzard.kinetic.Vector2d;
import net.edzard.kinetic.Animation;
import net.edzard.kinetic.Drawable;
import net.edzard.kinetic.Frame;

import org.gaoshin.openflier.model.Visibility;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application implements EntryPoint {

	private Stage stage;
	private Layer layer;
	private double angularSpeed = Math.PI / 2;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// final Label label = new Label(
		// "gwt-maven-plugin Archetype :: Project org.codehaus.mojo.gwt-maven-plugin");
		// RootPanel.get().add(label);

		Element div = DOM.createDiv();
		RootPanel.getBodyElement().appendChild(div);

		// Setup stage
		stage = Kinetic.createStage(div, 800, 800);
		layer = Kinetic.createLayer();
		stage.add(layer);

		final Box2d box2d = new Box2d(10, 10, 200, 200);
		final Rectangle c = Kinetic.createRectangle(box2d);
		c.setCornerRadius(18);
		c.setStroke(Colour.darkcyan);
		c.setStrokeWidth(6);
		c.setRotation(-Math.PI/4);
		layer.add(c);

		Vector2d position = new Vector2d(10, 30);
		Text text = Kinetic.createText(position, Visibility.Friend.getFileName());
		text.setRotation(Math.PI/4);
		text.setFontSize(24);
		layer.add(text);
		
    Animation animation = Kinetic.createAnimation(layer, new Drawable() {

        public void draw(Frame frame) {

        Vector2d position = c.getPosition();

        position.x += 1;

        position.y += 1;

        c.setPosition(position);

    }

});
animation.start();

		stage.draw();
	}
}

