import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './genre.reducer';

export const GenreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const genreEntity = useAppSelector(state => state.movii.genre.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genreDetailsHeading">
          <Translate contentKey="moviiApp.movieGenre.detail.title">Genre</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{genreEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="moviiApp.movieGenre.name">Name</Translate>
            </span>
          </dt>
          <dd>{genreEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="moviiApp.movieGenre.description">Description</Translate>
            </span>
          </dt>
          <dd>{genreEntity.description}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="moviiApp.movieGenre.icon">Icon</Translate>
            </span>
          </dt>
          <dd>
            {genreEntity.icon ? (
              <div>
                {genreEntity.iconContentType ? (
                  <a onClick={openFile(genreEntity.iconContentType, genreEntity.icon)}>
                    <img src={`data:${genreEntity.iconContentType};base64,${genreEntity.icon}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {genreEntity.iconContentType}, {byteSize(genreEntity.icon)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/genre" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/genre/${genreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenreDetail;
