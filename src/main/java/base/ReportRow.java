package base;

public class ReportRow {

    private String date;
    private String pnNumber;
    private String dob;
    private String patientName;
    private String location;
    private String noOfAttempts;
    private String noridianLetter;
    private String inLieuForm;
    private String claimStatus;
    private String softwareNotes;

    public ReportRow() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPnNumber() {
        return pnNumber;
    }

    public void setPnNumber(String pnNumber) {
        this.pnNumber = pnNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(String noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    public String getNoridianLetter() {
        return noridianLetter;
    }

    public void setNoridianLetter(String noridianLetter) {
        this.noridianLetter = noridianLetter;
    }

    public String getInLieuForm() {
        return inLieuForm;
    }

    public void setInLieuForm(String inLieuForm) {
        this.inLieuForm = inLieuForm;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getSoftwareNotes() {
        return softwareNotes;
    }

    public void setSoftwareNotes(String softwareNotes) {
        this.softwareNotes = softwareNotes;
    }

	@Override
	public String toString() {
		return "ReportRow [date=" + date + ", pnNumber=" + pnNumber + ", dob=" + dob + ", patientName=" + patientName
				+ ", location=" + location + ", noOfAttempts=" + noOfAttempts + ", noridianLetter=" + noridianLetter
				+ ", inLieuForm=" + inLieuForm + ", claimStatus=" + claimStatus + ", softwareNotes=" + softwareNotes
				+ "]";
	}
    
    
}