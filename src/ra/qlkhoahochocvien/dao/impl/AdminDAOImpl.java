package ra.qlkhoahochocvien.dao.impl;

import ra.qlkhoahochocvien.dao.IAdminDAO;
import ra.qlkhoahochocvien.model.Admin;
import ra.qlkhoahochocvien.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAOImpl implements IAdminDAO {
    @Override
    public Admin findAdminByUsername(String username) {
        String sql = "Select * from admin where username=?";
        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
            else {
                return null;
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
