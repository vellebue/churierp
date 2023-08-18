package org.bastanchu.churierp.churierpweb.view.administration.types;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.treegrid.TreeGrid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bastanchu.churierp.churierpback.dto.administration.types.AreaDto;
import org.bastanchu.churierp.churierpback.dto.administration.types.SubtypeDto;
import org.bastanchu.churierp.churierpback.dto.administration.types.TypeDto;
import org.bastanchu.churierp.churierpback.dto.administration.types.TypedEntityDto;
import org.bastanchu.churierp.churierpback.service.administration.TypesSubtypesService;
import org.bastanchu.churierp.churierpweb.component.tab.TabbedContainer;
import org.bastanchu.churierp.churierpweb.component.tab.TabbedListener;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;

public class TypesSubtypesView extends BodyView {

    private Logger logger = LoggerFactory.getLogger(TypesSubtypesView.class);

    @Autowired
    private TypesSubtypesService typesSubtypesService;

    private Map<Integer, Map<Integer, TreeGrid<TreeGridItem>>> treeGridsMap = new HashMap<>();
    private Map<Integer, Map<Integer, List<TreeGridItem>>> treeGridsModelMap = new HashMap<>();
    List<AreaDto> areasList = null;

    private Div mainPanel = new Div();
    private TypesPanel typesPanel = null;
    private SubtypesPanel subtypesPanel = null;

    private TypesPanelHandler typesPanelHandler;
    private SubtypesPanelHandler subtypesPanelHandler;

    public TypesSubtypesView(ApplicationContext appContext) {
        super(appContext);
        String title = getMessageSource().getMessage("churierpweb.administration.types.title",
                null, LocaleContextHolder.getLocale());
        setBodyTitle(title);
        String iconText = getMessageSource().getMessage("churierpweb.administration.types.icon.text",
                null, LocaleContextHolder.getLocale());
        String iconColor = getMessageSource().getMessage("churierpweb.administration.types.icon.color",
                null, LocaleContextHolder.getLocale());
        setBoydIcon(iconText, iconColor);
        mainPanel.getStyle().set("min-width","100%");
        add(mainPanel);
    }

    @Override
    protected void onInit() {
        areasList = typesSubtypesService.getFullTypesData();
        typesPanel = new TypesPanel(getMessageSource());
        typesPanelHandler = new TypesPanelHandler(typesPanel);
        typesPanelHandler.setTypesSubtypesService(typesSubtypesService);
        typesPanelHandler.setTreeGridsMap(treeGridsMap);
        typesPanelHandler.setTreeGridsModelMap(treeGridsModelMap);
        typesPanel.setTypesListener(typesPanelHandler);
        subtypesPanel = new SubtypesPanel(getMessageSource());
        subtypesPanelHandler = new SubtypesPanelHandler(subtypesPanel);
        subtypesPanel.setSubtypesListener(subtypesPanelHandler);
        TabbedContainer tabbedContainer = buildTabbedContainer(areasList);
        mainPanel.add(tabbedContainer);
        mainPanel.add(typesPanel);
        mainPanel.add(subtypesPanel);
    }

    @Override
    protected void onEnding() {
        mainPanel.removeAll();
    }

    private TabbedContainer buildTabbedContainer(List<AreaDto> areas) {
        TabbedContainer tabbedContainer = new TabbedContainer();
        Map<String, AreaDto> areasDtoMap = new HashMap<>();
        for (AreaDto area : areas) {
            Div accordionDiv = new Div();
            accordionDiv.setMinHeight((float) 15.5, Unit.PICAS);
            accordionDiv.add(buildAreaAccordion(area));
            tabbedContainer.addTab(area.getDescription(), accordionDiv);
            areasDtoMap.put(area.getDescription(), area);
        }
        typesPanelHandler.setAreasDtoMap(areasDtoMap);
        subtypesPanelHandler.setAreasDtoMap(areasDtoMap);
        tabbedContainer.setTabbedListener(e -> {
            typesPanelHandler.onSelectedTab(e);
            subtypesPanelHandler.onSelectedTab(e);
        });
        typesPanelHandler.onSelectedTab(tabbedContainer.getSelectedTab());
        subtypesPanelHandler.onSelectedTab(tabbedContainer.getSelectedTab());
        return tabbedContainer;
    }

