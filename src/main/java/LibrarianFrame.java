import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LibrarianFrame extends JFrame {
    LibrarianFrame(){
        setTitle("Welcome Librarian");

        JButton view_books_btn = new JButton("View Books");
        view_books_btn.setBackground(Utils.BUTTON_COLOR);
        view_books_btn.setForeground(Utils.FOREGROUND_COLOR);
        view_books_btn.addActionListener(e -> new ViewBooksFrame());

        JButton view_users_btn = new JButton("View Users");
        view_users_btn.setBackground(Utils.BUTTON_COLOR);
        view_users_btn.setForeground(Utils.FOREGROUND_COLOR);
        view_users_btn.addActionListener(e -> new ViewUsersFrame());

        JButton view_issued_books_btn = new JButton("View Issued Books");
        view_issued_books_btn.setBackground(Utils.BUTTON_COLOR);
        view_issued_books_btn.setForeground(Utils.FOREGROUND_COLOR);
        view_issued_books_btn.addActionListener(e -> new ViewIssuedBooksFrame());

        JButton view_returned_books_btn = new JButton("View Returned Books");
        view_returned_books_btn.setBackground(Utils.BUTTON_COLOR);
        view_returned_books_btn.setForeground(Utils.FOREGROUND_COLOR);
        view_returned_books_btn.addActionListener(e -> new ViewReturnedBooksFrame());

        JButton add_user_btn = new JButton("Add User");
        add_user_btn.setBackground(Utils.BUTTON_COLOR);
        add_user_btn.setForeground(Utils.FOREGROUND_COLOR);
        add_user_btn.addActionListener(e -> new AddUserFrame());

        JButton add_book_btn = new JButton("Add Book");
        add_book_btn.setBackground(Utils.BUTTON_COLOR);
        add_book_btn.setForeground(Utils.FOREGROUND_COLOR);
        add_book_btn.addActionListener(e -> new AddBookFrame());

        JButton add_issue_book_btn = new JButton("Issue Book");
        add_issue_book_btn.setBackground(Utils.BUTTON_COLOR);
        add_issue_book_btn.setForeground(Utils.FOREGROUND_COLOR);
        add_issue_book_btn.addActionListener(e -> new IssueBookFrame());

        JButton add_return_book_btn = new JButton("Return Book");
        add_return_book_btn.setBackground(Utils.BUTTON_COLOR);
        add_return_book_btn.setForeground(Utils.FOREGROUND_COLOR);
        add_return_book_btn.addActionListener(e -> new ReturnBookFrame());

        JButton delete_book_btn = new JButton("Delete Book");
        delete_book_btn.setBackground(Utils.BUTTON_COLOR);
        delete_book_btn.setForeground(Utils.FOREGROUND_COLOR);
        delete_book_btn.addActionListener(e -> new DeleteBookFrame());

        JButton change_password_btn = new JButton("Change Password");
        change_password_btn.setBackground(Utils.BUTTON_COLOR);
        change_password_btn.setForeground(Utils.FOREGROUND_COLOR);
        change_password_btn.addActionListener(e -> new ChangePasswordFrame());

        JButton export_data_btn = new JButton("Export all (Json)");
        export_data_btn.setBackground(Utils.BUTTON_COLOR);
        export_data_btn.setForeground(Utils.FOREGROUND_COLOR);
        export_data_btn.addActionListener(e -> {
            if(BddComm.getInstance().exportDataToJsonFile()){
                JOptionPane.showMessageDialog(null, "Data Exported!");
            }
            else{
                JOptionPane.showMessageDialog(null, "Data Export Failed!");
            }
        });

        JButton import_data_btn = new JButton("Import Data (Json)");
        import_data_btn.setBackground(Utils.BUTTON_COLOR);
        import_data_btn.setForeground(Utils.FOREGROUND_COLOR);
        import_data_btn.addActionListener(e -> new ImportDataJsonFrame());

        this.setLayout(new GridLayout(2, 6));

        this.add(add_user_btn);
        this.add(add_book_btn);
        this.add(add_issue_book_btn);
        this.add(add_return_book_btn);
        this.add(delete_book_btn);
        this.add(view_users_btn);
        this.add(view_books_btn);
        this.add(view_issued_books_btn);
        this.add(view_returned_books_btn);
        this.add(change_password_btn);
        this.add(export_data_btn);
        this.add(import_data_btn);

        this.setSize(800, 200);

        this.setVisible(true);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class ViewUsersFrame extends JFrame{
        ViewUsersFrame(){
            setTitle("Users List");

            Connection connection = BddComm.getInstance().connect();
            String sql = "select * from users";
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                JTable users_list = new JTable();
                String[] userColumnNames = {"User ID", "User Name", "User Type"};
                DefaultTableModel userModel = new DefaultTableModel();
                userModel.setColumnIdentifiers(userColumnNames);
                users_list.setModel(userModel);
                users_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                users_list.setFillsViewportHeight(true);
                users_list.setBackground(Utils.BACKGROUND_COLOR);
                users_list.setForeground(Utils.FOREGROUND_COLOR);

                JScrollPane scrollUser = new JScrollPane(users_list);
                scrollUser.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                while (rs.next()) {
                    int uid = rs.getInt(1);
                    String user_name = rs.getString(2);
                    int user_type = rs.getInt(4);
                    if (user_type == 1) {
                        userModel.addRow(new Object[]{uid, user_name, "ADMIN"});
                    } else {
                        userModel.addRow(new Object[]{uid, user_name, "USER"});
                    }
                }

                this.add(scrollUser);
                this.setSize(800, 400);
                this.setVisible(true);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, e1);
            }
        }
    }

    private class ViewIssuedBooksFrame extends JFrame{
        ViewIssuedBooksFrame(){
            setTitle("Issued Books List");

            Connection connection = BddComm.getInstance().connect();
            String sql = "SELECT * FROM issued_books";
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                JTable issue_book_list = new JTable();
                String[] issueBookColumnNames = {"Issue ID", "User ID", "Book ID", "Issue Date", "Period"};
                DefaultTableModel issuedBookModel = new DefaultTableModel();
                issuedBookModel.setColumnIdentifiers(issueBookColumnNames);
                issue_book_list.setModel(issuedBookModel);
                issue_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                issue_book_list.setFillsViewportHeight(true);
                issue_book_list.setFocusable(false);
                issue_book_list.setBackground(Utils.BACKGROUND_COLOR);
                issue_book_list.setForeground(Utils.FOREGROUND_COLOR);

                JScrollPane scrollIssuedBook = new JScrollPane(issue_book_list);
                scrollIssuedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollIssuedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                while (rs.next()) {
                    int iid = rs.getInt(1);
                    int uid = rs.getInt(2);
                    int bid = rs.getInt(3);
                    String issue_date = rs.getString(4);
                    int period = rs.getInt(5);

                    issuedBookModel.addRow(new Object[]{iid, uid, bid, issue_date, period});
                }

                this.add(scrollIssuedBook);
                this.setSize(800, 400);
                this.setVisible(true);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, e1);
            }
        }
    }

    private class ViewReturnedBooksFrame extends JFrame{
        ViewReturnedBooksFrame(){
            setTitle("Returned Books List");

            Connection connection = BddComm.getInstance().connect();
            String sql = "select * from returned_books";
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                JTable returned_book_list = new JTable();
                String[] returnBookColumnNames = {"Return ID", "Book ID", "User ID", "Return Date", "Fine"};
                DefaultTableModel returnBookModel = new DefaultTableModel();
                returnBookModel.setColumnIdentifiers(returnBookColumnNames);
                returned_book_list.setModel(returnBookModel);
                returned_book_list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                returned_book_list.setFillsViewportHeight(true);
                returned_book_list.setFocusable(false);
                returned_book_list.setBackground(Utils.BACKGROUND_COLOR);
                returned_book_list.setForeground(Utils.FOREGROUND_COLOR);

                JScrollPane scrollReturnedBook = new JScrollPane(returned_book_list);
                scrollReturnedBook.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollReturnedBook.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                while (rs.next()) {
                    int rid = rs.getInt(1);
                    int bid = rs.getInt(2);
                    int uid = rs.getInt(3);
                    String returned_date = rs.getString(4);
                    int fine = rs.getInt(5);

                    returnBookModel.addRow(new Object[]{rid, bid, uid, returned_date, fine});
                }

                this.add(scrollReturnedBook);
                this.setSize(800, 400);
                this.setVisible(true);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, e1);
            }
        }
    }

    private class AddUserFrame extends JFrame{
        AddUserFrame(){
            setTitle("Enter User Details"); //Frame to enter user details
            JLabel l1 = new JLabel("Username", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);

            JLabel l2 = new JLabel("Password", SwingConstants.CENTER);
            l2.setOpaque(true);
            l2.setBackground(Utils.BACKGROUND_COLOR);
            l2.setForeground(Utils.FOREGROUND_COLOR);

            JTextField add_username_tf = new JTextField();
            add_username_tf.setBackground(Utils.BACKGROUND_COLOR);
            add_username_tf.setForeground(Utils.FOREGROUND_COLOR);

            JPasswordField add_password_tf = new JPasswordField();
            add_password_tf.setBackground(Utils.BACKGROUND_COLOR);
            add_password_tf.setForeground(Utils.FOREGROUND_COLOR);

            JRadioButton user_type_radio1 = new JRadioButton("Admin");
            user_type_radio1.setHorizontalAlignment(SwingConstants.CENTER);
            user_type_radio1.setBackground(Utils.BACKGROUND_COLOR);
            user_type_radio1.setForeground(Utils.FOREGROUND_COLOR);

            JRadioButton user_type_radio2 = new JRadioButton("User");
            user_type_radio2.setHorizontalAlignment(SwingConstants.CENTER);
            user_type_radio2.setBackground(Utils.BACKGROUND_COLOR);
            user_type_radio2.setForeground(Utils.FOREGROUND_COLOR);

            ButtonGroup user_type_btn_grp = new ButtonGroup();
            user_type_btn_grp.add(user_type_radio1);
            user_type_btn_grp.add(user_type_radio2);

            JButton create_btn = new JButton("Create");
            create_btn.setBackground(Utils.BUTTON_COLOR);
            create_btn.setForeground(Utils.FOREGROUND_COLOR);

            JButton user_entry_cancel_btn = new JButton("Cancel");
            user_entry_cancel_btn.setBackground(Utils.BUTTON_COLOR);
            user_entry_cancel_btn.setForeground(Utils.FOREGROUND_COLOR);

            create_btn.addActionListener(e -> {

                String username = add_username_tf.getText();
                String password = add_password_tf.getText();

                Connection connection = BddComm.getInstance().connect();

                try {
                    Statement stmt = connection.createStatement();
                    if (user_type_radio1.isSelected()) {
                        stmt.executeUpdate("INSERT INTO USERS(USERNAME,PASSWORD,USER_TYPE) VALUES ('" + username + "','" + password + "','" + "1" + "')");
                        JOptionPane.showMessageDialog(null, "Admin added!");
                        this.dispose();
                    } else {
                        stmt.executeUpdate("INSERT INTO USERS(USERNAME,PASSWORD,USER_TYPE) VALUES ('" + username + "','" + password + "','" + "0" + "')");
                        JOptionPane.showMessageDialog(null, "User added!");
                        this.dispose();
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1);
                }
            });

            user_entry_cancel_btn.addActionListener(e -> this.dispose());

            this.add(l1);
            this.add(add_username_tf);
            this.add(l2);
            this.add(add_password_tf);
            this.add(user_type_radio1);
            this.add(user_type_radio2);
            this.add(create_btn);
            this.add(user_entry_cancel_btn);

            this.setSize(350, 200);

            this.setLayout(new GridLayout(4, 2));

            this.setVisible(true);

            this.setResizable(false);
        }
    }

    private class AddBookFrame extends JFrame {
        AddBookFrame(){
            //Creating Frame.
            setTitle("Enter Book Details");

            JLabel l1, l2, l3, l4, l5, l6, l7;
            l1 = new JLabel("ISBN", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);
            l2 = new JLabel("Name", SwingConstants.CENTER);
            l2.setOpaque(true);
            l2.setBackground(Utils.BACKGROUND_COLOR);
            l2.setForeground(Utils.FOREGROUND_COLOR);
            l3 = new JLabel("Publisher", SwingConstants.CENTER);
            l3.setOpaque(true);
            l3.setBackground(Utils.BACKGROUND_COLOR);
            l3.setForeground(Utils.FOREGROUND_COLOR);
            l4 = new JLabel("Edition", SwingConstants.CENTER);
            l4.setOpaque(true);
            l4.setBackground(Utils.BACKGROUND_COLOR);
            l4.setForeground(Utils.FOREGROUND_COLOR);
            l5 = new JLabel("Genre", SwingConstants.CENTER);
            l5.setOpaque(true);
            l5.setBackground(Utils.BACKGROUND_COLOR);
            l5.setForeground(Utils.FOREGROUND_COLOR);
            l6 = new JLabel("Price", SwingConstants.CENTER);
            l6.setOpaque(true);
            l6.setBackground(Utils.BACKGROUND_COLOR);
            l6.setForeground(Utils.FOREGROUND_COLOR);
            l7 = new JLabel("Pages", SwingConstants.CENTER);
            l7.setOpaque(true);
            l7.setBackground(Utils.BACKGROUND_COLOR);
            l7.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_isbn_tf = new JTextField();
            book_isbn_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_isbn_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_name_tf = new JTextField();
            book_name_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_name_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_publisher_tf = new JTextField();
            book_publisher_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_publisher_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_edition_tf = new JTextField();
            book_edition_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_edition_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_genre_tf = new JTextField();
            book_genre_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_genre_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_price_tf = new JTextField();
            book_price_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_price_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_pages_tf = new JTextField();
            book_pages_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_pages_tf.setForeground(Utils.FOREGROUND_COLOR);

            JButton create_btn = new JButton("Submit");
            create_btn.setBackground(Utils.BUTTON_COLOR);
            create_btn.setForeground(Utils.FOREGROUND_COLOR);

            JButton add_book_cancel_btn = new JButton("Cancel");
            add_book_cancel_btn.setBackground(Utils.BUTTON_COLOR);
            add_book_cancel_btn.setForeground(Utils.FOREGROUND_COLOR);

            create_btn.addActionListener(e -> {

                String book_isbn = book_isbn_tf.getText();
                String book_name = book_name_tf.getText();
                String book_publisher = book_publisher_tf.getText();
                String book_edition = book_edition_tf.getText();
                String book_genre = book_genre_tf.getText();

                int book_price = Integer.parseInt(book_price_tf.getText());
                int book_pages = Integer.parseInt(book_pages_tf.getText());

                Connection connection = BddComm.getInstance().connect();

                try {
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("INSERT INTO BOOKS(book_isbn,book_name,book_publisher,book_edition,book_genre,book_price,book_pages)"
                            + " VALUES ('" + book_isbn + "','" + book_name + "','" + book_publisher + "','" + book_edition + "','" + book_genre + "','" + book_price + "'," + book_pages + ")");
                    JOptionPane.showMessageDialog(null, "Book added!");
                    this.dispose();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1);
                }
            });

            add_book_cancel_btn.addActionListener(e -> this.dispose());

            this.add(l1);
            this.add(book_isbn_tf);
            this.add(l2);
            this.add(book_name_tf);
            this.add(l3);
            this.add(book_publisher_tf);
            this.add(l4);
            this.add(book_edition_tf);
            this.add(l5);
            this.add(book_genre_tf);
            this.add(l6);
            this.add(book_price_tf);
            this.add(l7);
            this.add(book_pages_tf);
            this.add(create_btn);
            this.add(add_book_cancel_btn);

            this.setSize(800, 500);

            this.setLayout(new GridLayout(8, 2));

            this.setVisible(true);

            this.setResizable(false);
        }
    }

    private class IssueBookFrame extends JFrame {
        IssueBookFrame(){
            setTitle("Enter Details");

            JPanel pickerPanel = new JPanel();
            JXDatePicker picker = new JXDatePicker();
            picker.setDate(Calendar.getInstance().getTime());
            picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
            pickerPanel.add(picker);
            pickerPanel.setBackground(Utils.BACKGROUND_COLOR);
            pickerPanel.setForeground(Utils.FOREGROUND_COLOR);

            JLabel l1, l2, l3, l4;
            l1 = new JLabel("Book ID", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);
            l2 = new JLabel("User/Student ID", SwingConstants.CENTER);
            l2.setOpaque(true);
            l2.setBackground(Utils.BACKGROUND_COLOR);
            l2.setForeground(Utils.FOREGROUND_COLOR);
            l3 = new JLabel("Period(days)", SwingConstants.CENTER);
            l3.setOpaque(true);
            l3.setBackground(Utils.BACKGROUND_COLOR);
            l3.setForeground(Utils.FOREGROUND_COLOR);
            l4 = new JLabel("Issued Date(DD-MM-YYYY)", SwingConstants.CENTER);
            l4.setOpaque(true);
            l4.setBackground(Utils.BACKGROUND_COLOR);
            l4.setForeground(Utils.FOREGROUND_COLOR);

            JTextField bid_tf = new JTextField();
            bid_tf.setBackground(Utils.BACKGROUND_COLOR);
            bid_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField uid_tf = new JTextField();
            uid_tf.setBackground(Utils.BACKGROUND_COLOR);
            uid_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField period_tf = new JTextField();
            period_tf.setBackground(Utils.BACKGROUND_COLOR);
            period_tf.setForeground(Utils.FOREGROUND_COLOR);

            JButton create_btn = new JButton("Submit");
            create_btn.setBackground(Utils.BUTTON_COLOR);
            create_btn.setForeground(Utils.FOREGROUND_COLOR);

            JButton issue_book_cancel_btn = new JButton("Cancel");
            issue_book_cancel_btn.setBackground(Utils.BUTTON_COLOR);
            issue_book_cancel_btn.setForeground(Utils.FOREGROUND_COLOR);

            create_btn.addActionListener(e -> {

                int uid = Integer.parseInt(uid_tf.getText());
                int bid = Integer.parseInt(bid_tf.getText());
                String period = period_tf.getText();
                Date oDate = picker.getDate();

                DateFormat oDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String issued_date = oDateFormat.format(oDate);

                int period_int = Integer.parseInt(period);

                Connection connection = BddComm.getInstance().connect();

                try {
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("INSERT INTO issued_books(UID,BID,ISSUED_DATE,PERIOD) VALUES ('" + uid + "','" + bid + "','" + issued_date + "'," + period_int + ")");

                    JOptionPane.showMessageDialog(null, "Book Issued!");
                    this.dispose();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1);
                }
            });

            issue_book_cancel_btn.addActionListener(e -> this.dispose());

            this.add(l1);
            this.add(bid_tf);
            this.add(l2);
            this.add(uid_tf);
            this.add(l3);
            this.add(period_tf);
            this.add(l4);
            this.getContentPane().add(pickerPanel);
            this.add(create_btn);
            this.add(issue_book_cancel_btn);

            this.setSize(600, 300);

            this.setLayout(new GridLayout(5, 2));

            this.setVisible(true);

            this.setResizable(false);
        }
    }

    private class ReturnBookFrame extends JFrame {
        ReturnBookFrame(){
            setTitle("Enter Details");

            JLabel l1 = new JLabel("Book ID", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);
            JLabel l2 = new JLabel("User ID", SwingConstants.CENTER);
            l2.setOpaque(true);
            l2.setBackground(Utils.BACKGROUND_COLOR);
            l2.setForeground(Utils.FOREGROUND_COLOR);
            JLabel l3 = new JLabel("Return Date(DD-MM-YYYY)", SwingConstants.CENTER);
            l3.setOpaque(true);
            l3.setBackground(Utils.BACKGROUND_COLOR);
            l3.setForeground(Utils.FOREGROUND_COLOR);
            JLabel l4 = new JLabel("Fine", SwingConstants.CENTER);
            l4.setOpaque(true);
            l4.setBackground(Utils.BACKGROUND_COLOR);
            l4.setForeground(Utils.FOREGROUND_COLOR);

            JTextField bid_tf = new JTextField();
            bid_tf.setBackground(Utils.BACKGROUND_COLOR);
            bid_tf.setForeground(Utils.FOREGROUND_COLOR);

            JTextField uid_tf = new JTextField();
            uid_tf.setBackground(Utils.BACKGROUND_COLOR);
            uid_tf.setForeground(Utils.FOREGROUND_COLOR);

            JPanel pickerPanel = new JPanel();
            JXDatePicker picker = new JXDatePicker();
            picker.setDate(Calendar.getInstance().getTime());
            picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

            JTextField fine_tf = new JTextField();
            fine_tf.setBackground(Utils.BACKGROUND_COLOR);
            fine_tf.setForeground(Utils.FOREGROUND_COLOR);

            pickerPanel.add(picker);

            pickerPanel.setBackground(Utils.BACKGROUND_COLOR);
            pickerPanel.setForeground(Utils.FOREGROUND_COLOR);

            JButton return_book_btn = new JButton("Return");
            return_book_btn.setBackground(Utils.BUTTON_COLOR);
            return_book_btn.setForeground(Utils.FOREGROUND_COLOR);

            JButton cancel_book_btn = new JButton("Cancel");
            cancel_book_btn.setBackground(Utils.BUTTON_COLOR);
            cancel_book_btn.setForeground(Utils.FOREGROUND_COLOR);

            return_book_btn.addActionListener(e -> {

                int bid = Integer.parseInt(bid_tf.getText());
                int uid = Integer.parseInt(uid_tf.getText());
                int fine = Integer.parseInt(fine_tf.getText());
                Date oDate = picker.getDate();
                DateFormat oDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String return_date = oDateFormat.format(oDate);

                try {
                    Connection connection = BddComm.getInstance().connect();

                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("INSERT INTO returned_books(bid,UID,return_date,fine) VALUES ('" + bid + "','" + uid + "','" + return_date + "'," + fine + ")");
                    JOptionPane.showMessageDialog(null, "Book Returned!");
                    this.dispose();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1);
                }
            });

            cancel_book_btn.addActionListener(e -> this.dispose());

            this.add(l1);
            this.add(bid_tf);
            this.add(l2);
            this.add(uid_tf);
            this.add(l3);
            this.getContentPane().add(pickerPanel);
            this.add(l4);
            this.add(fine_tf);
            this.add(return_book_btn);
            this.add(cancel_book_btn);

            this.setSize(600, 300);

            this.setLayout(new GridLayout(5, 2));

            this.setVisible(true);

            this.setResizable(false);
        }
    }

    private class DeleteBookFrame extends JFrame{
        DeleteBookFrame(){
            setTitle("Delete a Book");

            JLabel l1 = new JLabel("Book ID", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);

            JTextField book_id_tf = new JTextField();
            book_id_tf.setBackground(Utils.BACKGROUND_COLOR);
            book_id_tf.setForeground(Utils.FOREGROUND_COLOR);

            JButton delete_btn = new JButton("Confirm");
            delete_btn.setBackground(Utils.BUTTON_COLOR);
            delete_btn.setForeground(Utils.FOREGROUND_COLOR);

            JButton delete_book_cancel_btn = new JButton("Cancel");
            delete_book_cancel_btn.setBackground(Utils.BUTTON_COLOR);
            delete_book_cancel_btn.setForeground(Utils.FOREGROUND_COLOR);

            delete_btn.addActionListener(e -> {

                int bid = Integer.parseInt(book_id_tf.getText());

                try {
                    Connection connection = BddComm.getInstance().connect();

                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("DELETE FROM books where bid = "+ bid);
                    JOptionPane.showMessageDialog(null, "Book Deleted!");
                    this.dispose();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1);
                }
            });

            delete_book_cancel_btn.addActionListener(e -> this.dispose());

            this.add(l1);
            this.add(book_id_tf);
            this.add(delete_btn);
            this.add(delete_book_cancel_btn);

            this.setSize(600, 200);

            this.setLayout(new GridLayout(2, 2));

            this.setVisible(true);

            this.setResizable(false);
        }
    }

    private class ChangePasswordFrame extends JFrame{
        ChangePasswordFrame(){
            setTitle("Change User Password");

            JLabel l1 = new JLabel("User ID", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);

            JTextField user_id_tf = new JTextField();
            user_id_tf.setBackground(Utils.BACKGROUND_COLOR);
            user_id_tf.setForeground(Utils.FOREGROUND_COLOR);

            JLabel l2 = new JLabel("New Password", SwingConstants.CENTER);
            l2.setOpaque(true);
            l2.setBackground(Utils.BACKGROUND_COLOR);
            l2.setForeground(Utils.FOREGROUND_COLOR);

            JTextField new_pass_tf = new JTextField();
            new_pass_tf.setBackground(Utils.BACKGROUND_COLOR);
            new_pass_tf.setForeground(Utils.FOREGROUND_COLOR);

            JButton change_btn = new JButton("Confirm");
            change_btn.setBackground(Utils.BUTTON_COLOR);
            change_btn.setForeground(Utils.FOREGROUND_COLOR);

            JButton change_cancel_btn = new JButton("Cancel");
            change_cancel_btn.setBackground(Utils.BUTTON_COLOR);
            change_cancel_btn.setForeground(Utils.FOREGROUND_COLOR);

            change_btn.addActionListener(e -> {

                int uid = Integer.parseInt(user_id_tf.getText());
                String pass = new_pass_tf.getText();

                try {
                    Connection connection = BddComm.getInstance().connect();

                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("UPDATE users SET password = '" + pass + "' WHERE uid = "+uid);
                    JOptionPane.showMessageDialog(null, "Password Changed!");
                    this.dispose();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1);
                }
            });

            change_cancel_btn.addActionListener(e -> this.dispose());

            this.add(l1);
            this.add(user_id_tf);
            this.add(l2);
            this.add(new_pass_tf);
            this.add(change_btn);
            this.add(change_cancel_btn);

            this.setSize(600, 200);

            this.setLayout(new GridLayout(3, 2));

            this.setVisible(true);

            this.setResizable(false);
        }
    }

    private class ImportDataJsonFrame extends JFrame {

        File file = null;

        ImportDataJsonFrame() {

            setTitle("Import Json");

            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Json files", "json");
            chooser.setFileFilter(filter);

            JLabel l1 = new JLabel("File: ", SwingConstants.CENTER);
            l1.setOpaque(true);
            l1.setBackground(Utils.BACKGROUND_COLOR);
            l1.setForeground(Utils.FOREGROUND_COLOR);

            JButton browse_btn = new JButton("Browse");
            browse_btn.setBackground(Utils.BUTTON_COLOR);
            browse_btn.setForeground(Utils.FOREGROUND_COLOR);
            browse_btn.addActionListener(e-> {
                int returnval = chooser.showOpenDialog(null);
                if(returnval == JFileChooser.APPROVE_OPTION){
                    l1.setText("File: " + chooser.getSelectedFile().getName());
                    file = chooser.getSelectedFile();
                } else {
                    this.dispose();
                }
            });

            JButton import_overwrite_btn = new JButton("Import (overwrite)");
            import_overwrite_btn.setBackground(Utils.BUTTON_COLOR);
            import_overwrite_btn.setForeground(Utils.FOREGROUND_COLOR);
            import_overwrite_btn.addActionListener(e -> {
                if(file==null) return;
                int confirm = JOptionPane.showConfirmDialog(null, "Importing will delete already existing data in you database. Are you sure?");
                if(confirm == 0){
                    if(file.length() < 1){
                        JOptionPane.showMessageDialog(null, "Json File Empty");
                    }else {
                        BddComm.getInstance().ImportDataJson(file);
                    }
                }
            });

            this.add(l1);
            this.add(browse_btn);
            this.add(import_overwrite_btn);

            this.setSize(350, 200);

            this.setLayout(new GridLayout(3, 1));

            this.setVisible(true);

            this.setResizable(false);
        }
    }
}
