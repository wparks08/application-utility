package AppUtility.Domains.Form.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyCollection {
    private List<Property> properties;

    public PropertyCollection() {
        this.properties = new ArrayList<>();
    }

    public void add(Property property) {
        this.properties.add(property);
    }

    public Property[] toArray() {
        Property[] propertyArray = new Property[this.properties.size()];
        return this.properties.toArray(propertyArray);
    }

    public Map<String, String> toMap() {
        Map<String, String> propertyMap = new HashMap<>();
        properties.forEach((property -> propertyMap.put(property.getKey(), property.getValue())));
        return propertyMap;
    }
}
