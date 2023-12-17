import movie from 'app/entities/movie/movie/movie.reducer';
import genre from 'app/entities/movie/genre/genre.reducer';
import actor from 'app/entities/movie/actor/actor.reducer';
import comment from 'app/entities/movie/comment/comment.reducer';
import userMovies from 'app/entities/movie/user-movies/user-movies.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  movie,
  genre,
  actor,
  comment,
  userMovies,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
