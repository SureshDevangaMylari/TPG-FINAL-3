package selenium_elements_path;

public class RaintreeLocator {
	
	public static String XuserProvider="//input[@id='id.DASH!!5.userId_opnTasks']";//Insert Xpath of user/provider
	public static String XopenTask="//span[@data-index='11']//span[contains(.,'Open')]";//Insert Xpath of Open Task
	public static String XdashboardFilterPopup="";
	public static String XhistoryComments="//*[contains(text(),'History Comments')]/following::div[contains(@class,'w-memo-content')][1]";
	public static String Xtype="//*[@id=\'id.DASH!!7.taskType\']";
	public static String XnextActivityDate="//*[@id=\"id.DASH!!7.taskDueDateDays\"]";
	public static String XcommentSection="//*[@id='id.TASK!!8.comments']";
	public static String XinputDescription="//*[@id=\"id.TASK!!8.emrdesc\"]";
	public static String XpnNumber="//*[@id=\"id.TASK!!8.EMRPNBTR\"]";
	public static String XsendFax="//*[@id=\"id.TASK!!8.send_fax\"]";
	public static String XrecordCount="//*[@id=\"tabcontent\"]/div[8]/div/label[1]";
	public static String XfreeformDescription="//label[@for='id.TASK!!8.freeform']";
	public static String XpayerLabel="//label[normalize-space()='Primary Ins FC']//following::div[1]//div//label[1]";
	

	    public static String XgotoMenu="//div[text()='GoTo Menu']";
	    public static String Xpatientmenu="//span[text()='atient Menu']";
	    public static String Xpatientdemo="//span[text()='atient Demographics']";
	    public static String Xdateofb="//input[@id='id.DEMO!!15.dob^']";
		public static String XuserName = "//input[@name='user']";
		public static String Xpassword = "//input[@name='pass']";
		public static String XloginBtn = "//input[@value='Login']";
		public static String Xacknowledge = "//span[text()='cknowledge']";
		public static String XacknowledgeWarning = "//div[@id='id._MSG_WARNING!!8.btn_0']";
		public static String Xuserid = "//input[@name='userIDLimit']";
	    public static String Xinsincludeexclude="//div[@id='id.DASH!!7.btn_penAuthIns']";

	    
	  

		public static String Xdashboard = "//span[text()='Dash']"; 
		public static String Xauth = "//span[text()='th']";
		public static String XmoreFilters = "//span[contains(normalize-space(.),'More Filters')]";
		public static String Xuser_owner = "//input[@id='id.DASH!!7.userId_penauth']";
		public static String Xinsurance = "//input[@id='id.DASH!!7.penAuthIns']";
		public static String XsubStatus = "//select[@id='id.DASH!!7.penAuthSubStatus']";
	    public static String XcreatedfromFollowup=XsubStatus+"//option[@value='Created from Follow Up']";

		public static String XpendingWithInsurance = XsubStatus + "//option[@value='Pending with Insurance']";
		public static String Xsubstatus = "//*[@name='penAuthSubStatus']";
		public static String Xsubstatusinclude = "//*[@data-widgetname=\"btn_penauthsubstatus\"]";
		public static String XapplyFilters = "//div[contains(@class,'textnode')]//span[contains(.,'pply Filters')]";
		public static String Xnad = "//th[text()='NAD']";
		public static String XinputRowsPath = "(//table[@class='w-listbox-table'])[1]/tbody[1]/tr";
		public static String Xcontinue = "//div[text()='Continue']";
		public static String XnextActivityComment = "//div[@id='id.AUTH!!8.nextActivityComment']";
		public static String XnextActivityComments = XnextActivityComment + "/p";
		public static String Xbrowse = "//span[text()='Browse...']";

		

		

