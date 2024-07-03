package window;

import user.User;
import user.UserRole;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel {

    private User currentUser;

    public CustomTableModel(Object[][] data, Object[] columnNames, User currentUser){
        super(data, columnNames);
        this.currentUser = currentUser;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return currentUser.getRole() == UserRole.LIBRARIAN;
    }
}
