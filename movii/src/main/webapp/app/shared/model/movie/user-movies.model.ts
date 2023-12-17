import { IMovie } from 'app/shared/model/movie/movie.model';

export interface IUserMovies {
  id?: number;
  userId?: number | null;
  movie?: IMovie | null;
}

export const defaultValue: Readonly<IUserMovies> = {};
