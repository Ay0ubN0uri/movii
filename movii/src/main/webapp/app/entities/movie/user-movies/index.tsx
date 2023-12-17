import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserMovies from './user-movies';
import UserMoviesDetail from './user-movies-detail';
import UserMoviesUpdate from './user-movies-update';
import UserMoviesDeleteDialog from './user-movies-delete-dialog';

const UserMoviesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserMovies />} />
    <Route path="new" element={<UserMoviesUpdate />} />
    <Route path=":id">
      <Route index element={<UserMoviesDetail />} />
      <Route path="edit" element={<UserMoviesUpdate />} />
      <Route path="delete" element={<UserMoviesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserMoviesRoutes;
