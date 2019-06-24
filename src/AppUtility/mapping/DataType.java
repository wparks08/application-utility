package AppUtility.mapping;

public enum DataType {
    TEXT { public String toString() { return "Text"; }},
    DATE { public String toString() { return "Date"; }},
    SSN { public String toString() { return "SSN"; }},
    PHONE { public String toString() { return "Phone"; }},
    RADIO { public String toString() { return "Radio"; }},
    CHECKBOX { public String toString() { return "Checkbox"; }},
    INITIAL { public String toString() { return "Initial"; }}
}
