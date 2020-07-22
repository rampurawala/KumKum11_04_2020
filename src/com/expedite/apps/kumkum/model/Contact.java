package com.expedite.apps.kumkum.model;

public class Contact {

    // private variables
    int _id;
    String _name;
    String _phone_number;
    int logpin;
    int isdef;
    int studentid;
    int schoolid;
    int yearid;
    int classid;
    int secid;

    int userid;
    String _classname;
    String _studenrolldate;
    String _lastupdatedtiom;
    String _academicyear;
    String _updatedtime;
    int _routeid;
    String _classsecname;
    //int _isfeeshow;

    // For SMS
    int MSGID = 0;
    int SMSID;
    int SMS_STUD_ID;
    int SMS_SCHOOL_ID;
    int SMSDAY;
    int SMSMONTH;
    int SMSYEAR;
    int SMSMODID;
    int SMSYEARID;
    String _SMSTEXt;
    String _globaltext;
    int globalnumber;

    // For AcademicYearActivity
    int ROWID;
    int YEAR_ID;
    String YEAR_TEXT;
    int IS_CURRENT;
    int Year_SchoolId;
    int Year_StudId;
    int Year_IsDef;

    // For ExamDetails
    int EXAMROWID;
    int EXAMYEARID;
    int EXAMSRNO;
    int EXAMSTUDID;
    int EXAMSCHOOLID;
    int EXAM_EXAMID;
    String EXAM_EXAMNAME;
    String EXAM_EXAMMARKS;
    int EXAM_ISMARSHEET;
    String EXAM_MARKSHEET_PATH;
    String CATEGORY_ID;
    String CATEGORY_NAME;

    //for practice test data

    String testId;
    String testTicks;
    String testExpireDate;
    long testElpasedTime;
    String testAnsString;
    String testStartTime;

    public String getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(String testStartTime) {
        this.testStartTime = testStartTime;
    }

    public String getTestTicks() {
        return testTicks;
    }

    public void setTestTicks(String testTicks) {
        this.testTicks = testTicks;
    }

    public long getTestElpasedTime() {
        return testElpasedTime;
    }

    public void setTestElpasedTime(long testElpasedTime) {
        this.testElpasedTime = testElpasedTime;
    }

    public String getTestAnsString() {
        return testAnsString;
    }

