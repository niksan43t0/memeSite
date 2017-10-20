package firstblog.blog.service;

public interface PostConstruction {
    void createRoles();
    void createAdmin();
    void createCategories();
    void addMemes();
    void initializeDatabase();
}
