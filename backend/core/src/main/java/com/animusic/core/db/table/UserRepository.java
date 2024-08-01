package com.animusic.core.db.table;

import java.util.Optional;

import com.animusic.core.db.model.Playlist;
import com.animusic.core.db.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.NonNull;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

@Component
@NoRepositoryBean
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    static final String FAVOURITE_PLAYLIST_NAME = "Favourite tracks";

    Optional<Playlist> getUserFavouritePlaylist(Integer userId);

    class Impl extends RepositoryBase<User, Integer> implements UserRepository {

        public Impl(@NonNull EntityManager entityManager) {
            super(entityManager, User.class);
        }

        @Override
        public Optional<User> findByEmail(String email) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            var query = cb.createQuery(User.class);
            var root = query.from(User.class);
            query.select(root)
                    .where(cb.equal(root.get("email"), email));
            return getOptionalResult(entityManager.createQuery(query));
        }

        @Override
        public Optional<Playlist> getUserFavouritePlaylist(Integer userId) {
            var query = "SELECT p FROM Playlist p WHERE p.user.id = :userId AND p.name = :name";
            return getOptionalResult(
                    entityManager.createQuery(query, Playlist.class)
                            .setParameter("userId", userId)
                            .setParameter("name", FAVOURITE_PLAYLIST_NAME)
            );
        }
    }
}
