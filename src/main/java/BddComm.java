import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class BddComm {

    private static BddComm instance;

    public static BddComm getInstance() {
        if(instance == null) instance = new BddComm();
        return instance;
    }

    public Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?useSSL=false", "root", "admin");
            return conn;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean exportDataToJsonFile(){
        try {
            Connection conn = connect();
            if(conn.isClosed()) return false;
            JSONObject jsonObject = new JSONObject();
            JSONArray arrayBooks = new JSONArray();
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select * from books");
            while(result.next()) {
                JSONObject record = new JSONObject();
                record.put("BID", result.getInt("bid"));
                record.put("BookISBN", result.getString("book_isbn"));
                record.put("BookName", result.getString("book_name"));
                record.put("BookPublisher", result.getString("book_publisher"));
                record.put("BookEdition", result.getString("book_edition"));
                record.put("BookGenre", result.getString("book_genre"));
                record.put("BookPrice", result.getInt("book_price"));
                record.put("BookPages", result.getInt("book_pages"));
                arrayBooks.add(record);
            }
            jsonObject.put("Books", arrayBooks);
            JSONArray arrayUsers = new JSONArray();
            result = stmt.executeQuery("select * from users");
            while(result.next()) {
                JSONObject record = new JSONObject();
                record.put("UID", result.getInt("uid"));
                record.put("Username", result.getString("username"));
                record.put("Password", result.getString("password"));
                record.put("UserType", result.getInt("user_type"));
                arrayUsers.add(record);
            }
            jsonObject.put("Users", arrayUsers);
            JSONArray arrayIssued = new JSONArray();
            result = stmt.executeQuery("select * from issued_books");
            while(result.next()) {
                JSONObject record = new JSONObject();
                record.put("IID", result.getInt("iid"));
                record.put("UID", result.getInt("uid"));
                record.put("BID", result.getInt("bid"));
                record.put("IssuedDate", result.getString("issued_date"));
                record.put("period", result.getInt("period"));
                arrayIssued.add(record);
            }
            jsonObject.put("IssuedBooks", arrayIssued);
            JSONArray arrayReturned = new JSONArray();
            result = stmt.executeQuery("select * from returned_books");
            while(result.next()) {
                JSONObject record = new JSONObject();
                record.put("RID", result.getInt("rid"));
                record.put("UID", result.getInt("uid"));
                record.put("BID", result.getInt("bid"));
                record.put("ReturnDate", result.getString("return_date"));
                record.put("fine", result.getInt("fine"));
                arrayReturned.add(record);
            }
            jsonObject.put("ReturnedBooks", arrayReturned);
            FileWriter file = new FileWriter("output.json");
            file.write(jsonObject.toJSONString());
            file.close();
            conn.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Json file created");
        return true;
    }

    public void ImportDataJson(File file){
        if(file == null) return;

        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(file);

            DeleteData();
            Object obj = jsonParser.parse(reader);
            JSONArray array = (JSONArray) ((JSONObject)obj).get("Books");
            array.forEach(subObj -> {
                JSONObject pObject = (JSONObject) subObj;
                try {
                    stmt.executeUpdate("Insert into books values ("+pObject.get("BID")+", '"+pObject.get("BookISBN")+"', '"+pObject.get("BookName")+"', '"+pObject.get("BookPublisher")+"', '"+pObject.get("BookEdition")+"', '"+pObject.get("BookGenre")+"', "+pObject.get("BookPrice")+", "+pObject.get("BookPages")+")" );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            array = (JSONArray) ((JSONObject)obj).get("Users");
            array.forEach(subObj -> {
                JSONObject pObject = (JSONObject) subObj;
                try {
                    stmt.executeUpdate("Insert into users values ("+pObject.get("UID")+", '"+pObject.get("Username")+"', '"+pObject.get("Password")+"', "+pObject.get("UserType")+")" );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            array = (JSONArray) ((JSONObject)obj).get("IssuedBooks");
            array.forEach(subObj -> {
                JSONObject pObject = (JSONObject) subObj;
                try {
                    stmt.executeUpdate("Insert into issued_books values ("+pObject.get("IID")+", "+pObject.get("UID")+","+pObject.get("BID")+", '"+pObject.get("IssuedDate")+"', "+pObject.get("period")+")" );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            array = (JSONArray) ((JSONObject)obj).get("ReturnedBooks");
            array.forEach(subObj -> {
                JSONObject pObject = (JSONObject) subObj;
                try {
                    stmt.executeUpdate("Insert into returned_books values ("+pObject.get("RID")+","+pObject.get("BID")+","+pObject.get("UID")+", '"+pObject.get("ReturnDate")+"', "+pObject.get("fine")+")" );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteData() throws SQLException {
        Connection connection = connect();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("Delete from issued_books");
        stmt.executeUpdate("Delete from returned_books");
        stmt.executeUpdate("Delete from books");
        stmt.executeUpdate("Delete from users");
        connection.close();
    }

//    public ArrayList<Object[]> parsePlayersJson(){
//
//        retrievePlayers();
//        ArrayList<Object[]> data = new ArrayList<>();
//
//        try {
//            JSONParser jsonParser = new JSONParser();
//            FileReader reader = new FileReader("output.json");
//            Object obj = jsonParser.parse(reader);
//            JSONArray array = (JSONArray) ((JSONObject)obj).get("Players");
//            array.forEach(player -> {
//                JSONObject pObject = (JSONObject) player;
//                Object[] pData = new Object[] {pObject.get("ID"), pObject.get("PlayerName"), pObject.get("Score")};
//                data.add(pData);
//            });
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//
//        return data;
//    }
}
