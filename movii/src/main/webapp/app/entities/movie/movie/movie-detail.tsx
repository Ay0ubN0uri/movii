import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './movie.reducer';

export const MovieDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const movieEntity = useAppSelector(state => state.movii.movie.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="movieDetailsHeading">
          <Translate contentKey="moviiApp.movieMovie.detail.title">Movie</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{movieEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="moviiApp.movieMovie.title">Title</Translate>
            </span>
          </dt>
          <dd>{movieEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="moviiApp.movieMovie.description">Description</Translate>
            </span>
          </dt>
          <dd>{movieEntity.description}</dd>
          <dt>
            <span id="tmbdId">
              <Translate contentKey="moviiApp.movieMovie.tmbdId">Tmbd Id</Translate>
            </span>
          </dt>
          <dd>{movieEntity.tmbdId}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="moviiApp.movieMovie.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {movieEntity.thumbnail ? (
              <div>
                {movieEntity.thumbnailContentType ? (
                  <a onClick={openFile(movieEntity.thumbnailContentType, movieEntity.thumbnail)}>
                    <img src={`data:${movieEntity.thumbnailContentType};base64,${movieEntity.thumbnail}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {movieEntity.thumbnailContentType}, {byteSize(movieEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="banner">
              <Translate contentKey="moviiApp.movieMovie.banner">Banner</Translate>
            </span>
          </dt>
          <dd>
            {movieEntity.banner ? (
              <div>
                {movieEntity.bannerContentType ? (
                  <a onClick={openFile(movieEntity.bannerContentType, movieEntity.banner)}>
                    <img src={`data:${movieEntity.bannerContentType};base64,${movieEntity.banner}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {movieEntity.bannerContentType}, {byteSize(movieEntity.banner)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="releaseDate">
              <Translate contentKey="moviiApp.movieMovie.releaseDate">Release Date</Translate>
            </span>
          </dt>
          <dd>{movieEntity.releaseDate ? <TextFormat value={movieEntity.releaseDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="videoUrl">
              <Translate contentKey="moviiApp.movieMovie.videoUrl">Video Url</Translate>
            </span>
          </dt>
          <dd>{movieEntity.videoUrl}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="moviiApp.movieMovie.duration">Duration</Translate>
            </span>
          </dt>
          <dd>{movieEntity.duration}</dd>
          <dt>
            <span id="youtubeTrailer">
              <Translate contentKey="moviiApp.movieMovie.youtubeTrailer">Youtube Trailer</Translate>
            </span>
          </dt>
          <dd>{movieEntity.youtubeTrailer}</dd>
          <dt>
            <span id="views">
              <Translate contentKey="moviiApp.movieMovie.views">Views</Translate>
            </span>
          </dt>
          <dd>{movieEntity.views}</dd>
          <dt>
            <span id="director">
              <Translate contentKey="moviiApp.movieMovie.director">Director</Translate>
            </span>
          </dt>
          <dd>{movieEntity.director}</dd>
          <dt>
            <span id="averageRating">
              <Translate contentKey="moviiApp.movieMovie.averageRating">Average Rating</Translate>
            </span>
          </dt>
          <dd>{movieEntity.averageRating}</dd>
          <dt>
            <Translate contentKey="moviiApp.movieMovie.genre">Genre</Translate>
          </dt>
          <dd>{movieEntity.genre ? movieEntity.genre.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/movie" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/movie/${movieEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MovieDetail;