    private Accordion buildAreaAccordion(AreaDto area) {
        Map<Integer, TreeGrid<TreeGridItem>> treeGridsAreaMap = new HashMap<>();
        treeGridsMap.put(area.getId(), treeGridsAreaMap);
        Map<Integer, List<TreeGridItem>> treeGridsModelAreaMap = new HashMap<>();
        treeGridsModelMap.put(area.getId(), treeGridsModelAreaMap);
        Accordion accordion = new Accordion();
        Map<String, TypedEntityDto> typedEntityMap = new HashMap<>();
        for (TypedEntityDto typedEntity : area.getTypedEntities()) {
            accordion.add(typedEntity.getDescription(), buildTypedEntityTreeGrid(typedEntity));
            typedEntityMap.put(typedEntity.getDescription(), typedEntity);
        }
        accordion.setMaxHeight((float) 13.0, Unit.PICAS);
        accordion.close();
        accordion.addOpenedChangeListener(e -> {
            if (e.getOpenedPanel().isPresent()) {
                typesPanelHandler.onSelectedEntityId(typedEntityMap.get(e.getOpenedPanel().get().getSummaryText()).getId());
                subtypesPanelHandler.onSelectedEntityId(typedEntityMap.get(e.getOpenedPanel().get().getSummaryText()).getId());
            }
        });
        return accordion;
    }

    private TreeGrid<TreeGridItem> buildTypedEntityTreeGrid(TypedEntityDto typedEntityDto) {
        Map<Integer, TreeGrid<TreeGridItem>> treeGridsAreaMap = treeGridsMap.get(typedEntityDto.getAreaId());
        List<TreeGridItem> model = buildTreeGridModel(typedEntityDto);
        Map<Integer, List<TreeGridItem>> treGridsModelAreaMap = treeGridsModelMap.get(typedEntityDto.getAreaId());
        TreeGrid<TreeGridItem> treeGrid = new TreeGrid<>();
        treeGrid.setMaxHeight((float) 12.5 , Unit.PICAS);
        treeGrid.setItems(model, TreeGridItem::getChildren);
        treeGrid.addHierarchyColumn(TreeGridItem::getId).setHeader("ID");
        treeGrid.addColumn(TreeGridItem::getDescription).setHeader("Description");
        treeGridsAreaMap.put(typedEntityDto.getId(), treeGrid);
        treGridsModelAreaMap.put(typedEntityDto.getId(), model);
        treeGrid.addItemClickListener(event -> {
           TreeGridItem item =  event.getItem();
           logger.info("Item key " + item.getId() + ", description: " + item.getDescription());
           if (item.getParent() != null) {
               // Child item selected
               typesPanel.setTypeModel(typedEntityDto.getTypeDto(item.getParent().getId()));
               subtypesPanel.setSubtypeModel(typedEntityDto.getSubtypeDto(item.getParent().getId(), item.getId()));
           } else {
               // Parent item selected
               typesPanel.setTypeModel(typedEntityDto.getTypeDto(item.getId()));
               SubtypeDto subtypeModel = new SubtypeDto();
               subtypeModel.setTypeId(item.getId());
               subtypesPanel.setSubtypeModel(subtypeModel);
           }
        });
        return treeGrid;
    }

