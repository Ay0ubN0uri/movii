import dayjs from 'dayjs';
import { IMovie } from 'app/shared/model/movie/movie.model';

export interface IActor {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  bio?: string | null;
  nationality?: string | null;
  movies?: IMovie[] | null;
}

export const defaultValue: Readonly<IActor> = {};
