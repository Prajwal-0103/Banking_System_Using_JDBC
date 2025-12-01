import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {
    private Connection conn;
    private Scanner sc;
    public User(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void register(){
        sc.nextLine();
        System.out.print("Enter Full name: ");
        String fullName = sc.nextLine();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        email = email.toLowerCase();
        if(!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")){
            System.out.println("Invalid Email! Must contains @gmail.com address.");
            return;
        }
        if(user_exist(email)){
            System.out.println("Email already exists!  Please login");
            return;
        }
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        if(!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$")){
            System.out.println("Invalid Password!");
            System.out.println("Password must be:");
            System.out.println("- At least 8 characters long");
            System.out.println("- Must Contain at least one uppercase letter");
            System.out.println("- Must Contain at least one lowercase letter");
            System.out.println("- Must Contain at least one numeric digit and Special Character");
            return;
        }

        // INSERTION
        String reg_query = "INSERT INTO USER (full_name, email, password) VALUES (?, ?, ?)";
        try{
            PreparedStatement pstmt = conn.prepareStatement(reg_query);
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Account registered successfully!");
            }
            else{
                System.out.println("Account failed to register!");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String login(){
        sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM USER WHERE email = ? AND password = ?";
        try{
            PreparedStatement prest = conn.prepareStatement(login_query);
            prest.setString(1,email);
            prest.setString(2,password);
            ResultSet rs = prest.executeQuery();
            if(rs.next()){
                return email;

            }
            else{
                return null;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exist(String email){
        String query = "SELECT * FROM USER WHERE email = ?";
        try{
            PreparedStatement prst = conn.prepareStatement(query);
            prst.setString(1,email);
            ResultSet rs = prst.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
