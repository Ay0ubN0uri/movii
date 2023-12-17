import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMovie } from 'app/shared/model/movie/movie.model';
import { getEntities as getMovies } from 'app/entities/movie/movie/movie.reducer';
import { IActor } from 'app/shared/model/movie/actor.model';
import { getEntity, updateEntity, createEntity, reset } from './actor.reducer';

export const ActorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const movies = useAppSelector(state => state.movii.movie.entities);
  const actorEntity = useAppSelector(state => state.movii.actor.entity);
  const loading = useAppSelector(state => state.movii.actor.loading);
  const updating = useAppSelector(state => state.movii.actor.updating);
  const updateSuccess = useAppSelector(state => state.movii.actor.updateSuccess);

  const handleClose = () => {
    navigate('/actor');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getMovies({}));
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
    values.birthDate = convertDateTimeToServer(values.birthDate);

    const entity = {
      ...actorEntity,
      ...values,
      movies: mapIdList(values.movies),
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
          birthDate: displayDefaultDateTime(),
        }
      : {
          ...actorEntity,
          birthDate: convertDateTimeFromServer(actorEntity.birthDate),
          movies: actorEntity?.movies?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="moviiApp.movieActor.home.createOrEditLabel" data-cy="ActorCreateUpdateHeading">
            <Translate contentKey="moviiApp.movieActor.home.createOrEditLabel">Create or edit a Actor</Translate>
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
                  id="actor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('moviiApp.movieActor.firstName')}
                id="actor-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('moviiApp.movieActor.lastName')}
                id="actor-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('moviiApp.movieActor.birthDate')}
                id="actor-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('moviiApp.movieActor.bio')} id="actor-bio" name="bio" data-cy="bio" type="textarea" />
              <ValidatedField
                label={translate('moviiApp.movieActor.nationality')}
                id="actor-nationality"
                name="nationality"
                data-cy="nationality"
                type="text"
              />
              <ValidatedField
                label={translate('moviiApp.movieActor.movies')}
                id="actor-movies"
                data-cy="movies"
                type="select"
                multiple
                name="movies"
              >
                <option value="" key="0" />
                {movies
                  ? movies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/actor" replace color="info">
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

export default ActorUpdate;
