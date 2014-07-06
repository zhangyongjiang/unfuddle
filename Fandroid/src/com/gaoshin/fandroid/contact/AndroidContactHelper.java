package com.gaoshin.fandroid.contact;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.gaoshin.sorma.SormaContentResolver;
import com.gaoshin.sorma.annotation.AnnotatedDbTable;

public class AndroidContactHelper {
    private static AnnotatedDbTable<AndroidContact> contactTable = null;
    private static AnnotatedDbTable<AndroidContactPhone> phoneTable;
    static {
        try {
            contactTable = new AnnotatedDbTable(AndroidContact.class);
            phoneTable = new AnnotatedDbTable(AndroidContactPhone.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static AndroidContact getContactById(SormaContentResolver contentResolver, Long contactId) {
        String uri = "content://com.android.contacts/contacts";
        if(ContactsContract.Contacts.CONTENT_URI!=null)
            uri = ContactsContract.Contacts.CONTENT_URI.toString();
        Cursor cursor = contentResolver.query(uri, null, "_id=?", new String[]{contactId+""}, null);
        if(cursor!=null) {
            try {
                AndroidContact contact = contactTable.getObjectFromRow(cursor);
                return contact;
            } catch (Exception e) {
            }
            finally {
                try {
                    cursor.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }
    
    public static List<AndroidContact> recents(SormaContentResolver contentResolver, int max)
			throws Exception {
		String uri = "content://com.android.contacts/contacts";
		if(ContactsContract.Contacts.CONTENT_URI!=null)
		    uri = ContactsContract.Contacts.CONTENT_URI.toString();
        String orderBy = ContactsContract.Contacts.LAST_TIME_CONTACTED + " desc";
        Cursor cursor = contentResolver.query(uri, null, null, null, orderBy);
        List<AndroidContact> recents = new ArrayList<AndroidContact>();
		try {
			while (cursor.moveToNext() && max>0) {
				AndroidContact contact = contactTable.getObjectFromRow(cursor);
				if(contact.getHasPhoneNumber() == null || contact.getHasPhoneNumber() == false) {
				    continue;
		        }
                List<AndroidContactPhone> phoneList = getPhoneListByContactId(contentResolver, contact.getId());
				if(phoneList == null || phoneList.size() == 0) {
				    continue;
				}
				contact.setPhoneList(phoneList);
                recents.add(contact);
				max--;
			}
            return recents;
		} finally {
			try {
				if (cursor != null)
					cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    private static List<AndroidContactPhone> getPhoneListByContactId(
            SormaContentResolver contentResolver, long contactId) throws Exception {
        List<AndroidContactPhone> list = new ArrayList<AndroidContactPhone>();
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString(), null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[] { contactId + "" }, null);
        try {
            while (cursor.moveToNext()) {
                AndroidContactPhone phone = phoneTable.getObjectFromRow(cursor);
                list.add(phone);
            }
        } finally {
            try {
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    
}
