package com.gaoshin.fandroid.contact;

import java.util.ArrayList;
import java.util.List;

import android.provider.ContactsContract;

import com.gaoshin.sorma.annotation.Column;

public class AndroidContact {
	// BaseColumns 
	@Column(name=ContactsContract.Contacts._ID)
	private long id;
	
	// ContactsColumns
	@Column(name=ContactsContract.Contacts.DISPLAY_NAME)
	private String displayName;
	
	// ContactsColumns
	@Column(name=ContactsContract.Contacts.PHOTO_ID)
	private Integer photoId;
	
	// ContactsColumns
	@Column(name=ContactsContract.Contacts.IN_VISIBLE_GROUP)
	private Boolean inVisisbleGroup;
	
	// ContactsColumns
	@Column(name=ContactsContract.Contacts.HAS_PHONE_NUMBER)
	private Boolean hasPhoneNumber;
	
	// ContactsColumns
	@Column(name=ContactsContract.Contacts.LOOKUP_KEY)
	private String lookupKey;
	
	// ContactOptionsColumns
	@Column(name=ContactsContract.Contacts.TIMES_CONTACTED)
	private int timesContacted;
	
	// ContactOptionsColumns
	@Column(name=ContactsContract.Contacts.LAST_TIME_CONTACTED)
	private Long lastTimeContacted;
	
	// ContactOptionsColumns
	@Column(name=ContactsContract.Contacts.STARRED)
	private Boolean starred;
	
	// ContactOptionsColumns
	@Column(name=ContactsContract.Contacts.CUSTOM_RINGTONE)
	private String customRingtone;
	
	// ContactOptionsColumns
	@Column(name=ContactsContract.Contacts.SEND_TO_VOICEMAIL)
	private String sendToVoicemail;
	
	// ContactStatusColumns
	@Column(name=ContactsContract.Contacts.CONTACT_PRESENCE)
	private String contactPresence;
	
	// ContactStatusColumns
	@Column(name=ContactsContract.Contacts.CONTACT_STATUS)
	private String contactStatus;
	
	// ContactStatusColumns
	@Column(name=ContactsContract.Contacts.CONTACT_STATUS_TIMESTAMP)
	private String contactStatusTs;
	
	// ContactStatusColumns
	@Column(name=ContactsContract.Contacts.CONTACT_STATUS_RES_PACKAGE)
	private String contactStatusResPackage;
	
	// ContactStatusColumns
	@Column(name=ContactsContract.Contacts.CONTACT_STATUS_LABEL)
	private String contactStatusLabel;
	
	// ContactStatusColumns
	@Column(name=ContactsContract.Contacts.CONTACT_STATUS_ICON)
	private String contactStatusIcon;

	private List<AndroidContactPhone> phoneList = new ArrayList<AndroidContactPhone>();
	
	//updateContactGroups(cr,cm,contactId);
	//updateContactEmails(cr,cm,contactId);
	//updateContactNotes(cr,cm,contactId);
	//updateContactPostals(cr,cm,contactId);
	//updateContactIms(cr,cm,contactId);
	//updateContactWebsites(cr,cm,contactId);

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDisplayName() {
        return displayName;
	}

    public String getRawDisplayName() {
        return displayName;
    }

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public Boolean getInVisisbleGroup() {
		return inVisisbleGroup;
	}

	public void setInVisisbleGroup(Boolean inVisisbleGroup) {
		this.inVisisbleGroup = inVisisbleGroup;
	}

    public Boolean getHasPhoneNumber() {
        return hasPhoneNumber;
    }

    public boolean hasPhoneNumber() {
        return hasPhoneNumber!=null && hasPhoneNumber==true;
    }

	public void setHasPhoneNumber(Boolean hasPhoneNumber) {
		this.hasPhoneNumber = hasPhoneNumber;
	}

	public String getLookupKey() {
		return lookupKey;
	}

	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}

	public int getTimesContacted() {
		return timesContacted;
	}

	public void setTimesContacted(int timesContacted) {
		this.timesContacted = timesContacted;
	}

	public String getCustomRingtone() {
		return customRingtone;
	}

	public void setCustomRingtone(String customRingtone) {
		this.customRingtone = customRingtone;
	}

	public String getSendToVoicemail() {
		return sendToVoicemail;
	}

	public void setSendToVoicemail(String sendToVoicemail) {
		this.sendToVoicemail = sendToVoicemail;
	}

	public String getContactPresence() {
		return contactPresence;
	}

	public void setContactPresence(String contactPresence) {
		this.contactPresence = contactPresence;
	}

	public String getContactStatus() {
		return contactStatus;
	}

	public void setContactStatus(String contactStatus) {
		this.contactStatus = contactStatus;
	}

	public String getContactStatusTs() {
		return contactStatusTs;
	}

	public void setContactStatusTs(String contactStatusTs) {
		this.contactStatusTs = contactStatusTs;
	}

	public String getContactStatusResPackage() {
		return contactStatusResPackage;
	}

	public void setContactStatusResPackage(String contactStatusResPackage) {
		this.contactStatusResPackage = contactStatusResPackage;
	}

	public String getContactStatusLabel() {
		return contactStatusLabel;
	}

	public void setContactStatusLabel(String contactStatusLabel) {
		this.contactStatusLabel = contactStatusLabel;
	}

	public String getContactStatusIcon() {
		return contactStatusIcon;
	}

	public void setContactStatusIcon(String contactStatusIcon) {
		this.contactStatusIcon = contactStatusIcon;
	}

	public List<AndroidContactPhone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(List<AndroidContactPhone> phoneList) {
		this.phoneList = phoneList;
	}

	public void setStarred(Boolean starred) {
		this.starred = starred;
	}

	public Boolean getStarred() {
		return starred == null ? false : starred;
	}

	public void setLastTimeContacted(Long lastTimeContacted) {
		this.lastTimeContacted = lastTimeContacted;
	}

	public Long getLastTimeContacted() {
		return lastTimeContacted == null ? 0l : lastTimeContacted;
	}

    public boolean isFavorite() {
        return starred != null && starred;
    }
    
}