	public static String XAuthnumberinleft = "//input[@id='id.AUTH!!8.auth']";
	public static String XpatientFiles = "//div[@id='id.MMG!!1.2!sPatientFiles']/span[1]/img[1]";
	public static String XfindSearchBtn = "//div[@id='ToolbarButton.&Find']";
	public static String XfindBtn = "//div[@id='ToolbarButton.Find']";
	public static String XfindCriteria = "//input[@id='id._SMARTFIND!!3.fldCriteria']";
	public static String XcontinueBtn = "//*[@data-widgetname=\\'btncontinue\\']";
	public static String Xledger = "//div[@id='id.POP!!4.7!sLedger']";
	public static String XdosSearch = "//*[@id='id.FPOP!!6.2!sDOSSearch']";
	public static String XbeginDosDate = "//input[@id='id._RTDOSFILTERSCREEN!!8.from']";
	public static String XendDosDate = "//input[@id='id._RTDOSFILTERSCREEN!!8.to']";
	public static String SelectOps = "//div[text()='Select']";
	public static String XID = "//div[@data-widgetname='lblsubid']";
	public static String XDOB = "//div[@data-widgetname='lblsubdob']";
	public static String XTAX_ID = "//div[@data-widgetname='lbldoctortaxid']";
	public static String XIND_NPI = "//div[@data-widgetname='lbldoctorlic1']";
	public static String XGROUP_NPI = "//div[@data-widgetname='lbldoctorlic2']";
	public static String XeffectiveDate = "//input[@id='id.AUTH!!8.fdate']";
	public static String XtoDate = "//input[@id='id.AUTH!!8.tdate']";
	public static String XreceivedDate = "//input[@id='id.AUTH!!8.darec']";
	public static String XreceivedVisits = "//input[@id='id.AUTH!!8.receivedVisits']";
	public static String XmembID = "//label[text()='SSID']//following::label[1]";
	public static String XadvanceAuth = "(//label[@for='id.AUTH!!8.modeRadio2'])[2]";
	public static String XchargeTable = "((//*[@class='w-text-row openview'])/div[1])[1]";
	public static String XselectPurpleLineTextSearch_X = "//div[contains(text(),'";
	public static String XselectPurpleLineTextSearch_Y = "')]";
	public static String XStatus = "//*[text()='Status']//following::div[4]";
	public static String XfindpurpleLineSelect = "(//div[contains(@style,' background:var(--menu-variant7); color:rgb(255, 255, 255);')]/ancestor::div[1])[6]//div[contains(text(),'! SMCA2Noridian')]";
	public static String Xactive = "//div[text()='Active']";
	public static String XcaseNbr = "//input[@id='id._RTDOSFILTERSCREEN!!8.case']";
	public static String XsaveBtn = "//div[@id='ToolbarButton.Save']";
	public static String Xdescription = "//div[text()='Description']";
	public static String xOrangeBackground = "//div[@style='text-align: right; background: rgb(255, 174, 174); color: rgb(0, 0, 0);']";
	public static String XPurpleBackground = "//div[@style='text-align: left; background: rgb(150, 148, 197); color: rgb(255, 255, 255);']";
	public static String XbackButton = "//*[@class='backdown']";
	public static String XlogFollowUp = "//div[@id='id.RA!!7.0!sLogFollowUp']";
	public static String XviewCharges = "//div[@id='id.RA!!7.9!sViewcharges']";
	public static String XbillbarColumn = "//div[@id='id._RTCHGPAYSCREEN!!8.mainList']/div[1]/table[1]/tbody[1]/tr[*]/td[";
	public static String XbillbarTable = "//div[@id='id._RTCHGPAYSCREEN!!8.mainList']/div[1]/table[1]//tr";
	public static String XSub_Status = "//*[text()='Sub-status']//following::div[1]";
	public static String XSub_Status_option = "//*[text()='Sub-status']//following::div[1]//select/option[1]";
	public static String XproviderName = "//div[@data-widgetname='lbldoctorname']//div[1]";
	public static String XproviderInd_NPI = "//div[@data-widgetname='lbldoctorlic1']//div[1]";
	public static String XProviderTax_ID = "//div[@data-widgetname='lbldoctortaxid']//div[1]";
	public static String XProviderGroup_NPI = "//div[@data-widgetname='lbldoctorlic2']//div[1]";
	public static String XProviderTaxonomy_Code = "//div[@data-widgetname='lbldoctortxnmycode']//div[1]";
	public static String XProviderGroup_Taxonomy_Code = "//div[@data-widgetname='lbldoctorgtxnmycode']//div[1]";
	public static String XcompletedDate = "//label[text()='Converted - Authorization Complete']//following::div[3]//input";

	public static String XinsuranceName = "//div[@data-widgetname='lblinsname']";
	public static String XinsuranceBill = "//div[@data-widgetname='lblinsaddr1']";
	public static String XinsurancePayorId = "//div[@data-widgetname='lblinspayorid']";

	public static String XinsuranceFc = "//div[@data-widgetname='lblinsfc']";

	public static String XinsurancePhone = "//div[@data-widgetname='lblinsphone']";

	public static String XinsuranceContact = "//div[@data-widgetname='lblinscontact']";

	public static String XopenCase = "//span[text()='Ope']";
	public static String Xdiagnosis = "//input[@name='diag";
	public static String Xdiagnosis2tab = "//span[text()=' 5-8 ']";
	public static String Xdiagnosis3tab = "//span[text()=' 9-C ']";

	public static String XmarketRefCode = "//input[@name='sup_mRefs']";
	public static String XmarketRefName = "//div[@data-widgetname='mlabel2841']//div[1]";
	public static String XbillingRefCode = "//input[@name='billas_REFS']";
	public static String XbillingRefName = "//div[@data-widgetname='mlabel2889']//div[1]";
	public static String XopenAuth = "//div[@id='id._BILLBARFOLLOWUP!!8.bOpenAuth']";
	public static String XsubsRelation = "//*[@data-widgetname='lblreldesc']";
	public static String XsubsfirstName = "//input[@name='sfirst']";
	public static String XsubslastName = "//input[@name='slast']";
	public static String XsubsName = "//input[@name='sname']";

	public static String Xsubsaddress = "//input[@name='addr']";
	public static String Xsubscity = "//input[@name='city']";
	public static String Xsubsstate = "//input[@name='state']";
	public static String Xsubszipcode = "//input[@name='zip']";

