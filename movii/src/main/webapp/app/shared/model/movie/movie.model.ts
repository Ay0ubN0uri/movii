import dayjs from 'dayjs';
import { IGenre } from 'app/shared/model/movie/genre.model';
import { IActor } from 'app/shared/model/movie/actor.model';

export interface IMovie {
  id?: number;
  title?: string;
  description?: string;
  tmbdId?: string | null;
  thumbnailContentType?: string;
  thumbnail?: string;
  bannerContentType?: string | null;
  banner?: string | null;
  releaseDate?: dayjs.Dayjs;
  videoUrl?: string | null;
  duration?: string | null;
  youtubeTrailer?: string | null;
  views?: number | null;
  director?: string | null;
  averageRating?: number | null;
  genre?: IGenre | null;
  actors?: IActor[] | null;
}

export const defaultValue: Readonly<IMovie> = {};
