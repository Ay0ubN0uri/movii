import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Movie from './movie/movie';
import Genre from './movie/genre';
import Actor from './movie/actor';
import Comment from './movie/comment';
import UserMovies from './movie/user-movies';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('movii', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="movie/*" element={<Movie />} />
        <Route path="genre/*" element={<Genre />} />
        <Route path="actor/*" element={<Actor />} />
        <Route path="comment/*" element={<Comment />} />
        <Route path="user-movies/*" element={<UserMovies />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
