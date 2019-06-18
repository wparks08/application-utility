package AppUtility.mapping;

public enum DataType {
    TEXT { public String toString() { return "Text"; }},
    DATE { public String toString() { return "Date"; }},
    SSN { public String toString() { return "SSN"; }},
    PLAN { public String toString() { return "Plan"; }},
    RADIO { public String toString() { return "Radio"; }},
    CHECKBOX { public String toString() { return "Checkbox"; }}
}
