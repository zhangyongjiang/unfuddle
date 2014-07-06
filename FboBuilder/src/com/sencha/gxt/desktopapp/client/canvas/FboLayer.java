package com.sencha.gxt.desktopapp.client.canvas;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Line;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.model.Fline;
import com.gaoshin.fbobuilder.client.model.Fnode;

public class FboLayer {
	private Layer klayer;
	private List<Fnode> nodes = new ArrayList<Fnode>();
	private List<Fnode> currents = new ArrayList<Fnode>();
	private FboStage fboStage;

	public FboLayer(FboStage fboStage) {
		this.setFboStage(fboStage);
    }
	
	public Layer getKlayer() {
		return klayer;
	}

	public void setKlayer(Layer klayer) {
		this.klayer = klayer;
	}

	public List<Fnode> getNodes() {
	    return nodes;
    }

	public void setNodes(List<Fnode> nodes) {
	    this.nodes = nodes;
    }

	public List<Fnode> getCurrents() {
	    return currents;
    }

	public void setCurrents(List<Fnode> currents) {
	    this.currents = currents;
    }
	
	public boolean multipleSelection() {
		return currents.size() > 1;
	}

	public FboStage getFboStage() {
	    return fboStage;
    }

	public void setFboStage(FboStage fboStage) {
	    this.fboStage = fboStage;
    }

	public void onNewLine() {
		Fline fline = new Fline(this);
		Vector2d stageSize = fboStage.getStage().getSize();
		fboStage.getStage().getScale();
		
		Vector2d start = new Vector2d(10, 10);
		Vector2d end = new Vector2d(stageSize.x * 0.75, stageSize.y * 0.75);
		Line line = Kinetic.createLine(start, end);
		klayer.add(line);
    }
}
