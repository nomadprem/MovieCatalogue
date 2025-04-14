package com.example.moviecatalogue.service;

import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.model.MovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class MovieService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Movie> getTrendingMovies() {
        String url = "https://api.themoviedb.org/3/trending/movie/week?api_key=" + apiKey;
        ResponseEntity<MovieResponse> response = restTemplate.getForEntity(url, MovieResponse.class);
        return response.getBody() != null ? response.getBody().getResults() : Collections.emptyList();
    }

    public Movie getMovieDetails(Long movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        Movie movie = restTemplate.getForObject(url, Movie.class);
        System.out.println("Movie details fetched: " + movie);
        return movie;
    }


    public List<Movie> searchMovies(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + encodedQuery;
        ResponseEntity<MovieResponse> response = restTemplate.getForEntity(url, MovieResponse.class);
        return response.getBody() != null ? response.getBody().getResults() : Collections.emptyList();
    }

}
