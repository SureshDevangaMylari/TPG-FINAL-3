package base;

public class ErrorReportRow {
	    private String date;
	    private String pnNumber;
	    private String location;
	    private String errorMessage; 
	    
	    public ErrorReportRow() {
			// TODO Auto-generated constructor stub
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
	    
	    public String getLocation() {
	        return location;
	    }

	    public void setLocation(String location) {
	        this.location = location;
	    }
	    
	    public String getErrorMessage(){
	    	return errorMessage;
	    }
        public void setErrorMessage(String errorMessage) {
	        this.errorMessage = errorMessage;
	    }

}
