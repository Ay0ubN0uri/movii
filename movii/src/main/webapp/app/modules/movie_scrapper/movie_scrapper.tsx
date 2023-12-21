import React, { useState } from 'react';
import axios from 'axios';
import jsonData from './data.json';

export const MovieScrapper = () => {
  const [reviewsLimit, setReviewsLimit] = useState(1);
  const [moviesLimit, setMoviesLimit] = useState(1);
  const [isScraping, setIsScraping] = useState(false);


  // extract genres from the data file
  const allGenres = jsonData.map(movie => movie.genres).flat();
  const uniqueGenres = [...new Set(allGenres)];

  // extract actors from data file

  // extract movies from data file

  // extract comments from data file


  const handleGenreToDB = async () => {
    setIsScraping(true);

    try {
      for (const genreTitle of uniqueGenres) {
        const response = await axios.post(
          'http://localhost:8083/services/movie/api/genres',
          {
            name: genreTitle,
            description: null,
            icon: null,
            iconContentType: null,
          },
          {
            headers: {
              Authorization: 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwMzA0NTI1NywiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzAyOTU4ODU3fQ.uKqmDHC4CzDcdC_EqOvhLH7mWgp8tchvH9KuE6ko3nGsIdzefSeIPbsR_f1nlicmHLmEjO_zwOEWo6FJwRMR2A', // Replace with your actual access token
            },
          }
        );

        // Process the response as needed
        console.log('Saved genre:', response.data);
      }
      // const response = await axios.post(
      //   'http://localhost:8081/api/genres/',
      //   {
      //     name: data,
      //     description: null,
      //     icon : null,
      //     iconContentType : null,
      //   },
      //   {
      //     headers: {
      //       Authorization: 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwMzA0NTI1NywiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzAyOTU4ODU3fQ.uKqmDHC4CzDcdC_EqOvhLH7mWgp8tchvH9KuE6ko3nGsIdzefSeIPbsR_f1nlicmHLmEjO_zwOEWo6FJwRMR2A', // Replace with your actual access token
      //     },
      //   }
      // );

      // Process the response as needed
      // console.log('Scraped movies:', response.data);
    } catch (error) {
      console.error('Error scraping movies:', error);
    } finally {
      setIsScraping(false);
    }
  };

  return (
    <div className="container mt-5 md-5">
      <h1 className="mb-4">Scraping Configuration</h1>

      <div className="mb-3">
        <label className="form-label">Number of Movies:</label>
        <select
          className="form-select"
          value={moviesLimit}
          onChange={(e) => setMoviesLimit(Number(e.target.value))}
        >
          {[1, 15, 25, 50, 100, 250].map((num) => (
            <option key={num} value={num}>
              {num}
            </option>
          ))}
        </select>
      </div>

      <div className="mb-3">
        <label className="form-label">Number of Reviews:</label>
        <select
          className="form-select"
          value={reviewsLimit}
          onChange={(e) => setReviewsLimit(Number(e.target.value))}
        >
          {[1, 5, 10, 15, 24].map((num) => (
            <option key={num} value={num}>
              {num}
            </option>
          ))}
        </select>
      </div>

      <button
        type="button"
        className="btn btn-primary"
        onClick={handleGenreToDB}
        disabled={isScraping}
      >
        {isScraping ? 'Scraping...' : 'Start Scraping'}
      </button>
      {isScraping && (
        <div className="mt-3 progress">
          <div
            className="progress-bar progress-bar-striped progress-bar-animated"
            role="progressbar"
            style={{ width: '100%' }}
            aria-valuenow={100}
            aria-valuemin={0}
            aria-valuemax={100}
          >
            Scraping Progress
          </div>
        </div>
      )}
    </div>
  );
};