    public void setTestAnsString(String testAnsString) {
        this.testAnsString = testAnsString;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestExpireDate() {
        return testExpireDate;
    }

    public void setTestExpireDate(String testExpireDate) {
        this.testExpireDate = testExpireDate;
    }

    public String getTestExpireDateAfterStart() {
        return testExpireDateAfterStart;
    }

    public void setTestExpireDateAfterStart(String testExpireDateAfterStart) {
        this.testExpireDateAfterStart = testExpireDateAfterStart;
    }

    String testExpireDateAfterStart;

    public Contact(String testId, String testExpireDateAfterStart,int schoolId,int studId) {
        this.testId = testId;
        this.testExpireDateAfterStart = testExpireDateAfterStart;
        this.schoolid=schoolId;
        this.studentid=studId;
    }
    public Contact(String testId,int schoolid,int studentid) {
        this.testId = testId;
        this.schoolid = schoolid;
        this.studentid = studentid;
    }
    // For StudentAlbumDetails

    int AlbumStudId;
    int Album_AlbumId;
    String Album_AlbumName;
    String Album_Albumurl;
    String Album_PhotoFileName;
    int AlbumSchoolId;
    int AlbumClassSecId;
    long AlbumDateticks;
    String AlbumDateTime;

    // For Circular
    int cirstudid;
    int cirschoolid;
    int ciryearid;
    int cirgroupid;
    String cirGroupname;
    int CirId;
    String CirName;
    String cirDateText;
    String cirPath;
    String cirTicks;
    int cirisdeleted;

    // Empty constructor
    public Contact() {

    }

    // constructor
    public Contact(int id, String name, String _phone_number, int _logpin,
                   int _isdef, int _studid, int _schoolid, int _yearid, int _classid,
                   int _secid, int _userid, String _classname, String _studenrolldate,
                   String _lastupdatedtime, String _academicyear, String _updtime,
                   int _Routeid, String ClassSectionName) {
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this.logpin = _logpin;
        this.isdef = _isdef;
        this.studentid = _studid;
        this.schoolid = _schoolid;
        this.yearid = _yearid;
        this.classid = _classid;
        this.secid = _secid;
        this.userid = _userid;
        this._classname = _classname;
        this._studenrolldate = _studenrolldate;
        this._lastupdatedtiom = _lastupdatedtime;
        this._academicyear = _academicyear;
        this._updatedtime = _updtime;
        this._routeid = _Routeid;
        this._classsecname = ClassSectionName;
        //this._isfeeshow = isfeeshow;
    }

    // constructor
    public Contact(String name, String _phone_number, int _logpin, int _isdef,
                   int _studid, int _schoolid, int _yearid, int _classid, int _secid,
                   int _userid, String _classname, String _studenrolldate,
                   String _lastupdatedtime, String _academicyear, String updatedtime,
                   int RouteId) {
        this._name = name;
        this._phone_number = _phone_number;
        this.logpin = _logpin;
        this.isdef = _isdef;
        this.studentid = _studid;
        this.schoolid = _schoolid;
        this.yearid = _yearid;
        this.classid = _classid;
        this.secid = _secid;
        this.userid = _userid;
        this._classname = _classname;
        this._studenrolldate = _studenrolldate;
        this._lastupdatedtiom = _lastupdatedtime;
        this._academicyear = _academicyear;
        this._updatedtime = updatedtime;
        this._routeid = RouteId;
    }

    // constructor with class sec name

    public Contact(String name, String _phone_number, int _logpin, int _isdef,
                   int _studid, int _schoolid, int _yearid, int _classid, int _secid,
                   int _userid, String _classname, String _studenrolldate,
                   String _lastupdatedtime, String _academicyear, String updatedtime,
                   int RouteId, String sectionname) {
        this._name = name;
        this._phone_number = _phone_number;
        this.logpin = _logpin;
        this.isdef = _isdef;
        this.studentid = _studid;
        this.schoolid = _schoolid;
        this.yearid = _yearid;
        this.classid = _classid;
        this.secid = _secid;
        this.userid = _userid;
        this._classname = _classname;
        this._studenrolldate = _studenrolldate;
        this._lastupdatedtiom = _lastupdatedtime;
        this._academicyear = _academicyear;
        this._updatedtime = updatedtime;
        this._routeid = RouteId;
        this._classsecname = sectionname;
        //this._isfeeshow = isfeeshow;
    }

    public Contact(int yearid, String yeartext, int iscurrent,
                   int year_schoolid, int year_studid, int year_isdef) {
        this.YEAR_ID = yearid;
        this.YEAR_TEXT = yeartext;
        this.IS_CURRENT = iscurrent;
        this.Year_SchoolId = year_schoolid;
        this.Year_StudId = year_studid;
        this.Year_IsDef = year_isdef;
    }

    public Contact(int smsid, String smstext, int studid, int schoolid,
                   int smsday, int smsmonth, int smsyear, long moduleid, int yearid) {
        this.SMSID = smsid;
        this._SMSTEXt = smstext;
        this.SMS_STUD_ID = studid;
        this.SMS_SCHOOL_ID = schoolid;
        this.SMSDAY = smsday;
        this.SMSMONTH = smsmonth;
        this.SMSYEAR = smsyear;
        this.SMSMODID = (int) moduleid;
        this.SMSYEARID = yearid;
    }

    public Contact(int yearid, int studid, int schoolid, int examid,
                   String ExamName, String ExamMarks, int IsMarkshhet,
                   String MarksheetPath, String CategoryId, String CategoryName, int SrNo) {
        this.EXAMYEARID = yearid;
        this.EXAMSTUDID = studid;
        this.EXAMSCHOOLID = schoolid;
        this.EXAM_EXAMID = examid;
        this.EXAMSRNO = SrNo;
        this.EXAM_EXAMNAME = ExamName;
        this.EXAM_EXAMMARKS = ExamMarks;
        this.EXAM_ISMARSHEET = IsMarkshhet;
        this.EXAM_MARKSHEET_PATH = MarksheetPath;
        this.CATEGORY_ID = CategoryId;
        this.CATEGORY_NAME = CategoryName;
    }

    public Contact(int studid, int albumid, String albumname, String albumurl,
                   String photofilename, int schoolid, int classsecid, long Dateticks,
                   String DateTime) {
        this.AlbumStudId = studid;
        this.Album_AlbumId = albumid;
        this.Album_AlbumName = albumname;
        this.Album_Albumurl = albumurl;
        this.Album_PhotoFileName = photofilename;
        this.AlbumSchoolId = schoolid;
        this.AlbumClassSecId = classsecid;
        this.AlbumDateticks = Dateticks;
        this.AlbumDateTime = DateTime;
    }

    public Contact(int studid, int schoolid, int yearid, int cirgroupid,
                   String cirGroupname, int CirId, String CirName, String cirDateText,
                   String cirPath, String Ticks, int isdeleted) {
        this.cirstudid = studid;
        this.cirschoolid = schoolid;
        this.ciryearid = yearid;
        this.cirgroupid = cirgroupid;
        this.cirGroupname = cirGroupname;
        this.CirId = CirId;
        this.CirName = CirName;
        this.cirDateText = cirDateText;
        this.cirPath = cirPath;
        this.cirTicks = Ticks;
        this.cirisdeleted = isdeleted;

    }

    public Contact(String name, String entered_phoneNo, int i, int i1, int i2, int i3,
                   int i4, int i5, String classname, String studentenrolldate, String lastupdatedtime,
                   String academicyear, String updatedtime, int routeId, String classSecName) {
        this._name = name;
        this._phone_number = entered_phoneNo;
        this.studentid = i;
        this.schoolid = i1;
        this.yearid = i2;
        this.classid = i3;
        this.secid = i4;
        this.userid = i5;
        this._classname = classname;
        this._studenrolldate = studentenrolldate;
        this._lastupdatedtiom = lastupdatedtime;
        this._academicyear = academicyear;
        this._updatedtime = updatedtime;
        this._routeid = routeId;
        this._classsecname = classSecName;
    }

    public Contact(String fifth, String mobile, Integer isdef, String second, String third,
                   String sixth, String seventh, String eighth, String nineth, String Eleventh, Object sixteen, String tenth) {
        this._name = fifth;
        this._phone_number = mobile;
        this.isdef = isdef;
        this.studentid = Integer.parseInt(second);
        this.schoolid = Integer.parseInt(third);
        this.yearid = Integer.parseInt(sixth);
        this.classid = Integer.parseInt(seventh);
        this.secid = Integer.parseInt(eighth);
        this._classname = nineth;
        this._studenrolldate = Eleventh;
        this._lastupdatedtiom = String.valueOf(sixteen);
        this._academicyear = tenth;

    }

    // Circular Details

    public int getcirstudid() {
        return cirstudid;
    }

    public void setcirstudid(int stdid) {
        this.cirstudid = stdid;
    }

	/*public int getisfeeshow() {
		return _isfeeshow;
	}

	public void setisfeeshow(int isfeeshow) {
		this._isfeeshow = isfeeshow;
	}*/

    public int getcirschoolid() {
        return cirschoolid;
    }

    public void setcirschoolid(int cirschoolid) {
        this.cirschoolid = cirschoolid;
    }

    public int getciryearid() {
        return ciryearid;
    }

    public void setciryearid(int ciryearid) {
        this.ciryearid = ciryearid;
    }

    public int getcirgroupid() {
        return cirgroupid;
    }

    public void setcirgroupid(int cirgroupid) {
        this.cirgroupid = cirgroupid;
    }

    public String getcirGroupname() {
        return cirGroupname;
    }

    public void setcirGroupname(String cirGroupname) {
        this.cirGroupname = cirGroupname;
    }

    public int getCirId() {
        return CirId;
    }

    public void setCirId(int CirId) {
        this.CirId = CirId;
    }

    public String getCirName() {
        return CirName;
    }

    public void setCirName(String CirName) {
        this.CirName = CirName;
    }

    public String getcirDateText() {
        return cirDateText;
    }

    public void setcirDateText(String cirDateText) {
        this.cirDateText = cirDateText;
    }

    public String getcirPath() {
        return cirPath;
    }

    public void setcirPath(String cirPath) {
        this.cirPath = cirPath;
    }

    public String getcirTicks() {
        return cirTicks;
    }

    public void setcirTicks(String cirTicks) {
        this.cirTicks = cirTicks;
    }

    public int getcirisdeleted() {
        return cirisdeleted;
    }

    public void setcirisdeleted(int cirisdeleted) {
        this.cirisdeleted = cirisdeleted;
    }

    // Album Details

    // Get AlbumStudentId

    public int getAlbumStudId() {
        return AlbumStudId;
    }

    // setting id
    public void setAlbumStudId(int stdid) {
        this.AlbumStudId = stdid;
    }

    public int getAlbumId() {
        return Album_AlbumId;
    }

    // setting id
    public void setAlbumId(int albmid) {
        this.Album_AlbumId = albmid;
    }

    public String getAlbumName() {
        return this.Album_AlbumName;
    }

    public String getAlbumurl() {
        return this.Album_Albumurl;
    }

    public void setAlbumurl(String url) {
        this.Album_Albumurl = url;
    }

    // setting id
    public void setAlbumName(String aname) {
        this.Album_AlbumName = aname;
    }

    public String getAlbumPhotofile() {
        return this.Album_PhotoFileName;
    }

    // setting id
    public void setAlbumPhotofile(String photoname) {
        this.Album_PhotoFileName = photoname;
    }

    public int getAlbumSchoolId() {
        return this.AlbumSchoolId;
    }

    // setting id
    public void setAlbumSchoolId(int aschoolid) {
        this.AlbumSchoolId = aschoolid;
    }

    public int getAlbumClassSecId() {
        return this.AlbumClassSecId;
    }

    // setting id
    public void setAlbumClassSecId(int aclasssecid) {
        this.AlbumClassSecId = aclasssecid;
    }

    public long getAlbumdateticks() {
        return this.AlbumDateticks;
    }

    // setting id
    public void setAlbumdateticks(long albumdatetics) {
        this.AlbumDateticks = albumdatetics;
    }

    public String getAlbumDatetime() {
        return this.AlbumDateTime;
    }

    // setting id
    public void setAlbumDatetime(String albumdatetime) {
        this.AlbumDateTime = albumdatetime;
    }

    // End Album Details Propery Assign

    // **********************************************************************************
    // All AboutActivity AcademicYearActivity Work

    public int getExamSrNo() {
        return this.EXAMSRNO;
    }

    public void setExamSrNo(int srNo) {
        this.EXAMSRNO = srNo;
    }
    // Get Exam YearID

    public int getExamYearId() {
        return this.EXAMYEARID;
    }

    // setting id
    public void setExamYearId(int yid) {
        this.EXAMYEARID = yid;
    }

    //

    // Get Exam SchoolId
    public int getExamSchoolId() {
        return this.EXAMSCHOOLID;
    }

    // setting id
    public void setExamSchoolId(int yid) {
        this.EXAMSCHOOLID = yid;
    }

    //

    // Get Exam StudentId

    public int getExamStudId() {
        return this.EXAMSTUDID;
    }

    // setting id
    public void setExamStudId(int yid) {
        this.EXAMSTUDID = yid;
    }

    //

    // Get Exam ExamId

    public int getExamId() {
        return this.EXAM_EXAMID;
    }

    // setting id
    public void setExamId(int yid) {
        this.EXAM_EXAMID = yid;
    }

    //

    // Get Exam ExamName

    public String getExamName() {
        return this.EXAM_EXAMNAME;
    }

    // setting id
    public void setExamName(String ename) {
        this.EXAM_EXAMNAME = ename;
    }

    //

    // Get Exam Exammarks

    public String getExamMarks() {
        return this.EXAM_EXAMMARKS;
    }

    // setting id
    public void setExamMarks(String ename) {
        this.EXAM_EXAMMARKS = ename;
    }

    //

    // Get Exam Ismarksheet

    public int getExamIsMarksheet() {
        return this.EXAM_ISMARSHEET;
    }

    // setting id
    public void setExamIsMarksheet(int ismarksheet) {
        this.EXAM_ISMARSHEET = ismarksheet;
    }

    //

    // Get Exam MarksheetPath

    public String getExamMarksheetPath() {
        return this.EXAM_MARKSHEET_PATH;
    }

    // setting id
    public void setExamMarksheetPath(String path) {
        this.EXAM_MARKSHEET_PATH = path;
    }

    public String getCATEGORY_ID() {
        return this.CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID = CATEGORY_ID;
    }

    public String getCATEGORY_NAME() {
        return this.CATEGORY_NAME;
    }

    public void setCATEGORY_NAME(String CATEGORY_NAME) {
        this.CATEGORY_NAME = CATEGORY_NAME;
    }

    public int getYearSchoolId() {
        return this.Year_SchoolId;
    }

    // setting id
    public void setYearSchoolId(int yid) {
        this.Year_SchoolId = yid;
    }

    public int getYearStudId() {
        return this.Year_StudId;
    }

    // setting id
    public void setYearStudId(int yid) {
        this.Year_StudId = yid;
    }

    public int getROWID() {
        return this.ROWID;
    }

    // setting id
    public void setROWID(int yid) {
        this.ROWID = yid;
    }

    public int getYEAR_ID() {
        return this.YEAR_ID;
    }

    // setting id
    public void setYEAR_ID(int yid) {
        this.YEAR_ID = yid;
    }

    public int getISCURRENT() {
        return this.IS_CURRENT;
    }

    // setting id
    public void setISCURRENT(int yid) {
        this.IS_CURRENT = yid;
    }

    public String getYEAR_TEXT() {
        return this.YEAR_TEXT;
    }

    // setting id
    public void setYEAR_TEXT(String ytext) {
        this.YEAR_TEXT = ytext;
    }

    public int getISDefAcademicYear() {
        return this.Year_IsDef;
    }

    // setting id
    public void setISDefAcademicYear(int yearid) {
        this.Year_IsDef = yearid;
    }

    // **********************************************************************************
    // All AboutActivity SMS Work

    public int getGlobalNumber() {
        return this.globalnumber;
    }

    // setting id
    public void setGlobalNumber(int yid) {
        this.globalnumber = yid;
    }

    public String getGlobalText() {
        return this._globaltext;
    }

    // setting id
    public void setGlobalText(String globtext) {
        this._globaltext = globtext;
    }

    public int getSMSYEARID() {
        return this.SMSYEARID;
    }

    // setting id
    public void setSMSYEARID(int yid) {
        this.SMSYEARID = yid;
    }

    public int getSMSDAY() {
        return this.SMSDAY;
    }

    // setting id
    public void setSMSDAY(int sid) {
        this.SMSDAY = sid;
    }

    public int getSMSMONTH() {
        return this.SMSMONTH;
    }

    // setting id
    public void setSMSMONTH(int sid) {
        this.SMSMONTH = sid;
    }

    public int getSMSYEAR() {
        return this.SMSYEAR;
    }

    // setting id
    public void setSMSYEAR(int sid) {
        this.SMSYEAR = sid;
    }

    public int getSMSMODULEID() {
        return this.SMSMODID;
    }

    // setting id
    public void setSMSMODULEID(int sid) {
        this.SMSMODID = sid;
    }

    // getting ID
    public int getSMSID() {
        return this.SMSID;
    }

    // setting id
    public void setSMSID(int sid) {
        this.SMSID = sid;
    }

    // getting ID
    public int getMSGID() {
        return this.MSGID;
    }

    // setting id
    public void setMSGID(int sid) {
        this.MSGID = sid;
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

    // **********************************************************************************

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber() {
        return this._phone_number;
    }

    // setting phone number
    public void setPhoneNumber(String phone_number) {
        this._phone_number = phone_number;
    }

    // getting loginpin
    public int getLogPin() {
        return this.logpin;
    }

    // setting id
    public void setLogPin(int logpin) {
        this.logpin = logpin;
    }

    // getting Isdef
    public int getIsDef() {
        return this.isdef;
    }

    // setting id
    public void setIsDef(int isDef) {
        this.isdef = isDef;
    }

    // getting studentid
    public int getStudentId() {
        return this.studentid;
    }

    // setting id
    public void setStudnetId(int studid) {
        this.studentid = studid;
    }

    // getting SchoolId
    public int getSchoolId() {
        return this.schoolid;
    }

    // setting id
    public void SetSchoolId(int schoolid) {
        this.schoolid = schoolid;
    }

    // getting studentid
    public int getYearId() {
        return this.yearid;
    }

    // setting id
    public void SetYearId(int yearid) {
        this.yearid = yearid;
    }

    // getting classid
    public int getClassId() {
        return this.classid;
    }

    // setting id
    public void SetClassId(int classid) {
        this.classid = classid;
    }

    // getting secid
    public int getSecId() {
        return this.secid;
    }

    // setting id
    public void SetSecId(int Secid) {
        this.secid = Secid;
    }

    // getting secid
    public int getUserId() {
        return this.userid;
    }

    // setting id
    public void SetUserId(int userid) {
        this.userid = userid;
    }

    // getting Classname
    public String getClassName() {
        return this._classname;
    }

    // setting name
    public void setClassName(String classname) {
        this._classname = classname;
    }

    // getting enrolldate
    public String getstudentenrolldate() {
        return this._studenrolldate;
    }

    // setting name
    public void setstudentenrolldate(String studenrolldate) {
        this._studenrolldate = studenrolldate;
    }

    // getting enrolldate
    public String getlastupdatedtime() {
        return this._lastupdatedtiom;
    }

    // setting name
    public void setlastupdatedtime(String lastuptm) {
        this._lastupdatedtiom = lastuptm;
    }

    public String getAcademicyear() {
        return this._academicyear;
    }

    // setting name
    public void setAcademicyear(String acayear) {
        this._academicyear = acayear;
    }

    // getting updatedtime
    public String getUpdatedtime() {
        return this._updatedtime;
    }

    // setting updatedtime
    public void setUpdatedtime(String updatedtime) {
        this._updatedtime = updatedtime;
    }

    // getting RouteId
    public int getRouteId() {
        return this._routeid;
    }

    // setting RouteId
    public void setRouteId(int routeid) {
        this._routeid = routeid;
    }

    public String getClassSecName() {
        return this._classsecname;
    }

    public void setClassSecName(String classecname) {
        this._classsecname = classecname;
    }


   /* public String toString(){
        return testId*//* + ":" + testExpireDateAfterStart*//*;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((testId == null) ? 0 : testId.hashCode());
        //  result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contact other = (Contact) obj;
       *//* if (id != other.id)
            return false;*//*
        if (testId == null) {
            if (other.testId != null)
                return false;
        } else if (!testId.equals(other.testId))
            return false;
        return true;
    }
*/

    public String toString(){
        return schoolid+":"+studentid + ":" + testId;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + schoolid;
        result = prime * result + studentid;
        result = prime * result + ((testId == null) ? 0 : testId.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contact other = (Contact) obj;
        if (schoolid != other.schoolid)
            return false;
        if (studentid != other.studentid)
            return false;
        if (testId == null) {
            if (other.testId != null)
                return false;
        } else if (!testId.equals(other.testId))
            return false;
        return true;
    }







}
