import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGenre } from 'app/shared/model/movie/genre.model';
import { getEntities as getGenres } from 'app/entities/movie/genre/genre.reducer';
import { IActor } from 'app/shared/model/movie/actor.model';
import { getEntities as getActors } from 'app/entities/movie/actor/actor.reducer';
import { IMovie } from 'app/shared/model/movie/movie.model';
import { getEntity, updateEntity, createEntity, reset } from './movie.reducer';

export const MovieUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const genres = useAppSelector(state => state.movii.genre.entities);
  const actors = useAppSelector(state => state.movii.actor.entities);
  const movieEntity = useAppSelector(state => state.movii.movie.entity);
  const loading = useAppSelector(state => state.movii.movie.loading);
  const updating = useAppSelector(state => state.movii.movie.updating);
  const updateSuccess = useAppSelector(state => state.movii.movie.updateSuccess);

  const handleClose = () => {
    navigate('/movie');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getGenres({}));
    dispatch(getActors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.releaseDate = convertDateTimeToServer(values.releaseDate);
    if (values.views !== undefined && typeof values.views !== 'number') {
      values.views = Number(values.views);
    }
    if (values.averageRating !== undefined && typeof values.averageRating !== 'number') {
      values.averageRating = Number(values.averageRating);
    }

    const entity = {
      ...movieEntity,
      ...values,
      genre: genres.find(it => it.id.toString() === values.genre.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          releaseDate: displayDefaultDateTime(),
        }
      : {
          ...movieEntity,
          releaseDate: convertDateTimeFromServer(movieEntity.releaseDate),
          genre: movieEntity?.genre?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="moviiApp.movieMovie.home.createOrEditLabel" data-cy="MovieCreateUpdateHeading">
            <Translate contentKey="moviiApp.movieMovie.home.createOrEditLabel">Create or edit a Movie</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="movie-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('moviiApp.movieMovie.title')}
                id="movie-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('moviiApp.movieMovie.description')}
                id="movie-description"
                name="description"
                data-cy="description"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('moviiApp.movieMovie.tmbdId')}
                id="movie-tmbdId"
                name="tmbdId"
                data-cy="tmbdId"
                type="text"
              />
              <ValidatedBlobField
                label={translate('moviiApp.movieMovie.thumbnail')}
                id="movie-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('moviiApp.movieMovie.banner')}
                id="movie-banner"
                name="banner"
                data-cy="banner"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('moviiApp.movieMovie.releaseDate')}
                id="movie-releaseDate"
                name="releaseDate"
                data-cy="releaseDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('moviiApp.movieMovie.videoUrl')}
                id="movie-videoUrl"
                name="videoUrl"
                data-cy="videoUrl"
                type="text"
              />
              <ValidatedField
                label={translate('moviiApp.movieMovie.duration')}
                id="movie-duration"
                name="duration"
                data-cy="duration"
                type="text"
              />
              <ValidatedField
                label={translate('moviiApp.movieMovie.youtubeTrailer')}
                id="movie-youtubeTrailer"
                name="youtubeTrailer"
                data-cy="youtubeTrailer"
                type="text"
              />
              <ValidatedField label={translate('moviiApp.movieMovie.views')} id="movie-views" name="views" data-cy="views" type="text" />
              <ValidatedField
                label={translate('moviiApp.movieMovie.director')}
                id="movie-director"
                name="director"
                data-cy="director"
                type="text"
              />
              <ValidatedField
                label={translate('moviiApp.movieMovie.averageRating')}
                id="movie-averageRating"
                name="averageRating"
                data-cy="averageRating"
                type="text"
              />
              <ValidatedField id="movie-genre" name="genre" data-cy="genre" label={translate('moviiApp.movieMovie.genre')} type="select">
                <option value="" key="0" />
                {genres
                  ? genres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/movie" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MovieUpdate;
