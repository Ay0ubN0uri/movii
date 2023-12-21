import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import {MovieScrapper} from "app/modules/movie_scrapper/movie_scrapper";


const MovieScrapperRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>

    </ErrorBoundaryRoutes>
  </div>
);

export default MovieScrapperRoutes;
