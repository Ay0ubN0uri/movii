import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Modal, ModalHeader, ModalBody, ModalFooter} from 'reactstrap';
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
import {getEntity, updateEntity, createEntity, reset, isGenreExist} from './movie.reducer';
import {createEntity as createGenreEntity}  from '../genre/genre.reducer';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Oval } from 'react-loader-spinner';

import cheerio from 'cheerio';
import axios from 'axios';
import dayjs from "dayjs";
import {APP_LOCAL_DATETIME_FORMAT} from "app/config/constants";


const themoviedb = "https://www.themoviedb.org/";
const youtube_base_url = "https://youtube.com/watch?v=";

export const MovieUpdate = () => {
  const [scrapedData, setScrapedData] = useState({
      title: '',
      description: '',
      tmbdId : '',
      thumbnailContentType: 'image/jpg',
      thumbnail: '',
      bannerContentType: 'image/jpg',
      banner : '',
      releaseDate: '',
      videoUrl: '',
      duration: '',
      views : 0,
      youtubeTrailer: '',
      director : '',
      averageRating: 0,
      genre : '',
  });
  const [scrapping, setScrapping] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [imdbUrl, setImdbUrl] = useState('');
  const openModal = () => setShowModal(true);
  const closeModal = () => setShowModal(false);

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

  const fetchBannerAsBase64 = async (imageUrl) => {
    try {
      const response = await axios.get(`http://localhost:8083/api/image-to-base64?imageUrl=${themoviedb + imageUrl}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching Banner:', error);
      toast.error(`Error fetching Banner: ${error}`, { position: toast.POSITION.BOTTOM_RIGHT });
    }
  }

  const fetchThumbnailAsBase64 = async (videoId) => {
    try {
      const response = await axios.get(`http://localhost:8083/api/image-to-base64?imageUrl=https://i3.ytimg.com/vi/${videoId}/sddefault.jpg`);
      return response.data;
    } catch (error) {
      console.error('Error fetching Thumbnail:', error);
      toast.error(`Error fetching Thumbnail: ${error}`, { position: toast.POSITION.BOTTOM_RIGHT });
    }
  }

  // ex: /t/p/w300_and_h450_bestv2/dB6Krk806zeqd0YNp2ngQ9zXteH.jpg
  const getContentType = (url) => {
    const extension = url.split('.').pop().toLowerCase();
    switch (extension) {
      case 'jpg':
        return 'image/jpg';
      case 'jpeg':
        return 'image/jpeg';
      case 'png':
        return 'image/png';
      case 'gif':
        return 'image/gif';
      default:
        return 'unknown';
    }
  };

  const handleCheckGenreExistence = async (genre) => {
    try {
      const response = await dispatch(isGenreExist(genre));
      // @ts-ignore
      if (response.payload.success) {
        // @ts-ignore
        return { exists: true, id: response.payload.generId};
      } else {
        return { exists: false };
      }
    } catch (error) {
      console.error('Error checking genre existence:', error);
      return { exists: false};
    }
  };


  // const isGenreExist = async (name) => {
  //   try {
  //     const bearerToken = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwMzA4NjMzMSwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzAyOTk5OTMxfQ.N1arAy-zspxIdV1UC8ZvpRX0bSWO9m0ry0QxoDIKziLQ-kkBtOFxE--d2DngsnFkmVu5W-qwLrYBLjZJkkixmw';
  //     const headers = {
  //       Authorization: `Bearer ${bearerToken}`,
  //       'Content-Type': 'application/json',
  //     };
  //
  //     const response = await axios.get(`http://localhost:8083/services/movie/api/genres/name/${name}`, { headers });
  //     console.log(response.data);
  //
  //     const genreExists = response.status === 200;
  //     return genreExists;
  //
  //   } catch (error) {
  //     console.error('Error fetching data:', error);
  //     return false;
  //   }
  // };

  const handleScrape = async () => {
    try {

      setScrapping(true);

      // Additional check to verify the IMDb URL format
      if (!/^https:\/\/www\.themoviedb\.org\/(?:movie\/)?\d+-[^/]+$/.test(imdbUrl)) {
        console.error('Invalid URL format. Please enter a valid URL in the format: https://www.themoviedb.org/{movie_id}-{movie_name}');
        throw new Error("Invalid URL format. Please enter a valid URL in the format: https://www.themoviedb.org/{movie_id}-{movie_name}");
      }

      const response = await axios.get(`http://localhost:8083/api/scrape-imdb?url=${imdbUrl}`);

      const html = response.data;

      // Parse HTML using Cheerio
      const $ = cheerio.load(html);

      // Extract movie data using selectors
      const title = $("#original_header > div.header_poster_wrapper.false > section > div.title.ott_false > h2 > a").text().trim() ||
                            $("#original_header > div.header_poster_wrapper.true > section > div.title.ott_true > h2 > a").text().trim();

      const description = $("#original_header > div.header_poster_wrapper.false > section > div.header_info > div > p").text().trim() ||
                                  $("#original_header > div.header_poster_wrapper.true > section > div.header_info > div > p").text().trim();


      let tmbdId =
        $("#original_header > div.header_poster_wrapper.false > section > div.title.ott_false > h2 > a").attr("href") ||
        $("#original_header > div.header_poster_wrapper.true > section > div.title.ott_true > h2 > a").attr("href");

      if (tmbdId !== undefined) {
        tmbdId = tmbdId.replace("/movie/", "");
      }

      let bannerBase64 = null;
      const banner =  $("#original_header > div.poster_wrapper.false > div > div.image_content.backdrop > img").attr("data-src") ||
                            $("#original_header > div.poster_wrapper.true div.image_content.backdrop > img").attr("src");
      if(banner !== undefined){
        bannerBase64 = await fetchBannerAsBase64(banner);
      }

      const releaseDateStr =
        $("#original_header > div.header_poster_wrapper.true > section > div.title.ott_true > div > span.release").text().trim() ||
        $("#original_header > div.header_poster_wrapper.false > section > div.title.ott_false > div > span.release").text().trim();

      const extractedDate = releaseDateStr.match(/\d{2}\/\d{2}\/\d{4}/);
      const parsedDate = dayjs(extractedDate[0], 'MM/DD/YYYY').format(APP_LOCAL_DATETIME_FORMAT);
      const releaseDate = convertDateTimeFromServer(parsedDate);

      const video_id = $("#original_header > div.header_poster_wrapper.false > section > ul > li.video.none > a").attr("data-id")

      const duration = $("#original_header > div.header_poster_wrapper.false > section > div.title.ott_false > div > span.runtime").text().trim() ||
                              $("#original_header > div.header_poster_wrapper.true > section > div.title.ott_true > div > span.runtime").text().trim();

      const views = $("").text().trim();

      const director = $("#original_header > div.header_poster_wrapper.false > section > div.header_info > ol > li:nth-child(1) > p:nth-child(1) > a").text().trim() ||
                              $("#original_header > div.header_poster_wrapper.true > section > div.header_info > ol > li:nth-child(3) > p:nth-child(1) > a").text().trim();


      const averageRating = $("#original_header > div.header_poster_wrapper.false > section > ul > li.chart > div.consensus.details > div > div.user_score_chart").attr("data-percent") ||
                                    $("#original_header > div.header_poster_wrapper.true > section > ul > li.chart > div.consensus.details > div > div.user_score_chart").attr("data-percent");

      const genre = $("#original_header > div.header_poster_wrapper.false > section > div.title.ott_false > div > span.genres > a:nth-child(1)") .text().trim() ||
                          $("#original_header > div.header_poster_wrapper.true > section > div.title.ott_true > div > span.genres > a:nth-child(1)") .text().trim();

      // fetch genere from db to see if it si already there
      let genreExists = await handleCheckGenreExistence(genre);
      console.log("genreExists: ", genreExists);

      try{

        // Handle the result
        if (!genreExists.exists) {
          const newGenre = {
            name: genre,
            description: null,
            iconContentType: null,
            icon: null
          };

          const resGenre  = dispatch(createGenreEntity(newGenre));
          // @ts-ignore
          if (resGenre.payload) {
            // @ts-ignore
            genreExists.id = resGenre.playload.id;
            await dispatch(getGenres({}));
          } else {
            toast.error('Genre creation failed', { position: toast.POSITION.BOTTOM_RIGHT });
          }
        }
      }catch(err){
        toast.error(err, { position: toast.POSITION.BOTTOM_RIGHT })
      }

      // console.log("releaseDateStr", releaseDateStr);
      // console.log("extractedDate", extractedDate);
      // console.log("parsedDate", parsedDate);
      // console.log("releaseDate", releaseDate);

      let ge;
      try{
        ge = genres.find(it => it.id.toString() === genreExists.id.toString())
      }catch (err){
        console.error(err);
      }

      /// Set the scraped data to state
      setScrapedData({
        title,
        description,
        tmbdId,
        thumbnailContentType: 'image/jpg',
        thumbnail : await fetchThumbnailAsBase64(video_id),
        bannerContentType: getContentType(banner),
        banner : bannerBase64,
        releaseDate,
        videoUrl: youtube_base_url + video_id,
        duration,
        views: Number(views),
        youtubeTrailer: youtube_base_url + video_id,
        director,
        averageRating: Number(averageRating),
        genre: ge
      });

      // Call closeModal() when done
      closeModal();
      toast.success('Scraping successful!', { position: toast.POSITION.BOTTOM_RIGHT });

    } catch (error) {
      toast.error(`Error scraping TMDB data: ${error}`, { position: toast.POSITION.BOTTOM_RIGHT });
      console.error('Error scraping TMDB data:', error);
    }
    finally {
      setScrapping(false);
    }
  };


  return (
    <div>
      <div className="d-flex justify-content-center my-4">
        <Button color="primary" onClick={openModal}>
          Scrape TMDB Data
        </Button>
      </div>

      <Modal isOpen={showModal} toggle={closeModal} centered={true}>
        <ModalHeader toggle={closeModal}>Scrape TMDB Data</ModalHeader>
        <ModalBody className="d-flex flex-column justify-content-center">
          {scrapping &&
            <div className="d-flex justify-content-center">
              <Oval color="#00BFFF" height={100} width={100} />
            </div>
          }

          {!scrapping && (
            <div>
              <label className="mx-3">TMDB URL:</label>
              <input autoFocus={true} type="text" value={imdbUrl} onChange={(e) => setImdbUrl(e.target.value)} />
            </div>
          )}
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={closeModal}>
            Cancel
          </Button>
          <Button color="primary" onClick={handleScrape} disabled={loading}>
            Scrape
          </Button>
        </ModalFooter>
      </Modal>

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
            <ValidatedForm  defaultValues={isNew ? { ...defaultValues(), ...scrapedData } : defaultValues()} onSubmit={saveEntity}>
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

              {scrapedData.thumbnail && (
                <div>
                  <img src={`data:image/jpg;base64,${scrapedData.thumbnail}`} alt="Thumbnail" style={{ maxWidth: '100%' }}/>
                </div>
              )}

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

              {scrapedData.banner && (
                <div>
                  <img src={`data:image/jpg;base64,${scrapedData.banner}`} alt="Banner" style={{ maxWidth: '100%' }} />
                </div>
              )}

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
