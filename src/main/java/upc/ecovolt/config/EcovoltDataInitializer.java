package upc.ecovolt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Catalog;
import upc.ecovolt.entity.DataCatalog;
import upc.ecovolt.entity.Role;
import upc.ecovolt.entity.SubscriptionPlan;
import upc.ecovolt.entity.User;
import upc.ecovolt.repository.CatalogRepository;
import upc.ecovolt.repository.DataCatalogRepository;
import upc.ecovolt.repository.RoleRepository;
import upc.ecovolt.repository.SubscriptionPlanRepository;
import upc.ecovolt.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EcovoltDataInitializer implements CommandLineRunner {

    public static final String ADMIN_LOGIN = "admin.ecovolt";
    public static final String CUSTOMER_LOGIN = "maria.customer";
    public static final String DEFAULT_PASSWORD = "Ecovolt123";

    private final CatalogRepository catalogRepository;
    private final DataCatalogRepository dataCatalogRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionPlanRepository planRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        DataCatalog basicSupport = dataCatalog("SUPPORT_LEVELS", "Basic Support");
        dataCatalog("ROOM_TYPES", "Sala");
        dataCatalog("ROOM_TYPES", "Cocina");
        dataCatalog("DEVICE_CATEGORIES", "Iluminacion");
        dataCatalog("DEVICE_CATEGORIES", "Climatizacion");
        dataCatalog("DEVICE_STATUS", "Activo");

        SubscriptionPlan freePlan = planRepository.findByName("Free")
                .orElseGet(() -> {
                    SubscriptionPlan plan = new SubscriptionPlan();
                    plan.setName("Free");
                    plan.setMonthlyPrice(BigDecimal.ZERO);
                    plan.setDeviceLimit(5);
                    plan.setSupportLevel(basicSupport);
                    plan.setBillingCycle("MONTHLY");
                    plan.setCreatedBy("SYSTEM");
                    return planRepository.save(plan);
                });

        Role adminRole = role("ROLE_ADMIN");
        Role customerRole = role("ROLE_CUSTOMER");

        user(ADMIN_LOGIN, "admin@ecovolt.local", "Admin", "Ecovolt", "900000001", freePlan, Set.of(adminRole));
        user(CUSTOMER_LOGIN, "maria@ecovolt.local", "Maria", "Customer", "900000002", freePlan, Set.of(customerRole));
    }

    private Catalog catalog(String description) {
        return catalogRepository.findByDescription(description)
                .orElseGet(() -> {
                    Catalog catalog = new Catalog();
                    catalog.setDescription(description);
                    return catalogRepository.save(catalog);
                });
    }

    private DataCatalog dataCatalog(String catalogDescription, String description) {
        return dataCatalogRepository.findByDescriptionAndCatalog(description, catalogDescription).stream()
                .findFirst()
                .orElseGet(() -> {
                    DataCatalog dataCatalog = new DataCatalog();
                    dataCatalog.setCatalog(catalog(catalogDescription));
                    dataCatalog.setDescription(description);
                    return dataCatalogRepository.save(dataCatalog);
                });
    }

    private Role role(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setStatus(1);
                    return roleRepository.save(role);
                });
    }

    private void user(
            String login,
            String email,
            String firstName,
            String lastName,
            String phoneNumber,
            SubscriptionPlan plan,
            Set<Role> roles
    ) {
        userRepository.findByLogin(login).orElseGet(() -> {
            User user = new User();
            user.setLogin(login);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setSubscriptionPlan(plan);
            user.setRoles(roles);
            user.setCreatedBy("SYSTEM");
            return userRepository.save(user);
        });
    }
}
