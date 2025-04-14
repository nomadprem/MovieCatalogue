package com.example.moviecatalogue.service;

import com.example.moviecatalogue.model.FavoriteMovie;
import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.repository.FavoriteMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteMovieRepository favoriteMovieRepository;

    @Autowired
    public FavoriteService(FavoriteMovieRepository favoriteMovieRepository) {
        this.favoriteMovieRepository = favoriteMovieRepository;
    }

    public FavoriteMovie addFavorite(Movie movie) {
        // Check if the movie is already in favorites.
        if (favoriteMovieRepository.findByTmdbId(movie.getId()).isPresent()) {
            throw new IllegalStateException("Movie already in favorites");
        }
        FavoriteMovie favorite = new FavoriteMovie();
        favorite.setTmdbId(movie.getId());
        favorite.setTitle(movie.getTitle());
        favorite.setPosterPath(movie.getPosterPath());
        favorite.setOverview(movie.getOverview());
        favorite.setReleaseDate(movie.getReleaseDate());
        favorite.setRating(movie.getRating());

        return favoriteMovieRepository.save(favorite);
    }

    public List<FavoriteMovie> listFavorites() {
        return favoriteMovieRepository.findAll();
    }

    public void removeFavorite(Long tmdbId) {
        FavoriteMovie movie = favoriteMovieRepository.findByTmdbId(tmdbId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found in favorites"));
        favoriteMovieRepository.delete(movie);
    }
}
