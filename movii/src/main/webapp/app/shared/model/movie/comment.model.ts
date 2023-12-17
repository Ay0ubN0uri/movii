import dayjs from 'dayjs';
import { IMovie } from 'app/shared/model/movie/movie.model';
import { IUser } from '../user.model';

export interface IComment {
  id?: number;
  content?: string | null;
  rating?: number | null;
  createdDate?: dayjs.Dayjs | null;
  userId?: number | null;
  movie?: IMovie | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IComment> = {};
