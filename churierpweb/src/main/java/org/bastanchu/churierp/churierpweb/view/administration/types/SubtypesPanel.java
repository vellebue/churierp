package org.bastanchu.churierp.churierpweb.view.administration.types;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.bastanchu.churierp.churierpback.dto.administration.types.SubtypeDto;
import org.bastanchu.churierp.churierpweb.component.button.*;
import org.bastanchu.churierp.churierpweb.component.dialog.CustomConfirmDialog;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

class SubtypesPanel extends VerticalLayout {

    private Logger logger = LoggerFactory.getLogger(SubtypesPanel.class);

    private SubtypeDto subtypeModel = new SubtypeDto();

    private CustomForm<SubtypeDto> customForm = null;
    private Integer currentAreaId = null;
    private Integer currentEntityId = null;
    private String currentTypeId = null;
    private Div formContainer = new Div();
    private Div buttonBarContainer = new Div();
    private SubtypesListener subtypesListener;

    private MessageSource messageSource = null;

    public SubtypesPanel(MessageSource messageSource) {
        setMaxWidth(80, Unit.PERCENTAGE);
        this.messageSource = messageSource;
        customForm = new CustomForm<>(subtypeModel, messageSource, true);
        formContainer.setMinWidth(100, Unit.PERCENTAGE);
        customForm.setWidthPercentage(100.0);
        formContainer.add(customForm);
        add(formContainer);
        buttonBarContainer.setMinWidth(100, Unit.PERCENTAGE);
        buttonBarContainer.add(buildUpdateBar());
        add(buttonBarContainer);
    }

    public void setCurrentAreaId(Integer currentAreaId) {
        logger.info("Selected area id for type: " + currentAreaId);
        this.currentAreaId = currentAreaId;
    }

    public void setCurrentEntityId(Integer currentEntityId) {
        logger.info("Selected entity id for type: " + currentEntityId);
        this.currentEntityId = currentEntityId;
    }

    public void setCurrentTypeId(String currentTypeId) {
        logger.info("Selected type id " + currentTypeId);
        this.currentTypeId = currentTypeId;
        SubtypeDto currentsubSubtypeDto = new SubtypeDto();
        currentsubSubtypeDto.setTypeId(currentTypeId);
        setSubtypeModel(currentsubSubtypeDto);
    }

    public void setSubtypesListener(SubtypesListener subtypesListener) {
        this.subtypesListener = subtypesListener;
    }

    public SubtypeDto getSubtypeModel() {
        customForm.writeBean(subtypeModel);
        return subtypeModel;
    }

    public void setSubtypeModel(SubtypeDto subtypeModel) {
        this.subtypeModel = subtypeModel;
        formContainer.removeAll();
        customForm = new CustomForm<>(subtypeModel, messageSource, true);
        formContainer.add(customForm);
        customForm.readBean(subtypeModel);
        buttonBarContainer.removeAll();
        buttonBarContainer.add(buildUpdateBar());
    }

    public void addErrorMessage(String errorMessage) {
        customForm.addErrorMessageText(errorMessage);
    }

    public void removeErrorMessages() {
        customForm.removeErrorMessages();
    }

