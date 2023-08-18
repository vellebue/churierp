package org.bastanchu.churierp.churierpweb.component.dialog;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import org.bastanchu.churierp.churierpback.service.SpringContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class CustomConfirmDialog extends ConfirmDialog {

    private MessageSource messageSource = null;
    private CustomConfirmDialogConfirmedListener customConfirmDialogConfirmedListener;
    private CustomConfirmDialogCancelListener customConfirmDialogCancelListener;

    public CustomConfirmDialog(String headerText, String contentText) {
        super();
        this.setHeader(headerText);
        this.setText(contentText);
        messageSource = (MessageSource) SpringContextHolder.instance.
                getCurrentApplicationContext().getBean("messageSource");
        this.setConfirmText(messageSource.getMessage("churierpweb.customconfirmdialog.button.ok",
                null, LocaleContextHolder.getLocale()));
        this.setCancelText(messageSource.getMessage("churierpweb.customconfirmdialog.button.cancel",
                null, LocaleContextHolder.getLocale()));
        this.setCancelable(true);
        addConfirmListener(event -> {
            if (customConfirmDialogConfirmedListener != null) {
                customConfirmDialogConfirmedListener.onConfirmedAction(event);
            }
        });
        addCancelListener(event -> {
            if (customConfirmDialogCancelListener != null) {
                customConfirmDialogCancelListener.onCancelAction(event);
            }
        });
    }

    public void setCustomConfirmDialogConfirmedListener(CustomConfirmDialogConfirmedListener customConfirmDialogConfirmedListener) {
        this.customConfirmDialogConfirmedListener = customConfirmDialogConfirmedListener;
    }

    public void setCustomConfirmDialogCancelListener(CustomConfirmDialogCancelListener customConfirmDialogCancelListener) {
        this.customConfirmDialogCancelListener = customConfirmDialogCancelListener;
    }
}
