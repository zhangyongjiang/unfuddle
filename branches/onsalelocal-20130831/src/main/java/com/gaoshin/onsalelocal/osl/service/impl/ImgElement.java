package com.gaoshin.onsalelocal.osl.service.impl;

import org.dom4j.Element;

public class ImgElement implements Comparable<ImgElement>{
	public Element elem;
	public String src;
	public int w;
	public int h;
	
	public ImgElement() {
    }
	
	public ImgElement(int w, int h) {
		this.w = w;
		this.h = h;
    }

	@Override
    public int compareTo(ImgElement o) {
	    return o.w * o.h - w * h;
    }
	
}
