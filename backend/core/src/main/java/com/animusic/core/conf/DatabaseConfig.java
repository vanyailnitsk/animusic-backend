package com.animusic.core.conf;

import javax.sql.DataSource;

import com.animusic.core.db.table.AlbumRepository;
import com.animusic.core.db.table.AnimeBannerImageRepository;
import com.animusic.core.db.table.AnimeRepository;
import com.animusic.core.db.table.CoverArtRepository;
import com.animusic.core.db.table.ImageRepository;
import com.animusic.core.db.table.PlaylistRepository;
import com.animusic.core.db.table.PlaylistSoundtrackRepository;
import com.animusic.core.db.table.RefreshTokenRepository;
import com.animusic.core.db.table.SoundtrackRepository;
import com.animusic.core.db.table.UserRepository;
import jakarta.persistence.EntityManager;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.animusic.core.db.table")
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/changelog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    @Bean
    public AlbumRepository albumRepository(EntityManager entityManager) {
        return new AlbumRepository.Impl(entityManager);
    }

    @Bean
    public AnimeRepository animeRepository(EntityManager entityManager) {
        return new AnimeRepository.Impl(entityManager);
    }

    @Bean
    public AnimeBannerImageRepository animeBannerImageRepository(EntityManager entityManager) {
        return new AnimeBannerImageRepository.Impl(entityManager);
    }

    @Bean
    public CoverArtRepository coverArtRepository(EntityManager entityManager) {
        return new CoverArtRepository.Impl(entityManager);
    }

    @Bean
    public ImageRepository imageRepository(EntityManager entityManager) {
        return new ImageRepository.Impl(entityManager);
    }

    @Bean
    public PlaylistRepository playlistRepository(EntityManager entityManager) {
        return new PlaylistRepository.Impl(entityManager);
    }

    @Bean
    public PlaylistSoundtrackRepository playlistSoundtrackRepository(EntityManager entityManager) {
        return new PlaylistSoundtrackRepository.Impl(entityManager);
    }

    @Bean
    public SoundtrackRepository soundtrackRepository(EntityManager entityManager) {
        return new SoundtrackRepository.Impl(entityManager);
    }

    @Bean
    public UserRepository userRepository(EntityManager entityManager) {
        return new UserRepository.Impl(entityManager);
    }

    @Bean
    public RefreshTokenRepository refreshTokenRepository(EntityManager entityManager) {
        return new RefreshTokenRepository.Impl(entityManager);
    }

}
