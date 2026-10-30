package com.railway.dao;
import com.railway.model.User;import java.sql.*;
public class UserDao {
 public boolean create(String name,String email,String phone,String hash)throws SQLException{try(Connection c=com.railway.util.DB.getConnection();PreparedStatement p=c.prepareStatement("INSERT INTO users(full_name,email,phone,password_hash) VALUES(?,?,?,?)")){p.setString(1,name);p.setString(2,email.toLowerCase());p.setString(3,phone);p.setString(4,hash);return p.executeUpdate()==1;}}
 public User authenticate(String email,String password)throws SQLException{try(Connection c=com.railway.util.DB.getConnection();PreparedStatement p=c.prepareStatement("SELECT id,full_name,email,phone,password_hash,role FROM users WHERE email=?")){p.setString(1,email.toLowerCase());try(ResultSet r=p.executeQuery()){if(r.next()&&com.railway.util.Passwords.verify(password,r.getString("password_hash")))return new User(r.getLong("id"),r.getString("full_name"),r.getString("email"),r.getString("phone"),r.getString("role"));return null;}}}
}
