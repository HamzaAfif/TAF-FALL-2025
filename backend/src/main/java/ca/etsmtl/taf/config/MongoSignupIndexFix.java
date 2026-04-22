package ca.etsmtl.taf.config;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Defensive startup fix: remove an invalid unique index accidentally created on users.roles.name.
 * That index blocks normal user registration when multiple users share ROLE_USER.
 */
@Configuration
public class MongoSignupIndexFix {

  private static final Logger logger = LoggerFactory.getLogger(MongoSignupIndexFix.class);

  @Bean
  CommandLineRunner dropInvalidUsersRolesIndex(MongoTemplate mongoTemplate) {
    return args -> {
      try {
        var usersCollection = mongoTemplate.getCollection("users");

        // Try dropping by key first (works even if index name differs between environments).
        try {
          usersCollection.dropIndex(new Document("roles.name", 1));
          logger.warn("Dropped invalid unique index on users.roles.name");
        } catch (Exception keyDropEx) {
          // Fallback to known generated names and ignore if not found.
          try {
            usersCollection.dropIndex("roles.name");
            logger.warn("Dropped invalid unique index named roles.name");
          } catch (Exception nameDropEx) {
            try {
              usersCollection.dropIndex("roles.name_1");
              logger.warn("Dropped invalid unique index named roles.name_1");
            } catch (Exception ignored) {
              logger.info("No invalid users.roles.name index found to drop.");
            }
          }
        }
      } catch (Exception ex) {
        logger.warn("Unable to run signup index fix: {}", ex.getMessage());
      }
    };
  }
}