    private ButtonBar buildCreationBar() {
        ButtonBar creationBar = new ButtonBar();
        creationBar.setMinWidth(100, Unit.PERCENTAGE);
        GreenButton insertSubtypeButton = new GreenButton(
                messageSource.getMessage("churierpweb.administration.subtypes.button.createSubtype",
                        null, LocaleContextHolder.getLocale()), e -> {
                SubtypeDto sourceSubtypeDto = new SubtypeDto();
                if (customForm.writeBean(sourceSubtypeDto)) {
                    sourceSubtypeDto.setAreaId(currentAreaId);
                    sourceSubtypeDto.setEntityId(currentEntityId);
                    sourceSubtypeDto.setManageable(true);
                    subtypeModel = sourceSubtypeDto;
                    if (subtypesListener != null) {
                        if (subtypesListener.onInsertSubtype(sourceSubtypeDto)) {
                            // Inserted subtype successful, change to update mode
                            buttonBarContainer.removeAll();
                            buttonBarContainer.add(buildUpdateBar());
                        }
                    }
                }
        });
        creationBar.addButton(insertSubtypeButton);
        Button cancelButton = new Button(
                messageSource.getMessage("churierpweb.administration.subtypes.button.cancel",
                        null, LocaleContextHolder.getLocale()), e -> {
                SubtypeDto newSubtypeModelDto = new SubtypeDto();
                newSubtypeModelDto.setTypeId(subtypeModel.getTypeId());
                subtypeModel = newSubtypeModelDto;
                customForm.readBean(newSubtypeModelDto);
        });
        creationBar.addButton(cancelButton);
        return creationBar;
    }

    private ButtonBar buildUpdateBar() {
        ButtonBar updateBar = new ButtonBar();
        updateBar.setMinWidth(100, Unit.PERCENTAGE);
        GreenButton newSubtypeButton = new GreenButton(
                messageSource.getMessage("churierpweb.administration.subtypes.button.newSubtype",
                null, LocaleContextHolder.getLocale()),e -> {
            formContainer.removeAll();
            String typeId = subtypeModel.getTypeId();
            if ((typeId != null) && !typeId.trim().equals("")) {
                subtypeModel = new SubtypeDto();
                subtypeModel.setTypeId(typeId);
                customForm = new CustomForm<>(subtypeModel, messageSource);
                customForm.readBean(subtypeModel);
                formContainer.add(customForm);
                buttonBarContainer.removeAll();
                buttonBarContainer.add(buildCreationBar());
            }
        });
        updateBar.addButton(newSubtypeButton);
        BlueButton modifySubtypeButton = new BlueButton(
                messageSource.getMessage("churierpweb.administration.subtypes.button.modifySubtype",
                null, LocaleContextHolder.getLocale()), e -> {
                SubtypeDto sourceSubtypeDto = new SubtypeDto();
                if (customForm.writeBean(sourceSubtypeDto) && sourceSubtypeDto.getManageable()) {
                    subtypeModel = sourceSubtypeDto;
                    if (subtypesListener != null) {
                        subtypesListener.onUpdateSuptype(sourceSubtypeDto);
                    }
                }
        });
        updateBar.addButton(modifySubtypeButton);
        RedButton deleteSubtypeButton = new RedButton(
                messageSource.getMessage("churierpweb.administration.subtypes.button.deleteSubtype",
                null, LocaleContextHolder.getLocale()), e -> {
            CustomConfirmDialog deleteDialog = new CustomConfirmDialog(
                    messageSource.getMessage("churierpweb.administration.subtypes.dialog.deletedialog.header",
                            null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("churierpweb.administration.subtypes.dialog.deletedialog.text",
                            null, LocaleContextHolder.getLocale())
            );
            deleteDialog.setCustomConfirmDialogConfirmedListener(event -> {
                SubtypeDto sourceSubtypeDto = new SubtypeDto();
                if (customForm.writeBean(sourceSubtypeDto) && sourceSubtypeDto.getManageable()) {
                    subtypeModel = sourceSubtypeDto;
                    if (subtypesListener != null) {
                        if (subtypesListener.onDeleteSubtype(sourceSubtypeDto)) {
                            subtypeModel = new SubtypeDto();
                            customForm.readBean(subtypeModel);
                        }
                    }
                }
            });
            deleteDialog.open();
        });
        updateBar.addButton(deleteSubtypeButton);
        return updateBar;
    }

    public interface SubtypesListener {

        public boolean onInsertSubtype(SubtypeDto subtypeDto);

        public boolean onUpdateSuptype(SubtypeDto subtypeDto);

        public boolean onDeleteSubtype(SubtypeDto subtypeDto);

    }
}
