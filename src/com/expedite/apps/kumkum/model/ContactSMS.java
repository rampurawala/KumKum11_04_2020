package com.expedite.apps.kumkum.model;

public class ContactSMS {

	
	// For SMS
	int SMSID;
	int SMS_STUD_ID;
	int SMS_SCHOOL_ID;
	String _SMSTEXt;
	

	// Empty constructor
	public ContactSMS() {

	}
	public ContactSMS(int smsid, String smstext,int studid,int schoolid) {
		this.SMSID = smsid;
		this._SMSTEXt = smstext;
		this.SMS_STUD_ID = studid;
		this.SMS_SCHOOL_ID =  schoolid;
	}
	
	//**********************************************************************************
	// All AboutActivity SMS Work
	
	// getting ID
		public int getSMSID() {
			return this.SMSID;
		}

		// setting id
		public void setSMSID(int sid) {
			this.SMSID = sid;
		}

		// getting SMS Text
		public String getSMSText() {
			return this._SMSTEXt;
		}

		// setting SMSText
		public void setSMSText(String smstext) {
			this._SMSTEXt = smstext;
		}
		
		// getting SMS studid		
		public int getSMSStudid() {
			return this.SMS_STUD_ID;
		}

		// setting SMS id
		public void setSMSStudid(int sid) {
			this.SMS_STUD_ID = sid;
		}
		
		// getting SMS SchoolId	 	
				public int getSMSSchoolId() {
					return this.SMS_SCHOOL_ID;
				}

				// setting SMS id
				public void setSMSSchoolId(int sid) {
					this.SMS_SCHOOL_ID = sid;
				}
	//**********************************************************************************
}
