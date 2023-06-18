package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.combobox.ComboBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class SurrogateComboBox extends ComboBox {

    private Map<String, Map<String, String>> surrogateModelMap;
    private ComboBox masterComboBox;

    public SurrogateComboBox(Map<String, Map<String, String>> surrogateModelMap, ComboBox<String> masterComboBox, String label) {
        super(label);
        this.surrogateModelMap = surrogateModelMap;
        this.masterComboBox = masterComboBox;
        SurrogateComboBox thisCombo = this;
        masterComboBox.addValueChangeListener(e -> {
           String value = e.getValue();
           Map<String,String> currentMap = getCurrentMap(value);
           thisCombo.setItems(getItemKeys(value));
           thisCombo.setItemLabelGenerator(e1 -> e1 + " - " + currentMap.get(e1));
        });
    }

    private Map<String, String> getCurrentMap(String masterComboValue) {
        Map<String,String> currentMap = surrogateModelMap.get(masterComboValue);
        if (currentMap == null) {
            currentMap = new HashMap<>();
        }
        return currentMap;
    }

    private List<String> getItemKeys(String masterComboValue) {
        Map<String,String> currentMap = getCurrentMap(masterComboValue);
        List<String> itemKeys = currentMap.entrySet().stream().sorted((e1, e2) -> {
            if (e1.getValue().equals(e2.getValue())) {
                return e1.getKey().compareTo(e2.getKey());
            } else {
                return e1.getValue().compareTo(e2.getValue());
            }
        }).map( e -> e.getKey()).collect(Collectors.toList());
        return itemKeys;
    }
}
