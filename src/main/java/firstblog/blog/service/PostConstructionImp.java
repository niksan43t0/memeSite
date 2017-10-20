package firstblog.blog.service;

import firstblog.blog.entity.Category;
import firstblog.blog.entity.Meme;
import firstblog.blog.entity.Role;
import firstblog.blog.entity.User;
import firstblog.blog.repository.CategoryRepository;
import firstblog.blog.repository.MemeRepository;
import firstblog.blog.repository.RoleRepository;
import firstblog.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.HashSet;

@Service
public class PostConstructionImp implements PostConstruction{

    private final String ABC123 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private SecureRandom secureRandom = new SecureRandom();

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final CategoryRepository categoryRepository;

    private final MemeRepository memeRepository;

    @Autowired
    public PostConstructionImp(UserRepository userRepository, RoleRepository roleRepository, CategoryRepository categoryRepository, MemeRepository memeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.memeRepository = memeRepository;
    }

    @Override
    public void createRoles() {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        this.roleRepository.saveAndFlush(userRole);
        this.roleRepository.saveAndFlush(adminRole);
    }

    @Override
    public void createAdmin() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                "ng@gmail.com",
                "Nick Fury",
                bCryptPasswordEncoder.encode("123"),
                null
        );
        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);
        Role adminRole = this.roleRepository.findByName("ROLE_ADMIN");
        user.addRole(adminRole);

        this.userRepository.saveAndFlush(user);
    }


    @Override
    public void createCategories() {
        Category firstCategory = new Category("AutoCreated");
        this.categoryRepository.saveAndFlush(firstCategory);
    }

    @Override
    public void addMemes(){
        User user = this.userRepository.findByEmail("ng@gmail.com");
        Category category = this.categoryRepository.findByName("AutoCreated");

        //creates 50 memes in the repository, images are saved in project build
        for(int i = 0; i<50; i++){
            Meme meme = new Meme(randomString(8),
                    category,
                    new HashSet<>(),
                    user);
            this.memeRepository.saveAndFlush(meme);
        }
    }

    private String randomString(int length){

        StringBuilder myString = new StringBuilder(length);

        for( int j = 0; j < 8; j++ )
            myString.append( ABC123.charAt( secureRandom.nextInt(ABC123.length()) ) );

        return myString.toString();
    }

    @PostConstruct
    @Override
    public void initializeDatabase(){
        createRoles();
        createAdmin();
        createCategories();
        addMemes();
    }
}
