package ra.qlkhoahochocvien.dao;

import ra.qlkhoahochocvien.model.Admin;

public interface IAdminDAO {
    Admin findAdminByUsername(String username);
}
