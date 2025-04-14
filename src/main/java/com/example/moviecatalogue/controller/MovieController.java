package com.example.moviecatalogue.controller;

import com.example.moviecatalogue.model.FavoriteMovie;
import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.service.FavoriteService;
import com.example.moviecatalogue.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private FavoriteService favoriteService;

    // Display trending movies or search result on movies.html
    @GetMapping({"/", "/movies"})
    public String showMovies(Model model) {
        List<Movie> movies = movieService.getTrendingMovies();
        model.addAttribute("movies", movies);
        return "movies"; // Renders movies.html
    }

    // Show details for a specific movie
    @GetMapping("/movies/{id}")
    public String showMovieDetails(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieDetails(id);
        // (Optional) Log the movie to debug if required:
        System.out.println("Fetched movie: " + movie);
        if (movie == null) {
            model.addAttribute("error", "Movie details not found.");
            return "error";
        }
        model.addAttribute("movie", movie);
        return "movieDetails"; // This will load src/main/resources/templates/movieDetails.html
    }


    // Add a movie to favorites
    // Add to favorites
    @PostMapping("/movies/{id}/favorite")
    public String addFavorite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Movie movie = movieService.getMovieDetails(id);
        try {
            favoriteService.addFavorite(movie);
            redirectAttributes.addFlashAttribute("message", "Movie added to favorites.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "Movie is already in favorites.");
        }
        return "redirect:/movies/" + id;
    }

    // List favorites
    @GetMapping("/favorites")
    public String listFavorites(Model model) {
        List<FavoriteMovie> favorites = favoriteService.listFavorites();
        model.addAttribute("favorites", favorites);
        return "favorites"; // Loads src/main/resources/templates/favorites.html
    }

    // Remove favorite
    @PostMapping("/favorites/{tmdbId}/remove")
    public String removeFavorite(@PathVariable Long tmdbId, RedirectAttributes redirectAttributes) {
        favoriteService.removeFavorite(tmdbId);
        redirectAttributes.addFlashAttribute("message", "Movie removed from favorites.");
        return "redirect:/favorites";
    }


    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        List<Movie> movies = movieService.searchMovies(query);
        model.addAttribute("movies", movies);
        model.addAttribute("searchQuery", query);
        return "movies"; // Reuses src/main/resources/templates/movies.html
    }

}
