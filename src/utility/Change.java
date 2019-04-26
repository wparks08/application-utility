package utility;

public class Change {

    private String oldValue;
    private String newValue;
    private ChangeType changeType;
    private String ssn;

    public Change() {}

    public Change(String ssn, String oldValue, String newValue, ChangeType changeType) {
        this.ssn = ssn;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeType = changeType;
    }

    public Change(String ssn, String newValue, ChangeType changeType) {
        this.ssn = ssn;
        this.newValue = newValue;
        this.changeType = changeType;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
