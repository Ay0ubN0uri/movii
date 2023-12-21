import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { loadMoreDataWhenScrolled, parseHeaderForLinks } from 'react-jhipster';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IMovie, defaultValue } from 'app/shared/model/movie/movie.model';
import {IGenre} from "app/shared/model/movie/genre.model";

const initialState: EntityState<IMovie> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'services/movie/api/movies';

// Actions

export const getEntities = createAsyncThunk('movie/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IMovie[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'movie/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IMovie>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'movie/create_entity',
  async (entity: IMovie, thunkAPI) => {
    return axios.post<IMovie>(apiUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'movie/update_entity',
  async (entity: IMovie, thunkAPI) => {
    return axios.put<IMovie>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'movie/partial_update_entity',
  async (entity: IMovie, thunkAPI) => {
    return axios.patch<IMovie>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'movie/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    return await axios.delete<IMovie>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const isGenreExist = createAsyncThunk(
  'movie/fetch_entity_byName',
  async (name: string) => {
    try {
      const requestUrl = `services/movie/api/genres/name/${name}`;
      const response = await axios.get<IGenre>(requestUrl);
      console.log("res: ", response);
      return { success: true, data: response.data, generId: response.data.id, error : null };
    } catch (error) {
      return { success: false, data: null, generId: null, error: error.message };
    }
  },
  { serializeError: serializeAxiosError },
);



// slice

export const MovieSlice = createEntitySlice({
  name: 'movie',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addCase(isGenreExist.fulfilled, (state, action) => {
        const genreExists = action.payload;
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;
        const links = parseHeaderForLinks(headers.link);

        return {
          ...state,
          loading: false,
          links,
          entities: loadMoreDataWhenScrolled(state.entities, data, links),
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = MovieSlice.actions;

// Reducer
export default MovieSlice.reducer;
