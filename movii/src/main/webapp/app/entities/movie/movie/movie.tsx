import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './movie.reducer';

export const Movie = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const movieList = useAppSelector(state => state.movii.movie.entities);
  const loading = useAppSelector(state => state.movii.movie.loading);
  const links = useAppSelector(state => state.movii.movie.links);
  const updateSuccess = useAppSelector(state => state.movii.movie.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="movie-heading" data-cy="MovieHeading">
        <Translate contentKey="moviiApp.movieMovie.home.title">Movies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="moviiApp.movieMovie.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/movie/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="moviiApp.movieMovie.home.createLabel">Create new Movie</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={movieList ? movieList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {movieList && movieList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="moviiApp.movieMovie.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('title')}>
                    <Translate contentKey="moviiApp.movieMovie.title">Title</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                  </th>
                  <th className="hand" onClick={sort('description')}>
                    <Translate contentKey="moviiApp.movieMovie.description">Description</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                  </th>
                  <th className="hand" onClick={sort('tmbdId')}>
                    <Translate contentKey="moviiApp.movieMovie.tmbdId">Tmbd Id</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('tmbdId')} />
                  </th>
                  <th className="hand" onClick={sort('thumbnail')}>
                    <Translate contentKey="moviiApp.movieMovie.thumbnail">Thumbnail</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('thumbnail')} />
                  </th>
                  <th className="hand" onClick={sort('banner')}>
                    <Translate contentKey="moviiApp.movieMovie.banner">Banner</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('banner')} />
                  </th>
                  <th className="hand" onClick={sort('releaseDate')}>
                    <Translate contentKey="moviiApp.movieMovie.releaseDate">Release Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('releaseDate')} />
                  </th>
                  <th className="hand" onClick={sort('videoUrl')}>
                    <Translate contentKey="moviiApp.movieMovie.videoUrl">Video Url</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('videoUrl')} />
                  </th>
                  <th className="hand" onClick={sort('duration')}>
                    <Translate contentKey="moviiApp.movieMovie.duration">Duration</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('duration')} />
                  </th>
                  <th className="hand" onClick={sort('youtubeTrailer')}>
                    <Translate contentKey="moviiApp.movieMovie.youtubeTrailer">Youtube Trailer</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('youtubeTrailer')} />
                  </th>
                  <th className="hand" onClick={sort('views')}>
                    <Translate contentKey="moviiApp.movieMovie.views">Views</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('views')} />
                  </th>
                  <th className="hand" onClick={sort('director')}>
                    <Translate contentKey="moviiApp.movieMovie.director">Director</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('director')} />
                  </th>
                  <th className="hand" onClick={sort('averageRating')}>
                    <Translate contentKey="moviiApp.movieMovie.averageRating">Average Rating</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('averageRating')} />
                  </th>
                  <th>
                    <Translate contentKey="moviiApp.movieMovie.genre">Genre</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {movieList.map((movie, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/movie/${movie.id}`} color="link" size="sm">
                        {movie.id}
                      </Button>
                    </td>
                    <td>{movie.title}</td>
                    <td>{movie.description}</td>
                    <td>{movie.tmbdId}</td>
                    <td>
                      {movie.thumbnail ? (
                        <div>
                          {movie.thumbnailContentType ? (
                            <a onClick={openFile(movie.thumbnailContentType, movie.thumbnail)}>
                              <img src={`data:${movie.thumbnailContentType};base64,${movie.thumbnail}`} style={{ maxHeight: '30px' }} />
                              &nbsp;
                            </a>
                          ) : null}
                          <span>
                            {movie.thumbnailContentType}, {byteSize(movie.thumbnail)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>
                      {movie.banner ? (
                        <div>
                          {movie.bannerContentType ? (
                            <a onClick={openFile(movie.bannerContentType, movie.banner)}>
                              <img src={`data:${movie.bannerContentType};base64,${movie.banner}`} style={{ maxHeight: '30px' }} />
                              &nbsp;
                            </a>
                          ) : null}
                          <span>
                            {movie.bannerContentType}, {byteSize(movie.banner)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{movie.releaseDate ? <TextFormat type="date" value={movie.releaseDate} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{movie.videoUrl}</td>
                    <td>{movie.duration}</td>
                    <td>{movie.youtubeTrailer}</td>
                    <td>{movie.views}</td>
                    <td>{movie.director}</td>
                    <td>{movie.averageRating}</td>
                    <td>{movie.genre ? <Link to={`/genre/${movie.genre.id}`}>{movie.genre.name}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/movie/${movie.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/movie/${movie.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/movie/${movie.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="moviiApp.movieMovie.home.notFound">No Movies found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Movie;
