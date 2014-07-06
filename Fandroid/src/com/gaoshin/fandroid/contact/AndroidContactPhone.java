package com.gaoshin.fandroid.contact;

import android.provider.ContactsContract;

import com.gaoshin.sorma.annotation.Column;

public class AndroidContactPhone {
    @Column(name=ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
    private Long contactId;
    
	@Column(name=ContactsContract.CommonDataKinds.Phone.NUMBER)
	private String number;
	
	@Column(name=ContactsContract.CommonDataKinds.Phone.TYPE)
	private int type;
	
	@Column(name=ContactsContract.CommonDataKinds.Phone.LABEL)
	private String label;
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

    public boolean match(String search) {
        if(number != null && number.indexOf(search)!=-1) {
            return true;
        }
        if(label != null && label.toLowerCase().indexOf(search)!=-1) {
            return true;
        }
        return false;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getContactId() {
        return contactId;
    }
}
