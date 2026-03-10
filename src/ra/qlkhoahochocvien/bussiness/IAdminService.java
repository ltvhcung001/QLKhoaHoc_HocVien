package ra.qlkhoahochocvien.bussiness;

import ra.qlkhoahochocvien.model.Admin;

public interface IAdminService {
    public Admin login(String username, String password);

    void setPageSize(int pageSize);
}
