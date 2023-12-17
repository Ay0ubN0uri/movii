import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './actor.reducer';

export const ActorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const actorEntity = useAppSelector(state => state.movii.actor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="actorDetailsHeading">
          <Translate contentKey="moviiApp.movieActor.detail.title">Actor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{actorEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="moviiApp.movieActor.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{actorEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="moviiApp.movieActor.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{actorEntity.lastName}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="moviiApp.movieActor.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>{actorEntity.birthDate ? <TextFormat value={actorEntity.birthDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="bio">
              <Translate contentKey="moviiApp.movieActor.bio">Bio</Translate>
            </span>
          </dt>
          <dd>{actorEntity.bio}</dd>
          <dt>
            <span id="nationality">
              <Translate contentKey="moviiApp.movieActor.nationality">Nationality</Translate>
            </span>
          </dt>
          <dd>{actorEntity.nationality}</dd>
          <dt>
            <Translate contentKey="moviiApp.movieActor.movies">Movies</Translate>
          </dt>
          <dd>
            {actorEntity.movies
              ? actorEntity.movies.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {actorEntity.movies && i === actorEntity.movies.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/actor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/actor/${actorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActorDetail;
