import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewBooksFrame extends JFrame {
    ViewBooksFrame(){
        setTitle("Books Available");

        Connection connection = BddComm.getInstance().connect();
        String sql = "select * from BOOKS";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            JTable book_list = new JTable();
            String[] bookColumnNames = {"Book ID", "Book ISBN", "Book Name", "Book Publisher", "Book Edition", "Book Genre", "Book price", "Book Pages"};
            DefaultTableModel bookModel = new DefaultTableModel();
            bookModel.setColumnIdentifiers(bookColumnNames);
            book_list.setModel(bookModel);
            book_list.setBackground(Utils.BACKGROUND_COLOR);
            book_list.setForeground(Utils.FOREGROUND_COLOR);
            book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            book_list.setFillsViewportHeight(true);
            book_list.setFocusable(false);

            JScrollPane scrollBook = new JScrollPane(book_list);
            scrollBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            while (rs.next()) {
                int book_id = rs.getInt(1);
                String book_isbn = rs.getString(2);
                String book_name = rs.getString(3);
                String book_publisher = rs.getString(4);
                String book_edition = rs.getString(5);
                String book_genre = rs.getString(6);
                int book_price = rs.getInt(7);
                int book_pages = rs.getInt(8);

                bookModel.addRow(new Object[]{book_id, book_isbn, book_name, book_publisher, book_edition, book_genre, book_price, book_pages});
            }

            this.add(scrollBook);

            this.setSize(800, 400);

            this.setVisible(true);
        } catch (Exception e1) {
            //Creating Dialog box to show any error if occured!
            JOptionPane.showMessageDialog(null, e1);
        }
    }
}
