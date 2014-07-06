package common.util.web;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY) 
@XmlType(propOrder="")
public class Items <T> {
	private List<T> item;
	
	public Items() {
	}

	public Items(List<T> item) {
		setItem(item);
	}

	public void setItem(List<T> item) {
		this.item = item;
	}

	public List<T> getItem() {
		return item;
	}
}
