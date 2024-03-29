// =============================================================================
// SystemProperties.java
// =============================================================================

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import com.svengato.gui.DefaultFrame;

// -----------------------------------------------------------------------------

class SystemPropertiesTableModel extends DefaultTableModel
{
  ArrayList<String> properties;
  String[] columnNames = { "Property", "Value" };

  public SystemPropertiesTableModel() {
    properties = new ArrayList<String>();
    Properties sp = System.getProperties();
    for (Object key: sp.keySet()) {
      properties.add(key.toString());
    }
    Collections.sort(properties);

    fireTableDataChanged();
  }

  public int getRowCount() { return (properties == null ? 0 : properties.size()); }
  public int getColumnCount() { return columnNames.length; }
  public String getColumnName(int c) { return columnNames[c]; }

  public boolean isCellEditable(int r, int c) { return false; }

  public Object getValueAt(int r, int c) {
    String property = properties.get(r);

    switch (c) {
    case 0:
      return property;
    case 1:
      String value = System.getProperty(property);
      // special case: line separator
      if (property.equals("line.separator")) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < value.length(); ++i) {
          char ch = value.charAt(i);
          if (ch == '\r') {
            buf.append("\\r");
          } else if (ch == '\n') {
            buf.append("\\n");
          } else {
            buf.append(ch);
          }
        }
        return buf.toString();
      }
      return value;
    }
    return null;
  }
}

// -----------------------------------------------------------------------------

class SystemPropertiesPanel extends JPanel
{
  public SystemPropertiesPanel() {
    SystemPropertiesTableModel tmSystemProperties = new SystemPropertiesTableModel();

    JTable tblSystemProperties = new JTable();
    tblSystemProperties.setModel(tmSystemProperties);
		tblSystemProperties.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    tblSystemProperties.setRowSorter(new TableRowSorter(tmSystemProperties));
		tblSystemProperties.getTableHeader().setReorderingAllowed(false);

    setLayout(new BorderLayout());
    add(new JLabel("\u2318" + "C copies the selected system properties and their values to the clipboard, as a tab-separated table."), BorderLayout.NORTH);
    add(new JScrollPane(tblSystemProperties), BorderLayout.CENTER);
	}

	public static void main(String args[]) {
		new DefaultFrame("Java System Properties", 768, 512, new SystemPropertiesPanel());
	}
}

// =============================================================================