    private List<TreeGridItem> buildTreeGridModel(TypedEntityDto typedEntityDto) {
        List<TreeGridItem> list = new ArrayList<>();
        for (TypeDto typeDto : typedEntityDto.getTypes()) {
            List<TreeGridItem> subtypesList = new ArrayList<>();
            for (SubtypeDto subtypeDto : typeDto.getSubtypes()) {
                TreeGridItem subtypeItem = new TreeGridItem(subtypeDto.getAreaId(), subtypeDto.getEntityId(),
                        subtypeDto.getSubtypeId(), subtypeDto.getDescription(),
                        new ArrayList<>(), null);
                subtypesList.add(subtypeItem);
            }
            TreeGridItem typeItem = new TreeGridItem(typeDto.getAreaId(), typeDto.getEntityId(),
                    typeDto.getTypeId(), typeDto.getDescription(),
                    subtypesList, null);
            subtypesList.forEach(subtype -> {
                subtype.setParent(typeItem);
            });
            list.add(typeItem);
        }
        return list;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeGridItem {

        private Integer areaId;
        private Integer entityId;
        private String id;
        private String description;
        private List<TreeGridItem> children;
        private TreeGridItem parent;

        public boolean equals(Object object) {
            if (object instanceof TreeGridItem) {
                TreeGridItem item = (TreeGridItem) object;
                if (item.getParent() != null) {
                    return this.getId().equals(item.getId()) &&
                           this.getParent().getId().equals(item.getParent().getId());
                } else {
                    return this.getId().equals(item.getId());
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int hashCode = 0;
            if (getParent() != null) {
                hashCode = 37 * getParent().hashCode() + getId().hashCode();
            } else {
                hashCode = getId().hashCode();
            }
            return hashCode;
        }

    }

    private class TypesPanelHandler implements TypesPanel.TypesListener, TabbedListener {

        private TypesPanel typesPanel;

        private Map<String, AreaDto> areasDtoMap;
        private Map<Integer, Map<Integer, TreeGrid<TreeGridItem>>> treeGridsMap;
        private Map<Integer, Map<Integer, List<TreeGridItem>>> treeGridsModelMap;
        private TypesSubtypesService typesSubtypesService;

        public TypesPanelHandler(TypesPanel typesPanel) {
            this.typesPanel = typesPanel;
        }

        public void setAreasDtoMap(Map<String, AreaDto> areasDtoMap) {
            this.areasDtoMap = areasDtoMap;
        }

        public void setTreeGridsMap(Map<Integer, Map<Integer, TreeGrid<TreeGridItem>>> treeGridsMap) {
            this.treeGridsMap = treeGridsMap;
        }

        public void setTreeGridsModelMap(Map<Integer, Map<Integer, List<TreeGridItem>>> treeGridsModelMap) {
            this.treeGridsModelMap = treeGridsModelMap;
        }

        public void setTypesSubtypesService(TypesSubtypesService typesSubtypesService) {
            this.typesSubtypesService = typesSubtypesService;
        }

        @Override
        public void onSelectedTab(Tab selectedTab) {
            AreaDto selectedArea = areasDtoMap.get(selectedTab.getLabel());
            typesPanel.setCurrentAreaId(selectedArea.getId());
        }

        public void onSelectedEntityId(Integer selectedEntityId) {
            typesPanel.setCurrentEntityId(selectedEntityId);
        }

        @Override
        public boolean onInsertType(TypeDto typeDto) {
            TypeDto bbddTypeDto =
                    typesSubtypesService.getType(typeDto.getAreaId(), typeDto.getEntityId(), typeDto.getTypeId());
            if (bbddTypeDto == null) {
                // Register new type
                typesSubtypesService.createType(typeDto);
                //Update areas model (main model)
                Optional<AreaDto> currentAreaOptional = areasList.stream().
                        filter(it -> it.getId().equals(typeDto.getAreaId())).findFirst();
                if (currentAreaOptional.isPresent()) {
                    Optional<TypedEntityDto> currentEntityOptional = currentAreaOptional.get().getTypedEntities().stream().
                            filter(it -> it.getId().equals(typeDto.getEntityId())).findFirst();
                    if (currentEntityOptional.isPresent()) {
                        currentEntityOptional.get().getTypes().add(typeDto);
                    }
                }
                //Update TreeGrid model
                TreeGrid<TreeGridItem> treeGridComponent = treeGridsMap.get(typeDto.getAreaId()).get(typeDto.getEntityId());
                List<TreeGridItem> treeGridModel = treeGridsModelMap.get(typeDto.getAreaId()).get(typeDto.getEntityId());
                TreeGridItem treeGridItem = new TreeGridItem(typeDto.getAreaId(), typeDto.getEntityId(), typeDto.getTypeId(),
                        typeDto.getDescription(), new ArrayList<>(), null);
                treeGridModel.add(treeGridItem);
                treeGridComponent.setItems(treeGridModel, TreeGridItem::getChildren);
                typesPanel.removeErrorMessages();
                return true;
            } else {
                // Notify error
                typesPanel.addErrorMessage(
                        getMessageSource().getMessage("churierpweb.administration.types.errors.typeAlreadyExist",
                                new Object[] {bbddTypeDto.getTypeId()}, LocaleContextHolder.getLocale()));
                return false;
            }
        }

        @Override
        public boolean onUpdateType(TypeDto typeDto) {
            TreeGrid<TreeGridItem> treeGridComponent = treeGridsMap.get(typeDto.getAreaId()).get(typeDto.getEntityId());
            List<TreeGridItem> treeGridModel = treeGridsModelMap.get(typeDto.getAreaId()).get(typeDto.getEntityId());
            Optional<TreeGridItem> optionalTreeGridItem = treeGridModel.stream().filter(it ->
                it.getId().equals(typeDto.getTypeId()) ).findFirst();
            if (optionalTreeGridItem.isPresent()) {
                //Update areas model (main model)
                Optional<AreaDto> currentAreaOptional = areasList.stream().
                        filter(it -> it.getId().equals(typeDto.getAreaId())).findFirst();
                if (currentAreaOptional.isPresent()) {
                    Optional<TypedEntityDto> currentEntityOptional = currentAreaOptional.get().getTypedEntities().stream().
                            filter(it -> it.getId().equals(typeDto.getEntityId())).findFirst();
                    if (currentEntityOptional.isPresent()) {
                       Optional<TypeDto> currentTyperDto = currentEntityOptional.get().getTypes().stream().
                                filter( it -> it.getTypeId().equals(typeDto.getTypeId())).findFirst();
                       if (currentTyperDto.isPresent()) {
                           currentTyperDto.get().setDescription(typeDto.getDescription());
                       }
                    }
                }
                //Update treegrid model
                TreeGridItem selectedTreeGridItem = optionalTreeGridItem.get();
                selectedTreeGridItem.setDescription(typeDto.getDescription());
                TypeDto bbddTypeDto = typesSubtypesService.getType(typeDto.getAreaId(), typeDto.getEntityId(), typeDto.getTypeId());
                typeDto.setSubtypes(bbddTypeDto.getSubtypes());
                typesSubtypesService.updateType(typeDto);
                treeGridComponent.setItems(treeGridModel, TreeGridItem::getChildren);
                typesPanel.removeErrorMessages();
            }
            return true;
        }

        @Override
        public boolean onDeleteType(TypeDto typeDto) {
            TreeGrid<TreeGridItem> treeGridComponent = treeGridsMap.get(typeDto.getAreaId()).get(typeDto.getEntityId());
            List<TreeGridItem> treeGridModel = treeGridsModelMap.get(typeDto.getAreaId()).get(typeDto.getEntityId());
            Optional<TreeGridItem> optionalTreeGridItem = treeGridModel.stream().filter(it ->
                    it.getId().equals(typeDto.getTypeId()) ).findFirst();
            if (optionalTreeGridItem.isPresent()) {
                TreeGridItem selectedTreeGridItem = optionalTreeGridItem.get();
                TypeDto bbddTypeDto = typesSubtypesService.getType(typeDto.getAreaId(), typeDto.getEntityId(), typeDto.getTypeId());
                typeDto.setSubtypes(bbddTypeDto.getSubtypes());
                typesSubtypesService.deleteType(typeDto);
                treeGridModel.remove(selectedTreeGridItem);
                treeGridComponent.setItems(treeGridModel, TreeGridItem::getChildren);
                typesPanel.removeErrorMessages();
            }
            return true;
        }
    }

    private class SubtypesPanelHandler implements SubtypesPanel.SubtypesListener, TabbedListener {

        private SubtypesPanel subtypesPanel;

        private Map<String, AreaDto> areasDtoMap;

        public SubtypesPanelHandler(SubtypesPanel subtypesPanel) {
            this.subtypesPanel = subtypesPanel;
        }

        public void setAreasDtoMap(Map<String, AreaDto> areasDtoMap) {
            this.areasDtoMap = areasDtoMap;
        }

        @Override
        public void onSelectedTab(Tab selectedTab) {
            AreaDto selectedArea = areasDtoMap.get(selectedTab.getLabel());
            subtypesPanel.setCurrentAreaId(selectedArea.getId());
        }

        public void onSelectedEntityId(Integer selectedEntityId) {
            subtypesPanel.setCurrentEntityId(selectedEntityId);
        }

        @Override
        public boolean onInsertSubtype(SubtypeDto subtypeDto) {
            TypeDto bbddTypeDto =
                    typesSubtypesService.getType(subtypeDto.getAreaId(), subtypeDto.getEntityId(), subtypeDto.getTypeId());
            Optional<SubtypeDto> bbddSubtypeDtoOptional = bbddTypeDto.getSubtypes().stream().filter(it -> {
                return it.getSubtypeId().equals(subtypeDto.getSubtypeId());
            }).findFirst();
            if (bbddSubtypeDtoOptional.isEmpty()) {
                // Register new subtype
                bbddTypeDto.getSubtypes().add(subtypeDto);
                typesSubtypesService.updateType(bbddTypeDto);
                //Update areas model (main model)
                Optional<AreaDto> currentAreaOptional = areasList.stream().
                        filter(it -> it.getId().equals(bbddTypeDto.getAreaId())).findFirst();
                if (currentAreaOptional.isPresent()) {
                    Optional<TypedEntityDto> currentEntityOptional = currentAreaOptional.get().getTypedEntities().stream().
                            filter(it -> it.getId().equals(bbddTypeDto.getEntityId())).findFirst();
                    if (currentEntityOptional.isPresent()) {
                        Optional<TypeDto> currentTypeOptional = currentEntityOptional.get().getTypes().stream()
                                .filter(it -> it.getTypeId().equals(bbddTypeDto.getTypeId())).findFirst();
                        if (currentTypeOptional.isPresent()) {
                            currentTypeOptional.get().getSubtypes().add(subtypeDto);
                        }
                    }
                }
                //Update TreeGrid model
                TreeGrid<TreeGridItem> treeGridComponent = treeGridsMap.get(bbddTypeDto.getAreaId()).get(bbddTypeDto.getEntityId());
                List<TreeGridItem> treeGridModel = treeGridsModelMap.get(bbddTypeDto.getAreaId()).get(bbddTypeDto.getEntityId());
                TreeGridItem treeGridItem = treeGridModel.stream()
                        .filter(it -> it.getId().equals(bbddTypeDto.getTypeId())).findFirst().get();
                TreeGridItem treeGridSubItem = new TreeGridItem(subtypeDto.getAreaId(), subtypeDto.getEntityId(), subtypeDto.getSubtypeId(),
                        subtypeDto.getDescription(), new ArrayList<>(), treeGridItem);
                treeGridItem.getChildren().add(treeGridSubItem);
                treeGridComponent.setItems(treeGridModel, TreeGridItem::getChildren);
                treeGridComponent.expand(treeGridItem);
                subtypesPanel.removeErrorMessages();
                return true;
            } else {
                //Subtype is already present, notify errortypesPanel.addErrorMessage(
                subtypesPanel.addErrorMessage(getMessageSource().getMessage("churierpweb.administration.types.errors.subtypetypeAlreadyExist",
                                                new Object[] {bbddSubtypeDtoOptional.get().getSubtypeId()}, LocaleContextHolder.getLocale()));
                return false;
            }
        }

        @Override
        public boolean onUpdateSuptype(SubtypeDto subtypeDto) {
            TreeGrid<TreeGridItem> treeGridComponent = treeGridsMap.get(subtypeDto.getAreaId()).get(subtypeDto.getEntityId());
            List<TreeGridItem> treeGridModel = treeGridsModelMap.get(subtypeDto.getAreaId()).get(subtypeDto.getEntityId());
            Optional<TreeGridItem> optionalTreeGridItem = treeGridModel.stream().filter(it ->
                    it.getId().equals(subtypeDto.getTypeId()) ).findFirst();
            TypeDto typeDto = null;
            if (optionalTreeGridItem.isPresent()) {
                Optional<TreeGridItem> optionalTreeGridSubitem = optionalTreeGridItem.get().getChildren().stream()
                        .filter(it -> it.getId().equals(subtypeDto.getSubtypeId())).findFirst();
                if (optionalTreeGridSubitem.isPresent()) {
                    //Update areas model (main model)
                    Optional<AreaDto> currentAreaOptional = areasList.stream().
                            filter(it -> it.getId().equals(subtypeDto.getAreaId())).findFirst();
                    if (currentAreaOptional.isPresent()) {
                        Optional<TypedEntityDto> currentEntityOptional = currentAreaOptional.get().getTypedEntities().stream().
                                filter(it -> it.getId().equals(subtypeDto.getEntityId())).findFirst();
                        if (currentEntityOptional.isPresent()) {
                            Optional<TypeDto> currentTyperDto = currentEntityOptional.get().getTypes().stream().
                                    filter(it -> it.getTypeId().equals(subtypeDto.getTypeId())).findFirst();
                            if (currentTyperDto.isPresent()) {
                                typeDto = currentTyperDto.get();
                                Optional<SubtypeDto> currentSubtypeDto = currentTyperDto.get().getSubtypes().stream()
                                        .filter(it -> it.getSubtypeId().equals(subtypeDto.getSubtypeId())).findFirst();
                                if (currentSubtypeDto.isPresent()) {
                                    currentSubtypeDto.get().setDescription(subtypeDto.getDescription());
                                }
                            }
                        }
                    }
                    //Update treegrid model
                    if (typeDto != null) {
                        TreeGridItem selectedTreeGridItem = optionalTreeGridItem.get();
                        Optional<TreeGridItem> optionalTreeGridSubItem = selectedTreeGridItem.getChildren().stream()
                                .filter(it -> it.getId().equals(subtypeDto.getSubtypeId())).findFirst();
                        TreeGridItem treeGridSubItem = optionalTreeGridSubItem.get();
                        treeGridSubItem.setDescription(subtypeDto.getDescription());
                        typesSubtypesService.updateType(typeDto);
                        treeGridComponent.setItems(treeGridModel, TreeGridItem::getChildren);
                        treeGridComponent.expand(selectedTreeGridItem);
                        subtypesPanel.removeErrorMessages();
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onDeleteSubtype(SubtypeDto subtypeDto) {
            TreeGrid<TreeGridItem> treeGridComponent = treeGridsMap.get(subtypeDto.getAreaId()).get(subtypeDto.getEntityId());
            List<TreeGridItem> treeGridModel = treeGridsModelMap.get(subtypeDto.getAreaId()).get(subtypeDto.getEntityId());
            Optional<TreeGridItem> optionalTreeGridItem = treeGridModel.stream().filter(it ->
                    it.getId().equals(subtypeDto.getTypeId()) ).findFirst();
            if (optionalTreeGridItem.isPresent()) {
                TreeGridItem selectedTreeGridItem = optionalTreeGridItem.get();
                TypeDto bbddTypeDto = typesSubtypesService.getType(subtypeDto.getAreaId(), subtypeDto.getEntityId(), subtypeDto.getTypeId());
                boolean subtypeFound = false;
                for (int i = 0 ; (i < bbddTypeDto.getSubtypes().size()) && !subtypeFound ; i++) {
                    SubtypeDto currentSubtype = bbddTypeDto.getSubtypes().get(i);
                    subtypeFound = currentSubtype.getSubtypeId().equals(subtypeDto.getSubtypeId());
                    if (subtypeFound) {
                        bbddTypeDto.getSubtypes().remove(i);
                        Optional<TreeGridItem> subtypeItemOptional = selectedTreeGridItem.getChildren().stream().filter(it -> {
                            return it.getId().equals(subtypeDto.getSubtypeId());
                        }).findFirst();
                        if (subtypeItemOptional.isPresent()) {
                            TreeGridItem subtypeItem = subtypeItemOptional.get();
                            selectedTreeGridItem.getChildren().remove(subtypeItem);
                        }
                    }
                }
                typesSubtypesService.updateType(bbddTypeDto);
                treeGridComponent.setItems(treeGridModel, TreeGridItem::getChildren);
                treeGridComponent.expand(selectedTreeGridItem);
                subtypesPanel.removeErrorMessages();
            }
            return true;
        }
    }

}
