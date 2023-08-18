package org.bastanchu.churierp.churierpweb.view.administration.types;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.bastanchu.churierp.churierpback.dto.administration.types.TypeDto;
import org.bastanchu.churierp.churierpweb.component.button.BlueButton;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.button.RedButton;
import org.bastanchu.churierp.churierpweb.component.dialog.CustomConfirmDialog;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;

class TypesPanel extends VerticalLayout {

    private Logger logger = LoggerFactory.getLogger(TypesPanel.class);

    private MessageSource messageSource;

    private TypeDto typeModel = new TypeDto();
    private Integer currentAreaId = null;
    private Integer currentEntityId = null;
    private Div formContainer = new Div();
    private Div buttonBarContainer = new Div();
    private TypesListener typesListener;

    private CustomForm<TypeDto> customForm = null;

    public TypesPanel(MessageSource messageSource) {
        setMaxWidth(80, Unit.PERCENTAGE);
        this.messageSource = messageSource;
        customForm = new CustomForm<>(typeModel, messageSource, true);
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

    public void setTypesListener(TypesListener typesListener) {
        this.typesListener = typesListener;
    }

    public TypeDto getTypeModel() {
        customForm.writeBean(typeModel);
        return typeModel;
    }

    public void setTypeModel(TypeDto typeModel) {
        this.typeModel = typeModel;
        formContainer.removeAll();
        customForm = new CustomForm<>(typeModel, messageSource, true);
        customForm.readBean(typeModel);
        formContainer.add(customForm);
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
        GreenButton insertTypeButton = new GreenButton(
                messageSource.getMessage("churierpweb.administration.types.button.createType",
                        null, LocaleContextHolder.getLocale()), e -> {
                TypeDto sourceTypeDto = new TypeDto();
                if (customForm.writeBean(sourceTypeDto)) {
                    sourceTypeDto.setAreaId(currentAreaId);
                    sourceTypeDto.setEntityId(currentEntityId);
                    sourceTypeDto.setManageable(true);
                    sourceTypeDto.setSubtypes(new ArrayList<>());
                    typeModel = sourceTypeDto;
                    if (typesListener != null) {
                        if (typesListener.onInsertType(sourceTypeDto)) {
                            // Type succesfully create, remove creation mode
                            buttonBarContainer.removeAll();
                            buttonBarContainer.add(buildUpdateBar());
                        }
                    }
                }
        });
        creationBar.addButton(insertTypeButton);
        Button cancelButton = new Button(
                messageSource.getMessage("churierpweb.administration.types.button.cancel",
                        null, LocaleContextHolder.getLocale()), e -> {
                typeModel = new TypeDto();
                customForm.readBean(typeModel);
        });
        creationBar.addButton(cancelButton);
        return creationBar;
    }

    private ButtonBar buildUpdateBar() {
        ButtonBar updateBar = new ButtonBar();
        updateBar.setMinWidth(100, Unit.PERCENTAGE);
        GreenButton newTypeButton = new GreenButton(
                messageSource.getMessage("churierpweb.administration.types.button.newType",
                        null, LocaleContextHolder.getLocale()),e -> {
            formContainer.removeAll();
            typeModel = new TypeDto();
            customForm = new CustomForm<>(typeModel, messageSource);
            formContainer.add(customForm);
            buttonBarContainer.removeAll();
            buttonBarContainer.add(buildCreationBar());
        });
        updateBar.addButton(newTypeButton);
        BlueButton modifyTypeButton = new BlueButton(
                messageSource.getMessage("churierpweb.administration.types.button.modifyType",
                        null, LocaleContextHolder.getLocale()), e -> {
                TypeDto sourceTypeDto = new TypeDto();
                if (customForm.writeBean(sourceTypeDto) && sourceTypeDto.getManageable()) {
                    typeModel = sourceTypeDto;
                    if (typesListener != null) {
                        typesListener.onUpdateType(sourceTypeDto);
                    }
                }
        });
        updateBar.addButton(modifyTypeButton);
        RedButton deleteTypeButton = new RedButton(
                messageSource.getMessage("churierpweb.administration.types.button.deleteType",
                        null, LocaleContextHolder.getLocale()), e -> {
            CustomConfirmDialog deleteDialog = new CustomConfirmDialog(
                    messageSource.getMessage("churierpweb.administration.types.dialog.deletedialog.header",
                            null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("churierpweb.administration.types.dialog.deletedialog.text",
                            null, LocaleContextHolder.getLocale())
            );
            deleteDialog.setCustomConfirmDialogConfirmedListener(event -> {
                TypeDto sourceTypeDto = new TypeDto();
                if (customForm.writeBean(sourceTypeDto) && sourceTypeDto.getManageable()) {
                    typeModel = sourceTypeDto;
                    if (typesListener != null) {
                        if (typesListener.onDeleteType(sourceTypeDto)) {
                            typeModel = new TypeDto();
                            customForm.readBean(typeModel);
                        }
                    }
                }
            });
            deleteDialog.open();
        });
        updateBar.addButton(deleteTypeButton);
        return updateBar;
    }

    public interface TypesListener {

        public boolean onInsertType(TypeDto typeDto);

        public boolean onUpdateType(TypeDto typeDto);

        public boolean onDeleteType(TypeDto typeDto);

    }
}
