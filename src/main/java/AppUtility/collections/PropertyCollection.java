package AppUtility.collections;

import AppUtility.domains.form.Property.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PropertyCollection implements Collection<Property> {
    private final List<Property> properties;

    public PropertyCollection() {
        this.properties = new ArrayList<>();
    }

    public void add(Property property) {
        this.properties.add(property);
    }

    @Override
    public void addAll(java.util.Collection<? extends Property> properties) {
        this.properties.addAll(properties);
    }

    @Override
    public void addAll(Collection<Property> properties) {
        this.properties.addAll(properties.toList());
    }

    @Override
    public void clear() {
        this.properties.clear();
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