	public static String XsubsdateOfBirth = "//input[@name='dob']";
	public static String XsubsGender = "//*[@id='id.PAYOR!!10.m']";
	public static String Xsubssub_ID = "//input[@name='ssid']";
	public static String XsubsGroup_ID = "//input[@name='group']";
	public static String XsubsPlan = "//input[@name='plan']";

	public static String XauthTable = "//*[@id='id.PAYOR!!10.authList']//tr";

	public static String XauthEffectiveDateFrom = "//*[@id='id.AUTH!!13.fdate']";
	public static String XauthEffectiveDateTo = "//input[@name='tdate']";
	public static String Xauthstatus = "//input[@name='status']";
	public static String XauthNo = "//input[@name='auth']";

	public static String XauthType = "//input[@id='id.AUTH!!13.modeRadio2']";

	public static String Xadvancecharges = "//input[@name='proc";

	public static String Xadvancetypes = "//input[@name='visit";
	public static String Xadvanceused = "//input[@name='used";
	public static String Xadvancerems = "//input[@name='rem";

	public static String XsimpleAllowed = "//input[@id='id.AUTH!!13.simpleAllowed']";
	public static String XsimpleUsed = "//input[@id='id.AUTH!!13.simpleUsed']";
	public static String XsimpleRemaining = "//input[@id='id.AUTH!!13.simpleRem']";

	public static String XselectOption = "//div[@id='ToolbarButton.Select']/div[1]";

	public static String Xtable = "//table/tbody/tr";

	// Patient Name column header (click once to sort ascending)
	public static String XpatientNameHeader = "//th[contains(normalize-space(.),'Patient Name')]";

	// Patient Name cell within a task row (4th column: Created, By, Description, Patient Name)
	public static String XpatientNameCellRelative = "./td[4]";

	public static String XNAD = "//th[text()='NAD']";

	public static String XRefresh = "//*[text()='Refresh']";

	public static String Xpatient = "//label[text()='Patient']//following::div[2]//label";

	public static String Xmemid = "//label[text()='SSID']//following::div[2]//label";
	public static String XeffectDate = "//*[@name='fdate']";
	public static String XeffecDateto = "//*[@name='tdate']";
	public static String Xpin = "//label[text()='MR #']//following::div[2]//label";
	public static String XcaseNum = "//input[@name='emrprog']";
	public static String fullname = "//*[text()='Patient']//parent::div/parent::div/following-sibling::*";
	public static String code = "//*[text()='Code']//parent::div/parent::div/following-sibling::*";

	public static String dob = "//*[text()='DOB']//parent::div/parent::div/following-sibling::*";
	
	public static String XserviceLine = "//*[@name='servline']";

	public static String XicdCodes = "//div[@name='reason']/p/span";
	public static String XicdCodesDaig1 = "//*[@name='diag1']";
	public static String XicdCodesDaig2 = "//*[@name='diag2']";
	public static String XicdCodesDaig3 = "//*[@name='diag3']";
	public static String XicdCodesDaig4 = "//*[@name='diag4']";
	public static String Xprovider = "//*[@name='emrloc']";
	public static String XCasereason="//select[@id='id.REHAB!!17.caseReason']"; 
	public static String XpatientChart = "//*[contains(text(),'Patient Chart')]";
	public static String Xauthorization = "//*[text()='Authorization']";
	public static String XauthInput = "//*[@name='auth']";
	public static String XstatusCategory = "//*[@name='statusCategory']";
	public static String XnextActivity = "//*[@name='nextActivity']";
	public static String IreqDate = "id.AUTH!!8.dareq";
	public static String IreqVisit = "id.AUTH!!8.requestedVisits";
	public static String Isave = "//*[@id='ToolbarButton.Save']";
	public static String XprogressNoteType = "//*[@name='notetype']";
	public static String XinsuranceTable = "//*[@id='id.PAYOR!!16.authList']//table//tr";
	public static String Xstatus = "//*[@name='status']";
	public static String authrequired = "//*[@id='id.PAYOR!!16.authReq']";
	public static String Xreceived = "//*[@name='darec']";
	public static String Xsimpleauth = "//*[text()='Simple visit based auth']";
	public static String Xallowed = "//*[@name='simpleAllowed']";
//	public static String XreceivedVisits="//*[@name='receivedVisits']";

	public static String XpendingDate = "//label[text()='Converted - Pending Schedule']//following::div[3]//input";
	public static String XcheckboxCompleted = "//input[@aria-label='Converted - Authorization Complete']//following::label[1]";
	public static String Xcheckboxpending = "//input[@aria-label='Converted - Pending Schedule']//following::label[1]";
	public static String Xfirstname = "//*[@name='first^']";
	public static String Xmidname = "//*[@name='mi']";
	public static String Xlastname = "//*[@name='last^']";
	public static String Xphone = "//*[@name='phone3']";
	public static String Xadd="(//span[text()='dd'])[3]";
	 
	public static String Xcodeadd="//input[@name='code']";
	 
	public static String Xsave="//div[@id='id._RCDS!!9.btnFilterSave']//div[1]";
	 

}