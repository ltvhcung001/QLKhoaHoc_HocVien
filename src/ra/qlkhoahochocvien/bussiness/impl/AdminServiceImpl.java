package ra.qlkhoahochocvien.bussiness.impl;

import ra.qlkhoahochocvien.bussiness.IAdminService;
import ra.qlkhoahochocvien.dao.IAdminDAO;
import ra.qlkhoahochocvien.dao.impl.AdminDAOImpl;
import ra.qlkhoahochocvien.model.Admin;

public class AdminServiceImpl implements IAdminService {
    public final IAdminDAO adminDAO = new AdminDAOImpl();

    @Override
    public Admin login(String username, String password) {
        Admin admin = adminDAO.findAdminByUsername(username);
        if (admin != null)
            if (admin.getPassword().equals(password))
                return admin;

        return null;
    }
}
