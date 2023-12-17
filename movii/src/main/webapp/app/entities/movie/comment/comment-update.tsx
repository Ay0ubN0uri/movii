/* eslint-disable no-console */
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
import { IComment } from 'app/shared/model/movie/comment.model';
import { getEntity, updateEntity, createEntity, reset } from './comment.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';

export const CommentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const movies = useAppSelector(state => state.movii.movie.entities);
  const commentEntity = useAppSelector(state => state.movii.comment.entity);
  const loading = useAppSelector(state => state.movii.comment.loading);
  const updating = useAppSelector(state => state.movii.comment.updating);
  const updateSuccess = useAppSelector(state => state.movii.comment.updateSuccess);

  const handleClose = () => {
    navigate('/comment');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    // if (values.userId !== undefined && typeof values.userId !== 'number') {
    //   values.userId = Number(values.userId);
    // }
    if (values.user !== undefined) {
      values.userId = Number(values.user);
    }

    delete values.user;
    const entity = {
      ...commentEntity,
      ...values,
      movie: movies.find(it => it.id.toString() === values.movie.toString()),
    };
    console.log(entity);

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
        }
      : {
          ...commentEntity,
          createdDate: convertDateTimeFromServer(commentEntity.createdDate),
          movie: commentEntity?.movie?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="moviiApp.movieComment.home.createOrEditLabel" data-cy="CommentCreateUpdateHeading">
            <Translate contentKey="moviiApp.movieComment.home.createOrEditLabel">Create or edit a Comment</Translate>
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
                  id="comment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('moviiApp.movieComment.content')}
                id="comment-content"
                name="content"
                data-cy="content"
                type="textarea"
              />
              <ValidatedField
                label={translate('moviiApp.movieComment.rating')}
                id="comment-rating"
                name="rating"
                data-cy="rating"
                type="text"
              />
              <ValidatedField
                label={translate('moviiApp.movieComment.createdDate')}
                id="comment-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="comment-user" name="user" data-cy="user" label={translate('moviiApp.movieComment.userId')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {/* <ValidatedField
                label={translate('moviiApp.movieComment.userId')}
                id="comment-userId"
                name="userId"
                data-cy="userId"
                type="text"
              /> */}
              <ValidatedField
                id="comment-movie"
                name="movie"
                data-cy="movie"
                label={translate('moviiApp.movieComment.movie')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/comment" replace color="info">
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

export default CommentUpdate;
