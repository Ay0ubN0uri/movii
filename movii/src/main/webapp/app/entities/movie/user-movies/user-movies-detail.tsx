import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-movies.reducer';

export const UserMoviesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userMoviesEntity = useAppSelector(state => state.movii.userMovies.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userMoviesDetailsHeading">
          <Translate contentKey="moviiApp.movieUserMovies.detail.title">UserMovies</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userMoviesEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="moviiApp.movieUserMovies.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{userMoviesEntity.userId}</dd>
          <dt>
            <Translate contentKey="moviiApp.movieUserMovies.movie">Movie</Translate>
          </dt>
          <dd>{userMoviesEntity.movie ? userMoviesEntity.movie.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-movies" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-movies/${userMoviesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserMoviesDetail;
