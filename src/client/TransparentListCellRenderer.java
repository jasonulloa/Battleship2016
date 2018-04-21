package client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class TransparentListCellRenderer extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setForeground(Color.WHITE);
        setOpaque(isSelected);
        return this;
    }

}
