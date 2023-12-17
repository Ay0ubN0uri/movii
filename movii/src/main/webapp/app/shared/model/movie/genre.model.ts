import { IMovie } from 'app/shared/model/movie/movie.model';

export interface IGenre {
  id?: number;
  name?: string;
  description?: string | null;
  iconContentType?: string | null;
  icon?: string | null;
  movies?: IMovie[] | null;
}

export const defaultValue: Readonly<IGenre> = {};
