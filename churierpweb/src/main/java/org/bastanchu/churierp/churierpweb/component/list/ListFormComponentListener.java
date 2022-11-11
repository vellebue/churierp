package org.bastanchu.churierp.churierpweb.component.list;

import org.bastanchu.churierp.churierpback.dto.Copiable;
import org.bastanchu.churierp.churierpback.dto.Reseteable;

import java.util.List;

public interface ListFormComponentListener<T extends Reseteable & Copiable<T>> {

    public List<T> onLoadItems();

    public boolean onInsertItem(T item);

    public boolean onUpdateItem(T item);

    public boolean onDeleteItem(T item);

}
