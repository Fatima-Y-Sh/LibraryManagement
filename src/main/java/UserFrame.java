import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserFrame extends JFrame {

    String UID;

    UserFrame(String UID){
        this.UID = UID;
        setTitle("Welcome Student!");
        JButton view_books_btn = new JButton("View Books");
        view_books_btn.setBackground(Utils.BUTTON_COLOR);
        view_books_btn.setForeground(Utils.FOREGROUND_COLOR);
        view_books_btn.addActionListener(e -> new ViewBooksFrame());

        JButton view_user_issued_books_btn = new JButton("Issued Books");
        view_user_issued_books_btn.setBackground(Utils.BUTTON_COLOR);
        view_user_issued_books_btn.setForeground(Utils.FOREGROUND_COLOR);
        view_user_issued_books_btn.addActionListener(e -> new ViewUserIssued());

        JButton view_user_returned_books_btn = new JButton("My Returned Books");
        view_user_returned_books_btn.setBackground(Utils.BUTTON_COLOR);
        view_user_returned_books_btn.setForeground(Utils.FOREGROUND_COLOR);
        view_user_returned_books_btn.addActionListener(e -> new ViewUserReturned());

        this.setLayout(new GridLayout(3, 1));

        this.add(view_books_btn);
        this.add(view_user_issued_books_btn);
        this.add(view_user_returned_books_btn);

        this.setSize(500, 500);

        this.setVisible(true);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class ViewUserReturned extends JFrame{
        ViewUserReturned(){
            setTitle("My Returned Books");
            int userid = Integer.parseInt(UID);
            Connection connection = BddComm.getInstance().connect();

            String sql = "select returned_books.rid as rid, returned_books.bid as bid, returned_books.uid as userid,"
                    + "books.book_isbn as book_isbn, books.book_name as book_name, books.book_publisher as book_publisher,"
                    + "books.book_edition as book_edition, books.book_genre as book_genre, books.book_price as book_price,"
                    + "books.book_pages as book_pages, returned_books.return_date as return_date, returned_books.fine as fine "
                    + "from books, returned_books where books.bid=returned_books.bid and returned_books.uid=" + userid;

            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                JTable returned_book_list = new JTable();
                String[] returnedBookColumnNames = {"Return ID", "Book ID", "User ID", "Book ISBN", "Book Name", "Book Publisher", "Book Edition", "Book Genre", "Book Price", "Book Pages", "Returned Date", "Fine"};
                DefaultTableModel bookModel = new DefaultTableModel();
                bookModel.setColumnIdentifiers(returnedBookColumnNames);
                returned_book_list.setModel(bookModel);
                returned_book_list.setBackground(Utils.BACKGROUND_COLOR);
                returned_book_list.setForeground(Utils.FOREGROUND_COLOR);
                returned_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                returned_book_list.setFillsViewportHeight(true);
                returned_book_list.setFocusable(false);

                JScrollPane scrollIssuedBook = new JScrollPane(returned_book_list);
                scrollIssuedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollIssuedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                while (rs.next()) {
                    int rid = rs.getInt(1);
                    int bid = rs.getInt(2);
                    int uid = rs.getInt(3);
                    String book_isbn = rs.getString(4);
                    String book_name = rs.getString(5);
                    String book_publisher = rs.getString(6);
                    String book_edition = rs.getString(7);
                    String book_genre = rs.getString(8);
                    int book_price = rs.getInt(9);
                    int book_pages = rs.getInt(10);
                    String returned_date = rs.getString(11);
                    int fine = rs.getInt(12);

                    bookModel.addRow(new Object[]{rid, bid, uid, book_isbn, book_name, book_publisher, book_edition, book_genre, book_price, book_pages, returned_date, fine});
                }

                this.add(scrollIssuedBook);

                this.setSize(1200, 600);

                this.setVisible(true);

                this.setResizable(false);

            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, e1);
            }
        }
    }

    private class ViewUserIssued extends JFrame{
        ViewUserIssued(){
            setTitle("My Issued Books");
            int userid = Integer.parseInt(UID);
            Connection connection = BddComm.getInstance().connect();
            String sql = "select issued_books.iid as iid, issued_books.bid as bid, issued_books.uid as userid,"
                    + " books.book_isbn as book_isbn, books.book_name as book_name, books.book_publisher as book_publisher, "
                    + "books.book_edition as book_edition, books.book_genre as book_genre, books.book_price as book_price,"
                    + " books.book_pages as book_pages, issued_books.issued_date as issued_date, issued_books.period as period from books,"
                    + "issued_books where books.bid=issued_books.bid and issued_books.uid=" + userid;
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                JTable issued_book_list = new JTable();
                String[] issuedBookColumnNames = {"Issue ID", "Book ID", "User ID", "Book ISBN", "Book Name", "Book Publisher", "Book Edition", "Book Genre", "Book Price", "Book Pages", "Issued Date", "Period"};
                DefaultTableModel bookModel = new DefaultTableModel();
                bookModel.setColumnIdentifiers(issuedBookColumnNames);
                issued_book_list.setModel(bookModel);
                issued_book_list.setBackground(Utils.BACKGROUND_COLOR);
                issued_book_list.setForeground(Utils.FOREGROUND_COLOR);
                issued_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                issued_book_list.setFillsViewportHeight(true);
                issued_book_list.setFocusable(false);

                JScrollPane scrollIssuedBook = new JScrollPane(issued_book_list);
                scrollIssuedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollIssuedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                while (rs.next()) {
                    int iid = rs.getInt(1);
                    int bid = rs.getInt(2);
                    int uid = rs.getInt(3);
                    String book_isbn = rs.getString(4);
                    String book_name = rs.getString(5);
                    String book_publisher = rs.getString(6);
                    String book_edition = rs.getString(7);
                    String book_genre = rs.getString(8);
                    int book_price = rs.getInt(9);
                    int book_pages = rs.getInt(10);
                    String issued_date = rs.getString(11);
                    int period = rs.getInt(12);

                    bookModel.addRow(new Object[]{iid, bid, uid, book_isbn, book_name, book_publisher, book_edition, book_genre, book_price, book_pages, issued_date, period});
                }

                this.add(scrollIssuedBook);
                this.setSize(1200, 600);
                this.setVisible(true);
                this.setResizable(false);
            } catch (Exception e1) {

                JOptionPane.showMessageDialog(null, e1);
            }
        }
    }
}
